package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LiftSubsystem;

/**
 * @author Noah Dressander (532377),Charlie Vaughn(Cvaughn123)
 * @version 18 January 2020
 * 
 * Finalised command -- rules in subsystem
 */
public class LiftReleaseCommand extends CommandBase {
  private LiftSubsystem liftSubsystem;

  public LiftReleaseCommand (LiftSubsystem liftSubsystem) {
    this.liftSubsystem = liftSubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    liftSubsystem.releaseLift();  //turn on solenoid; fire lift.
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    liftSubsystem.liftReleaseOff();  //turn off solenoid before command ends
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}