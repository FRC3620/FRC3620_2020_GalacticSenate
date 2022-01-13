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
  private final double tFVelocity = .049; //0.049
  private final double tPVelocity = .39; //0.40
  private final double tIVelocity = .00000003; //0.
  private final int tizoneVelocity = 1500;
  private final double tDVelocity = 0; //7.75
  public double trpm = 4700; //4700

  //bottom FPID Values
  private final double bFVelocity = 0.0456;//.0456
  private final double bPVelocity = .12; //.45
  private final double bIVelocity = .000000008;//0.0000001
  private final int tIzoneVelocity = 1800;
  private final double bDVelocity = 1;//7.5
  private double brpm = 5000;

  //hood PID Values
  private final double hoodP = 0.13;
  private final double hoodI = 0;
  private final double hoodD = 0;
  private final double hoodIz = 0;
  private double requestedHoodPosition = 0;

  private double hoodCurrent = 0;
  private double hoodPercentOut = 0;
  private double hoodVoltage = 0;

  private double topShooterCurrent = 0;
  private double topPercentOutput = 0;
  private double topVoltage = 0;

  private double bottomShooterCurrent = 0;
  private double bottomPercentOutput = 0;
  private double bottomVoltage = 0;

  public double anyRPM;
  public double anyPosition;

  private double requestedTopShooterVelocity = 0;
  private double requestedBottomShooterVelocity = 0;

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
      falconTop.config_IntegralZone(kVelocitySlotIdx, tizoneVelocity, kTimeoutMs);
      falconTop.config_kD(kVelocitySlotIdx, tDVelocity, kTimeoutMs);
    }

    SmartDashboard.putNumber("anyRPM", anyRPM);
    SmartDashboard.putNumber("anyPosition", anyPosition);

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
      falconBottom.config_IntegralZone(kVelocitySlotIdx, tIzoneVelocity, kTimeoutMs);
    }

    if (hoodMotor != null) {
      anglePID = hoodMotor.getPIDController();
      anglePID.setP(hoodP);
      anglePID.setI(hoodI);
      anglePID.setD(hoodD);
      anglePID.setIZone(hoodIz);
      anglePID.setOutputRange(-0.5, 0.5);
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
    double calcHoodPosition;
    if(cy < 224){
      calcHoodPosition = 3.73187317480733 + 0.0327847309136473*cy +-0.0000114726741759497*cy*cy;
      calcHoodPosition = calcHoodPosition + SmartDashboard.getNumber("manualHoodPosition", 5);
    } else if(cy < 336){
      calcHoodPosition = 3.85000000000 + 0.0369791667*cy + -0.0000325521*cy*cy;
    }else if(cy < 403){
      calcHoodPosition = -28.1396700696 + 0.2136292223*cy + -0.0002749411*cy*cy;
    } else {
      calcHoodPosition = -56.8299016952515 + 0.355106208706275*cy + -0.000449346405275719*cy*cy;}


    // comment this line out when not testing
    //calcHoodPosition = SmartDashboard.getNumber("manualHoodPosition", 5);
    
    
    //double calcposition = 4.25 + 0.0252936*cy - 0.0002703*Math.pow((cy-363.778),2) - 0.00000054739*Math.pow((cy-363.778),3) + 0.000000000382*Math.pow((cy-363.778),4);
    //WORKS double calcposition = 3.7788078 + 0.0272996*cy - 0.0002548*Math.pow((cy-371),2) - 4.5881e-7*Math.pow((cy-371),3);
    // BEFORE ELIMS double calcposition = 3.7806806 + 0.0275988*cy - 0.0002576*Math.pow((cy-366.75),2) - 5.8642e-7*Math.pow((cy-366.75),3);
    //workds most of the time double calcposition = 5.3729344 + 0.0223497*cy - 0.0002213*Math.pow((cy-360.308),2) + 2.2798e-7*Math.pow((cy-360.308),3);
    //double calcposition = 23.3622 + -0.485745*cy + 0.00524459*cy*cy + -0.0000270027*cy*cy*cy + 0.0000000733328*cy*cy*cy*cy + -9.92152E-11*cy*cy*cy*cy*cy + 5.1978E-14*cy*cy*cy*cy*cy*cy;
    //double calcposition=13.5; 
    //double calcposition
    return 5.0 * calcHoodPosition;
  }

  public double calcTopRPM(double cy) {
    double calcTopRPM = 2650;
    if(cy < 252) {
      calcTopRPM = 4700;
    } else {
      if (cy > 420)
      calcTopRPM =  72028.1114902496 - 317.401960939169*cy  + 0.374097007406817*cy*cy;
    
      //WORKS calcTopRPM = 698.02383 + 8.1439319*cy - 0.0288023*Math.pow((cy-371),2) - 0.0001439*Math.pow((cy-371),3); 
      //M79calcTopRPM = 913.30193 + 7.6260199*cy - 0.0252182*Math.pow((cy-366.75),2) - 9.001e-5*Math.pow((cy-366.75),3); 
      //works most of the time calcTopRPM = 1.3333*(913.30193 + 7.6260199*cy - 0.0252182*Math.pow((cy-366.75),2) - 9.001e-5*Math.pow((cy-366.75),3));
      //workds completely calcTopRPM = 2314.525 - 9.81 *cy + 0.08563*cy*cy - 0.00010841*cy*cy*cy;
      // ree uncomment out!!!!!!calcTopRPM = 7711.05 + -118.718*cy + 1.45292*cy*cy + -0.00865716*cy*cy*cy + 0.0000264684*cy*cy*cy*cy + -0.0000000393972*cy*cy*cy*cy*cy + 2.25566E-11*cy*cy*cy*cy*cy*cy;
      //calcTopRPM= 2314.525 - 9.81 *cy + 0.08563*Math.pow((cy),2) - 0.00010841*Math.pow((cy),3);
      else calcTopRPM=4700;
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
    anyRPM = SmartDashboard.getNumber("anyRPM", anyRPM);
    anyPosition = SmartDashboard.getNumber("anyPosition", anyPosition);

    if(falconTop != null){
      hoodCurrent = hoodMotor.getOutputCurrent();
      hoodPercentOut = hoodMotor.getAppliedOutput();
      hoodVoltage = hoodMotor.getBusVoltage();
      double hoodPosition= RobotContainer.shooterSubsystemHoodEncoder.getPosition();

      topShooterCurrent = falconTop.getStatorCurrent();
      topPercentOutput = falconTop.getMotorOutputPercent();
      topVoltage = falconTop.getMotorOutputVoltage();

      bottomShooterCurrent = falconBottom.getStatorCurrent();
      bottomPercentOutput = falconBottom.getMotorOutputPercent();
      bottomVoltage = falconBottom.getMotorOutputVoltage();

      SmartDashboard.putNumber("Top Velocity", trpm);
      SmartDashboard.putNumber("Bottom Velocity", brpm);
      SmartDashboard.putNumber("Hood positon", hoodPosition);
      SmartDashboard.putNumber("anyRPM", anyRPM);
      SmartDashboard.putNumber("anyPosition", anyPosition);

      SmartDashboard.putNumber("z.actual", getActualTopShooterVelocity());
      SmartDashboard.putNumber("z.requested", getRequestedTopShooterVelocity());
  
      //SmartDashboard.putNumber("OutputBot%", bottomPercentOutput);
      //SmartDashboard.putNumber("Bottom ERROR", falconBottom.getClosedLoopError());
      //SmartDashboard.putNumber("Bottom RPM", falconBottom.getSelectedSensorVelocity() / 2048 * 600);
  
      SmartDashboard.putNumber("OutputTop%", topPercentOutput);
      //SmartDashboard.putNumber("Top ERROR", falconTop.getClosedLoopError());
      SmartDashboard.putNumber("TopActualRPM", falconTop.getSelectedSensorVelocity() / 2048.0 * 600.0);
  
      //SmartDashboard.putBoolean("hoodLimitSwitch", isHoodLimitDepressed());
  
      //SmartDashboard.putNumber("hoodEncoderInRevs", getActualHoodPosition());
  
    }
  
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
    if(position >= 85){
      requestedHoodPosition = 85;
    } else if (position < 0) {
      requestedHoodPosition=0;
    } else {
      requestedHoodPosition=position;
    }   
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
    requestedBottomShooterVelocity = bottomTargetVelocity;
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
      anglePID.setReference(-0.1, ControlType.kDutyCycle);
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

  public double getRequestedBottomShooterVelocity() {
    return requestedBottomShooterVelocity;
  }

  public double getTopShooterCurrent() {
    return topShooterCurrent;
  }

  public double getBottomShooterCurrent() {
    return bottomShooterCurrent;
  }

  public double getHoodPercentOut() {
    return hoodPercentOut;
  }

  public double getHoodVoltage() {
    return hoodVoltage;
  }

  public double getTopPercentOut() {
    return topPercentOutput;
  }

  public double getBottomPercentOut() {
    return bottomPercentOutput;
  }

  public double getTopVoltage() {
    return topVoltage;
  }

  public double getBottomVoltage() {
    return bottomVoltage;
  }

  public double getActualTopShooterVelocity() {
    if (falconTop != null) {
      return falconTop.getSelectedSensorVelocity();
    } else {
      return 0;
    }
  }

  public double getActualBottomShooterVelocity() {
    if (falconBottom != null) {
      return falconBottom.getSelectedSensorVelocity();
    } else {
      return 0;
    }
  }

  public double getHoodCurrent() {
    return hoodCurrent;
  }

  public double getRangeModifier() {
    return rangeModifier;
  }
}