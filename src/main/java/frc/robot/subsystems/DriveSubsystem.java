/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.commands.TeleOpDriveCommand;
import frc.robot.miscellaneous.DriveVectors;
import frc.robot.miscellaneous.SwerveCalculator;
import frc.robot.miscellaneous.Vector;

public class DriveSubsystem extends SubsystemBase {
  
  public final CANSparkMax rightFrontDriveMaster = RobotContainer.driveSubsystemRightFrontDrive;
	public final CANSparkMax rightFrontAzimuth = RobotContainer.driveSubsystemRightFrontAzimuth;
	public final CANEncoder rightFrontDriveEncoder = RobotContainer.driveSubsystemRightFrontDriveEncoder;
	public final CANEncoder rightFrontAzimuthEncoder = RobotContainer.driveSubsystemRightFrontAzimuthEncoder;
	public final AnalogInput rightFrontHomeEncoder = RobotContainer.driveSubsystemRightFrontHomeEncoder;
	public final CANPIDController rightFrontVelPID = rightFrontDriveMaster.getPIDController();
	public final CANPIDController rightFrontPositionPID = rightFrontAzimuth.getPIDController();

	public final CANSparkMax leftFrontDriveMaster = RobotContainer.driveSubsystemLeftFrontDrive;
	public final CANSparkMax leftFrontAzimuth = RobotContainer.driveSubsystemLeftFrontAzimuth;
	public final CANEncoder leftFrontDriveEncoder = RobotContainer.driveSubsystemLeftFrontDriveEncoder;
	public final CANEncoder leftFrontAzimuthEncoder = RobotContainer.driveSubsystemLeftFrontAzimuthEncoder;
	public final AnalogInput leftFrontHomeEncoder = RobotContainer.driveSubsystemLeftFrontHomeEncoder;
	public final CANPIDController leftFrontVelPID = leftFrontDriveMaster.getPIDController();
	public final CANPIDController leftFrontPositionPID = leftFrontAzimuth.getPIDController();

	public final CANSparkMax leftBackDriveMaster = RobotContainer.driveSubsystemLeftBackDrive;
	public final CANSparkMax leftBackAzimuth = RobotContainer.driveSubsystemLeftBackAzimuth;
	public final CANEncoder leftBackDriveEncoder = RobotContainer.driveSubsystemLeftBackDriveEncoder;
	public final CANEncoder leftBackAzimuthEncoder = RobotContainer.driveSubsystemLeftBackAzimuthEncoder;
	public final AnalogInput leftBackHomeEncoder = RobotContainer.driveSubsystemLeftBackHomeEncoder;
	public final CANPIDController leftBackVelPID = leftBackDriveMaster.getPIDController();
	public final CANPIDController leftBackPositionPID = leftBackAzimuth.getPIDController();

	public final CANSparkMax rightBackDriveMaster = RobotContainer.driveSubsystemRightBackDrive;
	public final CANSparkMax rightBackAzimuth = RobotContainer.driveSubsystemRightBackAzimuth;
	public final CANEncoder rightBackDriveEncoder = RobotContainer.driveSubsystemRightBackDriveEncoder;
	public final CANEncoder rightBackAzimuthEncoder = RobotContainer.driveSubsystemRightBackAzimuthEncoder;
	public final AnalogInput rightBackHomeEncoder = RobotContainer.driveSubsystemRightBackHomeEncoder;
	public final CANPIDController rightBackVelPID = rightBackDriveMaster.getPIDController();
	public final CANPIDController rightBackPositionPID = rightBackAzimuth.getPIDController();

	public AHRS ahrs = new AHRS(edu.wpi.first.wpilibj.SPI.Port.kMXP);

	//***********************************************************************************************************
	//                    MUST MAKE SURE THESE VALUES ARE RIGHT BEFORE RUNNING SWERVE CODE
	//***********************************************************************************************************

	private final double CHASIS_WIDTH = 22.25; //inches
	private final double CHASIS_LENGTH = 24.25; //inches

