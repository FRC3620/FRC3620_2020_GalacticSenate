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
     
      /*    Old Driving Pattern

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
      new AutoDriveCommand (1*12, -90, 0.25, 180, driveSubsystem)*/

      
      new AutoDriveCommand (4*12, 90, 0.25, 180, driveSubsystem)   //alpha
      ,
      new AutoDriveCommand (5*12, 180, 0.25, 180, driveSubsystem)  // bravo
      ,
      new AutoDriveCommand (15*12, 90, 0.25, 180, driveSubsystem)  // CHarlie
      ,
      new AutoDriveCommand (5*12, 0, 0.25, 180, driveSubsystem)   // delta
      ,
      new AutoDriveCommand (5*12, 90, 0.25, 180, driveSubsystem)  // echo
      ,
      new AutoDriveCommand (5*12, 180, 0.25, 180, driveSubsystem) // foxtrot
      ,
      new AutoDriveCommand (4.5*12, -90, 0.25, 180, driveSubsystem)  // gamma
      ,
      new AutoDriveCommand (5*12, 0, 0.25, 180, driveSubsystem) // hotel
      ,
      new AutoDriveCommand (15*12, -90, 0.25, 180, driveSubsystem) // india
      ,
      new AutoDriveCommand (5*12, 180, 0.25, 180, driveSubsystem)  //juliet
      ,
      new AutoDriveCommand (3*12, -90, 0.25, 180, driveSubsystem)  // kilo
      

    );
  }
}

