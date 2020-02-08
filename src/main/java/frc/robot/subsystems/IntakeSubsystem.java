package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax;

import frc.robot.RobotContainer;

/**
 * @author Noah Dressander (532377)
 * @version 17 January 2020
 */
public class IntakeSubsystem extends SubsystemBase {
  private final CANSparkMax intakeSparkMax = RobotContainer.intakeSubsystemSparkMax; // intake motor
  private final Solenoid holder1 = RobotContainer.intakeSubsystemHolder1;
  private final Solenoid holder2 = RobotContainer.intakeSubsystemHolder2;
  private final Solenoid armDown = RobotContainer.intakeSubsystemArmDown;

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

  public void moveArmDown() {
    armDown.set(true);
  }

  public void moveArmUp() {
    armDown.set(false);
  }

  public void ballHolderOn() {
    holder1.set(true);
    holder2.set(true);
  }

  public void ballHolderOff() {
    holder1.set(false);
    holder2.set(false);
  }
}