	private final double AZIMUTH_ENCODER_CONVERSION_FACTOR = (1/(11.7))*240; //units are tics*motor revolutions
	private final double SPEED_ENCODER_TICS = 42;
	private final double WHEEL_TO_ENCODER_RATIO_VELOCITY = (1/8.31); //for every full wheel turn, the motor turns 8.31 times
	private final double WHEEL_RADIUS = 2; //in inches
	private final double WHEEL_CIRCUMFERENCE = 2*Math.PI*WHEEL_RADIUS;
	private final double DRIVE_ENCODER_CONVERSION_FACTOR = WHEEL_CIRCUMFERENCE*WHEEL_TO_ENCODER_RATIO_VELOCITY;
 
	private final double MAX_VELOCITY_RPM = 500; //maximum velocity that the robot will travel when joystick is at full throtle, measured in RPM
	public final double MAX_VELOCITY_IN_PER_SEC = MAX_VELOCITY_RPM*WHEEL_CIRCUMFERENCE/60; //max velocity in inches per second
	private final double MAX_TURN = 3; //maximum angular velocity at which the robot will turn when joystick is at full throtle, measured in rad/s

	private double RIGHT_FRONT_ABSOLUTE_OFFSET = 119; // reading of the absolute encoders when the wheels are pointed at true 0 degrees (-180 to 180 degrees)
	private double LEFT_FRONT_ABSOLUTE_OFFSET = 140;
	private double LEFT_BACK_ABSOLUTE_OFFSET = 94;
	private double RIGHT_BACK_ABSOLUTE_OFFSET = 50; 

	private double kPositionP = 0.005;
	private double kPositionI = 0.00000;
	private double kPositionD = .0;
	private double kPositionFF = 0;
	private double kPositionIz = 0;
	private double kPositionMaxOutput = 1;
	
	private double kPositionMinOutput = -1;
	
	private double kVelocityP = 0.007;
	private double kVelocityI = 0.000000;
	private double kVelocityD = 0.1;
	private double kVelocityFF = .006;
	private double kVelocityIz = 0;
	private double kVelocityMaxOutput = 1;
	private double kVelocityMinOutput = -1;

	private boolean changeAzimuthTestHeading = false;
	private boolean fieldRelative = false;

	//***********************************************************************************************************
	//***********************************************************************************************************

	SwerveCalculator sc = new SwerveCalculator(CHASIS_WIDTH, CHASIS_LENGTH, MAX_VELOCITY_IN_PER_SEC, this);
	DriveVectors oldVectors;

