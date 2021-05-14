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

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.IFastDataLogger;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.wpilibj2.command.CommandBase;

import java.text.DecimalFormat;

public class ManualShootingCommand extends CommandBase {
  Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

  boolean shouldDoDataLogging = false;

  IFastDataLogger dataLogger;

  boolean weGotToSpeed;

  ShooterSubsystem shooterSubsystem;

  RumbleCommand rumbleCommandOperator;
  RumbleCommand rumbleCommandDriver;


  public ManualShootingCommand(ShooterSubsystem subsystem) {
    this.shooterSubsystem = subsystem;

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
    if (shouldDoDataLogging) {
      dataLogger = ShootingDataLogger.getShootingDataLogger("shooter_m");
      dataLogger.addDataProvider("we_got_to_speed", () -> weGotToSpeed ? 1 : 0);
      dataLogger.start();
    }
    weGotToSpeed = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    RobotContainer.shooterSubsystem.ShootPID();

    double ta = shooterSubsystem.getActualTopShooterVelocity();
    double ts = shooterSubsystem.getRequestedTopShooterVelocity();
    double terror = Double.NaN;
    if (ts != 0.0) {
      terror = ta / ts;
    }

    double ba = shooterSubsystem.getActualBottomShooterVelocity();
    double bs = shooterSubsystem.getRequestedBottomShooterVelocity();
    double berror = Double.NaN;
    if (ts != 0.0) {
      berror = ba / bs;
    }

    double b = RobotContainer.beltSubsystem.getFeederOutput();
    if (! weGotToSpeed) {
      double hoodSet = shooterSubsystem.getRequestedHoodPosition();
      double hoodAct = shooterSubsystem.getActualHoodPosition();
      logger.info (
              "tactual = {}, tsetpoint = {}, terror = {}, " +
                      "bactual = {}, bsetpoint = {}, berror = {}, " +
                      "hoodset = {}, hoodact = {}, belt = {}",
              f2(ta), f2(ts), f2(terror),
              f2(ba), f2(bs), f2(berror),
              f2(hoodSet), f2(hoodAct), f2(b));
    }
    if (terror >= 0.98 && terror <= 1.02 && berror >= 0.98 && berror <= 1.02) {
      if (!weGotToSpeed) {
        weGotToSpeed = true;
        logger.info("up to speed, rumbling...");
        rumbleCommandOperator.schedule();
        rumbleCommandDriver.schedule();
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooterSubsystem.ShooterOff();
    if (dataLogger != null) {
      // dataLogger.done();
      dataLogger = null;
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  private DecimalFormat f2Formatter = new DecimalFormat("#.##");

  private String f2(double f) {
    String rv = f2Formatter.format(f);
    return rv;
  }

}