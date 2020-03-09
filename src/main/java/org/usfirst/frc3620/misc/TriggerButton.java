package org.usfirst.frc3620.misc;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.*;

public class TriggerButton extends Button {
	Joystick stick;
	boolean isLeft;
	double deadZone;

	public TriggerButton(Joystick joystick, boolean isLeft) {
		this(joystick, isLeft, 0.1);
	}

	public TriggerButton(Joystick joystick, boolean isLeft, double deadZone) {
		this.isLeft = isLeft;
		stick = joystick;
		this.deadZone = deadZone;
	}

	public boolean get() {
		if (isLeft) {
			return Math.abs(stick.getRawAxis(XBoxConstants.AXIS_LEFT_TRIGGER)) > deadZone;
		} else {
			return Math.abs(stick.getRawAxis(XBoxConstants.AXIS_RIGHT_TRIGGER)) > deadZone;
		}
	}

}
