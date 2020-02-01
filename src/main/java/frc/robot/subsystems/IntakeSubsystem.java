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
  private final WPI_TalonFX intakeFalcon1 = RobotContainer.intakeSubsystemFalcon1; //intake motor 

  public IntakeSubsystem(){
  }
  /** 
  *Run intake: + = in, - = out 
  *@param speed double for intake speed
  *@author Sean Thursby (sthursbyg@gmail.com)
  *
  */
  public void intakeSet(double speed){ //runs intake at set velocity
    intakeFalcon1.set(speed);
  }

}
