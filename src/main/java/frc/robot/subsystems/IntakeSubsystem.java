package frc.robot.subsystems;

import org.slf4j.Logger;

import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.robot.RobotContainer;
/**
 * @author Noah Dressander (532377)
 * @version 17 January 2020
 */
public class IntakeSubsystem extends SubsystemBase {
  Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
  private final WPI_TalonFX intakeFalcon1 = RobotContainer.intakeSubsystemFalcon1;

  public IntakeSubsystem(){
  }

  public void intakeSet(double speed){
    intakeFalcon1.set(speed);
  }
}
