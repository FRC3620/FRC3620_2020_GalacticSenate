package frc.robot.subsystems;

import org.slf4j.Logger;
import frc.robot.Constants;

import org.usfirst.frc3620.misc.LightEffect;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.util.Color8Bit;


/**
 * @author Nick Zimanski (SlippStream)
 * @version 18 February 2020
 */
public class LightSubsystem extends SubsystemBase {
  Logger logger = EventLogging.getLogger(getClass(), Level.DEBUG);

  private AddressableLED ledStrip;
  private AddressableLEDBuffer ledBuffer;
  private LightEffect currentEffect;
  private Timer voltageTimer = new Timer();
  private Timer effectTimer = new Timer(); //Milliseconds = 0 on ANY parameter makes the effect indefinite and auto-skippable
  private ArrayList<LightEffect> queue = new ArrayList<LightEffect>();
  private ArrayList<String> passwordQueue = new ArrayList<String>();

  private boolean presetEnRoute = false;
  private boolean firstTime = false; //Tracks whether a light effect is displaying for the first cycle.
  private boolean isDSConnected = false;

  private final boolean defaultOverride = false;
  private final double defaultBatteryLowVoltage = 12.0; //Volts that, when dipped below, alert the lights
  private final int defaultMilliseconds = 3000;
  private final boolean defaultReturnsPrevious = false;
  private final Color8Bit defaultColor = new Color8Bit(0, 0, 0);
  private final String defaultPassword = null;

  private final int shot_defaultTrailLength = 15;
  private final int shot_defaultIntervalLength = 10;
  private final int blink_defaultBlinkRate = 500;
  private final int battery_voltageTimerInterval = 60; //number of seconds between low_voltage alerts

  private int shot_iter = 0;
  private boolean blink_isOn = false;
  private int twinkle_runCount = 0;
  private int heartbeat_runCount = 0;
  private double breathe_runCount = 4.712;
  private int blink_runCount = 0;

  ArrayList<Integer> shot_locationArr = new ArrayList<Integer>();

  /**
   * Instantiates the light strip and buffer. Refer to {@link frc.robot.RobotContainer#lightSubsystem RobotContainer.lightSubsytem} when making calls.
   * <p>
   * For example, {@code LightSubsystem lights = RobotContainer.lightSubsystem;}
   */
  public LightSubsystem() {
    ledStrip = new AddressableLED(Constants.PWM.LIGHTS);
    ledBuffer = new AddressableLEDBuffer(Constants.LED_STRIP_LENGTH);

    ledStrip.setLength(ledBuffer.getLength());

    ledStrip.setData(ledBuffer);
    ledStrip.start();

    effectTimer.reset();
    effectTimer.start();

    voltageTimer.reset();
    voltageTimer.start();
  }

  /**
   * Removes the current strip effect.
   */
  public void skipEffect() {queue.remove(0); /*logger.debug("Effect removed!");*/}

  /**
   * Removes all effects from the current queue (Advanced users only)
   */
  public void purgeQueue() {queue.clear(); currentEffect = null; /*logger.debug("Queue purged!");*/}

  /**
   * Removes all passwords from the current password queue (Advanced users only)
   */
  public void purgePasswordQueue() {passwordQueue.clear(); /*logger.debug("Password queue purged!");*/}

  /**
   * Adds a {@link frc.misc.LightEffect LightEffect} effect to the queue. (Advanced users only)
   * @param effect The {@link frc.misc.LightEffect LightEffect} object to add to queue 
   */
  public void setEffect(LightEffect effect) {
    // lets the underlying effect (like green twinkle for teleop) come back after the current effect
    if (queue.size() > 0 && queue.get(queue.size() - 1).getMilliseconds() == 0 && effect.getMilliseconds() != 0 && !presetEnRoute) effect.setReturnsPrevious(true);
    
    queue.add(effect); 
    //logger.debug("Queueing pattern: " + effect.getPattern());
  };

