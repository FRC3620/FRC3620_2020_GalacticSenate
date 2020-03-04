/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterRangeModAdjustCommand extends CommandBase {
  ShooterSubsystem shooterSubsystem;
  int direction; // positive one for up, negative one for down
  double sensitivity = 0.05; //the amount each button press changes the range modifier

  public ShooterRangeModAdjustCommand(int direction_) {
    shooterSubsystem = RobotContainer.shooterSubsystem;
    direction = direction_;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shooterSubsystem.modifyRangeModifer(direction*sensitivity);  
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
