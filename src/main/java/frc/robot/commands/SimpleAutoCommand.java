/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class SimpleAutoCommand extends SequentialCommandGroup {
  DriveSubsystem driveSubsystem;
  /**
   * Creates a new SimpleAutoCommand.
   */
  public SimpleAutoCommand(DriveSubsystem subsystem) {
    this.driveSubsystem = subsystem;
    addCommands(

      new AutoDriveCommand(4.3*12, 180, 0, 0, driveSubsystem),
      new AutoDriveCommand(22*12, 90, 0, 0, driveSubsystem),
      new AutoDriveCommand(21.5*12, -90, 0, 0, driveSubsystem),
      new AutoDriveCommand(4.3*12, 0, 0, 0, driveSubsystem));
  }
}
