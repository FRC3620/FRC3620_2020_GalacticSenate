/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.Date;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import org.usfirst.frc3620.logger.FastDataLoggerCollections;
import org.usfirst.frc3620.logger.IFastDataLogger;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class AccelerationTestLoggerCommand extends CommandBase {
  IFastDataLogger accelerationTestLogger;
  CommandScheduler commandScheduler;
  TalonFX talonFX;
  /**
   * Creates a new AccelerationTestLoggerCommand.
   */
  public AccelerationTestLoggerCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    accelerationTestLogger = new FastDataLoggerCollections();
    accelerationTestLogger.setInterval(0.001);
    accelerationTestLogger.setMaxLength(10.0);
    if (talonFX != null) {
      accelerationTestLogger.addDataProvider("wheelVelocity", () -> talonFX.getSupplyCurrent());
      accelerationTestLogger.addDataProvider("encoderVelocity", () -> talonFX.getSupplyCurrent());
    } else {
      accelerationTestLogger.addDataProvider("t", () -> Timer.getFPGATimestamp());
    }
    accelerationTestLogger.setFilename("accelerationTest");
    accelerationTestLogger.setFilenameTimestamp(new Date());    
    accelerationTestLogger.start();
  }
  
  
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  accelerationTestLogger.done();
  }

  private void done() {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return commandScheduler.timeSinceScheduled(this) > 2.0;
  }
}
