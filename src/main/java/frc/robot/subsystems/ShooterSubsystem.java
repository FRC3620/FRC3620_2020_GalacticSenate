/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class ShooterSubsystem extends SubsystemBase {
  /**
   * Creates a new ShooterSubsystem.
   */
  private final WPI_TalonFX falcon1 = RobotContainer.shooterSubsystemFalcon1;
  private final WPI_TalonFX falcon2 = RobotContainer.shooterSubsystemFalcon2; 

  //sets up all values for PID
  private final int kVelocitySlotIdx = 0;
  private final int kTimeoutMs = 0;
  private final double kFVelocity = 0.7 * 1023 / 14640; //0.7 * 1023 / 14640
  private final double kPVelocity = 0.97; //0.97
  private final double kIVelocity = 0.0000001; //0.0000001
  private final double kDVelocity = 7.5; //7.5
  private final double rpm = 4300; //4300 normal and 5100 for 30 foot shoot
   
  public ShooterSubsystem() {
    if (falcon1 != null) {
      //for PID you have to have a sensor to check on so you know the error
      falcon1.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, kVelocitySlotIdx, kTimeoutMs);

      //set max and minium(nominal) speed in percentage output
      falcon1.configNominalOutputForward(0, kTimeoutMs);
      falcon1.configNominalOutputReverse(0, kTimeoutMs);
      falcon1.configPeakOutputForward(+1, kTimeoutMs);
      falcon1.configPeakOutputReverse(-1, kTimeoutMs);

      //set up the falcon for using FPID
      falcon1.config_kF(kVelocitySlotIdx, kFVelocity, kTimeoutMs);
      falcon1.config_kP(kVelocitySlotIdx, kPVelocity, kTimeoutMs);
      falcon1.config_kI(kVelocitySlotIdx, kIVelocity, kTimeoutMs);
      falcon1.config_kD(kVelocitySlotIdx, kDVelocity, kTimeoutMs);
    }
  }

  @Override
  public void periodic() {
    /* This method will be called once per scheduler run
    SmartDashboard.putNumber("FValue", kFVelocity);
    SmartDashboard.putNumber("PValue", kPVelocity);
    SmartDashboard.putNumber("IValue", kIVelocity);
    SmartDashboard.putNumber("DValue", kDVelocity);
    SmartDashboard.putNumber("Output Voltage", Falcon1.getMotorOutputVoltage());
    SmartDashboard.putNumber("RPM", rpm);
    For testing use the values below
    SmartDashboard.putNumber("Output%", falcon1.getMotorOutputPercent());
    SmartDashboard.putNumber("ERROR", falcon1.getClosedLoopError());
    SmartDashboard.putNumber("Output Current", falcon1.getStatorCurrent());
    SmartDashboard.putNumber("Falcon Temperature", falcon1.getTemperature());
    SmartDashboard.putNumber("Velocity", falcon1.getSelectedSensorVelocity());*/
  }

  public void ShootPID(){
    /* converting rev/min to units/rev
    100ms for a min is 600ms
    TalonFX records in 2048 units/rev
    */
    //set target velocity using PID
    double targetVelocity = rpm * 2048 / 600;
    if (falcon1 != null) {
      falcon1.set(ControlMode.Velocity, targetVelocity);
    }
  }

  public void Shoot(){
    //set target velocity using percent output
    if (falcon1 != null) {
      falcon1.set(ControlMode.PercentOutput, 0.7);
    }
    //Falcon2.set();
  }

  public void ShooterOff(){
    //sets target velocity to zero
    if (falcon1 != null) {
      falcon1.set(ControlMode.PercentOutput, 0);
    }
    //Falcon2.set(0);
  }
}
