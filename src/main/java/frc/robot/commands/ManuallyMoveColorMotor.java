/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.Logger;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ArmSubsystem.TargetColor;

public class ManuallyMoveColorMotor extends CommandBase {
  ArmSubsystem armSubsystem;
  TargetColor previousColor = TargetColor.UNKNOWN;

  //private final int resetCount = 0;
   


  /**
   * Creates a new SpinControlPanel4TimesCommand.
   */
  public ManuallyMoveColorMotor(ArmSubsystem armSubsystem) {
    this.armSubsystem = armSubsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(armSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
// System.out.println("Worked");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    armSubsystem.ManualColorMotor();

  }


  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

    armSubsystem.stopRunningMotor();

  }

  // Returns true when the command should end.
  // (To return true the rotation count must be greater than or equal to 4)
  @Override
  public boolean isFinished() {

    return false;
  }
}
