/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;


import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import java.util.Set;

import com.revrobotics.ColorMatch;
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

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
  private  int blueCounter = 0; 
  private  int redCounter = 0; 
  private  int greenCounter = 0; 
  private  int yellowCounter = 0;
  private  int rotationCount = 0; 
  private final int resetCount = 0;
  Color previousDetectedColor = kRedTarget;
   

  
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
   
      m_colorMatcher.addColorMatch(kBlueTarget);
      m_colorMatcher.addColorMatch(kGreenTarget);
      m_colorMatcher.addColorMatch(kRedTarget);
      m_colorMatcher.addColorMatch(kYellowTarget);    
    
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();


   
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
      Color detectedColor = m_colorSensor.getColor();
  
      /**
       * Run the color match algorithm on our detected color
       */
      String colorString;
      ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
  
      if(previousDetectedColor != match.color) {
        if (match.color == kBlueTarget) {
          blueCounter ++;
          colorString = "Blue";
          SmartDashboard.putNumber("Blue", detectedColor.blue);
          SmartDashboard.putString("Detected Color", colorString); 
        }
        else 
        if (match.color == kRedTarget) {
          redCounter ++;
          colorString = "Red";
          SmartDashboard.putNumber("Red", detectedColor.red);
          SmartDashboard.putString("Detected Color", colorString); 
        }
        else
        if (match.color == kGreenTarget) {
          greenCounter ++;
          colorString = "Green";
          SmartDashboard.putNumber("Green", detectedColor.green);
          SmartDashboard.putString("Detected Color", colorString); 

        }
        else
        if (match.color == kYellowTarget) {
          yellowCounter ++;
          colorString = "Yellow";
          SmartDashboard.putString("Detected Color", colorString); 
      
        }
        else {
          colorString = "Unknown";
          SmartDashboard.putString("Detected Color", colorString); 
        
      }
      previousDetectedColor = match.color;

      /* 
     //this is for testing 
      if(redCounter > 30) {
        redCounter = resetCount;
      } else if (blueCounter > 30){
        blueCounter = resetCount;
      }else if(greenCounter > 30){
        greenCounter = resetCount;
      }else if(yellowCounter > 30){
        yellowCounter = resetCount;
      }
      */
        //for testing 
        if(blueCounter >=  2&& redCounter >= 2 && greenCounter >= 2 && yellowCounter >= 2){
          rotationCount ++;
          redCounter = resetCount;
          blueCounter = resetCount;
          greenCounter = resetCount;
          yellowCounter = resetCount;

        }

    }
    SmartDashboard.putString("Blue Counter", Integer.toString(blueCounter));
    SmartDashboard.putString("Red Counter", Integer.toString(redCounter)); 
    SmartDashboard.putString("Green Counter", Integer.toString(greenCounter)); 
    SmartDashboard.putString("Yellow Counter", Integer.toString(yellowCounter)); 
    SmartDashboard.putString("Rotation Counter", Integer.toString(rotationCount)); 





      /**
       * Open Smart Dashboard or Shuffleboard to see the color detected by the 
       * sensor.
       */
      
    
}
  

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
