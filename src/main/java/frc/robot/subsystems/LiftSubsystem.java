package frc.robot.subsystems;

import org.slf4j.Logger;

import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.XBoxConstants;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.robot.RobotContainer;
import frc.robot.commands.MoveLiftCommand;
import edu.wpi.first.wpilibj.util.Color8Bit;
/**
 * @author Sean Thursby
 * @version 18 January 2020
 */
public class LiftSubsystem extends SubsystemBase {
  Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
  private final Solenoid liftRelease = RobotContainer.liftSubsystemRelease; // solenoid fires lift upward
  private final WPI_TalonSRX liftController = RobotContainer.liftSubsystemWinch; // motor lower lift on winch

  public LiftSubsystem() {
    this.setDefaultCommand(new MoveLiftCommand(this));
  }

  public void raiseLift() { // fires lift upward *
    if (liftRelease != null) {
      liftRelease.set(true);
    }
  }

  public void allowLiftLower() { // stops solenoid; allows lift to be lowered
    if (liftRelease != null) {
      liftRelease.set(false);
    }
  }

  public void lowerLift() { // runs motor with winch
    if (liftController != null) {
      liftController.set(0.5);
    }
  }

  public void liftoff() { // runs motor with winch backward
    if (liftController != null) {
      liftController.set(0);
    }
  }
  
  
  public void liftPower(double speed) { // Runs lift controller based on joystick pos.
    if (liftController != null) {
      liftController.set(speed); //speed = speed passes through by moveliftcommand
        }
  }

}
