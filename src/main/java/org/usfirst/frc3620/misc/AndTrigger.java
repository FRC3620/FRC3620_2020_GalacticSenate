package org.usfirst.frc3620.misc;

import java.util.*;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.button.*;

/**
 * @author FOVEA1959
 * 
 * Implement a 'button' that is only depressed if BooleanSuppliers are true;
 * this is designed for making it easy to fire off commands only
 * if the bumpers are depressed in conjunction with something else.
 */
public class AndTrigger extends Trigger {
	List<BooleanSupplier> suppliers = new ArrayList<>();
	
	public AndTrigger(BooleanSupplier... booleanSuppliers) {
		super();
		for (BooleanSupplier buttonSupplier : booleanSuppliers) {
			this.suppliers.add(buttonSupplier);
		}
	}
	
	@Override
	public boolean get() {
		/*
		 * go through all the suppliers. If any of them are false, then we 
		 * say 'false'.
		 */
		for (BooleanSupplier buttonSupplier  : this.suppliers) {
			if (! buttonSupplier.getAsBoolean()) {
				return false;
			}
		}
		/*
		 * ok, all of them must have been pressed, so our virtual button is pressed
		 */
		return true;
	}

}
