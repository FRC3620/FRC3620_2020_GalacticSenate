/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.LiftSubsystem;
import edu.wpi.first.wpilibj.Timer;

public class MoveLiftCommand extends CommandBase {
  private LiftSubsystem liftSubsystem;
  Timer rainbowTimer = new Timer();
  /**
   * Creates a new MoveLif.
   */
  public MoveLiftCommand(LiftSubsystem m_liftSubsystem) {
    this.liftSubsystem = m_liftSubsystem;


    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_liftSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
   
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double liftPower = RobotContainer.getClimbingJoystick(); //Consistantly grabs the value of the climbing joystick
    liftSubsystem.liftPower(liftPower);
  }


  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    liftSubsystem.liftoff();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
