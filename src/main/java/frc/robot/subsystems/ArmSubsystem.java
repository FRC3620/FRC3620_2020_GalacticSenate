/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import frc.robot.commands.ManuallyMoveColorMotor;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class ArmSubsystem extends SubsystemBase {
  WPI_TalonSRX armMotor = RobotContainer.m_armMotor;

  /**
   * Change the I2C port below to match the connection of your color sensor
   */
  private final I2C.Port i2cPort = I2C.Port.kOnboard;

  /**
   * A Rev Color Sensor V3 object is constructed with an I2C port as a 
   * parameter. The device will be automatically initialized with default 
   * parameters.
   */
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

    /**
   * A Rev Color Match object is used to register and detect known colors. This can 
   * be calibrated ahead of time or during operation.
   * 
   * This object uses a simple euclidian distance to estimate the closest match
   * with given confidence range.
   */
  private final ColorMatch m_colorMatcher = new ColorMatch();

  /**
   * Note: Any example colors should be calibrated as the user needs, these
   * are here as a basic example.
   */

  private final Color kBlueTarget = ColorMatch.makeColor(0.170, 0.455, 0.320);
  private final Color kGreenTarget = ColorMatch.makeColor(0.246, 0.506, 0.249);
  private final Color kRedTarget = ColorMatch.makeColor(0.379, 0.408, 0.205);
  private final Color kYellowTarget = ColorMatch.makeColor(0.314, 0.524, 0.124);

  public enum TargetColor {
    BLUE, GREEN, RED, YELLOW, UNKNOWN
  }



  /**
   * Creates a new ArmSubsystem.
   */
  public ArmSubsystem() {
    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kGreenTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kYellowTarget);   
    this.setDefaultCommand(new ManuallyMoveColorMotor(this)); 
  }

 



  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void spinControlPanelWheel() {
    if(armMotor != null){
      armMotor.set(0.5);
    }
  }

  public void spinControlPanelWheelSlow() {
    if(armMotor != null){
      armMotor.set(0.35);
    }
  }

  public void stopRunningMotor() {
    if(armMotor != null){
      armMotor.set(0);
    }
  }

  public void popArmUp() {
    RobotContainer.solenoidArmUp.set(true);
  }

  public void popArmDown() {
    RobotContainer.solenoidArmUp.set(false);
  }

  public void ManualColorMotor()  {
    if(armMotor != null){
      armMotor.set(RobotContainer.getColorJoystick());
    }
    
  }

  public Color getCurrentColor() {
        /**
     * The method GetColor() returns a normalized color value from the sensor and can be
     * useful if outputting the color to an RGB LED or similar. To
     * read the raw color, use GetRawColor().
     * 
     * The color sensor works best when within a few inches from an object in
     * well lit conditions (the built in LED is a big help here!). The farther
     * an object is the more light from the surroundings will bleed into the 
     * measurements and make it difficult to accurately determine its color.
     */

    return m_colorSensor.getColor();
  }

  public TargetColor getTargetColor() {
    String Color = "No colors yet";
    ColorMatchResult match = m_colorMatcher.matchClosestColor(getCurrentColor());

    if (match.color == kBlueTarget) {
      Color = "Blue"; //Sets the color as Blue
      SmartDashboard.putString("Color Sensed", Color); //Pushes the color to ShuffleBoard
      return TargetColor.BLUE;
    } else if (match.color == kRedTarget) {
      Color = "Red"; //Sets the color
      SmartDashboard.putString("Color Sensed", Color); //Pushes it
      return TargetColor.RED;
    } else if (match.color == kGreenTarget) {
      Color = "Green"; //Sets it
      SmartDashboard.putString("Color Sensed", Color); //Pushes it
      return TargetColor.GREEN;
    } else if (match.color == kYellowTarget) {
      Color = "Yellow"; //You get the idea
      SmartDashboard.putString("Color Sensed", Color);
      return TargetColor.YELLOW;
    } else {
      Color = "Unknown Color";
      SmartDashboard.putString("Color Sensed", Color);
      return TargetColor.UNKNOWN;
    }
  }
}
