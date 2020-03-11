package frc.robot.subsystems;

import org.slf4j.Logger;

import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.RobotMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

import frc.robot.Robot;
import frc.robot.RobotContainer;
/**
 * @author Sean Thursby & Micah Wagoner
 * @version 16 February 2020
 */
public class LiftSubsystem extends SubsystemBase {
  private boolean lightTriggered = false;
  Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
  private final CANSparkMax liftController = RobotContainer.liftSubsystemWinch; // motor lower lift on winch
  private final CANEncoder liftEncoder = RobotContainer.liftEncoder;
  private DoubleSolenoid brake = RobotContainer.liftBrake;
  private Solenoid release = RobotContainer.liftRelease;
  private DigitalInput limitSwitch = RobotContainer.liftLimitSwitch;
  private Boolean encoderIsValid = false;
  private double liftEncoderZeroValue;
  private double desiredHeight = 0;
  private boolean autoMagicMode = true;

  private double liftPercentOut = 0;
  private double liftVoltage = 0;
  private double liftCurrent = 0;

  public LiftSubsystem() {
    resetEncoder();
    liftPower(0);
  }

  public boolean checkForLiftEncoder() {
    return(!(liftEncoder == null));
  }

  public void liftoff() { // turns off lift
    if (liftController != null) {
      liftController.set(0);
    }
  }

  public void BrakeOn() {
    brake.set(Value.kForward);
  }

  public void BrakeOff() {
    brake.set(Value.kReverse);
  }
  
  public boolean isLiftLimitDepressed() {
    if(limitSwitch.get() == true) {
      return false;
    }
    return true;
  }

  @Override
  public void periodic() {
      if(liftController != null) {
        liftPercentOut = liftController.getAppliedOutput();
        liftCurrent = liftController.getOutputCurrent();
        liftVoltage = liftController.getBusVoltage();
      }

      // Put code here to be run every loop
      SmartDashboard.putBoolean("liftBottomLimitSwitch", isLiftLimitDepressed());

      SmartDashboard.putNumber("liftEncoderInInches", getLiftHeight());

      if(Robot.getCurrentRobotMode() == RobotMode.TELEOP || Robot.getCurrentRobotMode() == RobotMode.AUTONOMOUS){
          
          if(isLiftLimitDepressed() && !encoderIsValid){
              resetEncoder();
              encoderIsValid = true;
          }
         
          double yPos = RobotContainer.getClimbingJoystick();
          if (Math.abs(yPos) > 0.2){
              setManualMode();
          }

          if(true){
            //if(!autoMagicMode){
              periodicManualMode();
          }else{
              //Automagic
              if (encoderIsValid){
                  periodicAutoMagicMode();
              }else{
                  //we want to be down, but we're not there yet
                  //we need to do some LiftMove with a negative
                  liftPower(-0.2);
              }
          }
      }
      if(liftController != null){
          SmartDashboard.putNumber("liftMotorPower", liftController.getAppliedOutput());
          SmartDashboard.putString("liftMode", autoMagicMode ? "AUTOMAGIC" : "MANUAL");
          SmartDashboard.putNumber("liftSetpoint", desiredHeight);
          SmartDashboard.putBoolean("liftEncoderValid", encoderIsValid);
          SmartDashboard.putNumber("Lift Output Current", liftController.getOutputCurrent());
      }
  }

  public void setManualMode() {
    if (autoMagicMode){
        logger.info("Switching to Manual Mode");
    }
    autoMagicMode = false;
  }

  private void periodicAutoMagicMode(){
    double currentheight = getLiftHeight();
    double error = currentheight - desiredHeight;
    
    if(Math.abs(error) > 1){
        if(error > 0){
            liftPower(-0.3);
        }

        if(error < 0){
            liftPower(+0.3);
            
        }
    } else {
        liftoff();
    }
  }

  private void periodicManualMode() {
    double yPos = RobotContainer.getClimbingJoystick();
    double speed = yPos * 1;

    liftPower(speed);
  }

  // Lift should move up from positive power 
  public void liftPower(double speed) { // Runs lift controller based on joystick pos.
    if (liftController != null) {
      if(isLiftLimitDepressed() == true && speed < 0) {
        speed = 0; 
      }

      if(encoderIsValid){
        double currentHeight = getLiftHeight();
        // we don't want the lift to blow past the 
        // limitswitch/hard stop and want power to be
        // low enough we don't go past it. 
        if(currentHeight > 78 && speed > 0) {
            speed = 0;
        }
      }

      if(release.get()){
        speed = 0;
      }

      if(speed != 0) {
        releaseBreak();
      } else {
        applyBrake();
      }

      if (speed >= 0.2 && lightTriggered == false) { //Light effect stuff
        Color8Bit color = new Color8Bit(255, 255, 255); //Light Color
        RobotContainer.lightSubsystem.setShot(color, 5700, false, 255, 100, false); //Makes a new long light "shot"
        lightTriggered = true;
        //SmartDashboard.putNumber("Speed", liftPower);
      }

      liftController.set(speed);
      //SmartDashboard.putNumber("Requested Motor Power", speed);
      //SmartDashboard.putNumber("Lift Current", liftController.getOutputCurrent());
    }
  }

  public void applyBrake() {
    brake.set(Value.kForward);
    //SmartDashboard.putBoolean("LiftBrake", true);
  }

  public void releaseBreak() {
    brake.set(Value.kReverse);
    //SmartDashboard.putBoolean("LiftBrake", false);
  }

  public void applyLiftPin() {
    release.set(true);
  }

  public void releaseLiftPin() {
    release.set(false);
  }

  private double ticstoinches(double tics) { 
    // turning the encoder readings from tics to inches
    double inches = tics * 0.508696934; //(9.75inches/19.16661837167tics)
    return inches;
  }

  public double getLiftHeight() {
    if(checkForLiftEncoder()) {
        double tics = liftEncoder.getPosition();
        double howfarwehavemoved = tics - liftEncoderZeroValue;
        double inches = ticstoinches(howfarwehavemoved);
        return -inches;
    } else {
        return(0);
    }
  }

  public void resetEncoder(){
    if(checkForLiftEncoder()) {
        liftEncoderZeroValue = liftEncoder.getPosition();
    }
  }

  public double getLiftPercentOut() {
    return liftPercentOut;
  }

  public double getLiftCurrent() {
    return liftCurrent;
  }

  public double getLiftVoltage() {
    return liftVoltage;
  }

}