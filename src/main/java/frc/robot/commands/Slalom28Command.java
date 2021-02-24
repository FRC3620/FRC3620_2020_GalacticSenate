
package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveSubsystem;



public class Slalom28Command extends SequentialCommandGroup{




public Slalom28Command(DriveSubsystem driveSubsystem){ 

super();

addCommands(

//new AutoDriveCommand(distance, strafeAngle, speed, heading, driveSubsystem)

new AutoDriveCommand (3*12, 180, .25, 180, driveSubsystem)
,
new AutoDriveCommand (2*12, 90, .25, 180, driveSubsystem)
,

new AutoDriveCommand (7.43*12, 42.3, .25, 180, driveSubsystem)
,
new AutoDriveCommand (7*12, 90, .25, 180, driveSubsystem)
,
new AutoDriveCommand (7*12, 180, .25, 180, driveSubsystem)
,
new AutoDriveCommand (1.25*12, 90, .25, 180, driveSubsystem)
,
new AutoDriveCommand (13.4*12, 33, .25, 180, driveSubsystem)
,
new AutoDriveCommand (19.5*12, -90 , .25, 180, driveSubsystem)
,
new AutoDriveCommand (7.5*12, 180, .25, 180, driveSubsystem)
,
new AutoDriveCommand (7.825*12, -90, .25, 180, driveSubsystem)
,
//repeat lap
new AutoDriveCommand (3*12, 180, .25, 180, driveSubsystem)
,
new AutoDriveCommand (2*12, 90, .25, 180, driveSubsystem)
,
new AutoDriveCommand (7.43*12, 42.3, .25, 180, driveSubsystem)
,
new AutoDriveCommand (7*12, 90, .25, 180, driveSubsystem)
,
new AutoDriveCommand (7*12, 180, .25, 180, driveSubsystem)
,
new AutoDriveCommand (1.25*12, 90, .25, 180, driveSubsystem)
,
new AutoDriveCommand (13.4*12, 33, .25, 180, driveSubsystem)
,
new AutoDriveCommand (19.5*12, -90 , .25, 180, driveSubsystem)
,
//get into finish zone
new AutoDriveCommand (5.375*12, 180 , .25, 180, driveSubsystem)
,
new AutoDriveCommand (7.5*12, -90 , .25, 180, driveSubsystem)
,
new AutoDriveCommand (6*12, 0 , .25, 180, driveSubsystem)

);
}
}