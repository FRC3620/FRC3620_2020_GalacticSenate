/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.VisionSubsystem;

public class AutoDriveAndAimCommand extends CommandBase {

  private DriveSubsystem driveSubsystem;
  private VisionSubsystem visionSubsystem;

  private double initialPosition;
  private double distanceTravelled;
  private double desiredDistance;
  private double desiredAngle;
  private double desiredHeading;
  private double pathSpeed;

  public AutoDriveAndAimCommand(double distance, double strafeAngle, double speed, double heading, DriveSubsystem driveSubsystem, VisionSubsystem visionSubsystem) {
    this.driveSubsystem = driveSubsystem;
    this.visionSubsystem = visionSubsystem;
    addRequirements(driveSubsystem);

    desiredDistance = distance;
    desiredAngle = strafeAngle;
    desiredHeading = heading;
    pathSpeed = speed;

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    initialPosition = driveSubsystem.getDriveMotorPositionRightFront(); //looks at the encoder on one drive motor
    driveSubsystem.setTargetHeading(desiredHeading);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double heading = driveSubsystem.getNavXFixedAngle(); 

    double currentPosition = driveSubsystem.getDriveMotorPositionRightFront();
    double spinX = -driveSubsystem.getSpinPower();
    if (!visionSubsystem.getShootingTargetCentered()){
      double yaw = visionSubsystem.getShootingTargetYaw();
      spinX = -0.025*yaw;// + -(yaw/Math.abs(yaw))*0.1; //absolute value division gets sign of yaw
    }
    driveSubsystem.timedDrive(desiredAngle, pathSpeed, spinX);

    distanceTravelled = Math.abs(currentPosition - initialPosition);
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
