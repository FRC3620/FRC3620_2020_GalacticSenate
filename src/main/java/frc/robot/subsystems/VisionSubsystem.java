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
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class VisionSubsystem extends SubsystemBase {

  private NetworkTableInstance inst = NetworkTableInstance.getDefault();
  private NetworkTable networkTable = inst.getTable("ChickenVision");

  private NetworkTableEntry shootingTargetPresent = networkTable.getEntry("Shooting Centered");
  private NetworkTableEntry shootingTargetYaw = networkTable.getEntry("Shooting Target Yaw");

  private Solenoid visionLight = RobotContainer.visionLight;

  public VisionSubsystem() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public double getShootingTargetYaw(){
    if (shootingTargetPresent.getBoolean(false)){
      return shootingTargetYaw.getDouble(0);
    }
    else{
      return 0;
    }
  }

  public boolean getShootingTargetPresent(){
    return shootingTargetPresent.getBoolean(false);
  }

  public void turnVisionLightOn() {
    visionLight.set(true);
  }

  public void turnVisionLightOff() {
    visionLight.set(false);
  }
}

