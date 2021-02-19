
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveSubsystem;

public class Slalom27Command extends SequentialCommandGroup {

    public Slalom27Command(DriveSubsystem driveSubsystem) {

        super();

        addCommands(

                // new AutoDriveCommand(distance, strafeAngle, speed, heading, driveSubsystem)

                new AutoDriveCommand(3.531 * 12, 90, .25, 180, driveSubsystem), //A forward 3.5' 
                new AutoDriveCommand(4 * 12, 180, .25, 180, driveSubsystem), //B to the left 4'
                 new AutoDriveCommand(3.5 * 12, 0, .25, 180, driveSubsystem), //C to the right 3'

                new AutoDriveCommand(3.3 * 12, 90, .25, 180, driveSubsystem), //D forward 3'
                new AutoDriveCommand(3.61* 12, 0, .25, 180, driveSubsystem), //E right 5'
                //new AutoDriveCommand(6 * 12, 45, .25, 180, driveSubsystem),
                new AutoDriveCommand(3.71 * 12, 90, .25, 180, driveSubsystem), //F forward 4'
                new AutoDriveCommand(8 * 12, 180, .25, 180, driveSubsystem), //G left 8'
                // pick up ball
                new AutoDriveCommand(7.12 * 12, 0, .25, 180, driveSubsystem), //H right 8'
                new AutoDriveCommand(7.54 * 12, 90, .25, 180, driveSubsystem),//I foward 7.5'
                new AutoDriveCommand(8.1 * 12, 180, .25, 180, driveSubsystem),//J left to hit
                new AutoDriveCommand(3.45 * 12, 0, .25, 180, driveSubsystem),//K back to center

                // pick up ball
                new AutoDriveCommand(3.8 * 12, 90, .25, 180, driveSubsystem)//L foward finish

        );
    }
}