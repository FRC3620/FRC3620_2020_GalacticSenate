/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.subsystems.DriveSubsystem;

public class Slalom26Command extends SlalomCommandGroup{

  /**
   * Creates a new Slalom26Command.
   */
  public Slalom26Command(DriveSubsystem driveSubsystem)
 {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(driveSubsystem);

    addCommands (
      new AutoDriveCommand (4*12, 90, 0.25, 180, driveSubsystem, "alpha", this)   //alpha
      ,
      new AutoDriveCommand (5*12, 180, 0.25, 180, driveSubsystem, "bravo", this)  // bravo
      ,
      new AutoDriveCommand (15*12, 90, 0.25, 180, driveSubsystem, "charlie", this)  // CHarlie
      ,
      new AutoDriveCommand (5*12, 0, 0.25, 180, driveSubsystem, "delta", this)   // delta
      ,
      new AutoDriveCommand (5*12, 90, 0.25, 180, driveSubsystem, "echo", this)  // echo
      ,
      new AutoDriveCommand (5*12, 180, 0.25, 180, driveSubsystem, "foxtrot", this) // foxtrot
      ,
      new AutoDriveCommand (4.5*12, -90, 0.25, 180, driveSubsystem, "gamma", this)  // gamma
      ,
      new AutoDriveCommand (5*12, 0, 0.25, 180, driveSubsystem, "hotel", this) // hotel
      ,
      new AutoDriveCommand (15*12, -90, 0.25, 180, driveSubsystem, "india", this) // india
      ,
      new AutoDriveCommand (5*12, 180, 0.25, 180, driveSubsystem, "juliet", this)  //juliet
      ,
      new AutoDriveCommand (3*12, -90, 0.25, 180, driveSubsystem, "kilo", this)  // kilo
    );
  }
}