  /**
   * Accepts a pattern and displays the preset animation if one exists for that pattern
   * @param pattern A {@link LightEffect.Pattern} pattern for which a preset exists
   */
  public void setPreset(LightEffect.Preset pattern) {
    presetEnRoute = true;
    switch (pattern) {
      case DISABLED:
        purgePasswordQueue();
        setTwinkle(new Color8Bit(100,0,0), 0, false);
        break;
      case INIT:
        setShot(new Color8Bit[] {LightEffect.Color.BLUE.value, LightEffect.Color.MAIZE.value}, 7500, false, 10, 2, false);
        break;
      case TELEOP:
        purgePasswordQueue();
        if (DriverStation.getInstance().getAlliance() == Alliance.Red) {setBlink(new Color8Bit(255, 0, 0), 1800, true, 350);}
        else {setBlink(new Color8Bit(0, 0, 255), 1800, false, 350);}
        setTwinkle(new Color8Bit(0, 180, 0), 0, false, false);
        break;
      case AUTO:
        setShot(new Color8Bit[] {new Color8Bit(0, 255, 0)}, 0, false, 4, 3, false);
        break;
      case TEST:
        setRainbow(0, false);
        break;
      case LOW_VOLTAGE:
        setBlink(new Color8Bit(255,0,0), 1500, true, 200, true);
        break;
      case DS_CONNECTED:
        setBreathe(LightEffect.Color.GREEN.value, 1800, true, true);
        break;
      case DS_DISCONNECTED:
        setBreathe(LightEffect.Color.ORANGE.value, 0, true, false);
        break;
      default:
        break;
    }
    presetEnRoute = false;
  }

  /**
   * Sets a solid color throughout the entire string.
   * @param red The RGB red value (0-255)
   * @param green The RGB green value (0-255)
   * @param blue The RGB blue value (0-255)
   */
  public void setSolidColor(int red, int green, int blue) {setSolidColor(new Color8Bit(red, green, blue));}

  /**
   * Sets a solid color throughout the entire string.
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param override (Optional) Whether or not to skip the queue
   * @param milliseconds (Optional) The time, in milliseconds, to run the effect for
   * @param returnsPrevious (Optional) Whether or not to requeue the effect this overrides. Defaults to false
   * @param password (Optional) The lock to put on an effect
   * @see Color8Bit
   */
  public void setSolidColor(Color8Bit color, int milliseconds, boolean override, boolean returnsPrevious, String password) {setEffect(new LightEffect(color, override, LightEffect.Pattern.SOLID, milliseconds, returnsPrevious, password));}
  public void setSolidColor(Color8Bit color, int milliseconds, boolean override, boolean returnsPrevious) {setSolidColor(color, milliseconds, override, returnsPrevious, defaultPassword);}
  public void setSolidColor(Color8Bit color, int milliseconds, boolean override) {setSolidColor(color, milliseconds, override, defaultReturnsPrevious);}
  public void setSolidColor(Color8Bit color, int milliseconds) {setSolidColor(color, milliseconds, defaultOverride);}
  public void setSolidColor(Color8Bit color) {setSolidColor(color, defaultMilliseconds);}






  


  /**
   * Sets the current pattern to a default, Full-RGB rainbow
   * @param milliseconds (Optional) The time, in milliseconds, to run the effect for.
   * @param override (Optional) Whether or not to skip the queue.
   * @param returnsPrevious (Optional) Whether or not to requeue the effect this overrides. Defaults to false
   * @param password (Optional) The lock to put on an effect
   */
  public void setRainbow(int milliseconds, boolean override, boolean returnsPrevious, String password) {setEffect(new LightEffect(defaultColor, override, LightEffect.Pattern.RAINBOW, milliseconds, returnsPrevious, password));}
  public void setRainbow(int milliseconds, boolean override, boolean returnsPrevious) {setRainbow(milliseconds, override, returnsPrevious, defaultPassword);}
  public void setRainbow(int milliseconds, boolean override) {setRainbow(milliseconds, override, defaultReturnsPrevious);}
  public void setRainbow(int milliseconds) {setRainbow(milliseconds, defaultOverride);}
  public void setRainbow() {setRainbow(defaultMilliseconds);}




