package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;

/**
 * @author Noah Dressander (532377)
 * @version 17 January 2020
 * 
 * Finalised command -- rules in subsystem
 */
  public class AutoStartIntakeCommand extends CommandBase {
    //<linking with subsystem>
    private IntakeSubsystem intakeSubsystem; 
    
    public AutoStartIntakeCommand (IntakeSubsystem intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
    }
    //</linking with subsystem>


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      intakeSubsystem.intakeSet(.6); //runs intake INWARD
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    //intakeSubsystem.intakeSet(0);  //runs intake OUTWARD until command runs again
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() { //starts/stops as button is pushed/released. Controlled in RobotContainer
      return(true);
  }
}