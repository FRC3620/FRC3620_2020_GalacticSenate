/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.miscellaneous;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.subsystems.DriveSubsystem;

/**
 * Add your docs here.
 */
public class SwerveCalculator {
    public double halfChasisWidth;  //Chasis width and length must be in meters 
    public double halfChasisLength; //Chasis width and length must be in meters 
    public double maxDriveVelocity;
    public DriveSubsystem driveSubsystem;

	public SwerveCalculator(double chasisWidth, double chasisLength, double maxVelocity, DriveSubsystem m_driveSubsystem) {		
		halfChasisWidth = chasisWidth / 2;     
        halfChasisLength = chasisLength / 2;	 	 
        maxDriveVelocity = maxVelocity;
        driveSubsystem = m_driveSubsystem;
	}

	public DriveVectors calculateEverything (double joyX, double joyY, double joyRotation) {

		double strafeX;
		double strafeY;

		if(driveSubsystem.getFieldRelative()){

			double robotHeading = driveSubsystem.getNavXFixedAngle(); //get NavX heading in degrees (from -180 to 180)
			double strafeVectorAngle = Math.atan2(joyY, joyX)*(180/Math.PI); //get our desired strafing angle in degrees
			strafeVectorAngle = strafeVectorAngle + robotHeading; //add heading to strafing angle to find our field-relative angle
			SmartDashboard.putNumber("joyX", joyX);
			SmartDashboard.putNumber("joyY", joyY);
			SmartDashboard.putNumber("Strafe Vector Angle", strafeVectorAngle);
			double strafeVectorMagnitude = Math.sqrt((joyX*joyX) + (joyY*joyY)); //get desired strafing magnitude

			strafeX = strafeVectorMagnitude*Math.cos(strafeVectorAngle*Math.PI/180); //calculate X and Y components of the new strafing vector
			strafeY = strafeVectorMagnitude*Math.sin(strafeVectorAngle*Math.PI/180);

		} else{
			double strafeVectorAngle = Math.atan2(joyY, joyX)*(180/Math.PI);
			strafeVectorAngle = normalizeAngle(strafeVectorAngle + 180);
			double strafeVectorMagnitude = Math.sqrt((joyX*joyX) + (joyY*joyY));

			strafeX = strafeVectorMagnitude*Math.cos(strafeVectorAngle*Math.PI/180); 
			strafeY = strafeVectorMagnitude*Math.sin(strafeVectorAngle*Math.PI/180);
		}

		double a = strafeX-(joyRotation*halfChasisLength);    //joyX is the horizontal input on the strafe joystick (the horizontal component of the desired vehicle speed), its units are in/s. 
		double b = strafeX+(joyRotation*halfChasisLength);	   //variables A and B are horizontal components of WHEEL velocity, their units are in/s
		double c = strafeY-(joyRotation*halfChasisWidth);     //joyY is the vertical input on the strafe joystick (the vertical component of the desired vehicle speed), its units are in/s.
		double d = strafeY+(joyRotation*halfChasisWidth);     //variables C and D are vertical components of WHEEL velocity, their units are in/s

		DriveVectors rv = new DriveVectors();    //created a DriveVectors object, which holds 4 vector objects (each with its respective direction and magnitude), one for each wheel
		rv.leftFront = fancyCalc(b,  d);          
		rv.rightFront = fancyCalc(b,  c);        //Each wheel velocity vector is calculated using one X (horizontal) component and one Y (vertical) component
		rv.leftBack = fancyCalc(a,  d);          //see fancyCalc();
		rv.rightBack = fancyCalc(a,  c);
		return rv;
	}

	Vector fancyCalc (double x_r, double y_r) {             //takes in an X and a Y component of a vector and returns a new Vector object with: 
		double angle = Math.atan2(y_r, x_r)*(180/Math.PI);  //direction (in degrees) 
		double velocity = Math.sqrt((x_r*x_r) + (y_r*y_r)); //speed (in meters per second)
		if(velocity>maxDriveVelocity) {
            velocity = maxDriveVelocity;
        }
		return new Vector (angle, velocity);
	}

	public double normalizeAngle(double angle) {//This method makes sure the angle difference calculated falls between -180 degrees and 180 degrees

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

	public double getAngleDifference(double y, double x) {//x = target angle, y = source angle, this finds the shortest angle difference for the swerve modules
		double diff = x - y;

		diff = normalizeAngle(diff);

		return diff;
	}
	
	double steeringCutoff = 20;
	public Vector determineNewVector (Vector calculated, Vector current) { //takes 2 vectors and calculates the angle between them and returns a new "fixed" vector
		double diff = getAngleDifference(current.getDirection(), calculated.getDirection());
		if(Math.abs(diff) < 90 ) { // if the difference is greater than 90 degrees, the angle is normalized (see normalizeAngle()) and added 180 degrees 
			if(Math.abs(calculated.getMagnitude())> steeringCutoff){
				return new Vector(current.getDirection() + diff, calculated.getMagnitude());     //this also means that it is faster to invert the power on the motors and go to the angle 180 greater than the one returned by fancyCalc
			}
			else{
				return new Vector(current.getDirection(), 0);
			}
		}
		else {
			if(diff>0) {
				if(Math.abs(calculated.getMagnitude())> steeringCutoff){
					return new Vector(current.getDirection() + diff - 180, -calculated.getMagnitude());     //this also means that it is faster to invert the power on the motors and go to the angle 180 greater than the one returned by fancyCalc
				}
				else{
					return new Vector(current.getDirection(), 0);
				}
			}
			else {
				if(Math.abs(calculated.getMagnitude())> steeringCutoff){
					return new Vector(current.getDirection() + diff + 180, -calculated.getMagnitude());     //this also means that it is faster to invert the power on the motors and go to the angle 180 greater than the one returned by fancyCalc
				}
				else{
					return new Vector(current.getDirection(), 0);
				}
			}  //new vector includes the new "fixed" angle and the motor inverted only if the angle difference is greater than 90 degrees
		}
	}
	
	public DriveVectors fixVectors(DriveVectors calculatedVectors, DriveVectors currentVectors ) { //fixes every vector of the DriverVectors object and returns them
		
		DriveVectors fixedVectors = new DriveVectors();
		
		fixedVectors.leftFront = determineNewVector(calculatedVectors.leftFront, currentVectors.leftFront);
		fixedVectors.rightFront = determineNewVector(calculatedVectors.rightFront, currentVectors.rightFront);
		fixedVectors.leftBack = determineNewVector(calculatedVectors.leftBack, currentVectors.leftBack);
		fixedVectors.rightBack = determineNewVector(calculatedVectors.rightBack, currentVectors.rightBack);

		
		//System.out.println ("calculated: " + calculatedVectors.leftBack);
		//System.out.println ("new: " + fixedVectors.leftBack);
		
		return fixedVectors;
	}
}
