package frc.robot.subsystems;

import org.slf4j.Logger;
import frc.robot.Constants;
import frc.misc.ColorPattern;
import frc.misc.LightEffect;
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
 * @version 11 January 2020
 */
public class LightSubsystem extends SubsystemBase {
  Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

  private AddressableLED ledStrip;
  private AddressableLEDBuffer ledBuffer;
  private LightEffect currentEffect;
  private Timer effectTimer = new Timer(); //Milliseconds = 0 on ANY parameter makes the effect indefinite and auto-skippable
  private ArrayList<LightEffect> queue = new ArrayList<LightEffect>();

  private final boolean defaultOverride = false;
  private final double defaultBatteryLowVoltage = 12.0; //Volts that, when dipped below, alert the lights
  private final int defaultMilliseconds = 3000;
  private final boolean defaultReturnsPrevious = false;
  private final Color8Bit defaultColor = new Color8Bit(0, 0, 0);

  private final int shot_defaultTrailLength = 15;
  private final int shot_defaultIntervalLength = 10;

  private final int blink_defaultBlinkRate = 500;
  private boolean blink_isOn = false;

  private int twinkle_runCount = 0;

  private double battery_voltageCheckTime = 0;
  private final int battery_voltageTimerInterval = 30; //number of seconds between low_voltage alerts

  ArrayList<Integer> shot_locationArr = new ArrayList<Integer>();

  /**
   * Instantiates the light strip and buffer. Refer to {@link frc.robot.RobotContainer#lightSubsystem RobotContainer.lightSubsytem} when making calls.
   * <p>
   * For example, {@code LightSubsystem lights = RobotContainer.lightSubsystem;}
   */
  public LightSubsystem() {
    ledStrip = new AddressableLED(Constants.PWM_LIGHT);
    ledBuffer = new AddressableLEDBuffer(Constants.LED_STRIP_LENGTH);

    ledStrip.setLength(ledBuffer.getLength());

    ledStrip.setData(ledBuffer);
    ledStrip.start();
    logger.info("Light Subsystem instantiated!");

    effectTimer.reset();
    effectTimer.start();
  }

  /**
   * Removes the current strip effect.
   */
  public void skipEffect() {queue.remove(0); logger.info("Effect removed!");}

  /**
   * Removes all effects from the current queue (Advanced users only)
   */
  public void purgeQueue() {queue.clear(); logger.info("Queue purged!");}

  /**
   * Adds a {@link frc.misc.LightEffect LightEffect} effect to the queue. (Advanced users only)
   * @param effect The {@link frc.misc.LightEffect LightEffect} object to add to queue 
   */
  public void setEffect(LightEffect effect) {queue.add(effect); logger.info("Queueing pattern: " + effect.pattern);};

  /**
   * Accepts a pattern and displays the preset animation if one exists for that pattern
   * @param pattern A {@link ColorPattern} pattern for which a preset exists
   */
  public void setPreset(ColorPattern.Preset pattern) {
    switch (pattern) {
      case INIT:
        setShot(new Color8Bit(0, 255, 0), 7500, true); //Light preset for Robot Init
        break;
      case TELEOP:
        if (DriverStation.getInstance().getAlliance() == Alliance.Red) {setBlink(new Color8Bit(255, 0, 0), 3000, true);}
        else {setBlink(new Color8Bit(0, 0, 255), 3000, true);}
        setTwinkle(new Color8Bit(0, 180, 0), 0, false);
        break;
      case AUTO:
        setShot(new Color8Bit(0, 255, 0), 0, false);
        break;
      case DISABLED:
        setTwinkle(new Color8Bit(100, 0, 0), 0, false);
        break;
      case TEST:
        setRainbow(0, false);
        break;
      case LOW_VOLTAGE:
        setBlink(new Color8Bit(255,0,0), 1000, true, 400, true);
      default:
        break;
    }
  }



