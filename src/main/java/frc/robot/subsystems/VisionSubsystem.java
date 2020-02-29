/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class VisionSubsystem extends SubsystemBase {

  private NetworkTableInstance inst = NetworkTableInstance.getDefault();
  private NetworkTable networkTable = inst.getTable("ChickenVision");

  private NetworkTableEntry targetCentered = networkTable.getEntry("targetCentered");
  private NetworkTableEntry targetYaw = networkTable.getEntry("targetYaw");
  private NetworkTableEntry targetAcquired = networkTable.getEntry("targetCentered");
  private NetworkTableEntry targetDistance = networkTable.getEntry("targetDistance");

  private Solenoid visionLight = RobotContainer.visionLight;

  private boolean visionTargeting = false;

  private PIDController spinPIDController;
	private double kSpinP = 0.0;
	private double kSpinI = 0.0;
	private double kSpinD = 0.0;
	private double spinPower = 0;

  public VisionSubsystem() {

    spinPIDController = new PIDController(kSpinP, kSpinI, kSpinD);
    spinPIDController.setTolerance(1);

  }

  @Override
  public void periodic() {
    if(visionTargeting){
      if(getShootingTargetAcquired()){
        spinPIDController.setSetpoint(0);
        spinPower = spinPIDController.calculate(getShootingTargetYaw());
      }
    }
  }

  public double getShootingTargetYaw(){
    
    if (targetCentered.getBoolean(false)){
      return targetYaw.getDouble(0);
    }
    return 0;
  }

  public boolean getShootingTargetAcquired(){
    return targetAcquired.getBoolean(false);
  }

  public boolean getShootingTargetCentered(){
    return targetCentered.getBoolean(false);
  }

  public double getShootingTargetDistance(){
    return targetDistance.getDouble(0);
  }

  public void setVisionTargetingTrue(){
    visionTargeting = true;
  }

  public void setVisionTargetingFalse(){
    visionTargeting = false;
  }

  public double getSpinPower(){
    return spinPower;
  }

  public void turnVisionLightOn() {
    visionLight.set(true);
  }

  public void turnVisionLightOff() {
    visionLight.set(false);
  }
}

