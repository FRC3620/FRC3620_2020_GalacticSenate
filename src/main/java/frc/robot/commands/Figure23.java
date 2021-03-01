package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.VisionSubsystem;



public class Figure23  extends SequentialCommandGroup {

    public Figure23(IntakeSubsystem intakeSubsystem, DriveSubsystem driveSubsystem) 




super();

if(
//blue
addCommands(
    new ZeroDriveEncodersCommand(driveSubsystem),
    new DeployIntakeCommand(intakeSubsystem),
    new WaitCommand(.2),
    new  AutoIntakeArmDownCommand(intakeSubsystem),
    new AutoStartIntakeCommand(intakeSubsystem),
new AutoDriveCommand(18*12, 90, .25, 180, driveSubsystem)
,
new AutoDriveCommand(5*12, 0, .25, 180, driveSubsystem)
, 
new AutoDriveCommand(2.5*12, 90, .25, 180, driveSubsystem)
,
new AutoDriveCommand(5*12, 180, .25, 180, driveSubsystem)
,
new AutoDriveCommand(2.5*12, 0, .25, 180, driveSubsystem),
new AutoDriveCommand(5.5*12, 0, .25, 180, driveSubsystem),
,
new AutoStopIntakeCommand(intakeSubsystem),

//red ball
new ZeroDriveEncodersCommand(driveSubsystem),
    new DeployIntakeCommand(intakeSubsystem),
    new WaitCommand(.2),
    new  AutoIntakeArmDownCommand(intakeSubsystem),
    new AutoStartIntakeCommand(intakeSubsystem),
new AutoDriveCommand(5*12, 90, .25, 180, driveSubsystem)
,
new AutoDriveCommand(2.5*12, 0, .25, 180, driveSubsystem)
, 
new AutoDriveCommand(5*12, 90, .25, 180, driveSubsystem)
,
new AutoDriveCommand(7.5*12, 180, .25, 180, driveSubsystem)
,
new AutoDriveCommand(20*12, 90, .25, 180, driveSubsystem),

new AutoIntakeArmUPCommand(intakeSubsystem),
new AutoStopIntakeCommand(intakeSubsystem)

}