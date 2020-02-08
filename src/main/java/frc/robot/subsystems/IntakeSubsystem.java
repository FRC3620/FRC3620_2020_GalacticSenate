package frc.robot.subsystems;

import org.slf4j.Logger;

import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;

import frc.robot.RobotContainer;

/**
 * @author Noah Dressander (532377)
 * @version 17 January 2020
 */
public class IntakeSubsystem extends SubsystemBase {
  private final CANSparkMax intakeSparkMax = RobotContainer.intakeSubsystemSparkMax; // intake motor
  private final Solenoid holder = RobotContainer.intakeSubsystemHold;
  private final Solenoid outtake = RobotContainer.intakeSubsystemOut;

  public IntakeSubsystem(){
  }
  /** 
  *Run intake: + = in, - = out 
  *@param speed double for intake speed
  *@author Sean Thursby (sthursbyg@gmail.com)
  *
  */
  public void intakeSet(double speed){    //runs intake
    if (intakeSparkMax != null) {
      intakeSparkMax.set(speed);
    }
  }

  @Override
  public void periodic() {
    //System.out.println("boo " + intakeFalcon1.get());
  }

  public void armDown() {
    outtake.set(true);
  }

  public void armUp() {
    outtake.set(false);
  }

}