  public DriveSubsystem() {

    rightFrontDriveEncoder.setPositionConversionFactor(DRIVE_ENCODER_CONVERSION_FACTOR);
	leftFrontDriveEncoder.setPositionConversionFactor(DRIVE_ENCODER_CONVERSION_FACTOR);
	leftBackDriveEncoder.setPositionConversionFactor(DRIVE_ENCODER_CONVERSION_FACTOR);
	rightBackDriveEncoder.setPositionConversionFactor(DRIVE_ENCODER_CONVERSION_FACTOR);

	rightFrontDriveEncoder.setVelocityConversionFactor((WHEEL_TO_ENCODER_RATIO_VELOCITY*WHEEL_CIRCUMFERENCE)/60);
	leftFrontDriveEncoder.setVelocityConversionFactor((WHEEL_TO_ENCODER_RATIO_VELOCITY*WHEEL_CIRCUMFERENCE)/60);
	leftBackDriveEncoder.setVelocityConversionFactor((WHEEL_TO_ENCODER_RATIO_VELOCITY*WHEEL_CIRCUMFERENCE)/60);
	rightBackDriveEncoder.setVelocityConversionFactor((WHEEL_TO_ENCODER_RATIO_VELOCITY*WHEEL_CIRCUMFERENCE)/60);

	rightFrontAzimuthEncoder.setPositionConversionFactor(AZIMUTH_ENCODER_CONVERSION_FACTOR);
	leftFrontAzimuthEncoder.setPositionConversionFactor(AZIMUTH_ENCODER_CONVERSION_FACTOR);
	leftBackAzimuthEncoder.setPositionConversionFactor(AZIMUTH_ENCODER_CONVERSION_FACTOR);
	rightBackAzimuthEncoder.setPositionConversionFactor(AZIMUTH_ENCODER_CONVERSION_FACTOR);

	rightBackDriveEncoder.setPositionConversionFactor(1);

	setPositionPID(rightFrontPositionPID);
	setPositionPID(leftFrontPositionPID);
	setPositionPID(leftBackPositionPID);
	setPositionPID(rightBackPositionPID);

	setVelocityPID(rightFrontVelPID);
	setVelocityPID(leftFrontVelPID);
	setVelocityPID(leftBackVelPID);
	setVelocityPID(rightBackVelPID);

	SmartDashboard.putNumber("P Gain Position", kPositionP);
	SmartDashboard.putNumber("I Gain Position", kPositionI);
	SmartDashboard.putNumber("D Gain Position", kPositionD);
	SmartDashboard.putNumber("I Zone Position", kPositionIz);
	SmartDashboard.putNumber("Feed Forward Position", kPositionFF);
	SmartDashboard.putNumber("Max Output Position", kVelocityMaxOutput);
	SmartDashboard.putNumber("Min Output Position", kVelocityMinOutput);
	
	SmartDashboard.putNumber("P Gain Velocity", kVelocityP);
	SmartDashboard.putNumber("I Gain Velocity", kVelocityI);
	SmartDashboard.putNumber("D Gain Velocity", kVelocityD);
	SmartDashboard.putNumber("I Zone Velocity", kVelocityIz);
	SmartDashboard.putNumber("Feed Forward Velocity", kVelocityFF);
	SmartDashboard.putNumber("Max Output Velocity", kVelocityMaxOutput);
	SmartDashboard.putNumber("Min Output Velocity", kVelocityMinOutput);
	SmartDashboard.putNumber("PID Position Setpoint", 0);

	SmartDashboard.putNumber("Azimuth Test Heading", 0);
	SmartDashboard.putBoolean("Change Test Heading", false);

	rightFrontPositionPID.setFeedbackDevice(rightFrontAzimuthEncoder);
	leftFrontPositionPID.setFeedbackDevice(rightFrontAzimuthEncoder);
	leftBackPositionPID.setFeedbackDevice(rightFrontAzimuthEncoder);
	rightBackPositionPID.setFeedbackDevice(rightFrontAzimuthEncoder);

	rightFrontVelPID.setFeedbackDevice(rightFrontDriveEncoder);
	leftFrontVelPID.setFeedbackDevice(leftFrontDriveEncoder);
	leftBackVelPID.setFeedbackDevice(leftBackDriveEncoder);
    rightBackVelPID.setFeedbackDevice(rightBackDriveEncoder);
    
    this.setDefaultCommand(new TeleOpDriveCommand(this));

  }

  @Override
  public void periodic() {

    SmartDashboard.putNumber("Conversion Factor", (WHEEL_TO_ENCODER_RATIO_VELOCITY*WHEEL_CIRCUMFERENCE)/60);

		if (rightFrontDriveEncoder != null) {
			SmartDashboard.putNumber("Right Front Velocity", rightFrontDriveEncoder.getVelocity());
			SmartDashboard.putNumber("Right Front Azimuth", rightFrontAzimuthEncoder.getPosition());
			SmartDashboard.putNumber("Right Front Azimuth fixed", getFixedPosition(rightFrontAzimuthEncoder));
			SmartDashboard.putNumber("Right Front Home Encoder", getHomeEncoderHeading(rightFrontHomeEncoder));
			SmartDashboard.putNumber("Right Front Drive Current Draw", rightFrontDriveMaster.getOutputCurrent());
		}
		if (leftFrontDriveEncoder != null) {	
			SmartDashboard.putNumber("Left Front Velocity", leftFrontDriveEncoder.getVelocity());
			SmartDashboard.putNumber("Left Front Azimuth", leftFrontAzimuthEncoder.getPosition());
			SmartDashboard.putNumber("Left Front Home Encoder", getHomeEncoderHeading(leftFrontHomeEncoder));
			SmartDashboard.putNumber("Left Front Drive Current Draw", leftFrontDriveMaster.getOutputCurrent());
		}
		if (leftBackDriveEncoder != null) {
			SmartDashboard.putNumber("Left Back Velocity", leftBackDriveEncoder.getVelocity());
			SmartDashboard.putNumber("Left Back Azimuth", leftBackAzimuthEncoder.getPosition());
			SmartDashboard.putNumber("Left Back Home Encoder", getHomeEncoderHeading(leftBackHomeEncoder));
			SmartDashboard.putNumber("Left Back Drive Current Draw", leftBackDriveMaster.getOutputCurrent());

		}
		if (rightBackDriveEncoder != null) {
			SmartDashboard.putNumber("Right Back Velocity", rightBackDriveEncoder.getVelocity());
			SmartDashboard.putNumber("Right Back Azimuth", rightBackAzimuthEncoder.getPosition());
			SmartDashboard.putNumber("Right Back Home Encoder", getHomeEncoderHeading(rightBackHomeEncoder));
			SmartDashboard.putNumber("Right Back Drive Current Draw", rightBackDriveMaster.getOutputCurrent());	
			SmartDashboard.putNumber("Right Back Drive Motor Position", rightBackDriveEncoder.getPosition());
		}
		
		SmartDashboard.putNumber("NavX heading", getNavXFixedAngle());

		updateVelocityPID(rightFrontVelPID);
		updateVelocityPID(leftFrontVelPID);
		updateVelocityPID(leftBackVelPID);
		updateVelocityPID(rightBackVelPID);
  }

