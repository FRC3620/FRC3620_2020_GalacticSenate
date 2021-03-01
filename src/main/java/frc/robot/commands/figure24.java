package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.VisionSubsystem;



public@ Figure24 (SequentialCommandGroup.ShooterSubsystem) {





public Figure23(DriveSubsystem driveSubsystem){ 

super();
if(
//blue
    addCommands(
        new ZeroDriveEncodersCommand(driveSubsystem),
        new DeployIntakeCommand(intakeSubsystem),
        new WaitCommand(.2),
        new  AutoIntakeArmDownCommand(intakeSubsystem),
        new AutoStartIntakeCommand(intakeSubsystem),
    new AutoDriveCommand(15*12, 90, .25, 180, driveSubsystem)
    ,
    new AutoDriveCommand(2.5*12, 0, .25, 180, driveSubsystem)
    , 
    new AutoDriveCommand(5*12, 90, .25, 180, driveSubsystem)
    ,
    new AutoDriveCommand(5*12, 180, .25, 180, driveSubsystem)
    ,
    new AutoDriveCommand(5*12, 90, .25, 180, driveSubsystem),
    new AutoDriveCommand(2.5*12, 0, .25, 180, driveSubsystem),
    new AutoDriveCommand(2.5*12, 90, .25, 180, driveSubsystem),
    new AutoIntakeArmUPCommand(intakeSubsystem),
    new AutoStopIntakeCommand(intakeSubsystem)





    //red
    new AutoDriveCommand(5*12, 90, .25, 180, driveSubsystem)
    ,
    new AutoDriveCommand(2.5*12, 180, .25, 180, driveSubsystem)
    ,
    new AutoDriveCommand(5*12, 90, .25, 180, driveSubsystem)
    ,
    new AutoDriveCommand(5*12, 0, .25, 180, driveSubsystem)
    ,
    new AutoDriveCommand(5*12, 90, .25, 180, driveSubsystem)
    ,
    new AutoDriveCommand(5*12, 180, .25, 180, driveSubsystem)
    ,
    new AutoDriveCommand(10*12, 90, .25, 180, driveSubsystem)
    )
    );
}
}
}