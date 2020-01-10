package frc.robot.subsystems;

import frc.robot.Constants;
import frc.misc.ColorPattern;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;

/**
 * @author Nick Zimanski (SlippStream)
 * @version 9 January 2020
 */
public class LightSubsystem extends SubsystemBase {
  private AddressableLED ledStrip;
  private AddressableLEDBuffer ledBuffer;
  private ColorPattern pattern = ColorPattern.NONE;

  private int m_rainbowFirstPixelHue = 0;
  private int m_shotTrailLength;
  private int m_shotIntervalLength = 20;
  private Color8Bit m_shotTrailColor;
  /**
   * Instantiates the light strip and buffer. Refer to {@link frc.robot.RobotContainer RobotContainer}.lightSubsytem when making calls
   */
  public LightSubsystem() {
    ledStrip = new AddressableLED(Constants.PWM_LIGHT);
    ledBuffer = new AddressableLEDBuffer(Constants.LED_STRIP_LENGTH);

    ledStrip.setLength(ledBuffer.getLength());
    ledStrip.setData(ledBuffer);
    ledStrip.start();
  }

  /**
   * Sets the pattern of strip
   * @param pattern The ColorPattern enum to switch the strip to. Subject to queue
   */
  private void setPattern(ColorPattern pattern) {
    this.pattern = pattern;
  }

  /**
   * Removes the current strip pattern. Immediate -- not subject to queue
   */
  private void clearPattern() {
    this.pattern = ColorPattern.NONE;
  }

  /**
   * Sets a solid color throughout the entire string.
   * @param red The RGB code for red (0-255)
   * @param green The RGB code for green (0-255)
   * @param blue The RGB code for blue (0-255)
   */
  public void setSolidColor(int red, int green, int blue) {
    setPattern(ColorPattern.SOLID);

    for (int i = 0; i < ledBuffer.getLength(); i++) {
      ledBuffer.setRGB(i, red, green, blue);
    }
  }

  /**
   * Sets a solid color throughout the entire string.
   * @param color The WPILIB color reference for a specific color
   * @see Color
   */
  public void setSolidColor(Color color) {
    setPattern(ColorPattern.SOLID);

    for (int i = 0; i < ledBuffer.getLength(); i++) {
      ledBuffer.setLED(i, color);
    }
  }

  /**
   * Sets the current pattern to a default, Full-RGB rainbow
   */
  public void setRainbow() {
    setPattern(ColorPattern.RAINBOW);
  }

  /**
   * Called in periodic if the lights are in rainbow mode
   */
  private void periodicRainbow() {
    for (int i = 0; i < ledBuffer.getLength(); i++) {
      final var hue = (m_rainbowFirstPixelHue + (i * 180 / ledBuffer.getLength())) % 180;

      ledBuffer.setHSV(i, hue, 255, 128);
    }

    m_rainbowFirstPixelHue += 3;
    m_rainbowFirstPixelHue %= 180; // Check bounds
  }

  /**
   * Sets a 'shot' animation where a light runs down the strip with a trail
   * @param red The RGB code for red (0-255)
   * @param green The RGB code for green (0-255)
   * @param blue The RGB code for blue (0-255)
   */
  public void setShot(int red, int green, int blue) {
    m_shotTrailColor = new Color8Bit(red, green, blue);
    m_shotTrailLength = 8;
    setPattern(ColorPattern.SHOT);
  }

  /**
   * Sets a 'shot' animation where a light runs down the strip with a trail.
   * @param red The RGB code for red (0-255)
   * @param green The RGB code for green (0-255)
   * @param blue The RGB code for blue (0-255)
   * @param trailLength (Optional) The length, in pixels, of LEDs to light behind the shot. Defaults to 8.
   */
  public void setShot(int red, int green, int blue, int trailLength) {
    m_shotTrailColor = new Color8Bit(red, green, blue);
    m_shotTrailLength = trailLength;
    setPattern(ColorPattern.SHOT);
  }

  /**
   * Sets a 'shot' animation where a light runs down the strip with a trail
   * @param color The WPILIB color reference for a specific color
   * @see Color
   */
  public void setShot(Color color) {
    m_shotTrailColor = new Color8Bit(color);
    m_shotTrailLength = 8;
    setPattern(ColorPattern.SHOT);
  }

  /**
   * Sets a 'shot' animation where a light runs down the strip with a trail
   * @param color The WPILIB color reference for a specific color
   * @param (Optional) The length, in pixels, of LEDs to light behind the shot. Defaults to 8.
   * @see Color
   */
  public void setShot(Color color, int trailLength) {
    m_shotTrailColor = new Color8Bit(color);
    m_shotTrailLength = trailLength;
    setPattern(ColorPattern.SHOT);
  }

  /**
   * Called in periodic if the lights are in shot mode
   */
  private void periodicShot() {
    final double valueInterval = 128.0 / m_shotTrailLength;

    ArrayList<Integer> shots = new ArrayList<Integer>();
    if (shots.size() == 0 || shots.get(shots.size() - 1) > m_shotTrailLength + m_shotIntervalLength) {
      shots.add(0);
    }
    for (int i = 0; i < shots.size(); i++) {
      int shot = shots.get(i);
      double colorVal = 128.0;

      for (int k = 0; k < m_shotTrailLength; k++) {
        int index = shot - k;
        if (index < 0) {break;}
        int[] hsv = RGBToHSV(m_shotTrailColor.red, m_shotTrailColor.green, m_shotTrailColor.blue);
        ledBuffer.setHSV(index, m_shotTrailColor, 128, colorVal - (valueInterval * k));
      }
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    switch (this.pattern) {
      case RAINBOW:
        periodicRainbow();
        break;
      case SHOT:
        periodicShot();
        break;
      case TWINKLE:
        //Placeholder, no code yet
        break;
      case SOLID:
      case NONE:
      default:
        break;
    }

    ledStrip.setData(ledBuffer);
  }

  private int[] RGBToHSV(int r, int g, int b) {
    double red = r / 255.0;
    double green = g / 255.0;
    double blue = b / 255.0;

    //FINISH CONVERSION EQUATION
  }
}
