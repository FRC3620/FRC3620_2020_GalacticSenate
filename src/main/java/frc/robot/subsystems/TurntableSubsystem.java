/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.sql.Time;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import org.usfirst.frc3620.misc.RobotMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;

public class TurntableSubsystem extends SubsystemBase {
  boolean encoderIsValid = false;
  CANSparkMax turntableDrive = RobotContainer.turntableSubsystemTurntableSpinner;
  CANEncoder turntableEncoder = RobotContainer.turntableSubsystemTurntableEncoder;
  CANPIDController turntablePID = RobotContainer.turntableSubsystemTurntableSpinner.getPIDController();
  Timer calibrationTimer;
  /**
   * Creates a new TurntableSubsystem.
   */
  public TurntableSubsystem() {
    turntableEncoder.setPositionConversionFactor(90/7.8);

    // set up PID for turntablePID here
  turntablePID.setP(0.0175);
  turntablePID.setI(0.0);
  turntablePID.setD(7.5);
  turntablePID.setFF(0.0);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    double turntableSpeed = turntableEncoder.getVelocity();
    double turntablePosition = turntableEncoder.getPosition();
    double turntableCurrent = turntableDrive.getOutputCurrent();
    if(Robot.getCurrentRobotMode() == RobotMode.TELEOP || Robot.getCurrentRobotMode() == RobotMode.AUTONOMOUS){
      if (!encoderIsValid) {
        turnTurntable(-0.045);

        if (calibrationTimer == null) {
          calibrationTimer = new Timer();
          calibrationTimer.reset();
          calibrationTimer.start();
         } else {
          if (calibrationTimer.get() > 0.5){
            if (Math.abs(turntableSpeed) < 20) {
              encoderIsValid = true;
              turnTurntable(0.0);
              turntableEncoder.setPosition(0.0);
            }
          
          }
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

  public void setTurntablePosition (double angle) {
    turntablePID.setReference(angle, ControlType.kPosition);
  }
}
