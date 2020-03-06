/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.commands.RumbleCommand.Hand;
import frc.robot.subsystems.RumbleSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.VisionSubsystem;

public class CreateShootingSolutionCommand extends CommandBase {
  ShooterSubsystem shooterSubsystem;
  VisionSubsystem visionSubsystem;
  RumbleSubsystem rumbleSubsystem;
  RumbleCommand rumbleCommandOperator;
  RumbleCommand rumbleCommandDriver;
  Logger logger;
  /**
   * Creates a new MoveHoodManuallyUpCommand.
   */
  public CreateShootingSolutionCommand(ShooterSubsystem subsystem1, VisionSubsystem subsystem2, RumbleSubsystem subsystem3) {
    this.shooterSubsystem = subsystem1;
    this.visionSubsystem = subsystem2;
    this.rumbleSubsystem = subsystem3;
    logger = EventLogging.getLogger(getClass(), Level.INFO);

    rumbleCommandOperator = new RumbleCommand (RobotContainer.rumbleSubsystemOperator, Hand.RIGHT, //
    1.0, // intensity
    1.0 // duration
    );
    rumbleCommandDriver = new RumbleCommand (RobotContainer.rumbleSubsystemDriver, Hand.RIGHT, //
    1.0, // intensity
    1.0 // duration
    );
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(visionSubsystem.getShootingTargetAcquired() && visionSubsystem.getShootingTargetCentered()){
      double pixelHeight = visionSubsystem.getShootingTargetYCenter();
      double calcPosition = shooterSubsystem.calcHoodPosition(pixelHeight);
      double calcRPM = shooterSubsystem.calcTopRPM(pixelHeight);
      logger.info("pixel Height = {}, calculated Position = {}, calculated RPM = {}", pixelHeight, calcPosition, calcRPM);
      shooterSubsystem.setTopRPM(calcRPM);
      shooterSubsystem.setPosition(calcPosition);
      //rumbleSubsystem.setRumble(Hand.BOTH, 0.5);
      rumbleCommandOperator.schedule();
      rumbleCommandDriver.schedule();
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}