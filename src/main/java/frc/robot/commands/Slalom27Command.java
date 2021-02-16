
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveSubsystem;

public class Slalom27Command extends SequentialCommandGroup {

    public Slalom27Command(DriveSubsystem driveSubsystem) {

        super();

        addCommands(

                // new AutoDriveCommand(distance, strafeAngle, speed, heading, driveSubsystem)

                new AutoDriveCommand(4 * 12, 90, .25, 180, driveSubsystem),
                new AutoDriveCommand(5 * 12, 180, .25, 180, driveSubsystem),

                new AutoDriveCommand(11.2 * 12, 26.5, .25, 180, driveSubsystem),
                new AutoDriveCommand(2.5 * 12, 90, .25, 180, driveSubsystem),
                new AutoDriveCommand(10 * 12, 180, .25, 180, driveSubsystem),
                new AutoDriveCommand(10 * 12, 0, .25, 180, driveSubsystem),
                // pick up ball
                new AutoDriveCommand(7.5 * 12, 90, .25, 180, driveSubsystem),
                new AutoDriveCommand(10 * 12, 180, .25, 180, driveSubsystem),
                new AutoDriveCommand(5 * 12, 0, .25, 180, driveSubsystem),
                // pick up ball
                new AutoDriveCommand(6 * 12, 90, .25, 180, driveSubsystem)

        );
    }
}