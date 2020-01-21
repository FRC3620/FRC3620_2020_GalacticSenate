package org.usfirst.frc3620.misc;

import edu.wpi.first.wpilibj2.command.button.*;

public class AnalogValueButton extends Button {
	AnalogValueProvider provider;
	double threshold;
	
	public AnalogValueButton(AnalogValueProvider provider, double threshold) {
		super();
		this.provider = provider;
		this.threshold = threshold;
	}

	@Override
	public boolean get() {
		return (provider.get() > threshold);
	}

}
