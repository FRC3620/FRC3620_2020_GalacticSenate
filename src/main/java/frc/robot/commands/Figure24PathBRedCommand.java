package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class Figure24PathBRedCommand extends SlalomCommandGroup {

    public Figure24PathBRedCommand(DriveSubsystem driveSubsystem, IntakeSubsystem intakeSubsystem) {
        super(driveSubsystem);
        addCommands(
                new ResetNavXCommand(driveSubsystem),
                new ZeroDriveEncodersCommand(driveSubsystem),
                new DeployIntakeCommand(intakeSubsystem),
                new WaitCommand(.2),

                new AutoDriveCommand(5 * 12, 90, .25, 180, driveSubsystem)
                ,
                new AutoDriveCommand(2.5 * 12, 180, .25, 180, driveSubsystem)
                ,
                new AutoDriveCommand(5 * 12, 90, .25, 180, driveSubsystem)
                ,
                new AutoDriveCommand(5 * 12, 0, .25, 180, driveSubsystem)
                ,
                new AutoDriveCommand(5 * 12, 90, .25, 180, driveSubsystem)
                ,
                new AutoDriveCommand(5 * 12, 180, .25, 180, driveSubsystem)
                ,
                new AutoDriveCommand(10 * 12, 90, .25, 180, driveSubsystem)
        );

    }
}
 