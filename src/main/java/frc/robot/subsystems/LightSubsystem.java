package frc.robot.subsystems;

import frc.robot.Constants;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * @author Nick Zimanski (SlippStream)
 * @version 8 January 2020
 */
public class LightSubsystem extends SubsystemBase {
  private AddressableLED ledStrip;
  private AddressableLEDBuffer ledBuffer;
  /**
   * Instantiates the light strip and buffer. Use sparingly
   */
  public LightSubsystem() {
    ledStrip = new AddressableLED(Constants.PWM_LIGHT);
    ledBuffer = new AddressableLEDBuffer(Constants.LED_STRIP_LENGTH);

    ledStrip.setLength(ledBuffer.getLength());
    ledStrip.setData(ledBuffer);
    ledStrip.start();
  }

  /**
   * @param red The RGB code for red (0-255)
   * @param green The RGB code for green (0-255)
   * @param blue The RGB code for blue (0-255)
   */
  public void setSolidColor(int red, int green, int blue) {
    for (int i = 0; i < ledBuffer.getLength(); i++) {
      ledBuffer.setRGB(i, red, green, blue);
    }

    ledStrip.setData(ledBuffer);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
