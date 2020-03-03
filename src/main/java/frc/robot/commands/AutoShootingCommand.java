/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.ShooterSubsystem;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * An example command that uses an example subsystem.
 */
public class AutoShootingCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final ShooterSubsystem shooterSubsystem;
  TalonFX talonFX;
  Timer timer = new Timer();
  
  public AutoShootingCommand(ShooterSubsystem subsystem) {
    this.shooterSubsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    talonFX = RobotContainer.shooterSubsystemFalcon1;
    
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double ERROR = (talonFX.getSelectedSensorVelocity() / RobotContainer.shooterSubsystem.trpm);
    RobotContainer.shooterSubsystem.ShootPID();
    if(ERROR >= 0.98 && ERROR <= 1.02) {
      RobotContainer.shooterSubsystem.BeltOn();
      timer.start();
    }
    //RobotContainer.shooterSubsystem.Shoot();
    //RobotContainer.shooterSubsystem.BeltOn();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooterSubsystem.ShooterOff();
    shooterSubsystem.BeltOff();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.hasElapsed(4);
  }
}