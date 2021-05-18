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
 * @author Jared Svetlev
 * @version 18 May 2021
 * 
 * Draft command
 * basically a merging of BeltDriverCommand.java and reverseIntakeCommand.java with intent to reverse the feeder when called
 */
public class reverseFeederCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final BeltSubsystem beltSubsystem;
  private final ShooterSubsystem shooterSubsystem;

  public reverseFeederCommand(BeltSubsystem beltSubsystem, ShooterSubsystem shooterSubsystem) {
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
    beltSubsystem.BeltOn(-.4);; // runs feeder OUTWARD
    SmartDashboard.putBoolean("Hopper is Active", true);
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
      return(false);
  }
}