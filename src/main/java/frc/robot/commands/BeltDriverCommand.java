/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.BeltSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * An example command that uses an example subsystem.
 */
public class BeltDriverCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final BeltSubsystem beltSubsystem;
  private final ShooterSubsystem shooterSubsystem;

  public BeltDriverCommand(BeltSubsystem beltSubsystem, ShooterSubsystem shooterSubsystem) {
    this.beltSubsystem = beltSubsystem;
    this.shooterSubsystem = shooterSubsystem;
    addRequirements(beltSubsystem);
    SmartDashboard.putBoolean("Hopper is Active", false);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double topError = (shooterSubsystem.getActualTopShooterVelocity() / shooterSubsystem.getRequestedTopShooterVelocity());
    double botError = (shooterSubsystem.getActualBottomShooterVelocity() / shooterSubsystem.getRequestedBottomShooterVelocity());
    if(topError >= 0.50 && topError <= 1.5 && botError >= 0.50 && botError <= 1.50) {
      beltSubsystem.BeltOn(.4);
      SmartDashboard.putBoolean("Hopper is Active", true);
    } else{
      beltSubsystem.BeltOff();
      SmartDashboard.putBoolean("Hopper is Active", false);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    beltSubsystem.BeltOff();
    SmartDashboard.putBoolean("Hopper is active", false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}