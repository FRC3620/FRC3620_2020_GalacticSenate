package frc.robot.commands;

import frc.robot.subsystems.DriveSubsystem;

public class PixyTestPathARedCommand extends SlalomCommandGroup {

  private static final double SPEED = 0.25;

  public PixyTestPathARedCommand(DriveSubsystem driveSubsystem) {
    super(driveSubsystem);

    addCommands (
      new AutoDriveCommand(2 * 12, 90, SPEED, 180, driveSubsystem, "alpha", this)
    );
  }

}
