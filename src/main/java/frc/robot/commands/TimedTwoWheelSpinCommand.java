/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.VisionSubsystem;

public class TimedTwoWheelSpinCommand extends CommandBase {
  DriveSubsystem driveSubsystem;
  VisionSubsystem visionSubsystem;
  Timer timer = new Timer();
  /**
   * Creates a new TimedTwoWheelSpinCommand.
   */
  public TimedTwoWheelSpinCommand(DriveSubsystem driveSubsystem, VisionSubsystem visionSubsystem) {
    this.driveSubsystem = driveSubsystem;
    this.visionSubsystem = visionSubsystem;
    boolean amICorrecting;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
    driveSubsystem.setForcedManualModeTrue();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double yaw = visionSubsystem.getShootingTargetYaw();
    boolean areWeCentered = visionSubsystem.getShootingTargetCentered();
    driveSubsystem.twoWheelRotation(0.3);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSubsystem.teleOpDrive(0, 0, 0); //stops drive
    driveSubsystem.setForcedManualModeFalse();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.hasElapsed(.1);
  }
}
