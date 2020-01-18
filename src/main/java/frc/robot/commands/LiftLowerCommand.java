package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LiftSubsystem;

/**
 * @author Noah Dressander (532377),Charlie Vaughn(Cvaughn123)
 * @version 18 January 2020
 * 
 * Finalised command -- rules in subsystem
 */
    public class LiftLowerCommand extends CommandBase {
      private LiftSubsystem liftSubsystem;

    public LiftLowerCommand (LiftSubsystem liftSubsystem) {
       this.liftSubsystem = liftSubsystem;
    }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    liftSubsystem.lowerLift(); //run winch motor 
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    liftSubsystem.liftoff(); // stop running motor
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}