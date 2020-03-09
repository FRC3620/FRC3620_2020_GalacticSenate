package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
  public double intakeCurrent;
  public double intakeVelocity;
  private CANEncoder intakeSparkEncoder = intakeSparkMax.getEncoder();

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
    intakeVelocity = intakeSparkEncoder.getVelocity();
    intakeCurrent = intakeSparkMax.getOutputCurrent();
    //System.out.println("boo " + intakeFalcon1.get());
    //SmartDashboard.putBoolean("intakeSolenoid", armDown.get());
    if (intakeSparkMax != null) {
      //SmartDashboard.putNumber("IntakeCurrent Output", intakeCurrent);
      //SmartDashboard.putNumber("Intake Velocity", intakeVelocity);
    }
  }

  public void moveArmDown() {
    armDown.set(true);
  }

  public void moveArmUp() {
    armDown.set(false);
  }
}
