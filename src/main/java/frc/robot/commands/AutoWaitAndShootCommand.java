/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.RumbleSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.VisionSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutoWaitAndShootCommand extends SequentialCommandGroup {
  DriveSubsystem driveSubsystem;
  ShooterSubsystem shooterSubsystem;
  VisionSubsystem visionSubsystem;
  IntakeSubsystem intakeSubsystem;

  /**
   * Creates a new SimpleAutoCommand.
   */
  public AutoWaitAndShootCommand(DriveSubsystem m_driveSubsystem, ShooterSubsystem m_shooterSubsystem,
      VisionSubsystem m_visionSubsystem, IntakeSubsystem m_intakeSubsystem) {
    this.driveSubsystem = m_driveSubsystem;
    this.shooterSubsystem = m_shooterSubsystem;
    this.visionSubsystem = m_visionSubsystem;
    this.intakeSubsystem = m_intakeSubsystem;
    addCommands(
      new AutoTurnVisionLightOnCommand(visionSubsystem),
      new ZeroDriveEncodersCommand(driveSubsystem),
      new SetShooterUpForTenFeetCommand(shooterSubsystem),
      new WaitCommand(8),
      new AutoShootingCommand(shooterSubsystem, 3),
      new AutoDriveCommand(4.7*12, 90, 0.5, 180, driveSubsystem)
      
      );
  }
}
