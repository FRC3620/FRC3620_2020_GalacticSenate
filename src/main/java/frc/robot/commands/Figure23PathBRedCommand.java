package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class Figure23PathBRedCommand extends SlalomCommandGroup {

    public Figure23PathBRedCommand(DriveSubsystem driveSubsystem, IntakeSubsystem intakeSubsystem) {
        super(driveSubsystem);
        addCommands(

                new DeployIntakeCommand(intakeSubsystem)
                ,
                 new WaitCommand(.2)
                 ,
                new AutoDriveCommand(1.5 * 12, 135, 1, 180, driveSubsystem),
                new AutoDriveCommand(2 * 12, 90, 1, 180, driveSubsystem),
                new AutoDriveCommand(4.5 * 12, 30, 1, 180, driveSubsystem),
                new AutoDriveCommand(2 * 12, 90, 1, 180, driveSubsystem),
                new AutoDriveCommand(6 * 12, 150, 1, 180, driveSubsystem),
                new AutoDriveCommand(10.5 * 12, 90, 1, 180, driveSubsystem));
               // new AutoDriveCommand(2 * 12, 90, 75, 180, driveSubsystem),
               // new AutoDriveCommand(4 * 12, 60, 75, 180, driveSubsystem));
    }

    
}