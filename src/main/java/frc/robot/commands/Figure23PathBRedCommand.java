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
                new AutoDriveCommand(1 * 12, 110, 1, 180, driveSubsystem),
                new AutoDriveCommand(0.5 * 12, 70, 1, 180, driveSubsystem),
                new AutoDriveCommand(1.5 * 12, 30, 1, 180, driveSubsystem),
                new AutoDriveCommand(1.0 * 12, 10, 1, 180, driveSubsystem),
                new AutoDriveCommand(1.0 * 12, 30, 1, 180, driveSubsystem),
                new AutoDriveCommand(2.0 * 12, 90, 1, 180, driveSubsystem),
                new AutoDriveCommand(1.8 * 12, 155, 1, 180, driveSubsystem),
                new AutoDriveCommand(1.7 * 12, 170, 1, 180, driveSubsystem),
                new AutoDriveCommand(1.8 * 12, 155, 1, 180, driveSubsystem),
                new AutoDriveCommand(10.5 * 12, 90, 1, 180, driveSubsystem));
               // new AutoDriveCommand(2 * 12, 90, 75, 180, driveSubsystem),
               // new AutoDriveCommand(4 * 12, 60, 75, 180, driveSubsystem));
    }

    
}