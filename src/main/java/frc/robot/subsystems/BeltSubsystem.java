package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.commands.BeltIdleCommand;

public class BeltSubsystem extends SubsystemBase {

  private final WPI_TalonSRX feeder = RobotContainer.shooterSubsystemBallFeeder;

  public BeltSubsystem() {
    this.setDefaultCommand(new BeltIdleCommand(this));
  }

  @Override
  public void periodic() {
     
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

}