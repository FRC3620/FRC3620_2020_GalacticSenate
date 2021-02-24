/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.VisionSubsystem;

public class AutoCreateShootingSolutionCommand extends CreateShootingSolutionCommand {
  public AutoCreateShootingSolutionCommand(ShooterSubsystem subsystem1, VisionSubsystem subsystem2) {
    // use CreateShootingSolutionCommand (see "extends CreateShootingSolutionCommand" above)
    super(subsystem1, subsystem2, false);
  }
}