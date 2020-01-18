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
  private final WPI_TalonFX Falcon1 = RobotContainer.shooterSubsystemFalcon1;
  private final WPI_TalonFX Falcon2 = RobotContainer.shooterSubsystemFalcon2; 

  public final int kVelocitySlotIdx = 0;
  public final int kTimeoutMs = 0;
  public double kFVelocity = 0.7 * 1023 / 14640; //0.7 * 1023 / 14640
  public double kPVelocity = 0.95; //0.95
  public double kIVelocity = 0.0000001; //0.0000001
  public double kDVelocity = 6.9; //6.9
  public double rpm = 4300; //4300 normal and 5100 for 30 foot shoot
   
  public ShooterSubsystem() {
    Falcon1.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, kVelocitySlotIdx, kTimeoutMs);

    Falcon1.configNominalOutputForward(0, kTimeoutMs);
    Falcon1.configNominalOutputReverse(0, kTimeoutMs);
    Falcon1.configPeakOutputForward(+1, kTimeoutMs);
    Falcon1.configPeakOutputReverse(-1, kTimeoutMs);
   //set P
    Falcon1.config_kF(kVelocitySlotIdx, kFVelocity, kTimeoutMs);
    Falcon1.config_kP(kVelocitySlotIdx, kPVelocity, kTimeoutMs);
    Falcon1.config_kI(kVelocitySlotIdx, kIVelocity, kTimeoutMs);
    Falcon1.config_kD(kVelocitySlotIdx, kDVelocity, kTimeoutMs);
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
    SmartDashboard.putNumber("Output%", Falcon1.getMotorOutputPercent());
    SmartDashboard.putNumber("ERROR", Falcon1.getClosedLoopError());
    SmartDashboard.putNumber("Output Current", Falcon1.getStatorCurrent());
    SmartDashboard.putNumber("Falcon Temperature", Falcon1.getTemperature());
    SmartDashboard.putNumber("Velocity", Falcon1.getSelectedSensorVelocity());
    */
  }

  public void ShootPID(){
    /* converting rev/min to units/rev
    4300 rev/min from measuring with TACH
    100ms for a min is 600ms
    TalonFX records in 2048 units/rev
    */
    double targetVelocity = rpm * 2048 / 600;
    Falcon1.set(ControlMode.Velocity, targetVelocity);
  }

  public void Shoot(){
    Falcon1.set(ControlMode.PercentOutput, 0.7);
    //Falcon2.set();
  }

  public void ShooterOff(){
    Falcon1.set(ControlMode.PercentOutput, 0);
    //Falcon2.set(0);
  }
}
