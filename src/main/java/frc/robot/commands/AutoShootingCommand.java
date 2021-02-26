/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.BeltSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.Timer;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.IFastDataLogger;

import java.text.DecimalFormat;

public class AutoShootingCommand extends CommandBase {
  Logger logger = EventLogging.getLogger(getClass(), EventLogging.Level.INFO);

  boolean shouldDoDataLogging = true;

  IFastDataLogger dataLogger;

  boolean weGotToSpeed;

  ShooterSubsystem shooterSubsystem;
  BeltSubsystem beltSubsystem;

  Timer commandTimer;
  Timer spinupTimer;

  double shootingTime;
  boolean weLoggedBeltOn;

  public AutoShootingCommand(ShooterSubsystem subsystem, double duration) {
    // should probably addRequirements() here to declare BeltSubsystem dependency.
    this.shooterSubsystem = subsystem;
    this.shootingTime = duration;

    // this is inconsistent with passing them in
    this.beltSubsystem = RobotContainer.beltSubsystem;

    commandTimer = new Timer();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    logger.info ("Autoshooting for {} s", shootingTime);

    if (shouldDoDataLogging) {
      dataLogger = ShootingDataLogger.getShootingDataLogger("shooter_a", this.shootingTime);
      dataLogger.addDataProvider("we_got_to_speed", () -> weGotToSpeed ? 1 : 0);
      dataLogger.start();
    }

    weGotToSpeed = false;
    weLoggedBeltOn = false;

    commandTimer.reset();
    commandTimer.start();
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

        logger.info ("up to speed, starting spinup timer");
        spinupTimer = new Timer();
        spinupTimer.reset();
        spinupTimer.start();
      }
    }

    if (spinupTimer != null) {
      if (spinupTimer.hasElapsed(1.0)) {
        if (! weLoggedBeltOn) {
          weLoggedBeltOn = true;
          logger.info ("spinning up belt");
        }
        RobotContainer.beltSubsystem.BeltOn(0.3);
      } else {
        logger.info("command timer = {}, spinup timer = {}", f2(commandTimer.get()), f2(spinupTimer.get()));
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.beltSubsystem.BeltOff();
    shooterSubsystem.ShooterOff();

    if (dataLogger != null) {
      dataLogger.done();
    }

    commandTimer.stop();
    if (spinupTimer != null) {
      spinupTimer.stop();
      spinupTimer = null;
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return commandTimer.hasElapsed(shootingTime);
  }

  private DecimalFormat f2Formatter = new DecimalFormat("#.##");

  private String f2(double f) {
    String rv = f2Formatter.format(f);
    return rv;
  }
}