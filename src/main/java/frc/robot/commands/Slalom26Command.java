/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;

public class Slalom26Command extends SlalomCommandGroup{

  /**
   * Creates a new Slalom26Command.
   */
  private static final double SPEED = 0.3;
  public Slalom26Command(DriveSubsystem driveSubsystem)
 {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(driveSubsystem);

    addCommands (
      new AutoSteerCommand(90, driveSubsystem, "-alpha-", this),
      new AutoDriveCommand (3.4*12, 90, SPEED, 180, driveSubsystem, "alpha", this)   //alpha
      ,
      new AutoSteerCommand(180, driveSubsystem, "-bravo-", this),
      new AutoDriveCommand (4*12, 180, SPEED, 180, driveSubsystem, "bravo", this)  // bravo
      ,
      new AutoSteerCommand(90, driveSubsystem, "-charlie-", this),
      new AutoDriveCommand (10*12, 90, .7, 180, driveSubsystem, "charlie", this)  // Charlie
      ,
      new AutoDriveCommand(1*12, 90, SPEED, 180, driveSubsystem),
      //new WaitCommand(1.0),
      new AutoSteerCommand(0, driveSubsystem, "-delta-", this),
      new AutoDriveCommand (5*12, 0, SPEED, 180, driveSubsystem, "delta", this)   // delta
      ,
      new AutoSteerCommand(90, driveSubsystem, "-echo-", this),
      new AutoDriveCommand (4*12, 90, SPEED, 180, driveSubsystem, "echo", this)  // echo
      ,
      new AutoSteerCommand(180, driveSubsystem, "-foxtrot-", this),
      new AutoDriveCommand (5*12, 180, SPEED, 180, driveSubsystem, "foxtrot", this) // foxtrot
      ,
      //new AutoSteerCommand(180, driveSubsystem, "-gamma-", this),
      new AutoSteerCommand(-90, driveSubsystem, "-gamma-", this),
      new AutoDriveCommand (4.5*12, -90, SPEED, 180, driveSubsystem, "gamma", this)  // gamma
      ,
      new AutoSteerCommand(0, driveSubsystem, "-hotel-", this),
      new AutoDriveCommand (5*12, 0, SPEED, 180, driveSubsystem, "hotel", this) // hotel
      ,
      new AutoSteerCommand(-90, driveSubsystem, "-india-", this),
      new AutoDriveCommand (12*12, -90, .5, 180, driveSubsystem, "india", this) // india
      ,
      new AutoSteerCommand(180, driveSubsystem, "-juliet-", this),
      new AutoDriveCommand (4*12, 180, SPEED, 180, driveSubsystem, "juliet", this)  //juliet
      ,
      new AutoSteerCommand(-90, driveSubsystem, "-kilo-", this),
      new AutoDriveCommand (3*12, -90, SPEED, 180, driveSubsystem, "kilo", this)  // kilo
    );
  }
}

