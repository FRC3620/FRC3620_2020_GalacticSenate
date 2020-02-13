/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.miscellaneous;

public class SwerveSettingsContainer {
    @Override
	public String toString() {
		return "SwerveSettingsContainer [leftFront=" + leftFront + ", rightFront=" + rightFront + ", leftBack=" + leftBack
				+ ", rightBack=" + rightBack + "]";
	}

	public double leftFront;
	public double rightFront;
	public double leftBack;
	public double rightBack;
}