  /**
   * Sets a solid color throughout the entire string.
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param override (Optional) Whether or not to skip the queue
   * @param milliseconds (Optional) The time, in milliseconds, to run the effect for
   * @param returnsPrevious (Optional) Whether or not to requeue the effect this overrides. Defaults to false
   * @see Color8Bit
   */
  public void setSolidColor(Color8Bit color, int milliseconds, boolean override, boolean returnsPrevious) {setEffect(new LightEffect(color, override, ColorPattern.Pattern.SOLID, milliseconds, returnsPrevious));}
  /**
   * Sets a solid color throughout the entire string.
   * @param red The RGB code for red (0-255)
   * @param green The RGB code for green (0-255)
   * @param blue The RGB code for blue (0-255)
   */
  public void setSolidColor(int red, int green, int blue) {setSolidColor(new Color8Bit(red, green, blue));}
  /**
   * Sets a solid color throughout the entire string.
   * @param color The WPILIB Color8Bit reference for a specific color
   * @see Color8Bit
   */
  public void setSolidColor(Color8Bit color) {setSolidColor(color, defaultMilliseconds);}
  /**
   * Sets a solid color throughout the entire string.
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param milliseconds The time, in milliseconds, to run the effect for
   * @see Color8Bit
   */
  public void setSolidColor(Color8Bit color, int milliseconds) {setSolidColor(color, milliseconds, defaultOverride);}
   /**
   * Sets a solid color throughout the entire string.
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param override (Optional) Whether or not to skip the queue
   * @param milliseconds (Optional) The time, in milliseconds, to run the effect for
   * @see Color8Bit
   */
  public void setSolidColor(Color8Bit color, int milliseconds, boolean override) {setSolidColor(color, milliseconds, override, defaultReturnsPrevious);;}

  
  
