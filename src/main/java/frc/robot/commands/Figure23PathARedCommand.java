package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class Figure23PathARedCommand extends SlalomCommandGroup {

    public Figure23PathARedCommand(DriveSubsystem driveSubsystem, IntakeSubsystem intakeSubsystem) {
        super(driveSubsystem);
        addCommands(

                new DeployIntakeCommand(intakeSubsystem)
                ,
                 new WaitCommand(.2)
                 ,
                new AutoDriveCommand(3 * 12, 90, 75, 180, driveSubsystem),
                new AutoDriveCommand(7 * 12, 50, 75, 180, driveSubsystem),
                new AutoDriveCommand(2 * 12, 90, 75, 180, driveSubsystem),
                new AutoDriveCommand(2 * 12, 160, 75, 180, driveSubsystem),
                new AutoDriveCommand(5 * 12, 230, 75, 180, driveSubsystem),
                new AutoDriveCommand(2 * 12, 150, 75, 180, driveSubsystem),
                new AutoDriveCommand(2 * 12, 90, 75, 180, driveSubsystem),
                new AutoDriveCommand(4 * 12, 60, 75, 180, driveSubsystem));
    }

    
}