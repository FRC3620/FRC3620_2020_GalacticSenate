/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.VisionSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class GoldenAutoCommand extends SequentialCommandGroup {
  DriveSubsystem driveSubsystem;
  ShooterSubsystem shooterSubsystem;
  VisionSubsystem visionSubsystem;
  IntakeSubsystem intakeSubsystem;
  /**
   * Creates a new GoldenAutoCommand.
   */
  public GoldenAutoCommand(DriveSubsystem m_driveSubsystem, ShooterSubsystem m_shooterSubsystem,
  VisionSubsystem m_visionSubsystem, IntakeSubsystem m_intakeSubsystem) {
    this.driveSubsystem = m_driveSubsystem;
    this.intakeSubsystem = m_intakeSubsystem;
    this.shooterSubsystem = m_shooterSubsystem;
    this.visionSubsystem = m_visionSubsystem;
    addCommands(
      new ZeroDriveEncodersCommand(driveSubsystem),
      new DeployIntakeCommand(intakeSubsystem),
      new WaitCommand(.2),
      new AutoDriveCommand(7.3*12, -90, 0.5, 0, driveSubsystem),
      new WaitCommand(0.3),
      new AutoIntakeArmUpCommand(intakeSubsystem),
      new AutoDriveCommand(2*12, 45, 0.8, 0, driveSubsystem),
      new WaitCommand(.3),
      new AutoDriveCommand(8*12, 7, 0.65, 0, driveSubsystem),
      new AutoTurnVisionLightOnCommand(visionSubsystem),
      new AutoStopIntakeCommand(intakeSubsystem),
      new AutoSpinCommand(0.6, -150, driveSubsystem),
      new AutoStartIntakeCommand(intakeSubsystem),
      new WaitCommand(0.4),
      new DriveAndAlignCommand(driveSubsystem, visionSubsystem),
      new AutoStopIntakeCommand(intakeSubsystem),
      new AutoCreateShootingSolutionCommand(shooterSubsystem, visionSubsystem), 
      new AutoShootingCommand(shooterSubsystem, 4)
      
    /*new AutoSnapToHeadingCommand(-113, driveSubsystem),
      new WaitCommand(2),
      new AutoSnapToHeadingCommand(0, driveSubsystem),
      new WaitCommand(.1),
      new AutoDriveCommand(1*12, -63, 0.8, 0, driveSubsystem),
      new WaitCommand(.2),
      new AutoSemiElipseCommand(5, 1,  0.6, driveSubsystem),
      new AutoDriveCommand(9*12, -90, 0.8, 0, driveSubsystem),
      new WaitCommand(.2),
      new AutoSnapToHeadingCommand(-180, driveSubsystem*/
      );


  }
    
  
}
