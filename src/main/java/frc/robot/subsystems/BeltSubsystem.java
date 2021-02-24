package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.commands.BeltIdleCommand;

public class BeltSubsystem extends SubsystemBase {

  private final WPI_TalonSRX feeder = RobotContainer.shooterSubsystemBallFeeder;
  private double feederCurrent = 0;
  private double feederOutput = 0;
  private double feederVoltage = 0;

  public BeltSubsystem() {
    this.setDefaultCommand(new BeltIdleCommand(this));
  }

  @Override
  public void periodic() {
     if(feeder != null){
       feederCurrent = feeder.getStatorCurrent();
       feederOutput = feeder.getMotorOutputPercent();
       SmartDashboard.putNumber("belt.output", feederOutput);
       feederVoltage = feeder.getMotorOutputVoltage();
     }
  }

  public double getBeltPower() {
    return feeder.getMotorOutputPercent();
  }

  public void BeltOn(double speed){
    if(feeder != null) {
      feeder.set(ControlMode.PercentOutput, speed); 
    }
  }

  public void BeltOff(){
    if(feeder != null) {
      feeder.set(ControlMode.PercentOutput, 0);                  
    }
  }

  public double getFeederCurrent() {
    return feederCurrent;
  }

  public double getFeederOutput() {
    return feederOutput;
  }

  public double getFeederVoltage() {
    return feederVoltage;
  }

}