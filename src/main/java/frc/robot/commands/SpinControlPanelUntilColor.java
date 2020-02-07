/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ArmSubsystem.TargetColor;

public class SpinControlPanelUntilColor extends CommandBase {
  ArmSubsystem armSubsystem = RobotContainer.armSubsystem;
  TargetColor previousColor = TargetColor.UNKNOWN;
  public String blue = "B";
  public String red = "R";
  public String green = "G";
  public String yellow = "Y";
 
  //Used to get the game message and make gameSpecificMessage the string
 
  
   

  
  
   

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
    RobotContainer.armSubsystem.popArmUp();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() { 
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    armSubsystem.stopSpinningControlPanelWheel();
    armSubsystem.popArmDown();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    
    /*
      Used to display Blue, Red, Green, and Yellow counter and the DetectedColor on the SmartDashboard. 
      It also adds to Blue, Red, Green, and Yellow counter.

      */
      TargetColor currentColor = armSubsystem.getTargetColor();
      if(previousColor != currentColor) {
        String colorString;
        if (currentColor == TargetColor.BLUE) {
       
          colorString = "Blue";
        } else if (currentColor == TargetColor.RED) {
        
          colorString = "Red";
        } else if (currentColor == TargetColor.GREEN) {
       
          colorString = "Green";
        } else if (currentColor == TargetColor.YELLOW) {
      
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
    String gameSpecificMessage = DriverStation.getInstance().getGameSpecificMessage();
    if(gameSpecificMessage.length() > 0) {
    if(!gameSpecificMessage.equals(blue) && !gameSpecificMessage.equals(red) && !gameSpecificMessage.equals(green) && !gameSpecificMessage.equals(yellow)) {
      //nothing
    }else  {
      RobotContainer.armSubsystem.spinControlPanelWheelSlow();
      if(gameSpecificMessage.equals(blue)) {
        if(currentColor == TargetColor.RED ) {
          return true;
        } 
     }
      if(gameSpecificMessage.equals(red)) {
        if(currentColor == TargetColor.BLUE ) {
          return true;
        } 
      }
      if(gameSpecificMessage.equals(green)) {
        if(currentColor == TargetColor.YELLOW) {
          return true;
        } 
      }
        if(gameSpecificMessage.equals(yellow)) {
        if(currentColor == TargetColor.GREEN ) {
          return true;
        } 
      }
    }
      
  
    }
    return false;
  }
}
