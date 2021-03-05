package frc.robot.commands;



import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.AutoDriveCommand;
import frc.robot.subsystems.DriveSubsystem;


public class QuarterTurnTestCommand extends SlalomCommandGroup{
    DriveSubsystem driveSubsystem;
    
    public QuarterTurnTestCommand (DriveSubsystem driveSubsystem){
        super(driveSubsystem);

    

    addCommands(
   // new AutoDriveCommand(distance, strafeAngle, speed, heading, driveSubsystem)
 
    new AutoDriveCommand(4 * 12, 90, .25, 180, driveSubsystem)
    ,
    new WaitCommand(.5)
    ,
    new AutoDriveCommand(.25 * 12, 105, .25, 180, driveSubsystem)
    ,
    new AutoDriveCommand(.25 * 12, 120, .25, 180, driveSubsystem)
    ,
    new AutoDriveCommand(.25 * 12, 135, .25, 180, driveSubsystem)
    ,
    new AutoDriveCommand(.25 * 12, 150, .25, 180, driveSubsystem)
    ,
    new AutoDriveCommand(.25 * 12, 165, .25, 180, driveSubsystem)
    ,
    new AutoDriveCommand(.25 * 12, 180, .25, 180, driveSubsystem)
    ,
    new WaitCommand(1)
    ,
   // new AutoSemiElipseCommand(3, 10, .3, driveSubsystem)
    );
    }
}