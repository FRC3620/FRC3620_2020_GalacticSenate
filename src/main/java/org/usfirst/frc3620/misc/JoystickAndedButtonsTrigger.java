package org.usfirst.frc3620.misc;

import java.util.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.*;

/**
 * @author FOVEA1959
 * 
 * Implement a 'button' that is only depressed if many buttons are depressed; 
 * this is designed for making it easy to fire off commands only
 * if the bumpers are depressed in conjunction with something else.
 */
public class JoystickAndedButtonsTrigger extends Trigger {
	Joystick joystick;
	List<Integer> buttonNumbers = new ArrayList<>();
	
	public JoystickAndedButtonsTrigger(Joystick joystick, int... buttonNumbers) {
		super();
		this.joystick = joystick;
		for (int i : buttonNumbers) {
			this.buttonNumbers.add(i);
		}
	}
	
	@Override
	public boolean get() {
		/*
		 * go through all the buttons. If any of them are not depressed, then we 
		 * say 'false'.
		 */
		for (int buttonNumber : buttonNumbers) {
			if (! joystick.getRawButton(buttonNumber)) {
				return false;
			}
		}
		/*
		 * ok, all of them must have been pressed, so our virtual button is pressed
		 */
		return true;
	}

}