  public double getStrafeXValue() {
		double rv = MAX_VELOCITY_RPM*RobotContainer.getDriveHorizontalJoystick();
		return rv;
	}

	public double getStrafeYValue() {
		double rv = MAX_VELOCITY_RPM*RobotContainer.getDriveVerticalJoystick();
		return rv;
	}

	public double getRotationValue() {
		double rv = MAX_TURN*RobotContainer.getDriveSpinJoystick();
		return rv;
	}

	public void testDrive(double x, double y){
		rightFrontDriveMaster.set(y);
		leftFrontDriveMaster.set(y);
		rightBackDriveMaster.set(y);
		leftBackDriveMaster.set(y);

		rightFrontAzimuth.set(x);
		leftFrontAzimuth.set(x);
		rightBackAzimuth.set(x);
		leftBackAzimuth.set(x);
	}

	public void stopDrive(){
		rightFrontDriveMaster.set(0);
		leftFrontDriveMaster.set(0);
		rightBackDriveMaster.set(0);
		leftBackDriveMaster.set(0);

		rightFrontAzimuth.set(0);
		leftFrontAzimuth.set(0);
		rightBackAzimuth.set(0);
		leftBackAzimuth.set(0);
	}

	public void azimuthTest(double heading){

		DriveVectors newVectors = new DriveVectors();    
		newVectors.leftFront = new Vector(heading, 0);         
		newVectors.rightFront = new Vector(heading, 0);      
		newVectors.leftBack = new Vector(heading, 0);        
		newVectors.rightBack = new Vector(heading, 0);

		DriveVectors currentDirections = getCurrentVectors();

		newVectors = sc.fixVectors(newVectors, currentDirections);

		rightFrontPositionPID.setReference(newVectors.rightFront.getDirection(), ControlType.kPosition);
    	leftFrontPositionPID.setReference(newVectors.leftFront.getDirection(), ControlType.kPosition);
    	leftBackPositionPID.setReference(newVectors.leftBack.getDirection(), ControlType.kPosition);
		rightBackPositionPID.setReference(newVectors.rightBack.getDirection(), ControlType.kPosition);
		SmartDashboard.putNumber("Commanded Azimuth Left Back", newVectors.leftBack.getDirection());
			//SmartDashboard.putBoolean("Change Test Heading", false);
		
		

	}

