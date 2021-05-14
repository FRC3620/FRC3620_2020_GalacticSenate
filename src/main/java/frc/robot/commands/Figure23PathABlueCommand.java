package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class Figure23PathABlueCommand extends SlalomCommandGroup {

    public Figure23PathABlueCommand(DriveSubsystem driveSubsystem, IntakeSubsystem intakeSubsystem) {
        super(driveSubsystem);
        addCommands(

                  new  DeployIntakeCommand(intakeSubsystem)
                ,
                 //new WaitCommand(.2)
                 //,
                //new AutoDriveCommand(3.62 * 12, 90, 90, 180, driveSubsystem),
                new AutoDriveCommand(6.25 * 12, 45, 90, 180, driveSubsystem),
                new AutoDriveCommand(2 * 12, 90, 90, 180, driveSubsystem),
                new AutoDriveCommand(2.41 * 12, 160, 90, 180, driveSubsystem),
                new AutoDriveCommand(3.75 * 12, 230, 90, 180, driveSubsystem), //4 to 3   
                new AutoDriveCommand(2.5 * 12, 150, 90, 150, driveSubsystem), // going back leg
                new AutoDriveCommand(2 * 12, 90, 90, 150, driveSubsystem),
                new AutoDriveCommand(5 * 12, 40, 90, 150, driveSubsystem)
                ,
                new AutoDriveCommand(5 * 12, 100, 90, 150, driveSubsystem)
                
        );
    }

    
    }

