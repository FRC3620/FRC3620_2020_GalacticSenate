package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;

/**
 * @author Noah Dressander (532377),Charlie Vaughn(Cvaughn123)
 * @version 18 January 2020
 * 
 * Finalised command -- rules in subsystem
 */
    public class AutoIntakeArmUpCommand extends CommandBase {
      private IntakeSubsystem intakeSubsystem;

    public AutoIntakeArmUpCommand (IntakeSubsystem intakeSubsystem) {
       this.intakeSubsystem = intakeSubsystem;
    }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
    intakeSubsystem.moveArmUp();
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