	public void teleOpDrive(double strafeX, double strafeY, double spinX) {

		double vx = strafeX*MAX_VELOCITY_IN_PER_SEC;
		double vy = strafeY*MAX_VELOCITY_IN_PER_SEC;
		double vr = spinX*MAX_TURN;

		DriveVectors newVectors = sc.calculateEverything(vx, vy, vr);

		SmartDashboard.putNumber("Left Front Calculated Vectors", newVectors.leftBack.getDirection());
		System.out.println("Left Front Calculated Vectors: " + newVectors.leftBack.getDirection());

		DriveVectors currentDirections = getCurrentVectors();

		SmartDashboard.putNumber("Left Front Current Vectors", currentDirections.leftBack.getDirection());
		System.out.println("Left Front Current Vectors: " + currentDirections.leftBack.getDirection());

		newVectors = sc.fixVectors(newVectors, currentDirections);

		rightFrontPositionPID.setReference(newVectors.rightFront.getDirection(), ControlType.kPosition);
    	leftFrontPositionPID.setReference(newVectors.leftFront.getDirection(), ControlType.kPosition);
    	leftBackPositionPID.setReference(newVectors.leftBack.getDirection(), ControlType.kPosition);
		rightBackPositionPID.setReference(newVectors.rightBack.getDirection(), ControlType.kPosition);

		
		rightFrontVelPID.setReference(newVectors.rightFront.getMagnitude(), ControlType.kVelocity);
		leftFrontVelPID.setReference(newVectors.leftFront.getMagnitude(), ControlType.kVelocity);
		leftBackVelPID.setReference(newVectors.leftBack.getMagnitude(), ControlType.kVelocity);
		rightBackVelPID.setReference(newVectors.rightBack.getMagnitude(), ControlType.kVelocity);
		
		SmartDashboard.putNumber("Left Front Commanded Vectors", newVectors.leftBack.getDirection());
		System.out.println("Left Front Commanded Vectors: " + newVectors.leftBack.getDirection());

		//SET DRIVE AND AZIMUTH CONTROLERS HERE
	}

	public void timedDrive(double heading, double speed, double spin){            // degrees are from -180 to 180 degrees with 0 degrees pointing east

		double vx = Math.cos(Math.toRadians(heading))*MAX_VELOCITY_IN_PER_SEC*speed;  //both spin and speed are set as a decimal from 0 to 1 that represents the percentage of the maximum strafe or turn speed
		double vy = Math.sin(Math.toRadians(heading))*MAX_VELOCITY_IN_PER_SEC*speed;
		double vr = spin*MAX_TURN;

		DriveVectors newVectors = sc.calculateEverything(vx, vy, vr);

		DriveVectors currentDirections = getCurrentVectors();

		newVectors = sc.fixVectors(newVectors, currentDirections);

		rightFrontPositionPID.setReference(newVectors.rightFront.getDirection(), ControlType.kPosition);
    	leftFrontPositionPID.setReference(newVectors.leftFront.getDirection(), ControlType.kPosition);
    	leftBackPositionPID.setReference(newVectors.leftBack.getDirection(), ControlType.kPosition);
		rightBackPositionPID.setReference(newVectors.rightBack.getDirection(), ControlType.kPosition);
		
		rightFrontVelPID.setReference(newVectors.rightFront.getMagnitude(), ControlType.kVelocity);
		leftFrontVelPID.setReference(newVectors.leftFront.getMagnitude(), ControlType.kVelocity);
		leftBackVelPID.setReference(newVectors.leftBack.getMagnitude(), ControlType.kVelocity);
		rightBackVelPID.setReference(newVectors.rightBack.getMagnitude(), ControlType.kVelocity);


	}

	public void setPositionPID(CANPIDController pidController) {
		pidController.setP(kPositionP);	
		pidController.setI(kPositionI);
		pidController.setD(kPositionD);
		pidController.setIZone(kPositionIz);
		pidController.setFF(kPositionFF);
		pidController.setOutputRange(kVelocityMinOutput, kVelocityMaxOutput);

	}

	public void setVelocityPID(CANPIDController pidController) {
		pidController.setP(kVelocityP);	
		pidController.setI(kVelocityI);
		pidController.setD(kVelocityD);
		pidController.setIZone(kVelocityIz);
		pidController.setFF(kVelocityFF);
		pidController.setOutputRange(kVelocityMinOutput, kVelocityMaxOutput);
	}

	public void updateVelocityPID(CANPIDController pidController) {

		double p = SmartDashboard.getNumber("P Gain Velocity", 0);
    	double i = SmartDashboard.getNumber("I Gain Velocity", 0);
    	double d = SmartDashboard.getNumber("D Gain Velocity", 0);
    	double iz = SmartDashboard.getNumber("I Zone Velocity", 0);
    	double ff = SmartDashboard.getNumber("Feed Forward Velocity", 0);
    	double max = SmartDashboard.getNumber("Max Output Velocity", 0);
    	double min = SmartDashboard.getNumber("Min Output Velocity", 0);

		if((p != kVelocityP)) {
			pidController.setP(p);
			kVelocityP = p;	
			System.out.println("Updated P value");
			System.out.println(pidController.getP());
		}
		if((i != kVelocityI)) {
			pidController.setI(i);
			kVelocityI = i;	
		}
		if((d != kVelocityD)) {
			pidController.setD(d);
			kVelocityD = d;	
		}
		if((iz != kVelocityIz)) {
			pidController.setIZone(iz);
			kVelocityIz = iz;	
		}
		if((ff != kVelocityFF)) {
			pidController.setFF(ff);
			kVelocityFF = ff;	
		}
		if((max != kVelocityMaxOutput) || (min != kVelocityMinOutput)) {
			pidController.setOutputRange(min, max);
			kVelocityMaxOutput = max;	
			kVelocityMinOutput = min;	
		}
	
	}

