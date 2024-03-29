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

import org.usfirst.frc3620.logger.FastDataLoggerCollections;
import org.usfirst.frc3620.logger.IFastDataLogger;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * An example command that uses an example subsystem.
 */
public class ShootingCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final ShooterSubsystem shooterSubsystem;
  IFastDataLogger dataLogger;
  Timer timer;
  TalonFX talonFX;
  RumbleCommand rumbleCommandOperator;
  RumbleCommand rumbleCommandDriver;
  
  public ShootingCommand(ShooterSubsystem subsystem) {
    this.shooterSubsystem = subsystem;
    timer = new Timer();
    // Use addRequirements() here to declare subsystem dependencies.
    talonFX = RobotContainer.shooterSubsystemFalcon1;

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
    dataLogger = new FastDataLoggerCollections();
    dataLogger.setInterval(0.001);
    dataLogger.setMaxLength(10.0);
    if (talonFX != null) {
      dataLogger.addDataProvider("setpoint", () -> talonFX.getClosedLoopTarget());
      dataLogger.addDataProvider("rpm", () -> talonFX.getSelectedSensorVelocity());
      dataLogger.addDataProvider("outputCurrent", () -> talonFX.getStatorCurrent());
      dataLogger.addDataProvider("supplyCurrent", () -> talonFX.getSupplyCurrent());
      dataLogger.addDataProvider("outputVoltage", () -> talonFX.getMotorOutputVoltage());
      dataLogger.addDataProvider("supplyVoltage", () -> talonFX.getBusVoltage());
      dataLogger.addDataProvider("outputPercent", () -> talonFX.getMotorOutputPercent());
    } else {
      dataLogger.addDataProvider("t", () -> Timer.getFPGATimestamp());
    }
    dataLogger.setFilename("test");
    dataLogger.setFilenameTimestamp(new Date());
    //dataLogger.start();
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
    //dataLogger.done();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}