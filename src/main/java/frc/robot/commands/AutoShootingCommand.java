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
  Timer timer = new Timer();
  double shootingTime;
  
  public AutoShootingCommand(ShooterSubsystem subsystem, double duration) {
    super(subsystem);
    // should probably addRequirements() here to declare BeltSubsystem dependency.
    this.shootingTime = duration;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    super.initialize();

    timer.reset();
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    super.execute();
  }

  @Override
  void readyToShoot() {
    RobotContainer.beltSubsystem.BeltOn(1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.beltSubsystem.BeltOff();

    super.end(interrupted);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.hasElapsed(shootingTime);
  }
}