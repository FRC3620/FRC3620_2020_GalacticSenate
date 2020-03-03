/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.VisionSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class SimpleAutoCommand extends SequentialCommandGroup {
  DriveSubsystem driveSubsystem;
  ShooterSubsystem shooterSubsystem;
  VisionSubsystem visionSubsystem;

  /**
   * Creates a new SimpleAutoCommand.
   */
  public SimpleAutoCommand(DriveSubsystem m_driveSubsystem, ShooterSubsystem m_shooterSubsystem, VisionSubsystem m_visionSubsystem) {
    this.driveSubsystem = m_driveSubsystem;
    this.shooterSubsystem = m_shooterSubsystem;
    this.visionSubsystem = m_visionSubsystem;
    addCommands(
      new ZeroDriveEncodersCommand(driveSubsystem),
      new AutoDriveAndAimCommand(2*12, -90, 0.3, 180, driveSubsystem, visionSubsystem),
      new AutoShootingCommand(shooterSubsystem),
      new AutoDriveCommand(7.7*12, -73, 0, 0, driveSubsystem),
      new AutoDriveCommand(12*12, -90, 0, 0, driveSubsystem),
      new AutoDriveCommand(8.5*12, 90, 0, 0, driveSubsystem),
      new SnapToHeadingCommand(180, driveSubsystem));
  }
}
