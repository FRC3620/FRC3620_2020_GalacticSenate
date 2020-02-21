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
      new AutoDriveCommand(7*12, -90, 0, 0, driveSubsystem),
      new AutoDriveCommand(7.5*12, 17, 0, 0, driveSubsystem),
      new WaitCommand(.3),
      new AutoSnapToHeadingCommand(-113, driveSubsystem),
      new WaitCommand(2),
      new AutoSnapToHeadingCommand(-23, driveSubsystem),
      new WaitCommand(.1),
      new AutoDriveCommand(1.3*12, -63, -23, 0, driveSubsystem),
      new WaitCommand(.2),
      new AutoSemicircleCommand(5.5, 0.5, driveSubsystem),
      new AutoDriveCommand(10*12, -90, 0, 0, driveSubsystem),
      new WaitCommand(.2),
      new AutoSnapToHeadingCommand(-180, driveSubsystem));


  }
    
  
}