  /**
   * Sets the current pattern to a default, Full-RGB rainbow
   * @param milliseconds (Optional) The time, in milliseconds, to run the effect for.
   * @param override (Optional) Whether or not to skip the queue.
   * @param returnsPrevious (Optional) Whether or not to requeue the effect this overrides. Defaults to false
   */
  public void setRainbow(int milliseconds, boolean override, boolean returnsPrevious) {setEffect(new LightEffect(defaultColor, override, ColorPattern.Pattern.RAINBOW, milliseconds, returnsPrevious));}
  /**
   * Sets the current pattern to a default, Full-RGB rainbow
   */
  public void setRainbow() {setRainbow(defaultMilliseconds, defaultOverride, defaultReturnsPrevious);}
  /**
   * Sets the current pattern to a default, Full-RGB rainbow
   * @param milliseconds The time, in milliseconds, to run the effect for.
   */
  public void setRainbow(int milliseconds) {setRainbow(milliseconds, defaultOverride);}
  /**
   * Sets the current pattern to a default, Full-RGB rainbow
   * @param milliseconds The time, in milliseconds, to run the effect for.
   * @param override (Optional) Whether or not to skip the queue.
   */
  public void setRainbow(int milliseconds, boolean override) {setRainbow(milliseconds, override, defaultReturnsPrevious);}



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
    var effect = new LightEffect(color, override, ColorPattern.Pattern.SHOT, milliseconds, returnsPrevious);
    effect.m_shotIntervalLength = shotInterval;
    effect.m_shotTrailLength = trailLength;
    setEffect(effect);
  }
  /**
   * Sets a 'shot' animation where a light runs down the strip with a trail
   * @param red The RGB code for red (0-255)
   * @param green The RGB code for green (0-255)
   * @param blue The RGB code for blue (0-255)
   */
  public void setShot(int red, int green, int blue) {setShot(new Color8Bit(red, green, blue));}
  /**
   * Sets a 'shot' animation where a light runs down the strip with a trail
   * @param color The WPILIB Color8Bit reference for a specific color
   * @see Color8Bit
   */
  public void setShot(Color8Bit color) {setShot(color, defaultMilliseconds);}
  /**
   * Sets a 'shot' animation where a light runs down the strip with a trail.
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param milliseconds The time, in milliseconds, to run the effect for
   */
  public void setShot(Color8Bit color, int milliseconds) {setShot(color, milliseconds, defaultOverride);}
  /**
   * Sets a 'shot' animation where a light runs down the strip with a trail.
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param override Whether or not to skip the queue
   * @param milliseconds The time, in milliseconds, to run the effect for
   */
  public void setShot(Color8Bit color, int milliseconds, boolean override) {setShot(color, milliseconds, override, shot_defaultTrailLength);}
  /**
   * Sets a 'shot' animation where a light runs down the strip with a trail.
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param milliseconds The time, in milliseconds, to run the effect for
   * @param override Whether or not to skip the queue. Defaults to false.
   * @param trailLength The length, in pixels, of LEDs to light up as part of a shot
   */
  public void setShot(Color8Bit color, int milliseconds, boolean override, int trailLength) {setShot(color, milliseconds, override, trailLength, shot_defaultIntervalLength);}
  /**
   * Sets a 'shot' animation where a light runs down the strip with a trail
   * @param color The WPILIB color reference for a specific color
   * @param milliseconds (Optional) The time, in milliseconds, to run the effect for. Defaults to 3000
   * @param override (Optional) Whether or not to skip the queue. Defaults to false
   * @param trailLength (Optional) The length, in pixels, of LEDs to light behind the shot. Defaults to 15.
   * @param shotInterval (Optional) The length, in pixel, of LEDs between shots. Defaults to 10.
   * @see Color8Bit
   */
  public void setShot(Color8Bit color, int milliseconds, boolean override, int trailLength, int shotInterval) {setShot(color, milliseconds, override, trailLength, shotInterval, defaultReturnsPrevious);}





  /**
   * Sets a 'twinkle' animation on a solid color, where individual LEDs fluctuate brightness
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param milliseconds (Optional) The time, in milliseconds, to run the effect for. Defaults to 3000.
   * @param override (Optional) Whether or not to skip the queue. Defaults to false.
   * @param returnsPrevious (Optional) Whether or not to requeue the effect this overrides. Defaults to false
   */
  public void setTwinkle(Color8Bit color, int milliseconds, boolean override, boolean returnsPrevious) {setEffect(new LightEffect(color, override, ColorPattern.Pattern.TWINKLE, milliseconds, returnsPrevious));}
  /**
   * Sets a 'twinkle' animation on a solid color, where individual LEDs fluctuate brightness
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param override Whether or not to skip the queue
   */
  public void setTwinkle(Color8Bit color) {setTwinkle(color, defaultMilliseconds);}

  /**
   * Sets a 'twinkle' animation on a solid color, where individual LEDs fluctuate brightness
   * @param color The WPILIB Color8Bit reference for a specific color
   */
  public void setTwinkle(Color8Bit color, int milliseconds) {setTwinkle(color, milliseconds, defaultOverride);}
  /**
   * Sets a 'twinkle' animation on a solid color, where individual LEDs fluctuate brightness
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param milliseconds (Optional) The time, in milliseconds, to run the effect for. Defaults to 3000.
   * @param override (Optional) Whether or not to skip the queue. Defaults to false.
   */
  public void setTwinkle(Color8Bit color, int milliseconds, boolean override) {setEffect(new LightEffect(color, override, ColorPattern.Pattern.TWINKLE, milliseconds, defaultReturnsPrevious));}



  /**
   * Sets a 'blink' animation on a solid color, where the light flashes on and off
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param milliseconds (Optional) The time, in milliseconds, to run the effect for. Defaults to 3000.
   * @param override (Optional) Whether or not to skip the queue. Defaults to false.
   * @param blinkRate (Optional) How long, in milliseconds, the lights will take for a on/off cycle. For example if blinkRate is 200, The lights will be on for 0.1 seconds, off for 0.1 seconds and so on. Defaults to 500
   * @param returnsPrevious (Optional) Whether or not to requeue the effect this overrides. Defaults to false
   */
  public void setBlink(Color8Bit color, int milliseconds, boolean override, int blinkRate, boolean returnsPrevious) {
    var effect = new LightEffect(color, override, ColorPattern.Pattern.BLINK, milliseconds, returnsPrevious);
    effect.m_blinkFlashRate = blinkRate;
    setEffect(effect);
  }
   /**
   * Sets a 'blink' animation on a solid color, where the light flashes on and off
   * @param color The WPILIB Color8Bit reference for a specific color
   */
  public void setBlink(Color8Bit color) {setBlink(color, defaultMilliseconds);}
 /**
   * Sets a 'blink' animation on a solid color, where the light flashes on and off
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param milliseconds The time, in milliseconds, to run the effect for. Defaults to 3000.
   */
  public void setBlink(Color8Bit color, int milliseconds) {setBlink(color, milliseconds, defaultOverride);}
  /**
   * Sets a 'blink' animation on a solid color, where the light flashes on and off
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param milliseconds The time, in milliseconds, to run the effect for. Defaults to 3000.
   * @param override Whether or not to skip the queue. Defaults to false.
   */
  public void setBlink(Color8Bit color, int milliseconds, boolean override) {setBlink(color, milliseconds, override, blink_defaultBlinkRate);}
