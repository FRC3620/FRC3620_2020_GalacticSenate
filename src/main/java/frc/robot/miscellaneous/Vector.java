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
public class Vector {
    @Override
		public String toString() {
			return "[d=" + direction + ", m=" + magnitude + "]";
		}

		public double getDirection() {
			return direction;
		}

		public double getMagnitude() {
			return magnitude;
		}
		
		private double direction;
		private double magnitude;
		
		public Vector(double _direction, double _magnitude) {
			direction = _direction;
			magnitude = _magnitude;
		}
	}
