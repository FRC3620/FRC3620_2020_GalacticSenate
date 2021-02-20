/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem;

public class PixyTestCommand extends CommandBase {
  /**
   * Creates a new PixyTestCommand.
   */
  public PixyTestCommand(DriveSubsystem driveSubsystem) {
    super();
    // Use addRequirements() here to declare subsystem dependencies
    path1 = new PixyTestPath1(driveSubsystem);
    path2 = new PixyTestPath2(driveSubsystem);
  };


  Command path1, path2;

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    boolean isItThere = RobotContainer.pixySubsystem.isThereAPowerCell();
    if (isItThere) {
      SmartDashboard.putString ("pixy.isThereAPowerCell", "yep");
      path1.schedule();
    } else {
      SmartDashboard.putString ("pixy.isThereAPowerCell", "nope");
      path2.schedule();
    }
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