/**
   * Sets a 'blink' animation on a solid color, where the light flashes on and off
   * @param color The WPILIB Color8Bit reference for a specific color
   * @param milliseconds (Optional) The time, in milliseconds, to run the effect for. Defaults to 3000.
   * @param override (Optional) Whether or not to skip the queue. Defaults to false.
   * @param blinkRate (Optional) How long, in milliseconds, the lights will be on before being turn off. For example if blinkRate is 200, The lights will be on for 0.2 seconds, off for 0.2 seconds and so on. Defaults to 200
   */
  public void setBlink(Color8Bit color, int milliseconds, boolean override, int blinkRate) {setBlink(color, milliseconds, override, blinkRate, defaultReturnsPrevious);}



  private void checkVoltage() {
    if (RobotController.getBatteryVoltage() < defaultBatteryLowVoltage && Timer.getFPGATimestamp() > battery_voltageCheckTime + battery_voltageTimerInterval) {
      battery_voltageCheckTime = Timer.getFPGATimestamp();
      setPreset(ColorPattern.Preset.LOW_VOLTAGE);
    }
  }





  /**
   * Called in periodic if the lights are in shot mode
   * @return The new effect to be updated in the queue
   * @param effect The effect from the queue to act on
   */
  private LightEffect periodicShot(LightEffect effect) {
    final int trailLength = effect.m_shotTrailLength;
    final int intervalLength = effect.m_shotIntervalLength;
    final int[] hsv = effect.getHSV();
    final double valueInterval = hsv[2] / trailLength;

    
    if (shot_locationArr.size() == 0 || shot_locationArr.get(shot_locationArr.size() - 1) > trailLength + intervalLength) {
      shot_locationArr.add(0);
    }
    for (int i = 0; i < shot_locationArr.size(); i++) {
      shot_locationArr.set(i, shot_locationArr.get(i) + 1);
      int shot = shot_locationArr.get(i);

      if (shot > trailLength + ledBuffer.getLength()) {
        shot_locationArr.remove(0);
        break;
      }

      for (int k = 0; k < trailLength + intervalLength; k++) {
        int index = shot - k;
        int interval = (int)(hsv[2] - (valueInterval * k));

        if (interval < 0) interval = 0;
        if (index < 0 || index >= ledBuffer.getLength()) {continue;}

        ledBuffer.setHSV(index, hsv[0], hsv[1], interval);
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
    int[] hsv = effect.getHSV();
    final int midSat = hsv[2];
    final int interval = 100;

    final double lightRate = 0.05;
    int[] saturationBuffer = new int[ledBuffer.getLength()];
    double random = Math.random();

    if (twinkle_runCount % 15 == 0) {
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
    else if (twinkle_runCount % 5 == 0) {
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
    var color = effect.getColor();
    if ((Timer.getFPGATimestamp() * 1000) % effect.m_blinkFlashRate/2 < 10) {
      for (int i = 0; i < ledBuffer.getLength(); i++) {
        if (blink_isOn) ledBuffer.setLED(i, LightEffect.BLACK);
        else ledBuffer.setLED(i, color);
      }
      blink_isOn = !blink_isOn;
    }
    twinkle_runCount++;
    return effect;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    
    checkVoltage();

    if (!queue.isEmpty()) {
      if (currentEffect == null) {
      currentEffect = queue.get(0);
      logger.info("Displaying effect: " + currentEffect.pattern);
      }

      switch (currentEffect.pattern) {
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
        default:
          break;
      }


      //Checks if there is an effect waiting on an indefinite effect or if there is a timer at limit
      if ((currentEffect.milliseconds == 0 && queue.size() > 1) || (effectTimer.get() * 1000 > currentEffect.milliseconds && currentEffect.milliseconds != 0)) {
        if (currentEffect.milliseconds == 0 && queue.get(1).returnsPrevious) {queue.add(2, currentEffect);}

          logger.info("Ending effect: " + currentEffect.pattern);
          queue.remove(0);
          currentEffect = null;

          effectTimer.reset();
          effectTimer.start();
      }

      if (queue.size() > 1 && queue.get(queue.size() - 1).override) {
        LightEffect priorityEffect = queue.get(queue.size() - 1);
        purgeQueue();
        queue.add(0, priorityEffect);
        currentEffect = null;
      }

      if (queue.size() == 0) {effectTimer.reset();}
    }

    ledStrip.setData(ledBuffer);
  }
}
