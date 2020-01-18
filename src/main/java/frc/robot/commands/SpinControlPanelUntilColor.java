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
 // public String getGameSpecificMessage(){
    //return gameSpecificMessage;
    //}
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
    if(blueCounter >= 2 && redCounter >= 2 && greenCounter >= 2 && yellowCounter >= 2){
      blueCounter = 0; 
      redCounter = 0; 
      greenCounter = 0; 
      yellowCounter = 0;
      rotationCount ++;
    }
    
    
  
  

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
