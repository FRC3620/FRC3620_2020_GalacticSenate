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
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class ShooterSubsystem extends SubsystemBase {
  /**
   * Creates a new ShooterSubsystem.
   */
  private final WPI_TalonFX falconTop = RobotContainer.shooterSubsystemFalcon1;
  private final WPI_TalonFX falconBottom = RobotContainer.shooterSubsystemFalcon2; 
  private final WPI_TalonSRX feeder = RobotContainer.shooterSubsystemBallFeeder;

  //sets up all values for PID
  private final int kVelocitySlotIdx = 0;
  private final int kTimeoutMs = 0;

  //top FPID Values
  private final double tFVelocity = 0.045; //0.045
  private final double tPVelocity = 0.60; //0.60
  private final double tIVelocity = 0.000003; //0.000003
  private final double tDVelocity = 7; //7.75
  private final double trpm = 5200; //5200

  //bottom FPID Values
  private final double bFVelocity = 0.0465;
  private final double bPVelocity = 0.45;
  private final double bIVelocity = 0.0000001;
  private final double bDVelocity = 7.5;
  private final double brpm = 4000;

  public ShooterSubsystem() {
    if (falconTop != null) {
      //for PID you have to have a sensor to check on so you know the error
      falconTop.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, kVelocitySlotIdx, kTimeoutMs);

      //set max and minium(nominal) speed in percentage output
      falconTop.configNominalOutputForward(0, kTimeoutMs);
      falconTop.configNominalOutputReverse(0, kTimeoutMs);
      falconTop.configPeakOutputForward(+1, kTimeoutMs);
      falconTop.configPeakOutputReverse(-1, kTimeoutMs);

      //set up the topfalcon for using FPID
      falconTop.config_kF(kVelocitySlotIdx, tFVelocity, kTimeoutMs);
      falconTop.config_kP(kVelocitySlotIdx, tPVelocity, kTimeoutMs);
      falconTop.config_kI(kVelocitySlotIdx, tIVelocity, kTimeoutMs);
      falconTop.config_kD(kVelocitySlotIdx, tDVelocity, kTimeoutMs);
    }

    if (falconBottom != null) {
      //for PID you have to have a sensor to check on so you know the error
      falconBottom.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, kVelocitySlotIdx, kTimeoutMs);

      //set max and minium(nominal) speed in percentage output
      falconBottom.configNominalOutputForward(0, kTimeoutMs);
      falconBottom.configNominalOutputReverse(0, kTimeoutMs);
      falconBottom.configPeakOutputForward(+1, kTimeoutMs);
      falconBottom.configPeakOutputReverse(-1, kTimeoutMs);

      //set up the bottomfalcon for using FPID
      falconBottom.config_kF(kVelocitySlotIdx, bFVelocity, kTimeoutMs);
      falconBottom.config_kP(kVelocitySlotIdx, bPVelocity, kTimeoutMs);
      falconBottom.config_kI(kVelocitySlotIdx, bIVelocity, kTimeoutMs);
      falconBottom.config_kD(kVelocitySlotIdx, bDVelocity, kTimeoutMs);
    }
  }

  @Override
  public void periodic() {
    /* This method will be called once per scheduler run
    SmartDashboard.putNumber("FValue", kFVelocity);
    SmartDashboard.putNumber("PValue", kPVelocity);
    SmartDashboard.putNumber("IValue", kIVelocity);
    SmartDashboard.putNumber("DValue", kDVelocity);
    SmartDashboard.putNumber("Output Voltage", falconTop.getMotorOutputVoltage());
    SmartDashboard.putNumber("RPM", rpm);
    For testing use the values below */
    if (falconBottom != null){
      SmartDashboard.putNumber("Output%", falconBottom.getMotorOutputPercent());
    SmartDashboard.putNumber("ERROR", falconBottom.getClosedLoopError());
    SmartDashboard.putNumber("Output Current", falconBottom.getStatorCurrent());
    SmartDashboard.putNumber("Falcon Temperature", falconBottom.getTemperature());
    SmartDashboard.putNumber("Velocity", falconBottom.getSelectedSensorVelocity());
    }

  }

  public void ShootPID(){
    /* converting rev/min to units/rev
    100ms for a min is 600ms
    TalonFX records in 2048 units/rev
    */
    //set target velocity using PID
    double topTargetVelocity = trpm * 2048 / 600;
    if (falconTop != null) {
      falconTop.set(ControlMode.Velocity, topTargetVelocity);
    }

    double bottomTargetVelocity = brpm * 2048 / 600;
    if (falconBottom != null) {
      falconBottom.set(ControlMode.Velocity, bottomTargetVelocity);
    }
    
    if(feeder != null) {
      feeder.set(0.5); //load next ball into shooter
    }
    /*
    if (falconBottom != null) {
      falconBottom.set(ControlMode.PercentOutput, 0.60);
    } */
  }

  public void Shoot(){
    //set target velocity using percent output
    if (falconTop != null) {
      falconTop.set(ControlMode.PercentOutput, 0.7);
    }

    if (falconBottom != null) {
      falconBottom.set(ControlMode.PercentOutput, 0.35);
    }

    if(feeder != null) {
      feeder.set(0.2); //load next ball into shooter
    }
  }

  public void ShooterOff(){
    //sets target velocity to zero
    if (falconTop != null) {
      falconTop.set(ControlMode.PercentOutput, 0);
    }

    if (falconBottom != null) {
      falconBottom.set(ControlMode.PercentOutput, 0);
    }

    if(feeder != null) {
      feeder.set(0.0); //stop loading balls into shooter
    }
  }

}

