/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

import org.usfirst.frc3620.misc.RobotMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;

public class TurntableSubsystem extends SubsystemBase {
  boolean encoderIsValid = false;
  CANSparkMax turntableDrive = RobotContainer.turntableSubsystemTurntableSpinner;
  CANEncoder turntableEncoder = RobotContainer.turntableSubsystemTurntableEncoder;
  /**
   * Creates a new TurntableSubsystem.
   */
  public TurntableSubsystem() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    double turntableSpeed = turntableEncoder.getVelocity();
    double turntablePosition = turntableEncoder.getPosition();
    double turntableCurrent = turntableDrive.getOutputCurrent();
    if(Robot.getCurrentRobotMode() == RobotMode.TELEOP || Robot.getCurrentRobotMode() == RobotMode.AUTONOMOUS){
      if (!encoderIsValid) {
        turnTurntable(-0.05);

        if (Math.abs(turntableCurrent) > 2) {
          encoderIsValid = true;
          turnTurntable(0.0);
          turntableEncoder.setPosition(0.0);
        }
      }
    }

    SmartDashboard.putNumber("turntableSpeed", turntableSpeed);
    SmartDashboard.putNumber("turntableposition", turntablePosition);
    SmartDashboard.putNumber("turntableCurrent", turntableCurrent);
    SmartDashboard.putBoolean("turntableEncoderValid", encoderIsValid);
  }

  /**
   * 
   * @param speed speed to turn it. positive is clockwise
   */
  public void turnTurntable(double speed) {
    turntableDrive.set(speed);
  }
}
