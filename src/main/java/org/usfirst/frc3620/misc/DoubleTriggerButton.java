package org.usfirst.frc3620.misc;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.*;

public class DoubleTriggerButton extends Button {
	Joystick stick;
	double deadZone;

	public DoubleTriggerButton(Joystick joystick) {
		this(joystick, 0.1);
	}

	public DoubleTriggerButton(Joystick joystick, double deadZone) {
		stick = joystick;
		this.deadZone = deadZone;
	}

	public boolean get() {
		return (Math.abs(stick.getRawAxis(XBoxConstants.AXIS_LEFT_TRIGGER)) > deadZone && 
				Math.abs(stick.getRawAxis(XBoxConstants.AXIS_RIGHT_TRIGGER)) > deadZone);
	}
}