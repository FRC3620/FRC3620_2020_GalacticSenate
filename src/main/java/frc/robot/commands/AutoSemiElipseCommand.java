/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

  Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
  
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
    logger.info ("eliipse beginning");
    initialPosition = driveSubsystem.getDriveMotorPositionRightFront(); //looks at the encoder on one drive motor
    currentHeading = driveSubsystem.getNavXFixedAngle();
    driveSubsystem.setTargetHeading(currentHeading);
    elipseConstant = Math.sqrt((radiusA*radiusA + radiusB*radiusB)/2);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    distanceTravelled = Math.abs(driveSubsystem.getDriveMotorPositionRightFront() - initialPosition);
    double angleSwept = 180 * distanceTravelled / elipseConstant;

    SmartDashboard.putNumber("ellipse.angleSwept", angleSwept);
    SmartDashboard.putNumber("ellipse.distancetravelled", distanceTravelled);
    SmartDashboard.putNumber("ellipse.elipseconstant", elipseConstant);

    driveSubsystem.timedDrive(angleSwept, speed, 0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    logger.info ("eliipse done");
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
