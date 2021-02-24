/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.commands.RumbleCommand.Hand;
import frc.robot.subsystems.ShooterSubsystem;

import java.util.Date;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.FastDataLoggerCollections;
import org.usfirst.frc3620.logger.IFastDataLogger;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * An example command that uses an example subsystem.
 */
public class ShootingCommand extends CommandBase {
  final private boolean doDataLogging = true;

  Logger logger = EventLogging.getLogger(getClass(), Level.INFO);


  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final ShooterSubsystem shooterSubsystem;
  IFastDataLogger dataLogger;
  Timer timer;
  TalonFX rightTalonFX, bottomTalonFX, leftTalonFX;
  RumbleCommand rumbleCommandOperator;
  RumbleCommand rumbleCommandDriver;
  
  public ShootingCommand(ShooterSubsystem subsystem) {
    this.shooterSubsystem = subsystem;
    timer = new Timer();
    // Use addRequirements() here to declare subsystem dependencies.
    rightTalonFX = RobotContainer.shooterSubsystemFalcon1;
    leftTalonFX = RobotContainer.shooterSubsystemFalcon2;
    bottomTalonFX =RobotContainer.shooterSubsystemFalcon3;
    rumbleCommandOperator = new RumbleCommand (RobotContainer.rumbleSubsystemOperator, Hand.RIGHT, //
    1.0, // intensity
    1.0 // duration
    );
    rumbleCommandDriver = new RumbleCommand (RobotContainer.rumbleSubsystemDriver, Hand.RIGHT, //
    1.0, // intensity
    1.0 // duration
    );
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.start();
    if (doDataLogging) {
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
    double error = (shooterSubsystem.getActualTopShooterVelocity() / shooterSubsystem.getRequestedTopShooterVelocity());
    if(error >= 0.98 && error <= 1.02) {
      rumbleCommandOperator.schedule();
      rumbleCommandDriver.schedule();
    }

    //RobotContainer.shooterSubsystem.Shoot();
    //RobotContainer.shooterSubsystem.BeltOn();
  }

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