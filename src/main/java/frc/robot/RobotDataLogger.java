package org.usfirst.frc3620.robot;

import java.text.DecimalFormat;

import org.usfirst.frc3620.logger.DataLogger;
import org.usfirst.frc3620.misc.CANDeviceFinder;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotController;
import org.usfirst.frc3620.misc.CANDeviceId;

public class RobotDataLogger {
	PowerDistributionPanel powerDistributionPanel = null;
	DriverStation driverStation = DriverStation.getInstance();

	public RobotDataLogger (DataLogger dataLogger, CANDeviceFinder canDeviceFinder) {
		powerDistributionPanel = new PowerDistributionPanel();

		dataLogger.addDataProvider("robotMode", () -> Robot.currentRobotMode.toString());
		dataLogger.addDataProvider("robotModeInt", () -> Robot.currentRobotMode.ordinal());
		dataLogger.addDataProvider("batteryVoltage", () -> f2(RobotController.getBatteryVoltage()));

		// do not log extra stuff
		if (canDeviceFinder.isDevicePresent(CANDeviceId.CANDeviceType.PDP, 0)) {
			dataLogger.addDataProvider("pdp.totalCurrent", () -> f2(powerDistributionPanel.getTotalCurrent()));
			dataLogger.addDataProvider("pdp.totalPower", () -> f2(powerDistributionPanel.getTotalPower()));
			dataLogger.addDataProvider("pdp.totalEnergy", () -> f2(powerDistributionPanel.getTotalEnergy()));
		}
			
			// this needs work!

		//This is here for now from last year just for referece for poor micah
		if (RobotContainer.pivotSubsystemMax2 != null) {
			dataLogger.addDataProvider("pivotMotor2AppliedOutputPower", () -> f2(RobotContainer.pivotSubsystemMax2.getAppliedOutput()));
			dataLogger.addDataProvider("pivotMotor2Current", () -> f2(RobotContainer.pivotSubsystemMax2.getOutputCurrent()));
        }

	}

	private DecimalFormat f2Formatter = new DecimalFormat("#.##");

	private String f2(double f) {
		String rv = f2Formatter.format(f);
		return rv;
	}
}