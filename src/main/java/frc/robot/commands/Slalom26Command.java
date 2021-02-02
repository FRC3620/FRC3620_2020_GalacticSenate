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
public class Slalom26Command extends SequentialCommandGroup{

  /**
   * Creates a new Slalom26Command.
   */
  public Slalom26Command(DriveSubsystem driveSubsystem)
 {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super();

    addCommands (
      //Triangle Test
      //new AutoDriveCommand (1.0, 45, 0.25, 0, driveSubsystem),

      //new AutoDriveCommand (1.0, 0, 0.25, 0, driveSubsystem)

      new AutoDriveCommand (9*12, 146.4, 0.25, 180, driveSubsystem)
      ,
      new AutoDriveCommand (10*12, 90, 0.25, 180, driveSubsystem)
      ,
      new AutoDriveCommand (7.4*12, 42.5, 0.25, 180, driveSubsystem)
      ,
      new AutoDriveCommand (6.4*12, 132.5, 0.25, 180, driveSubsystem)
      ,
      new AutoDriveCommand (8*12, -90, 0.25, 180, driveSubsystem)
      ,
      new AutoDriveCommand (6.4*12, -42.5, 0.25, 180, driveSubsystem)
      ,
      new AutoDriveCommand (10*12, -90, 0.25, 180, driveSubsystem)
      ,
      new AutoDriveCommand (7.4*12, -132.5, 0.25, 180, driveSubsystem)
      ,
      new AutoDriveCommand (1*12, -90, 0.25, 180, driveSubsystem)

    );
  }
}

