/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.Hand;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.RumbleSubsystem;

public class SaveSwerveAnalogEncodersSettingsCommand extends CommandBase {
  DriveSubsystem m_subsystem;
  Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
  /**
   * Creates a new SaveSwerveAnalogEncodersSettingsCommand.
   */
  public SaveSwerveAnalogEncodersSettingsCommand(DriveSubsystem subsystem) {
    super();
    // Use addRequirements() here to declare subsystem dependencies.
    m_subsystem = subsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (RobotContainer.isItOkToRecalibrateTheSwerveHeadings()){
      m_subsystem.saveCurrentAbsoluteEncoderOffsets();
      logger.info ("Swerve settings saved!");
    } else {
      logger.warn ("Cannot reset the swerve headings unless you hold down the bumpers");
      RumbleCommand(RobotContainer.rumbleSubsystemDriver,Hand.RIGHT,.5,3);
      //will state a warning and trigger rumblers
    }
  }

  private void RumbleCommand(RumbleSubsystem rumbleSubsystemDriver, Hand right, double d, int i) {
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
}
