/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.Timer;

public class AutoShootingCommand extends AbstractShootingCommand {
  Timer commandTimer = new Timer();
  Timer spinupTimer;
  double shootingTime;
  boolean loggedBeltOn;
  
  public AutoShootingCommand(ShooterSubsystem subsystem, double duration) {
    super(subsystem);
    // should probably addRequirements() here to declare BeltSubsystem dependency.
    this.shootingTime = duration;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    super.initialize();

    logger.info ("Autoshooting for {} s", shootingTime);

    commandTimer.reset();
    commandTimer.start();
    loggedBeltOn = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    super.execute();

    if (spinupTimer != null) {
      if (! loggedBeltOn) {
        logger.info("spinup timer = {}, command timer = {}", spinupTimer.get(), commandTimer.get());
      }
      if (spinupTimer.hasElapsed(2.0)) {
        if (! loggedBeltOn) {
          logger.info ("spinning up belt");
          loggedBeltOn = true;
        }
        RobotContainer.beltSubsystem.BeltOn(1);
      }
    }
  }

  @Override
  void readyToShoot() {
    if (spinupTimer == null) {
      logger.info ("starting spinup timer");
      spinupTimer = new Timer();
      spinupTimer.reset();
      spinupTimer.start();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.beltSubsystem.BeltOff();
    commandTimer.stop();
    if (spinupTimer != null) {
      spinupTimer.stop();
    }

    super.end(interrupted);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return commandTimer.hasElapsed(shootingTime);
  }
}