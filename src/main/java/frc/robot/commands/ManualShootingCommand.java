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
public class ManualShootingCommand extends AbstractShootingCommand {
  RumbleCommand rumbleCommandOperator;
  RumbleCommand rumbleCommandDriver;
  boolean weRumbled;
  
  public ManualShootingCommand(ShooterSubsystem subsystem) {
    super(subsystem);

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
    super.initialize();
    weRumbled = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    super.execute();
  }

  @Override
  void readyToShoot() {
    if (!weRumbled) {
      rumbleCommandOperator.schedule();
      rumbleCommandDriver.schedule();
      weRumbled = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}