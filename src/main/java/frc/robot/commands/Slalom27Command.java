
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveSubsystem;

public class Slalom27Command extends SlalomCommandGroup {

    public Slalom27Command(DriveSubsystem driveSubsystem) {

        super(driveSubsystem);

        addCommands(

                // new AutoDriveCommand(distance, strafeAngle, speed, heading, driveSubsystem)
                new AutoSteerCommand(90, driveSubsystem, "-alpha-", this),
                new AutoDriveCommand(3.531 * 12, 90, .25, 180, driveSubsystem, "alpha", this), // forward

                new AutoSteerCommand(180, driveSubsystem, "-beta-", this),
                new AutoDriveCommand(4 * 12, 180, .25, 180, driveSubsystem, "beta", this), //  to the left
                new AutoDriveCommand(3.5 * 12, 0, .25, 180, driveSubsystem, "charlie", this), // to the right

                new AutoSteerCommand(90, driveSubsystem, "-delta-", this),
                new AutoDriveCommand(3.3 * 12, 90, .25, 180, driveSubsystem, "delta", this), // forward

                new AutoSteerCommand(180, driveSubsystem, "-echo-", this),
                new AutoDriveCommand(3.61* 12, 0, .25, 180, driveSubsystem, "echo", this), // right 

                new AutoSteerCommand(90, driveSubsystem, "-foxtrot-", this),
                new AutoDriveCommand(3.71 * 12, 90, .25, 180, driveSubsystem, "foxtrot", this), // forward

                new AutoSteerCommand(180, driveSubsystem, "-golf-", this),
                new AutoDriveCommand(8 * 12, 180, .25, 180, driveSubsystem, "golf", this), // left
                new AutoDriveCommand(7.12 * 12, 0, .25, 180, driveSubsystem, "hotel", this), // right

                new AutoSteerCommand(90, driveSubsystem, "-india-", this),
                new AutoDriveCommand(7.54 * 12, 90, .25, 180, driveSubsystem, "india", this), // forrard

                new AutoSteerCommand(180, driveSubsystem, "-juliet-", this),
                new AutoDriveCommand(8.1 * 12, 180, .25, 180, driveSubsystem, "juliet", this), // left to hit
                new AutoDriveCommand(3.45 * 12, 0, .25, 180, driveSubsystem, "kilo", this), // back to center

                new AutoSteerCommand(90, driveSubsystem, "-lima-", this),
                new AutoDriveCommand(3.8 * 12, 90, .25, 180, driveSubsystem, "lima", this) // foward finish

        );
    }
}