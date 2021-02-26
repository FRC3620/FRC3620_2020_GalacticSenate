
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

      new AutoSteerCommand(90, driveSubsystem),
      new AutoDriveCommand (10.5*12, 90, .40, 180, driveSubsystem), //foward new barrel
      new AutoDriveCommand(4.1*12, 0, .35, 180, driveSubsystem),    //right
      new AutoDriveCommand (4.3*12, -90, .35, 180, driveSubsystem), //back
      new AutoDriveCommand (4.1*12, -180, .30, 180, driveSubsystem), //left
      new AutoDriveCommand (12*12, 90, .40, 180, driveSubsystem), //foward new barrel
      new AutoDriveCommand (4.5*12, -180, .40, 180, driveSubsystem), // left
      new AutoDriveCommand (5.5*12, -90, .35, 180, driveSubsystem), // back
      new AutoDriveCommand (8.5*12, 0, .35, 180, driveSubsystem), //right new barrel
      new AutoDriveCommand (10*12, 90, .40, 180, driveSubsystem), //foward 
      new AutoDriveCommand (3.7*12, -180, .35, 180, driveSubsystem), //left
      new AutoDriveCommand (21*12, -90, .40, 180, driveSubsystem) //return back
      
    );
  }
}