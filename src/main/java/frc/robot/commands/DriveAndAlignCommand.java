/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.VisionSubsystem;

public class DriveAndAlignCommand extends CommandBase {
  private DriveSubsystem driveSubsystem;
  private VisionSubsystem visionSubsystem;
  /**
   * Creates a new TeleOpDriveCommand.
   */
  public DriveAndAlignCommand(DriveSubsystem m_driveSubsystem, VisionSubsystem m_visionSubsystem) {
    this.driveSubsystem = m_driveSubsystem;
    this.visionSubsystem = m_visionSubsystem;
    addRequirements(m_driveSubsystem);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double strafeX = RobotContainer.getDriveHorizontalJoystick();
    double strafeY = RobotContainer.getDriveVerticalJoystick();
    double spinXDriver = RobotContainer.getDriveSpinJoystick();
    double spinX;
    
    spinX = spinXDriver;

    if (visionSubsystem.getShootingTargetPresent()){
      double yaw = visionSubsystem.getShootingTargetYaw();
      spinX = -0.03*yaw;
    }

    driveSubsystem.teleOpDrive(strafeX, strafeY, spinX);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSubsystem.teleOpDrive(0,0,0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}