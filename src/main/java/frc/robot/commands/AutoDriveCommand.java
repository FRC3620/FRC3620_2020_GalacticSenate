/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class AutoDriveCommand extends CommandBase {

  private DriveSubsystem driveSubsystem;

  private double initialPositionRightFront;
  private double initialPositionLeftFront;
  private double initialPositionRightBack;
  private double initialPositionLeftBack;
  private double distanceTravelled;
  private double desiredDistance;
  private double desiredAngle;
  private double desiredHeading;
  private double pathSpeed;

  public AutoDriveCommand(double distance, double strafeAngle, double speed, double heading, DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
    addRequirements(driveSubsystem);

    desiredDistance = distance;
    desiredAngle = strafeAngle;
    desiredHeading = heading;
    pathSpeed = speed;

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    driveSubsystem.setAutoSpinMode();
    initialPositionRightFront = driveSubsystem.getDriveMotorPositionRightFront(); //looks at the encoder on one drive motor
    initialPositionLeftFront = driveSubsystem.getDriveMotorPositionLeftFront();
    initialPositionRightBack = driveSubsystem.getDriveMotorPositionRightBack();
    initialPositionLeftBack = driveSubsystem.getDriveMotorPositionLeftBack();
    driveSubsystem.setTargetHeading(desiredHeading);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double heading = driveSubsystem.getNavXFixedAngle(); 

    double currentPositionRightFront = driveSubsystem.getDriveMotorPositionRightFront();
    double currentPositionLeftFront = driveSubsystem.getDriveMotorPositionLeftFront();
    double currentPositionRightBack = driveSubsystem.getDriveMotorPositionRightBack();
    double currentPositionLeftBack = driveSubsystem.getDriveMotorPositionLeftBack();
    double spinX = -driveSubsystem.getSpinPower();
    driveSubsystem.timedDrive(desiredAngle, pathSpeed, spinX);

    double distanceTravelledRightFront = Math.abs(currentPositionRightFront - initialPositionRightFront);
    double distanceTravelledLeftFront = Math.abs(currentPositionLeftFront - initialPositionLeftFront);
    double distanceTravelledRightBack = Math.abs(currentPositionRightBack - initialPositionRightBack);
    double distanceTravelledLeftBack = Math.abs(currentPositionLeftBack - initialPositionLeftBack);

    distanceTravelled = (distanceTravelledRightFront + distanceTravelledLeftFront + distanceTravelledRightBack + distanceTravelledLeftBack)/4;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSubsystem.teleOpDrive(0,0,0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(distanceTravelled >= desiredDistance){
      return true;
    }
    return false;
  }
}
