/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.miscellaneous;

/**
 * Add your docs here.
 */
public class DriveVectors {
    @Override
	public String toString() {
		return "DriveVectors [leftFront=" + leftFront + ", rightFront=" + rightFront + ", leftBack=" + leftBack
				+ ", rightBack=" + rightBack + "]";
	}

	public Vector leftFront;
	public Vector rightFront;
	public Vector leftBack;
	public Vector rightBack;
}
