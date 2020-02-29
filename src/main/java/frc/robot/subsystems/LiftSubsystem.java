package frc.robot.subsystems;

import org.slf4j.Logger;

import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;

import frc.robot.RobotContainer;
import frc.robot.commands.MoveLiftCommand;
/**
 * @author Sean Thursby
 * @version 18 January 2020
 */
public class LiftSubsystem extends SubsystemBase {
  private boolean lightTriggered = false;
  Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
  private final CANSparkMax liftController = RobotContainer.liftSubsystemWinch; // motor lower lift on winch
  private DoubleSolenoid brake = RobotContainer.liftBrake;

  public LiftSubsystem() {
    this.setDefaultCommand(new MoveLiftCommand(this));
  }

  public void liftoff() { // turns off lift
    if (liftController != null) {
      liftController.set(0);
    }
  }
  
  // Lift should move up from positive power 
  public void liftPower() { // Runs lift controller based on joystick pos.
    if (liftController != null) {
      double liftPower = RobotContainer.getClimbingJoystick(); //Consistantly grabs the value of the climbing joystick
      if(liftPower != 0) {
        brake.set(Value.kReverse);
        liftController.set(liftPower); //speed = speed passes through by moveliftcommand
      } else {
        liftController.set(0);
        brake.set(Value.kForward);
      }

      if (liftPower >= 0.2 && lightTriggered == false) { //Light effect stuff
        Color8Bit color = new Color8Bit(255, 255, 255); //Light Color
        RobotContainer.lightSubsystem.setShot(color, 5700, false, 255, 100, false); //Makes a new long light "shot"
        lightTriggered = true;
        //SmartDashboard.putNumber("Speed", liftPower);
      }
    }
  }
}
