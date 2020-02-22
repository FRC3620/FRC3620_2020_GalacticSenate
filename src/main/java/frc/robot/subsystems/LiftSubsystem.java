package frc.robot.subsystems;

import org.slf4j.Logger;

import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.LightEffect;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMax;

import frc.robot.RobotContainer;
import frc.robot.commands.MoveLiftCommand;
/**
 * @author Sean Thursby
 * @version 18 January 2020
 */
public class LiftSubsystem extends SubsystemBase {
  Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
  private final Solenoid liftRelease = RobotContainer.liftSubsystemRelease; // solenoid fires lift upward
  private final CANSparkMax liftController = RobotContainer.liftSubsystemWinch; // motor lower lift on winch

  public LiftSubsystem() {
    this.setDefaultCommand(new MoveLiftCommand(this));
  }

  public void releaseLift() { // releases lift
    if (liftRelease != null) {
      liftRelease.set(true);
    }
  }

  public void liftReleaseOff() { // turns off release
    if (liftRelease != null) {
      liftRelease.set(false);
    }
  }

  public void liftoff() { // turns off lift
    if (liftController != null) {
      liftController.set(0);
    }
  }
  
  
  public void liftPower(double speed) { // Runs lift controller based on joystick pos.
    if (liftController != null) {
      liftController.set(speed); //speed = speed passes through by moveliftcommand
      SmartDashboard.putNumber("Speed", speed);
    }
  }

}