  /**
   * Sets a 'shot' animation where a light runs down the strip with a trail
   * @param color The WPILIB color reference for a specific color
   * @param milliseconds (Optional) The time, in milliseconds, to run the effect for. Defaults to 3000
   * @param override (Optional) Whether or not to skip the queue. Defaults to false
   * @param trailLength (Optional) The length, in pixels, of LEDs to light behind the shot. Defaults to 15.
   * @param shotInterval (Optional) The length, in pixel, of LEDs between shots. Defaults to 10.
   * @param returnsPrevious (Optional) Whether or not to requeue the effect this overrides. Defaults to false
   * @see Color8Bit
   */
  public void setShot(Color8Bit color, int milliseconds, boolean override, int trailLength, int shotInterval, boolean returnsPrevious) {
    Color8Bit[] c = {color};
    setShot(c, milliseconds, override, trailLength, shotInterval, returnsPrevious);
  }

  /**
   * Sets a 'shot' animation where a light runs down the strip with a trail
   * @param colors An array of each color to be shot, in order
   * @param milliseconds (Optional) The time, in milliseconds, to run the effect for. Defaults to 3000
   * @param override (Optional) Whether or not to skip the queue. Defaults to false
   * @param trailLength (Optional) The length, in pixels, of LEDs to light behind the shot. Defaults to 15.
   * @param shotInterval (Optional) The length, in pixel, of LEDs between shots. Defaults to 10.
   * @param returnsPrevious (Optional) Whether or not to requeue the effect this overrides. Defaults to false
   * @param password (Optional) The lock to put on an effect
   * @see Color8Bit
   */
  public void setShot(Color8Bit[] colors, int milliseconds, boolean override, int trailLength, int shotInterval, boolean returnsPrevious, String password) {
    var effect = new LightEffect(colors[0], override, LightEffect.Pattern.SHOT, milliseconds, returnsPrevious, password);
    effect.m_shotIntervalLength = shotInterval;
    effect.m_shotTrailLength = trailLength;
    effect.m_shotColors = colors;
    shot_locationArr.clear();
    shot_iter = 0;
    setEffect(effect);
  }
  public void setShot(Color8Bit[] colors, int milliseconds, boolean override, int trailLength, int shotInterval, boolean returnsPrevious) {setShot(colors, milliseconds, override, trailLength, shotInterval, returnsPrevious, defaultPassword);}
  public void setShot(Color8Bit color, int milliseconds, boolean override, int trailLength, int shotInterval) {setShot(color, milliseconds, override, trailLength, shotInterval, defaultReturnsPrevious);}
  public void setShot(Color8Bit color, int milliseconds, boolean override, int trailLength) {setShot(color, milliseconds, override, trailLength, shot_defaultIntervalLength);}
  public void setShot(Color8Bit color, int milliseconds, boolean override) {setShot(color, milliseconds, override, shot_defaultTrailLength);}
  public void setShot(Color8Bit color, int milliseconds) {setShot(color, milliseconds, defaultOverride);}
  public void setShot(Color8Bit color) {setShot(color, defaultMilliseconds);}






  /**
   * Sets a 'twinkle' animation on a solid color, where individual LEDs fluctuate brightness
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param milliseconds (Optional) The time, in milliseconds, to run the effect for. Defaults to 3000.
   * @param override (Optional) Whether or not to skip the queue. Defaults to false.
   * @param returnsPrevious (Optional) Whether or not to requeue the effect this overrides. Defaults to false
   * @param password (Optional) The lock to put on an effect
   */
  public void setTwinkle(Color8Bit color, int milliseconds, boolean override, boolean returnsPrevious, String password) {
    LightEffect effect = new LightEffect(color, override, LightEffect.Pattern.TWINKLE, milliseconds, returnsPrevious, password);
    setEffect(effect);
  }
  public void setTwinkle(Color8Bit color, int milliseconds, boolean override, boolean returnsPrevious) {setTwinkle(color, milliseconds, override, returnsPrevious, defaultPassword);}
  public void setTwinkle(Color8Bit color, int milliseconds, boolean override) {setTwinkle(color, milliseconds, override, defaultReturnsPrevious, defaultPassword);}
  public void setTwinkle(Color8Bit color, int milliseconds) {setTwinkle(color, milliseconds, defaultOverride);}
  public void setTwinkle(Color8Bit color) {setTwinkle(color, defaultMilliseconds);}




