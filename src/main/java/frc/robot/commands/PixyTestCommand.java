/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.PixySubsystem;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import java.util.List;

public class PixyTestCommand extends CommandBase {
  Logger logger = EventLogging.getLogger(getClass(), EventLogging.Level.INFO);

  PixySubsystem pixySubsystem;

  public PixyTestCommand(PixySubsystem pixySubsystem) {
    super();
    this.pixySubsystem = pixySubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    List<PixySubsystem.PixyBlockPlus> blocks = RobotContainer.pixySubsystem.getLastBlockPluses();

    GalacticSearchPath whichPathToTake = PixyPathFinder.findPath(blocks);

    pixySubsystem.saveBlocksForDebugging(blocks, whichPathToTake);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }

}