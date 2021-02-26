package frc.robot.commands;

import frc.robot.subsystems.DriveSubsystem;

public class PixyTestPath2Command extends SlalomCommandGroup {

  private static final double SPEED = 0.25;

  public PixyTestPath2Command(DriveSubsystem driveSubsystem) {
    super(driveSubsystem);

    addCommands (
            new AutoDriveCommand(2 * 12, 180, SPEED, 180, driveSubsystem, "alpha", this)
    );
  }

}
