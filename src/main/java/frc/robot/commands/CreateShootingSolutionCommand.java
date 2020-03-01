/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.RumbleCommand.Hand;
import frc.robot.subsystems.RumbleSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.VisionSubsystem;

public class CreateShootingSolutionCommand extends CommandBase {
  ShooterSubsystem shooterSubsystem;
  VisionSubsystem visionSubsystem;
  RumbleSubsystem rumbleSubsystem;
  double pixelHeight;
  double calcRPM;
  double calcPosition;
  /**
   * Creates a new MoveHoodManuallyUpCommand.
   */
  public CreateShootingSolutionCommand(ShooterSubsystem subsystem1, VisionSubsystem subsystem2, RumbleSubsystem subsystem3) {
    this.shooterSubsystem = subsystem1;
    this.visionSubsystem = subsystem2;
    this.rumbleSubsystem = subsystem3;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(visionSubsystem.getShootingTargetAcquired() && visionSubsystem.getShootingTargetCentered()){
      pixelHeight = visionSubsystem.getShootingTargetYCenter();
      calcPosition = shooterSubsystem.calcHoodPosition(pixelHeight);
      calcRPM = shooterSubsystem.calcTopRPM(pixelHeight);
      shooterSubsystem.setTopRPM(calcRPM);
      shooterSubsystem.setPosition(calcPosition);
      //rumbleSubsystem.setRumble(Hand.BOTH, 0.5);
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