/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class AutoSemiElipseCommand extends CommandBase {
  DriveSubsystem driveSubsystem;
  double currentHeading;
  double distanceTravelled;
  double initialPosition;
  double speed;
  double radiusA; //in feet
  double radiusB;
  double elipseConstant;

  /**
   * Creates a new AutoSemicircleCommand.
   */
  public AutoSemiElipseCommand(double a, double b, double speed, DriveSubsystem driveSubsystem) {
    this.radiusA = a*12; //convert to feet
    this.radiusB = b*12;
    this.driveSubsystem = driveSubsystem;
    this.speed = speed;

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    initialPosition = driveSubsystem.getDriveMotorPosition(); //looks at the encoder on one drive motor
    currentHeading = driveSubsystem.getNavXFixedAngle();
    driveSubsystem.setTargetHeading(currentHeading);
    elipseConstant = Math.sqrt((radiusA*radiusA + radiusB*radiusB)/2);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    distanceTravelled = Math.abs(driveSubsystem.getDriveMotorPosition() - initialPosition);
    double angleSwept = distanceTravelled / elipseConstant; //approximation to the perimeter of an ellipse in radians

    double joyX = radiusA*Math.sin(angleSwept); 
    double joyY = radiusB*Math.cos(angleSwept);

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
    if (distanceTravelled > 3.1416*elipseConstant){ // stop driving after we have travelled half a circumference
      return true;
    }
    return false;
  }
}
