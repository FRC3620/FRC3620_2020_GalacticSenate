/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ArmSubsystem.TargetColor;

public class SpinControlPanelUntilColor extends CommandBase {
  ArmSubsystem armSubsystem = RobotContainer.armSubsystem;
  TargetColor previousColor = TargetColor.UNKNOWN;

  private int blueCounter = 0; 
  private int redCounter = 0; 
  private int greenCounter = 0; 
  private int yellowCounter = 0;
  private int rotationCount = 0; 
  public String blue = "B";
  public String red = "R";
  public String green = "G";
  public String yellow = "Y";
 /*
  Used to get the game message and make gameSpecificMessage the string
 
  public String getGameSpecificMessage(){
    return gameSpecificMessage;
    }
  
  */
    public String gameSpecificMessage = blue;

  //private final int resetCount = 0;
   


  /**
   * Creates a new SpinControlPanel4TimesCommand.
   */
  public SpinControlPanelUntilColor() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.armSubsystem);
  }

  // Called when the command is initially scheduled.
  //Blue, Red, Green, and Yellow counter are used to find the rotaion count for the control panel
  @Override
  public void initialize() {
    blueCounter = 0; 
    redCounter = 0; 
    greenCounter = 0; 
    yellowCounter = 0;
    rotationCount = 0; 
    RobotContainer.m_armMotor.set(0.5);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

      /*
      Used to display Blue, Red, Green, and Yellow counter and the DetectedColor on the SmartDashboard. 
      It also adds to Blue, Red, Green, and Yellow counter.

      */
    TargetColor currentColor = armSubsystem.getTargetColor();
    if(previousColor != currentColor) {
      String colorString;
      if (currentColor == TargetColor.BLUE) {
        blueCounter ++;
        colorString = "Blue";
      } else if (currentColor == TargetColor.RED) {
        redCounter ++;
        colorString = "Red";
      } else if (currentColor == TargetColor.GREEN) {
        greenCounter ++;
        colorString = "Green";
      } else if (currentColor == TargetColor.YELLOW) {
        yellowCounter ++;
        colorString = "Yellow";
      } else {
        colorString = "Unknown";
      }
      SmartDashboard.putString("Detected Color", colorString); 

      previousColor = currentColor;
    }

    /*

    A Series of if statements that checks the game message to see what color it is, then looks for the color a 
    quarter down the color wheel to stop spinning and scan the color for 5 seconds 
    */
    if(gameSpecificMessage.equals(blue)) {
      if(currentColor == TargetColor.RED ) {
        RobotContainer.m_armMotor.set(0);
      } 
   }
    if(gameSpecificMessage.equals(red)) {
      if(currentColor == TargetColor.BLUE ) {
        RobotContainer.m_armMotor.set(0);
      } 
    }
    if(gameSpecificMessage.equals(green)) {
      if(currentColor == TargetColor.YELLOW) {
        RobotContainer.m_armMotor.set(0);
      } 
    }
      if(gameSpecificMessage.equals(yellow)) {
      if(currentColor == TargetColor.GREEN ) {
        RobotContainer.m_armMotor.set(0);
      } 
    }
   
    //this checks Blue, Red, Green, and Yellow counter to see if all of them are above 2 and then reset them if they are
    //into 1 rotation count
    if(blueCounter >= 2 && redCounter >= 2 && greenCounter >= 2 && yellowCounter >= 2){
      blueCounter = 0; 
      redCounter = 0; 
      greenCounter = 0; 
      yellowCounter = 0;
      rotationCount ++;
    }
    
    
  
  
      //Displays how many times we've seen Blue, Red, Green, and Yellow.
    SmartDashboard.putString("Blue Counter", Integer.toString(blueCounter));
    SmartDashboard.putString("Red Counter", Integer.toString(redCounter)); 
    SmartDashboard.putString("Green Counter", Integer.toString(greenCounter)); 
    SmartDashboard.putString("Yellow Counter", Integer.toString(yellowCounter)); 
    SmartDashboard.putString("Rotation Counter", Integer.toString(rotationCount)); 
  


    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    armSubsystem.stopSpinningControlPanelWheel();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
   
  }
}