  /**
   * Sets a 'blink' animation on a solid color, where the light flashes on and off
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param milliseconds (Optional) The time, in milliseconds, to run the effect for. Defaults to 3000.
   * @param override (Optional) Whether or not to skip the queue. Defaults to false.
   * @param blinkRate (Optional) How long, in milliseconds, the lights will take for a on/off cycle. For example if blinkRate is 200, The lights will be on for 0.1 seconds, off for 0.1 seconds and so on. Defaults to 500
   * @param returnsPrevious (Optional) Whether or not to requeue the effect this overrides. Defaults to false
   * @param password (Optional) The lock to put on an effect
   */
  public void setBlink(Color8Bit color, int milliseconds, boolean override, int blinkRate, boolean returnsPrevious, String password) {
    var effect = new LightEffect(color, override, LightEffect.Pattern.BLINK, milliseconds, returnsPrevious, password);
    effect.m_blinkFlashRate = blinkRate;
    setEffect(effect);
  }
  public void setBlink(Color8Bit color, int milliseconds, boolean override, int blinkRate, boolean returnsPrevious) {setBlink(color, milliseconds, override, blinkRate, returnsPrevious, defaultPassword);}
  public void setBlink(Color8Bit color, int milliseconds, boolean override, int blinkRate) {setBlink(color, milliseconds, override, blinkRate, defaultReturnsPrevious);}
  public void setBlink(Color8Bit color, int milliseconds, boolean override) {setBlink(color, milliseconds, override, blink_defaultBlinkRate);}
  public void setBlink(Color8Bit color, int milliseconds) {setBlink(color, milliseconds, defaultOverride);}
  public void setBlink(Color8Bit color) {setBlink(color, defaultMilliseconds);}


  

  /**
   * Sets a 'Breath' animation where a color fades in and out
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param milliseconds (Optional) The time, in milliseconds, to run the effect for. Defaults to 3000.
   * @param override (Optional) Whether or not to skip the queue. Defaults to false.
   * @param returnsPrevious (Optional) Whether or not to requeue the effect this overrides. Defaults to false
   * @param password (Optional) The lock to put on an effect
   */
  public void setBreathe(Color8Bit color, int milliseconds, boolean override, boolean returnsPrevious, String password) {
    var effect = new LightEffect(color, override, LightEffect.Pattern.BREATHE, milliseconds, returnsPrevious, password);
    setEffect(effect);
  }
  public void setBreathe(Color8Bit color, int milliseconds, boolean override, boolean returnsPrevious) {setBreathe(color, milliseconds, override, returnsPrevious, defaultPassword);}
  public void setBreathe(Color8Bit color, int milliseconds, boolean override) {setBreathe(color,milliseconds, override, defaultReturnsPrevious);}
  public void setBreathe(Color8Bit color, int milliseconds) {setBreathe(color, milliseconds, defaultOverride);} 
  public void setBreathe(Color8Bit color) {setBreathe(color, defaultMilliseconds);}







  /**
   * Sets a `Heartbeat` animation where a normal color rhythmically pulses
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param milliseconds (Optional) The time, in milliseconds, to run the effect for. Defaults to 3000.
   * @param override (Optional) Whether or not to skip the queue. Defaults to false.
   * @param returnsPrevious (Optional) Whether or not to requeue the effect this overrides. Defaults to false
   * @param password (Optional) The lock to put on an effect
   */
  public void setHeartbeat(Color8Bit color, int milliseconds, boolean override, boolean returnsPrevious, String password) {
    var effect = new LightEffect(color, override, LightEffect.Pattern.HEARTBEAT, milliseconds, returnsPrevious, password);
    setEffect(effect);
  }
  public void setHeartbeat(Color8Bit color, int milliseconds, boolean override, boolean returnsPrevious) {setHeartbeat(color, milliseconds, override, returnsPrevious, defaultPassword);}
  public void setHeartbeat(Color8Bit color, int milliseconds, boolean override) {setHeartbeat(color, milliseconds, override, defaultReturnsPrevious);}
  public void setHeartbeat(Color8Bit color, int milliseconds) {setHeartbeat(color, milliseconds, defaultOverride);}
  public void setHeartbeat(Color8Bit color) {setHeartbeat(color, defaultMilliseconds);}




