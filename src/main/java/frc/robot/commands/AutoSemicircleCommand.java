/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class AutoSemicircleCommand extends CommandBase {
  DriveSubsystem driveSubsystem;
  double currentHeading;
  double distanceTravelled;
  double initialPosition;
  double speed;
  double circleRadius; //in feet

  /**
   * Creates a new AutoSemicircleCommand.
   */
  public AutoSemicircleCommand(double radius, double speed, DriveSubsystem driveSubsystem) {
    circleRadius = radius*12; //convert to feet
    this.driveSubsystem = driveSubsystem;
    this.speed = speed;

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    initialPosition = driveSubsystem.getDriveMotorPosition(); //looks at the encoder on one drive motor
    currentHeading = driveSubsystem.getNavXFixedAngle();
    driveSubsystem.setTargetHeading(currentHeading);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    distanceTravelled = Math.abs(driveSubsystem.getDriveMotorPosition() - initialPosition);
    double angleSwept = distanceTravelled / circleRadius; //in radians

    double joyX = Math.sin(angleSwept); 
    double joyY = Math.cos(angleSwept);

    driveSubsystem.teleOpDrive(joyX, joyY, 0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSubsystem.teleOpDrive(0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (distanceTravelled > 3.1416*circleRadius){ // stop driving after we have travelled half a circumference
      return true;
    }
    return false;
  }
}
