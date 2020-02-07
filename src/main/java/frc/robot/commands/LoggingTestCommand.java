/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.Date;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import org.usfirst.frc3620.logger.FastDataLoggerCollections;
import org.usfirst.frc3620.logger.IFastDataLogger;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class LoggingTestCommand extends CommandBase {
  IFastDataLogger dataLogger;
  CommandScheduler commandScheduler;
  TalonFX talonFX;

  /**
   * Creates a new LoggingTestCommand.
   */
  public LoggingTestCommand(TalonFX _talonFX) {
    // Use addRequirements() here to declare subsystem dependencies.
    commandScheduler = CommandScheduler.getInstance();

    talonFX = _talonFX;

    int kTimeoutMs = 0;

    if (talonFX != null) {
      talonFX.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 1, kTimeoutMs);
      talonFX.setStatusFramePeriod(StatusFrameEnhanced.Status_4_AinTempVbat, 1, kTimeoutMs);
      talonFX.configSelectedFeedbackSensor(FeedbackDevice.Tachometer, 0, kTimeoutMs);
    }
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    dataLogger = new FastDataLoggerCollections();
    dataLogger.setInterval(0.001);
    dataLogger.setMaxLength(10.0);
    if (talonFX != null) {
      dataLogger.addDataProvider("rpm", () -> talonFX.getSelectedSensorVelocity());
      dataLogger.addDataProvider("outputCurrent", () -> talonFX.getStatorCurrent());
      dataLogger.addDataProvider("supplyCurrent", () -> talonFX.getSupplyCurrent());
      dataLogger.addDataProvider("outputVoltage", () -> talonFX.getMotorOutputVoltage());
      dataLogger.addDataProvider("supplyVoltage", () -> talonFX.getBusVoltage());
    } else {
      dataLogger.addDataProvider("t", () -> Timer.getFPGATimestamp());
    }
    dataLogger.setFilename("test");
    dataLogger.setFilenameTimestamp(new Date());    
    dataLogger.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    dataLogger.done();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return commandScheduler.timeSinceScheduled(this) > 2.0;
  }
}
