package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;

/**
 * @author Noah Dressander (532377)
 * @version 17 January 2020
 * 
 * Finalised command -- rules in subsystem
 */
  public class IntakeCommand extends CommandBase {
    //<linking with subsystem>
    private IntakeSubsystem intakeSubsystem;
    private Timer timer = new Timer();
    private boolean isCurrentTooMuch;
    
    public IntakeCommand (IntakeSubsystem intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
    }
    //</linking with subsystem>


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    isCurrentTooMuch = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      if(intakeSubsystem.intakeCurrent > 40 && isCurrentTooMuch == false){
        timer.reset();
        timer.start();
        isCurrentTooMuch = true;
      }

      if(isCurrentTooMuch){
        
        intakeSubsystem.intakeSet(-0.3);

        if(timer.hasElapsed(4)){
          timer.stop();
          isCurrentTooMuch = false;
        }
      } else {
        intakeSubsystem.intakeSet(.6); //runs intake INWARD
      }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.intakeSet(0);  //runs intake OUTWARD until command runs again
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() { //starts/stops as button is pushed/released. Controlled in RobotContainer
      return(false);
  }
}