  /**
   * Takes the current effect out if it shares this method's password
   * @param password The string passed to the original passworded effect
   */
  public void removeEffectByPassword(String password) {if(!passwordQueue.contains(password)) passwordQueue.add(password);}





  private void checkVoltage(double voltage) {
    if (isDSConnected && voltage < defaultBatteryLowVoltage && voltageTimer.get() > battery_voltageTimerInterval) {
      setPreset(LightEffect.Preset.LOW_VOLTAGE);
      voltageTimer.reset();
      voltageTimer.start();
    }
  }

  private void checkDS() {
    if (isDSConnected != DriverStation.getInstance().isDSAttached()) {
      isDSConnected = DriverStation.getInstance().isDSAttached();

      if (isDSConnected) {
        purgeQueue();
        setPreset(LightEffect.Preset.DS_CONNECTED);
        setPreset(LightEffect.Preset.DISABLED);
      }
      else {
        purgeQueue();
        setPreset(LightEffect.Preset.DS_DISCONNECTED);
      }
    }
  }





  /**
   * Called in periodic if the lights are in shot mode
   * @return The new effect to be updated in the queue
   * @param effect The effect from the queue to act on
   */
  private LightEffect periodicShot(LightEffect effect) {

    if (firstTime) blankBuffer();

    final int trailLength = effect.m_shotTrailLength;
    final int intervalLength = effect.m_shotIntervalLength;
    final Color8Bit[] colors = effect.m_shotColors;

    final int[][] HSV = new int[colors.length][];
    for (int c = 0; c < colors.length; c++) {HSV[c] = LightEffect.getHSV(colors[c]);}

    final double[] valueIntervals = new double[colors.length];
    for (int d = 0; d < colors.length; d++) {valueIntervals[d] = Math.ceil((double)HSV[d][2]/(double)trailLength);}
    
    if (shot_locationArr.size() == 0 || shot_locationArr.get(shot_locationArr.size() - 1) > (trailLength + intervalLength)) {
      shot_locationArr.add(0);
    }


    for (int i = 0; i < shot_locationArr.size(); i++) {
      shot_locationArr.set(i, shot_locationArr.get(i) + 1);
      int shot = shot_locationArr.get(i);
      int alt = (i+shot_iter)%effect.m_shotColors.length;

      if (shot > trailLength + ledBuffer.getLength()) {
        shot_locationArr.remove(0);
        shot_iter++;
        break;
      }

        for (int k = 0; k < trailLength + intervalLength; k++) {
          int index = shot - k;
          int interval = (int)(HSV[alt][2] - (valueIntervals[alt] * k));

          if (interval < 0) interval = 0;
          if (index < 0 || index >= ledBuffer.getLength()) {continue;}

          ledBuffer.setHSV(index, HSV[alt][0], HSV[alt][1], interval);
        }
    }
    return effect;
  }
  /**
   * Called in periodic if the lights are in rainbow mode
   * @return The new effect to be updated in the queue
   * @param effect The effect from the queue to act on
   */
  private LightEffect periodicRainbow(LightEffect effect) {
    var firstPixel = effect.m_rainbowFirstPixelHue;
    for (int i = 0; i < ledBuffer.getLength(); i++) {
      final var hue = (firstPixel + (i * 180 / ledBuffer.getLength())) % 180;

      ledBuffer.setHSV(i, hue, 255, 128);
    }

    firstPixel += 2;
    firstPixel %= 180; // Check bounds
    effect.m_rainbowFirstPixelHue = firstPixel; //reset object
    return effect;
  }
  /**
   * Called in periodic if the lights are in solid mode
   * @return The new effect to be updated in the queue
   * @param effect The effect from the queue to act on
   */
  private LightEffect periodicSolid(LightEffect effect) {
    var color = effect.getColor();
    for (int i = 0; i < ledBuffer.getLength(); i++) {
      ledBuffer.setLED(i, color);
    }
    return effect;
  }
  /**
   * Called in periodic if the lights are in twinkle mode
   * @return The new effect to be updated in the queue
   * @param effect The effect from the queue to act on
   */
  private LightEffect periodicTwinkle(LightEffect effect) {

    if (firstTime) {setBuffer(effect.getColor());}

    int[] hsv = effect.getHSV();
    final int midSat = hsv[2];
    final int interval = Math.abs(128 - midSat);

    final double lightRate = 0.08;
    int[] saturationBuffer = new int[ledBuffer.getLength()];
    double random = Math.random();

    if (twinkle_runCount % 12 == 0) {
      twinkle_runCount = 0;
      for (int p = 0; p < saturationBuffer.length; p++) {
        if (saturationBuffer[p] == 0) saturationBuffer[p] = midSat; //Sets saturation to medium if the strip is off
      }
      
      for (int i = 0; i < saturationBuffer.length; i++) {
        if (Math.random() > lightRate) continue;
        if (saturationBuffer[i] < midSat && random >= 1/(midSat - saturationBuffer[i])) {saturationBuffer[i] += interval;} //More likely to trend toward medium saturation the farther away it is
        else if (saturationBuffer[i] > midSat && random >= 1/(saturationBuffer[i] - midSat)) {saturationBuffer[i] -= interval;} // ^^
        else {
          if (random > 0.5) {saturationBuffer[i] += interval * Math.random();}
          else {saturationBuffer[i] -= interval * Math.random();}
        } //Randomly move light in either direction
        ledBuffer.setHSV(i, hsv[0], hsv[1], saturationBuffer[i]);
      }
    }
    else if (twinkle_runCount % 4 == 0) {
      for (int i = 0; i < saturationBuffer.length; i++) {
        if (Math.random() > lightRate) continue;
        if (saturationBuffer[i] < midSat && random >= 1/(midSat - saturationBuffer[i])) {saturationBuffer[i] += interval;} //More likely to trend toward medium saturation the farther away it is
        else if (saturationBuffer[i] > midSat && random >= 1/(saturationBuffer[i] - midSat)) {saturationBuffer[i] -= interval;} // ^^
        ledBuffer.setHSV(i, hsv[0], hsv[1], saturationBuffer[i]);
      }
    }
    twinkle_runCount++;
    return effect;
  }

