
package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AutoDriveCommand;
import frc.robot.subsystems.DriveSubsystem;



public class Slalom27Command extends SequentialCommandGroup{




public Slalom27Command(DriveSubsystem driveSubsystem){ 

super();

addCommands(
//new AutoDriveCommand(distance, strafeAngle, speed, heading, driveSubsystem)

//Straight 
new AutoDriveCommand (8.6*12, 90, .25, 180, driveSubsystem)
,
//Right
new AutoDriveCommand (3*12, 0, .25, 180, driveSubsystem)
,
//Back
new AutoDriveCommand (3*12, -90, .25, 180, driveSubsystem)
,
//Left
new AutoDriveCommand (3*12, 180, .25, 180, driveSubsystem)
,
//Straight
new AutoDriveCommand (10*12, 90, .25, 180, driveSubsystem)
,
//left
new AutoDriveCommand (5*12, 180, .25, 180, driveSubsystem)
,
//Back
new AutoDriveCommand(5*12, -90, .25, 180, driveSubsystem)
,
//Right
new AutoDriveCommand(10*12, 0, .25, 180, driveSubsystem)
,
//Straight
new AutoDriveCommand( 10*12, 90, .25, 180, driveSubsystem)
,
//left
new AutoDriveCommand( 5*12, 180, .25, 180, driveSubsystem)
,
//Back
new AutoDriveCommand( 22*12, -90, .25, 180, driveSubsystem)



);
}
}