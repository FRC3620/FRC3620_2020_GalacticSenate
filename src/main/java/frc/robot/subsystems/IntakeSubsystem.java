package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

import frc.robot.RobotContainer;

/**
 * @author Noah Dressander (532377)
 * @version 17 January 2020
 */
public class IntakeSubsystem extends SubsystemBase {
  private final CANSparkMax intakeSparkMax = RobotContainer.intakeSubsystemSparkMax; // intake motor
  private final Solenoid armDown = RobotContainer.intakeSubsystemArmDown;

  private double intakeCurrent = 0;
  private double intakePercentOut = 0;
  private double intakeVoltage = 0;

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
    //SmartDashboard.putBoolean("intakeSolenoid", armDown.get());
    if (intakeSparkMax != null) {
      intakeCurrent = intakeSparkMax.getOutputCurrent();
      intakePercentOut = intakeSparkMax.getAppliedOutput();
      intakeVoltage = intakeSparkMax.getBusVoltage();
      //SmartDashboard.putNumber("IntakeCurrent Output", intakeCurrent);
    }
  }

  public void moveArmDown() {
    armDown.set(true);
  }

  public void moveArmUp() {
    armDown.set(false);
  }

  public double getIntakePercentOut() {
    return intakePercentOut;
  }

  public double getIntakeCurrent() {
    return intakeCurrent;
  }

  public double getIntakeVoltage() {
    return intakeVoltage;
  }
}
