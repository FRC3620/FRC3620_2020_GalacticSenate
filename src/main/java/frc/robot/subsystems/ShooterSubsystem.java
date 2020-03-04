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
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import org.usfirst.frc3620.misc.RobotMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;

public class ShooterSubsystem extends SubsystemBase {
  /**
   * Creates a new ShooterSubsystem.
   */
  private final WPI_TalonFX falconTop = RobotContainer.shooterSubsystemFalcon1;
  private final WPI_TalonFX falconBottom = RobotContainer.shooterSubsystemFalcon3;
  private final CANSparkMax hoodMotor = RobotContainer.shooterSubsystemHoodMax;
  private CANEncoder hoodEncoder = RobotContainer.shooterSubsystemHoodEncoder;
  private CANPIDController anglePID;
  private DigitalInput limitSwitch = RobotContainer.hoodLimitSwitch;
  private Boolean encoderIsValid = false;

  //sets up all values for PID
  private final int kVelocitySlotIdx = 0;
  private final int kTimeoutMs = 0;
  private double rangeModifier = 1.0; //Multiply this by distance from goal before calculating range 


  /*
  PID Values Link
  https://docs.google.com/spreadsheets/d/1Ap6Y6N5QLvBdPORXFwbMu-nDgwLP74CkY4iBRznK5PU/edit?usp=sharing
  */
  //top FPID Values
  private final double tFVelocity = 0.045; //0.045
  private final double tPVelocity = 0.6; //0.60
  private final double tIVelocity = 0.000003; //0.000003
  private final double tDVelocity = 7; //7.75
  public double trpm = 4100; //5200

  //bottom FPID Values
  private final double bFVelocity = 0.0465;
  private final double bPVelocity = 0.45;
  private final double bIVelocity = 0.0000001;
  private final double bDVelocity = 7.5;
  private double brpm = 4000;

  //hood PID Values
  private final double hoodP = 0.13;
  private final double hoodI = 0;
  private final double hoodD = 0;
  private final double hoodIz = 0;
  private double requestedHoodPosition = 0;

  private double requestedTopShooterVelocity = 0;

  public ShooterSubsystem() {
    resetEncoder();
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

    if (hoodMotor != null) {
      anglePID = hoodMotor.getPIDController();
      anglePID.setP(hoodP);
      anglePID.setI(hoodI);
      anglePID.setD(hoodD);
      anglePID.setIZone(hoodIz);
      anglePID.setOutputRange(-0.3, 0.3);
    }
  }
  
  public boolean checkForHoodEncoder() {
    return(!(hoodEncoder == null));
  }

  public boolean isHoodLimitDepressed() {
    if(limitSwitch.get() == true) {
      return false;
    }
    return true;
  }

  public double calcHoodPosition(double cy) {
    double calcposition = 4.25 + 0.0252936*cy - 0.0002703*Math.pow((cy-363.778),2) - 0.00000054739*Math.pow((cy-363.778),3) + 0.000000000382*Math.pow((cy-363.778),4);
    return calcposition;
  }

  public double calcTopRPM(double cy) {
    double calcTopRPM = 2650;
    if(cy > 200) {
      //calcTopRPM = 3530.57629-(19.24789286*cy)+(7.372612103e-2*Math.pow(cy,2))-(1.4845263e-5*Math.pow(cy, 3))-(1.057778756e-7*Math.pow(cy, 4));
      calcTopRPM =  1575.7776 + 6.0863219*cy - 0.0333833*Math.pow((cy-388.545),2) - 0.0001513*Math.pow((cy-388.545),3) - 0.00000038496*Math.pow((cy-388.545),4);
    }
    return calcTopRPM;
  }

  public void modifyRangeModifer(double mod) {
    rangeModifier += mod;
  }

  @Override
  public void periodic() {
    //trpm = SmartDashboard.getNumber("Top Velocity", 4100);
    //brpm = SmartDashboard.getNumber("Bottom Velocity", 4000);
    //hoodPosition = SmartDashboard.getNumber("Hood Position", 0);

    //SmartDashboard.putNumber("Top Velocity", trpm);
    //SmartDashboard.putNumber("Bottom Velocity", brpm);

    //SmartDashboard.putNumber("OutputBot%", falconBottom.getMotorOutputPercent());
    //SmartDashboard.putNumber("Bottom ERROR", falconBottom.getClosedLoopError());
    //SmartDashboard.putNumber("Bottom RPM", falconBottom.getSelectedSensorVelocity());

    //SmartDashboard.putNumber("OutputTop%", falconTop.getMotorOutputPercent());
    //SmartDashboard.putNumber("Top ERROR", falconTop.getClosedLoopError());
    //SmartDashboard.putNumber("Top RPM", falconTop.getSelectedSensorVelocity());

    //SmartDashboard.putBoolean("hoodLimitSwitch", isHoodLimitDepressed());

    //SmartDashboard.putNumber("hoodEncoderInRevs", getActualHoodPosition());

    if(Robot.getCurrentRobotMode() == RobotMode.TELEOP || Robot.getCurrentRobotMode() == RobotMode.AUTONOMOUS){
      if(isHoodLimitDepressed() && !encoderIsValid){
          resetEncoder();
          encoderIsValid = true;
      }

      if(encoderIsValid){
        anglePID.setReference(requestedHoodPosition, ControlType.kPosition);
      } else {
          //we want to be down, but we're not there yet
          //we need to do some runHood with a negative
          runHoodDownSlowly();
      }
    }
    SmartDashboard.putNumber("hoodSetpoint", requestedHoodPosition);
    SmartDashboard.putBoolean("hoodEncoderValid", encoderIsValid);
  }

  public void setTopRPM(double RPM) {
    trpm = RPM;
  }

  public void setPosition(double position) {
    if(position > 17){
      requestedHoodPosition = 17;
    }
    requestedHoodPosition = position;
  }

  public void ShootPID(){
    /* converting rev/min to units/rev
    100ms for a min is 600ms
    TalonFX records in 2048 units/rev
    */
    //set target velocity using PID
    double topTargetVelocity = trpm * 2048 / 600;
    requestedTopShooterVelocity = topTargetVelocity;
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

  public void runHoodDownSlowly(){
    if(hoodMotor != null){
      anglePID.setReference(-0.2, ControlType.kDutyCycle);
    }
  }

  public void stopHood(){
    if(hoodMotor != null){
      anglePID.setReference(0, ControlType.kDutyCycle);
    }
  }

  public double getActualHoodPosition() {
    if(checkForHoodEncoder()) {
        double revs = hoodEncoder.getPosition();
        return revs;
    } else {
        return(0);
    }
  }

  public void resetEncoder(){
    if(checkForHoodEncoder()) {
        hoodEncoder.setPosition(0);
    }
  }

  public double getRequestedHoodPosition(){
    return requestedHoodPosition;
  }
  
  public double getRequestedTopShooterVelocity() {
    return requestedTopShooterVelocity;
  }

  public double getActualTopShooterVelocity() {
    if (falconTop != null) {
    return falconTop.getSelectedSensorVelocity();
    } else {
      return 0;
    }
  }

  public double getRangeModifier() {
    return rangeModifier;
  }
}