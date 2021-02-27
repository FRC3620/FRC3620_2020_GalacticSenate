package frc.robot.commands;

import frc.robot.subsystems.DriveSubsystem;

public class PixyTestPathABlueCommand extends SlalomCommandGroup {

  private static final double SPEED = 0.25;

  public PixyTestPathABlueCommand(DriveSubsystem driveSubsystem) {
    super(driveSubsystem);

    addCommands (
            new AutoDriveCommand(2 * 12, 180, SPEED, 180, driveSubsystem, "alpha", this)
    );
  }

}
