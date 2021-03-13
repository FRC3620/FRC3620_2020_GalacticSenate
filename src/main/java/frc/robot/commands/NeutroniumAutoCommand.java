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
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;

public class NeutroniumAutoCommand extends SequentialCommandGroup {
  Logger logger = EventLogging.getLogger(getClass(), EventLogging.Level.INFO);
  DriveSubsystem driveSubsystem;
  ShooterSubsystem shooterSubsystem;
  VisionSubsystem visionSubsystem;
  IntakeSubsystem intakeSubsystem;

  /**
   * Creates a new NeutroniumAutoCommand.
   */
  public NeutroniumAutoCommand(DriveSubsystem m_driveSubsystem, ShooterSubsystem m_shooterSubsystem,
                               VisionSubsystem m_visionSubsystem, IntakeSubsystem m_intakeSubsystem) {
    this.driveSubsystem = m_driveSubsystem;
    this.intakeSubsystem = m_intakeSubsystem;
    this.shooterSubsystem = m_shooterSubsystem;
    this.visionSubsystem = m_visionSubsystem;
    addCommands(

new ResetNavXCommand(driveSubsystem)
,
//new ZeroDriveEncodersCommand(driveSubsystem)


new DeployIntakeCommand(intakeSubsystem)
,
new WaitCommand(.2)
,
      new AutoDriveCommand(4*12,90,75,180, driveSubsystem)
      ,
      new AutoDriveCommand(3*12,35,75,180, driveSubsystem)
      ,
      new AutoDriveCommand(3*12,90,75,180, driveSubsystem)
      ,
      new AutoDriveCommand(2*12,160,75,180, driveSubsystem)
      ,
      new AutoDriveCommand(5*12,230,75,180, driveSubsystem)
      ,
      new AutoDriveCommand(2*12,150,75,180, driveSubsystem)
      ,
      new AutoDriveCommand(14*12, 85,75,180, driveSubsystem)
      ,
      new AutoIntakeArmUpCommand (intakeSubsystem)







     // new ZeroDriveEncodersCommand(driveSubsystem),
     // new SetHoodPositionCommand(45.0),
     // new MessageCommand (logger,"pretending to run around"),
     // new WaitCommand (2.0),

      /*
      new DeployIntakeCommand(intakeSubsystem),
      new WaitCommand(.2),
      new AutoDriveCommand(7.3*12, -90, 0.5, 0, driveSubsystem),
      new WaitCommand(0.3),
      */
     // new AutoIntakeArmUpCommand(intakeSubsystem),
      /*
      new AutoDriveCommand(2*12, 45, 0.8, 0, driveSubsystem),
      new WaitCommand(.3),
      new AutoDriveCommand(8*12, 7, 0.65, 0, driveSubsystem),
      */
      //new AutoTurnVisionLightOnCommand(visionSubsystem),
      /*
      new AutoStopIntakeCommand(intakeSubsystem),
      new AutoSpinCommand(0.6, -150, driveSubsystem),
      new AutoStartIntakeCommand(intakeSubsystem),
      new WaitCommand(0.4),
      */
     /* new MessageCommand (logger, "Aligning..."),
      new DriveAndAlignCommand(driveSubsystem, visionSubsystem),
      new MessageCommand (logger, "Aligned..."),
      new AutoStopIntakeCommand(intakeSubsystem),
      new MessageCommand (logger, "Creating Solution..."),
      new AutoCreateShootingSolutionCommand(shooterSubsystem, visionSubsystem),
      new MessageCommand (logger, "Shooting..."),
      new AutoShootingCommand(shooterSubsystem, 10),
      new MessageCommand (logger, "Done!")

      
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
