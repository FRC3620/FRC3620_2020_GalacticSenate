
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveSubsystem;


public class Slalom25Command extends SequentialCommandGroup {
 
  public Slalom25Command(DriveSubsystem driveSubsystem) {
  


    super();
    //AutoSemiElipseCommand(double a, double b, double speed, DriveSubsystem driveSubsystem)
      //-180 left, 0 right, 90 foward, -90 back
    addCommands (
     // new AutoSemiElipseCommand(5, 3, 0, driveSubsystem),

      
     // Direction test
     /*
     new AutoDriveCommand(3 * 12, 0, .15, 180, driveSubsystem),
      new AutoDriveCommand(3 * 12, -90, .15, 180, driveSubsystem),
      new AutoDriveCommand(3 * 12, 180, .15, 180, driveSubsystem),
      new AutoDriveCommand(3 * 12, 90, .15, 180, driveSubsystem),
      new AutoSemiElipseCommand(5, 3, 0.01, driveSubsystem)
      */
      
      new AutoSteerCommand(90, driveSubsystem)
,
      new AutoDriveCommand (10.5*12, 90, .40, 180, driveSubsystem) //foward new barrel
,
      new AutoDriveCommand(4.1*12, 0, .35, 180, driveSubsystem)    //right
,
      new AutoDriveCommand (4.3*12, -90, .35, 180, driveSubsystem) //back
,
      new AutoDriveCommand (4.75*12, -180, .30, 180, driveSubsystem) //left
,
      new AutoDriveCommand (12*12, 90, .45, 180, driveSubsystem) //foward new barrel
,
      new AutoDriveCommand (4.25*12, -180, .40, 180, driveSubsystem) // left
,
      new AutoDriveCommand (6.5*12, -90, .40, 180, driveSubsystem) // back
,
      new AutoDriveCommand(3.5*12, 0, .35, 180, driveSubsystem) //right
,
      new AutoSteerCommand(45, driveSubsystem) //steer 45
,
      new AutoDriveCommand(8*12, 45, .45, 180, driveSubsystem) // diagonal
,
      new AutoDriveCommand(5*12, 90, .35, 180, driveSubsystem) //foward
,
      new AutoDriveCommand (3.4*12, -180, .35, 180, driveSubsystem) //left
      ,
      new AutoDriveCommand (21*12, -90, .70, 180, driveSubsystem) //return back
    );
  }
}