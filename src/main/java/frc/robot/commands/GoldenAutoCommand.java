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

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class GoldenAutoCommand extends SequentialCommandGroup {
  DriveSubsystem driveSubsystem;
  /**
   * Creates a new GoldenAutoCommand.
   */
  public GoldenAutoCommand(DriveSubsystem subsystem) {
    driveSubsystem = subsystem;
    addCommands(
      new ZeroDriveEncodersCommand(driveSubsystem),
      new AutoSnapToHeadingCommand(0, driveSubsystem),
      new WaitCommand(.1),
      new AutoDriveCommand(5*12, -90, 0.8, 0, driveSubsystem),
      new WaitCommand(0.3),
      new AutoDriveCommand(6.7*12, 17, 0.8, 0, driveSubsystem),
      new WaitCommand(.3),
      new AutoSnapToHeadingCommand(-113, driveSubsystem),
      new WaitCommand(2),
      new AutoSnapToHeadingCommand(0, driveSubsystem),
      new WaitCommand(.1),
      new AutoDriveCommand(1*12, -63, 0.8, 0, driveSubsystem),
      new WaitCommand(.2),
      new AutoSemiElipseCommand(5, 1,  0.6, driveSubsystem),
      new AutoDriveCommand(9*12, -90, 0.8, 0, driveSubsystem),
      new WaitCommand(.2),
      new AutoSnapToHeadingCommand(-180, driveSubsystem));


  }
    
  
}
