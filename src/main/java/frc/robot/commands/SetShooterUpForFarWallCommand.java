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

public class SetShooterUpForFarWallCommand extends CommandBase {
  ShooterSubsystem shooterSubsystem;

  final double wallTopRPM = 4100;
  final double wallHoodPosition = 0;

  /**
   * Creates a new MoveHoodManuallyUpCommand.
   */
  public SetShooterUpForFarWallCommand(ShooterSubsystem shooterSubsystem) {
    this.shooterSubsystem = shooterSubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.intakeSubsystem.intakeSet(0);
    shooterSubsystem.setTopRPM(wallTopRPM);
    shooterSubsystem.setPosition(wallHoodPosition);
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