package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.OrchestraSubsystem;

/**
 * @author Noah Dressander (532377)
 * @version 17 January 2020
 * 
 * Finalised command -- rules in subsystem
 */
  public class ConductOrchestraCommand extends CommandBase {

  private OrchestraSubsystem orchestraSubsystem;

  public ConductOrchestraCommand(OrchestraSubsystem orchestraSubsystem) {
        this.orchestraSubsystem = orchestraSubsystem;
        addRequirements(orchestraSubsystem);
    }
    //</linking with subsystem>


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    orchestraSubsystem.startMusic();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    orchestraSubsystem.stopMusic();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() { //starts/stops as button is pushed/released. Controlled in RobotContainer
      return(false);
  }
}