  private LightEffect periodicBlink(LightEffect effect) {
    if (firstTime) {
      blink_isOn = true;
      blink_runCount = 0;
    }

    var color = effect.getColor();
    blink_runCount++;
    if (blink_runCount > effect.m_blinkFlashRate/20) {
      for (int i = 0; i < ledBuffer.getLength(); i++) {
        if (!blink_isOn) ledBuffer.setLED(i, LightEffect.Color.BLACK.value);
        else ledBuffer.setLED(i, color);
      }
      blink_runCount = 0;
      blink_isOn = !blink_isOn;
    }
  
    return effect;
  }

  private LightEffect periodicHeartbeat(LightEffect effect) {
    var hsv = effect.getHSV();
    if (effect.m_value == 0) effect.m_value = 128;

    heartbeat_runCount++;
    //begining pulse 1 or pulse 2
    if (heartbeat_runCount == 10 || heartbeat_runCount == 28) {effect.m_value = 128 + 75;}
    //ending pulse 1 or pulse 2
    if (heartbeat_runCount >= 15 && heartbeat_runCount < 28 || heartbeat_runCount >= 38) {
      if (effect.m_value > 128) {effect.m_value -= 8;}
      else effect.m_value = 128;


      if (heartbeat_runCount == 70) heartbeat_runCount = 0;
    }

    for (int i = 0; i < ledBuffer.getLength(); i++) {
      ledBuffer.setHSV(i, hsv[0], hsv[1], effect.m_value);
    }

    return effect;
  }

  private LightEffect periodicBreathe(LightEffect effect) {
    var hsv = effect.getHSV();
    effect.m_value = 0;

    breathe_runCount += 0.06;
    if (breathe_runCount > 10.995) breathe_runCount = 4.712;

    effect.m_value = (int)(Math.sin(breathe_runCount) * 127.5 + 127.5);

    for (int i = 0; i < ledBuffer.getLength(); i++) {
      ledBuffer.setHSV(i, hsv[0], hsv[1], effect.m_value);
    }

    return effect;
  }






  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    
    checkVoltage(RobotController.getBatteryVoltage());
    checkDS();

