package org.usfirst.frc3620.misc;

import java.util.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.*;

/**
 * @author FOVEA1959
 * 
 * Implement a 'button' that is depressed if any one of many buttons 
 * is depressed. If you want to tie a command to many buttons, this means
 * you only have one instance of the command, instead of several instances,
 * each tied to it's own button.
 */
public class JoystickOredButtonsTrigger extends Trigger {
	Joystick joystick;
	List<Integer> buttonNumbers = new ArrayList<>();
	
	public JoystickOredButtonsTrigger(Joystick joystick, int... buttonNumbers) {
		super();
		this.joystick = joystick;
		for (int i : buttonNumbers) {
			this.buttonNumbers.add(i);
		}
	}
	
	@Override
	public boolean get() {
		/*
		 * go through all the buttons. If any of them are depressed, then we 
		 * say 'true'.
		 */
		for (int buttonNumber : buttonNumbers) {
			if (joystick.getRawButton(buttonNumber)) {
				return true;
			}
		}
		/*
		 * ok, none of them must have been pressed, so our virtual button is not pressed
		 */
		return false;
	}

}
