/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
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
  public double kFVelocity = 0;
  public double kPVelocity = 0;
  public double kIVelocity = 0;
  public double kDVelocity = 0;
   
  public ShooterSubsystem() {
    Falcon1.set(ControlMode.Velocity, kVelocity);

    Falcon1.configPeakOutputForward(+1.0, kTimeoutMs);
    Falcon1.configPeakOutputReverse(-1.0, kTimeoutMs);

    //set PID
    Falcon1.config_kF(kVelocitySlotIdx, kFVelocity, kTimeoutMs);
    Falcon1.config_kP(kVelocitySlotIdx, kPVelocity, kTimeoutMs);
    Falcon1.config_kI(kVelocitySlotIdx, kIVelocity, kTimeoutMs);
    Falcon1.config_kD(kVelocitySlotIdx, kDVelocity, kTimeoutMs);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Output Current", Falcon1.getStatorCurrent());
    SmartDashboard.putNumber("Output Voltage", Falcon1.getMotorOutputVoltage());
    SmartDashboard.putNumber("Falcon Temperature", Falcon1.getTemperature());
    SmartDashboard.putNumber("Speed", Falcon1.getSelectedSensorVelocity());
  }

  public void Shoot(double speed){
    // TODO do conversions to targetvelocity from rpm using websites/techometer
    Falcon1.set(ControlMode.Velocity, targetVelocity);
    System.out.println("Shooting power: " + Falcon1.get());
    //Falcon2.set(speed);
  }

  public void ShooterOff(){
    Falcon1.set(ControlMode.PercentOutput, 0);
    //Falcon2.set(0);
  }
}
