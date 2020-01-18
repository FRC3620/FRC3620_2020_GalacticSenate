package frc.robot.subsystems;

import org.slf4j.Logger;

import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.robot.RobotContainer;
/**
 * @author Sean Thursby 
 * @version 18 January 2020
 */
public class LiftSubsystem extends SubsystemBase {
  Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
  private final Solenoid liftRelease = RobotContainer.liftSubsystemRelease;           //solenoid fires lift upward
  private final Compressor liftCompressor = RobotContainer.liftSubsystemCompressor;   //commpressor feeds solenoid
  private final WPI_TalonFX liftController = RobotContainer.liftSubsystemWinch;       //motor lower lift on winch
  public LiftSubsystem(){
  }
public Boolean raiseLift() {  //fires lift upward
  liftRelease.set(true);
  return(true);
}

public Boolean allowLiftLower() {   //stops solenoid; allows lift to be lowered
  liftRelease.set(false);
  return(true);
}

public void lowerLift() {   //runs motor with winch
  liftController.set(0.5);
}

public void liftoff() {    //runs motor with winch backward
  liftController.set(0);
}


}
