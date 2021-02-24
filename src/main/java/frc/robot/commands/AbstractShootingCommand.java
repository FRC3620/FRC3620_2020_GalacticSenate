/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.ShooterSubsystem;

import java.util.Date;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.FastDataLoggerCollections;
import org.usfirst.frc3620.logger.IFastDataLogger;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.wpilibj2.command.CommandBase;

abstract class AbstractShootingCommand extends CommandBase {
  Logger logger;

  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final ShooterSubsystem shooterSubsystem;
  TalonFX rightTalonFX, bottomTalonFX, leftTalonFX;

  double lowerShooterSpeedLimit, upperShooterSpeedLimit;

  IFastDataLogger dataLogger;

  // not public
  AbstractShootingCommand(ShooterSubsystem subsystem) {
    // should probably use addRequirements() here to declare subsystem dependencies.
    this.shooterSubsystem = subsystem;

    logger = EventLogging.getLogger(getClass(), getDesiredLoggingLevel());

    rightTalonFX = RobotContainer.shooterSubsystemFalcon1;
    leftTalonFX = RobotContainer.shooterSubsystemFalcon2;
    bottomTalonFX = RobotContainer.shooterSubsystemFalcon3;

    // get and cache values. don't want to call these a zillion times in execute().
    lowerShooterSpeedLimit = defineLowerShooterSpeedLimit();
    upperShooterSpeedLimit = defineUpperShooterSpeedLimit();
  }

  // can override this in subclass to turn logging off
  boolean shouldDoDataLogging() {
    return true;
  }

  // can override this in subclass for different log level
  Level getDesiredLoggingLevel() {
    return Level.INFO;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (shouldDoDataLogging()) {
      dataLogger = new FastDataLoggerCollections();
      dataLogger.setInterval(0.005);
      dataLogger.setMaxLength(10.0);
      if (rightTalonFX != null) {
        dataLogger.addDataProvider("right_setpoint", () -> rightTalonFX.getClosedLoopTarget());
        dataLogger.addDataProvider("right_rpm", () -> rightTalonFX.getSelectedSensorVelocity());
        dataLogger.addDataProvider("right_outputCurrent", () -> rightTalonFX.getStatorCurrent());
        dataLogger.addDataProvider("right_supplyCurrent", () -> rightTalonFX.getSupplyCurrent());
        dataLogger.addDataProvider("right_outputVoltage", () -> rightTalonFX.getMotorOutputVoltage());
        dataLogger.addDataProvider("right_supplyVoltage", () -> rightTalonFX.getBusVoltage());
        dataLogger.addDataProvider("right_outputPercent", () -> rightTalonFX.getMotorOutputPercent());
        dataLogger.addDataProvider("right_error", () -> rightTalonFX.getClosedLoopError());
      }
      if (bottomTalonFX != null) {
        dataLogger.addDataProvider("bottom_setpoint", () -> bottomTalonFX.getClosedLoopTarget());
        dataLogger.addDataProvider("bottom_rpm", () -> bottomTalonFX.getSelectedSensorVelocity());
        dataLogger.addDataProvider("bottom_outputCurrent", () -> bottomTalonFX.getStatorCurrent());
        dataLogger.addDataProvider("bottom_supplyCurrent", () -> bottomTalonFX.getSupplyCurrent());
        dataLogger.addDataProvider("bottom_outputVoltage", () -> bottomTalonFX.getMotorOutputVoltage());
        dataLogger.addDataProvider("bottom_supplyVoltage", () -> bottomTalonFX.getBusVoltage());
        dataLogger.addDataProvider("bottom_outputPercent", () -> bottomTalonFX.getMotorOutputPercent());
        dataLogger.addDataProvider("bottom_error", () -> bottomTalonFX.getClosedLoopError());
      }
      if (leftTalonFX != null) {
        dataLogger.addDataProvider("left_setpoint", () -> leftTalonFX.getClosedLoopTarget());
        dataLogger.addDataProvider("left_rpm", () -> leftTalonFX.getSelectedSensorVelocity());
        dataLogger.addDataProvider("left_outputCurrent", () -> leftTalonFX.getStatorCurrent());
        dataLogger.addDataProvider("left_supplyCurrent", () -> leftTalonFX.getSupplyCurrent());
        dataLogger.addDataProvider("left_outputVoltage", () -> leftTalonFX.getMotorOutputVoltage());
        dataLogger.addDataProvider("left_supplyVoltage", () -> leftTalonFX.getBusVoltage());
        dataLogger.addDataProvider("left_outputPercent", () -> leftTalonFX.getMotorOutputPercent());
        dataLogger.addDataProvider("left_error", () -> leftTalonFX.getClosedLoopError());
      }
      dataLogger.setFilename("shooter");
      dataLogger.setFilenameTimestamp(new Date());
      dataLogger.start();
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    RobotContainer.shooterSubsystem.ShootPID();
    double a = shooterSubsystem.getActualTopShooterVelocity();
    double s = shooterSubsystem.getRequestedTopShooterVelocity();
    double error = Double.NaN;
    if (s != 0.0) {
      error = a / s;
    }
    double b = RobotContainer.beltSubsystem.getFeederOutput();
    logger.debug ("actual = {}, setpoint = {}, error = {}, belt = {}", a, s, error, b);
    if(error >= lowerShooterSpeedLimit && error <= upperShooterSpeedLimit) {
      readyToShoot();
    }
  }

  // override this in subclass if you want different limits.
  // this is invoked once and the value is cached.
  double defineLowerShooterSpeedLimit() {
    return 0.98;
  }

  // override this in subclass if you want different limits
  // this is invoked once and the value is cached.
  double defineUpperShooterSpeedLimit() {
    return 1.02;
  }

  // have to override this with whatever code you want to run when the shooter is up to speed.
  // it may get called multiple times!
  abstract void readyToShoot();

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.shooterSubsystem.ShooterOff();
    if (dataLogger != null) {
      logger.info ("being done with datalogger");
      dataLogger.done();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}