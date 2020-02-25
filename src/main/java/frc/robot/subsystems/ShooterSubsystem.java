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
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class ShooterSubsystem extends SubsystemBase {
  /**
   * Creates a new ShooterSubsystem.
   */
  private final WPI_TalonFX falconTop = RobotContainer.shooterSubsystemFalcon1;
  private final WPI_TalonFX falconBottom = RobotContainer.shooterSubsystemFalcon3; 
  private final WPI_TalonSRX feeder = RobotContainer.shooterSubsystemBallFeeder;
  private final CANSparkMax hoodMotor = RobotContainer.shooterSubsystemHoodMax;
  private CANEncoder hoodEncoder = RobotContainer.shooterSubsystemHoodEncoder;
  private CANPIDController anglePID = hoodMotor.getPIDController();

  //sets up all values for PID
  private final int kVelocitySlotIdx = 0;
  private final int kTimeoutMs = 0;

  /*
  PID Values Link
  https://docs.google.com/spreadsheets/d/1Ap6Y6N5QLvBdPORXFwbMu-nDgwLP74CkY4iBRznK5PU/edit?usp=sharing
  */
  //top FPID Values
  private final double tFVelocity = 0.045; //0.045
  private final double tPVelocity = 0.6; //0.60
  private final double tIVelocity = 0.000003; //0.000003
  private final double tDVelocity = 7; //7.75
  private double trpm = 4100; //5200

  //bottom FPID Values
  private final double bFVelocity = 0.0465;
  private final double bPVelocity = 0.45;
  private final double bIVelocity = 0.0000001;
  private final double bDVelocity = 7.5;
  private double brpm = 4000;

  //feeder FPID Values
  private final double fFVelocity = 0.0465;
  private final double fPVelocity = 0;
  private final double fIVelocity = 0;
  private final double fDVelocity = 0;
  private double frpm = 1000;

  //hood PID Values
  private final double hoodP = 0;
  private final double hoodI = 0;
  private final double hoodD = 0;
  private final double hoodIz = 0;
  private double hoodPosition = 0;

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

    if (feeder != null) {
      //for PID you have to have a sensor to check on so you know the error
      feeder.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, kVelocitySlotIdx, kTimeoutMs);

      //set max and minium(nominal) speed in percentage output
      feeder.configNominalOutputForward(0, kTimeoutMs);
      feeder.configNominalOutputReverse(0, kTimeoutMs);
      feeder.configPeakOutputForward(+1, kTimeoutMs);
      feeder.configPeakOutputReverse(-1, kTimeoutMs);

      //set up the feeder for using FPID
      feeder.config_kF(kVelocitySlotIdx, fFVelocity, kTimeoutMs);
      feeder.config_kP(kVelocitySlotIdx, fPVelocity, kTimeoutMs);
      feeder.config_kI(kVelocitySlotIdx, fIVelocity, kTimeoutMs);
      feeder.config_kD(kVelocitySlotIdx, fDVelocity, kTimeoutMs);
    }

    SmartDashboard.putNumber("Top Velocity", trpm);
    SmartDashboard.putNumber("Bottom Velocity", brpm);
    SmartDashboard.putNumber("Hood Position", hoodPosition);

    if (hoodMotor != null) {
      hoodEncoder = hoodMotor.getEncoder();
      anglePID.setReference(hoodPosition, ControlType.kPosition);
      anglePID.setP(hoodP);
      anglePID.setI(hoodI);
      anglePID.setD(hoodD);
      anglePID.setIZone(hoodIz);
      anglePID.setOutputRange(-0.5, 0.5);
    }
  }

  @Override
  public void periodic() {
    trpm = SmartDashboard.getNumber("Top Velocity", 4100);
    brpm = SmartDashboard.getNumber("Bottom Velocity", 4000);
    hoodPosition = SmartDashboard.getNumber("Hood Position", 0);

    //double currentPosition = hoodEncoder.getPosition();
    //double ERROR = hoodPosition - currentPosition;
    //SmartDashboard.putNumber("HoodEncoderTicks", currentPosition);
    //SmartDashboard.putNumber("HoodERROR", ERROR);

    //SmartDashboard.putNumber("OutputBot%", falconBottom.getMotorOutputPercent());
    //SmartDashboard.putNumber("Bottom ERROR", falconBottom.getClosedLoopError());
    //SmartDashboard.putNumber("Bottom Velocity", falconBottom.getSelectedSensorVelocity());

    //SmartDashboard.putNumber("OutputTop%", falconTop.getMotorOutputPercent());
    //SmartDashboard.putNumber("Top ERROR", falconTop.getClosedLoopError());
    //SmartDashboard.putNumber("Top Velocity", falconTop.getSelectedSensorVelocity());
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
  }

  public void ShooterOff(){
    //sets target velocity to zero
    if (falconTop != null) {
      falconTop.set(ControlMode.PercentOutput, 0);
    }

    if (falconBottom != null) {
      falconBottom.set(ControlMode.PercentOutput, 0);
    }
  }

  public void PIDBeltOn(){
    double feederTargetVelocity = frpm;
    if(feeder != null) {
      feeder.set(ControlMode.Velocity, feederTargetVelocity); 
    }
  }

  public void BeltOff(){
    if(feeder != null) {
      feeder.set(ControlMode.PercentOutput, 0);                  
    }
  }
}