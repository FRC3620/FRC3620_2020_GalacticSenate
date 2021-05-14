package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class Figure24PathBBlueCommand extends SlalomCommandGroup {

    public Figure24PathBBlueCommand(DriveSubsystem driveSubsystem, IntakeSubsystem intakeSubsystem) {
        super(driveSubsystem);
        addCommands(
              
        new DeployIntakeCommand(intakeSubsystem),
                /*new WaitCommand(.2),

                new AutoDriveCommand(3 * 12, 90, 25, 180, driveSubsystem)
                ,*/
                new AutoDriveCommand(6 * 12, 65, 1, 180, driveSubsystem)
                ,
                new AutoDriveCommand(1.5 * 12, 90, 1, 180, driveSubsystem)
                ,
                new AutoDriveCommand(5 * 12, 160, 1, 180, driveSubsystem)
                ,
                new AutoDriveCommand(2 * 12, 90, 1, 180, driveSubsystem),
                new AutoDriveCommand(6 * 12, 25, 1, 180, driveSubsystem),
                new AutoDriveCommand(4 * 12, 90, 1, 180, driveSubsystem),
                new AutoIntakeArmUpCommand(intakeSubsystem),
                new AutoStopIntakeCommand(intakeSubsystem)
        );
    }
}
 