package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveSubsystem;



public class Figure23 extends SequentialCommandGroup{




public Figure23(DriveSubsystem driveSubsystem){ 

super();

addCommands(

//new AutoDriveCommand(distance, strafeAngle, speed, heading, driveSubsystem)

new AutoDriveCommand(5*12, 90, .25, 180, driveSubsystem)
,
//Pick up ball
new AutoDriveCommand(5.6*12, 50, .25, 180, driveSubsystem)
,
//Pick up ball
new AutoDriveCommand(8*12, 157.5, .25, 180, driveSubsystem)
,
//Pick up ball
new AutoDriveCommand(14, 90, .25, 180, driveSubsystem)


);
}
}