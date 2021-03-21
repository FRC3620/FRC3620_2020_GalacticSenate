package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class Figure23PathBblueCommand extends SlalomCommandGroup {

    public Figure23PathBblueCommand(DriveSubsystem driveSubsystem, IntakeSubsystem intakeSubsystem) {
        super(driveSubsystem);
        addCommands(

                new DeployIntakeCommand(intakeSubsystem)
                ,
                 new WaitCommand(.2)
                 ,
                new AutoDriveCommand(3 * 12, 90, 75, 180, driveSubsystem),
                new AutoDriveCommand(6 * 12, 65, 75, 180, driveSubsystem),
                new AutoDriveCommand(1.5 * 12, 90, 75, 180, driveSubsystem),
                new AutoDriveCommand(5 * 12, 160, 75, 180, driveSubsystem),
                new AutoDriveCommand(2 * 12, 90, 75, 180, driveSubsystem),
                new AutoDriveCommand(5.5 * 12, 25, 75, 180, driveSubsystem),
                new AutoDriveCommand(5 * 12, 90, 75, 180, driveSubsystem));
                //new AutoDriveCommand(4 * 12, 60, 75, 180, driveSubsystem));
    }

    
}