    if (!queue.isEmpty()) {
      if (currentEffect == null) {
        currentEffect = queue.get(0);
        //logger.debug("Displaying effect: " + currentEffect.getPattern());
        firstTime = true;

        effectTimer.reset();
        effectTimer.start();
      }

      switch (currentEffect.getPattern()) {
        case RAINBOW:
          queue.set(0, periodicRainbow(currentEffect));
          break;
        case SHOT:
          queue.set(0, periodicShot(currentEffect));
          break;
        case SOLID:
          queue.set(0, periodicSolid(currentEffect));
          break;
        case TWINKLE:
          queue.set(0, periodicTwinkle(currentEffect));
          break;
        case BLINK:
          queue.set(0, periodicBlink(currentEffect));
          break;
        case BREATHE:
          queue.set(0, periodicBreathe(currentEffect));
          break;
        case HEARTBEAT:
          queue.set(0, periodicHeartbeat(currentEffect));
          break;
        default:
          break;
      }
      firstTime = false;

      if (passwordQueue.size() != 0) {
        for (int i = 0; i < passwordQueue.size(); i++) {
          String pass = passwordQueue.get(i);
          for (int k = 0; k < queue.size(); k ++) {
            if (queue.get(k).getPassword() != null && queue.get(k).getPassword().equals(pass)) {
              //Killing the effect
              //logger.debug("Removing effect: " + queue.get(k).getPattern() + " with password: " + pass);
              
              if (k == 0) currentEffect = null; //Sets the current effect to null, bringing in the next effect

              queue.remove(k); //removes the effect

            }
          } //end for k
        } //end for i
        passwordQueue.clear();
      }

      //Checks if there is an effect waiting on an indefinite effect or if there is a timer at limit
      if (currentEffect != null) {
        if (((currentEffect.getMilliseconds() == 0 && queue.size() > 1 && currentEffect.getPassword() == null && !queue.get(queue.size() - 1).getOverride()) || (effectTimer.get() * 1000 > currentEffect.getMilliseconds() && currentEffect.getMilliseconds() != 0))) {
          
          //Returns previous clause
          if (currentEffect.getMilliseconds() == 0 && queue.get(1).getReturnsPrevious() && !queue.get(queue.size() - 1).getOverride() && !currentEffect.getReturnsPrevious()) {
            currentEffect.setOverride(false);
            queue.add(currentEffect);
          } //returns previous if applicable

          //Killing the effect
          //logger.debug("Ending effect: " + currentEffect.getPattern());
          queue.remove(0);
          currentEffect = null;
        } 

        //Checks override effects
        if ((queue.size() > 1 && queue.get(queue.size() - 1).getOverride()) && currentEffect != null) {

          //returns previous clause
          if (queue.get(queue.size() - 1).getReturnsPrevious() && !currentEffect.getReturnsPrevious()) {
            currentEffect.setOverride(false);
            queue.add(1, currentEffect);
          }

          LightEffect priorityEffect = queue.get(queue.size() - 1);
          if (!currentEffect.equalTo(priorityEffect)) {
            logger.debug("Overriding " + currentEffect.getPattern());
            queue.remove(0);
            queue.add(0, priorityEffect);
            currentEffect = null;
          }
          else {
            queue.remove(queue.size() - 1);
          }
        }
      }
      if (queue.size() == 0) {effectTimer.reset();}
    }
    else blankBuffer(); //blanks the strip if nothing is active

    ledStrip.setData(ledBuffer);
  }

  /**
   * Sets the entire strip one color instantly
   */
  private void setBuffer(Color8Bit color) {
    for (int i = 0; i < ledBuffer.getLength(); i++) {
      ledBuffer.setLED(i, color);
    }
  }

  /**
   * Sets the strip to black instantly
   */
  private void blankBuffer() {setBuffer(new Color8Bit(0,0,0));}

  /**
   * Checks the size of the queue
   * @return The size of the current effect queue
   */
  public int getQueueSize() {return this.queue.size();}

  /**
   * Checks whether or not an effect is present in the queue
   * @param password The password to check for
   * @return Whether or not an effect with the given password is present
   */
  public boolean isEffectPresent(String password) {
    boolean present = false;
    for (int i = 0; i < this.queue.size(); i++) {
      if (this.queue.get(i).getPassword().equals(password)) {
        present = true;
      }
    }
    return present;
  }
}
