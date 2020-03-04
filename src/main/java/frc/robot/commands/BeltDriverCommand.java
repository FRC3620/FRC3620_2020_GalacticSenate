/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.BeltSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * An example command that uses an example subsystem.
 */
public class BeltDriverCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final BeltSubsystem beltSubsystem;
  private double ERROR = (RobotContainer.shooterSubsystemFalcon1.getSelectedSensorVelocity() / RobotContainer.shooterSubsystem.trpm);

  public BeltDriverCommand(BeltSubsystem subsystem) {
    this.beltSubsystem = subsystem;
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(ERROR >= 0.98 && ERROR <= 1.02){
      RobotContainer.beltSubsystem.BeltOn(1);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.beltSubsystem.BeltOff();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(ERROR >= 0.98 && ERROR <= 1.02){
      return false;
    }
    return true;
  }
}