	public void updatePositionPID(CANPIDController pidController) {

		double p = SmartDashboard.getNumber("P Position Gain", 0);
    	double i = SmartDashboard.getNumber("I Position Gain", 0);
    	double d = SmartDashboard.getNumber("D Position Gain", 0);
    	double iz = SmartDashboard.getNumber("I Zone Position", 0);
    	double ff = SmartDashboard.getNumber("Feed Forward Position", 0);
    	double max = SmartDashboard.getNumber("Max Output Position", 0);
    	double min = SmartDashboard.getNumber("Min Output Position", 0);

		if((p != kPositionP)) {
			pidController.setP(p);
			kPositionP = p;	
		}
		if((i != kPositionI)) {
			pidController.setI(i);
			kPositionI = i;	
		}
		if((d != kPositionD)) {
			pidController.setD(d);
			kPositionD = d;	
		}
		if((iz != kPositionIz)) {
			pidController.setIZone(iz);
			kPositionIz = iz;	
		}
		if((ff != kPositionFF)) {
			pidController.setFF(ff);
			kPositionFF = ff;	
		}
		if((max != kPositionMaxOutput) || (min != kPositionMinOutput)) {
			pidController.setOutputRange(min, max);
			kPositionMaxOutput = max;	
			kPositionMinOutput = min;	
		}
	
	}

	public void setAllPID() {
		/*setPositionTalonPID(leftFrontAzimuth);
		setVelocityTalonPID(leftFrontDriveMaster);

		setPositionTalonPID(rightFrontAzimuth);
		setVelocityTalonPID(rightFrontDriveMaster);

		setPositionTalonPID(leftBackAzimuth);
		setVelocityTalonPID(leftBackDriveMaster);

		setPositionTalonPID(rightBackAzimuth);
		setVelocityTalonPID(rightBackDriveMaster);*/
	}

	public double getHomeEncoderHeading(AnalogInput encoder){
		
		double heading = encoder.getValue();
		heading = ((heading+1)*360)/4096; // converting heading from tics (ranging from 0 to 4095) to degrees (ranging from 1 to 0)
		if(heading>180){
			heading = heading-360;         // converting from 1-360 degrees to -180 to 180 degrees
		}
		return -heading;

	}

	public void zeroRelativeEncoders(){

		rightFrontAzimuthEncoder.setPosition(0);
		leftFrontAzimuthEncoder.setPosition(0);
		leftBackAzimuthEncoder.setPosition(0);
		rightBackAzimuthEncoder.setPosition(0);

	}

	public void fixRelativeEncoders(){
		rightFrontAzimuthEncoder.setPosition(getHomeEncoderHeading(rightFrontHomeEncoder) - RIGHT_FRONT_ABSOLUTE_OFFSET);
		leftFrontAzimuthEncoder.setPosition(getHomeEncoderHeading(leftFrontHomeEncoder) - LEFT_FRONT_ABSOLUTE_OFFSET);
		leftBackAzimuthEncoder.setPosition(getHomeEncoderHeading(leftBackHomeEncoder) - LEFT_BACK_ABSOLUTE_OFFSET);
		rightBackAzimuthEncoder.setPosition(getHomeEncoderHeading(rightBackHomeEncoder) - RIGHT_BACK_ABSOLUTE_OFFSET);
	}
	public double getFixedPosition(CANEncoder encoder){
		double azimuth = encoder.getPosition();
		if(azimuth > 180){
			azimuth = -360 + azimuth;
		}
		if(azimuth < -180){
			azimuth = 360 + azimuth;
		} 
		
		if(azimuth == -0) {azimuth = 0;}
		
		azimuth = Math.round(azimuth);
		
		return azimuth;
	}

	public Vector readModuleEncoders(CANEncoder azimuthEncoder, CANEncoder speedEncoder) { 

		if(azimuthEncoder != null) {
			double azimuth = azimuthEncoder.getPosition();
			

			if(speedEncoder != null) {

				double wheelSpeed = speedEncoder.getVelocity(); //tics per 100ms

				Vector rv = new Vector(azimuth, wheelSpeed);
				return rv;

			}

		}
		return new Vector(0,0);
	}

	public double convertDirection(double azimuthTics) { //took this out to make it testable

		double encoderReading = azimuthTics;
		double azimuth = (360.0*encoderReading)/AZIMUTH_ENCODER_CONVERSION_FACTOR;
		azimuth = azimuth % 360;
		if(azimuth > 180){
			azimuth = -360 + azimuth;
		}
		if(azimuth < -180){
			azimuth = 360 + azimuth;
		} 
		
		if(azimuth == -0) {azimuth = 0;}
		
		azimuth = Math.round(azimuth);
		
		return azimuth;
	}

	public double getNavXFixedAngle(){ //returns angle in the range of -180 degrees to 180 degrees with 0 being the front

		double angle =180 + ahrs.getAngle(); // added 180 degrees to make north the front of the robot.

		angle = angle % 360;
		
		if(angle > 180){
			angle = -360 + angle;
		}
		if(angle < -180){
			angle = 360 + angle;
		}
		
		if(angle == -0) {angle = 0;}

		return angle;
		
	}

	public double getNavXAbsoluteAngle(){ //returns raw degrees, can accumulate past 360 or -360 degrees 

		double angle =ahrs.getAngle();

		return angle;
		
	}
	
	public double convertVelocity(double velocity) { //took this out to make it testable

		double encoderSpeed = velocity; //RPM
		encoderSpeed = encoderSpeed/60; //Motor Revolutions per second

		double wheelSpeed = encoderSpeed/WHEEL_TO_ENCODER_RATIO_VELOCITY; //wheel revolutions per second
		wheelSpeed = wheelSpeed/WHEEL_CIRCUMFERENCE; //inches per second

		return wheelSpeed;
	}

	public Vector convertSingleVector(Vector v) {

		double speed = v.getMagnitude();
		speed = speed * WHEEL_CIRCUMFERENCE;
		speed = speed * WHEEL_TO_ENCODER_RATIO_VELOCITY
;
		speed = speed * SPEED_ENCODER_TICS;
		speed = speed * 10; //converted to tics/100ms

		double direction = v.getDirection();
		if(direction < 0){
			direction = direction + 360;//converts back to [0, 360] degrees
		}
		direction = (direction*AZIMUTH_ENCODER_CONVERSION_FACTOR)/360; // converts to encoder tics 
		Vector rv = new Vector(direction, speed);

		return rv;

	}

	public DriveVectors convertAllVectors(DriveVectors dv) {
		DriveVectors rv = dv;
		rv.leftFront = convertSingleVector(dv.leftFront);
		rv.rightFront = convertSingleVector(dv.rightFront);
		rv.leftBack = convertSingleVector(dv.leftBack);
		rv.rightBack = convertSingleVector(dv.rightBack);

		return rv;
	}

	public DriveVectors getCurrentVectors() {
		DriveVectors rv = new DriveVectors();
		rv.leftFront = readModuleEncoders(leftFrontAzimuthEncoder, leftFrontDriveEncoder);
		rv.rightFront = readModuleEncoders(rightFrontAzimuthEncoder, rightFrontDriveEncoder);
		rv.leftBack = readModuleEncoders(leftBackAzimuthEncoder, leftBackDriveEncoder);
		rv.rightBack = readModuleEncoders(rightBackAzimuthEncoder, rightBackDriveEncoder);

		return rv;
	}

	public double getMaxVelocity(){
		return MAX_VELOCITY_IN_PER_SEC;
	}

	public double getWheelCircumference(){
		return WHEEL_CIRCUMFERENCE;
	}

	public double getDriveMotorPosition(){
		return rightFrontDriveEncoder.getPosition();
	}

	public void switchFieldRelative(){
		fieldRelative = !fieldRelative;
	}

	public boolean getFieldRelative(){
		return fieldRelative;
	}

	public void resetNavX() {
		ahrs.reset();
	}
}
