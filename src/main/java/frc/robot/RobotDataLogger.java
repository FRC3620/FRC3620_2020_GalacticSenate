package frc.robot;

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

		dataLogger.addDataProvider("matchTime", () -> f2(driverStation.getMatchTime()));
		dataLogger.addDataProvider("robotMode", () -> Robot.currentRobotMode.toString());
		dataLogger.addDataProvider("robotModeInt", () -> Robot.currentRobotMode.ordinal());
		dataLogger.addDataProvider("batteryVoltage", () -> f2(RobotController.getBatteryVoltage()));

		if (canDeviceFinder.isDevicePresent(CANDeviceId.CANDeviceType.PDP, 0)) {
			dataLogger.addDataProvider("pdp.totalCurrent", () -> f2(powerDistributionPanel.getTotalCurrent()));
			dataLogger.addDataProvider("pdp.totalPower", () -> f2(powerDistributionPanel.getTotalPower()));
			dataLogger.addDataProvider("pdp.totalEnergy", () -> f2(powerDistributionPanel.getTotalEnergy()));
		}

		if (RobotContainer.theCompressor != null) {
			dataLogger.addDataProvider("compressorCurrent", () -> f2(RobotContainer.theCompressor.getCompressorCurrent()));
		}

		if (RobotContainer.liftSubsystemWinch != null){
			dataLogger.addDataProvider("Lift.Winch.CurrentOut", () -> f2(RobotContainer.liftSubsystem.getLiftCurrent()));
			dataLogger.addDataProvider("Lift.Winch.PercentOut", () -> f2(RobotContainer.liftSubsystem.getLiftPercentOut()));
			dataLogger.addDataProvider("Lift.Winch.Voltage", () -> f2(RobotContainer.liftSubsystem.getLiftVoltage()));
		}

		if(RobotContainer.shooterSubsystemFalcon1 != null){
			dataLogger.addDataProvider("Shooter.Hood.Position.Requested", () -> f2(RobotContainer.shooterSubsystem.getRequestedHoodPosition()));
			dataLogger.addDataProvider("Shooter.Hood.Position.Actual", () -> f2(RobotContainer.shooterSubsystem.getActualHoodPosition()));
			dataLogger.addDataProvider("Shooter.Hood.CurrentOut", () -> f2(RobotContainer.shooterSubsystem.getHoodCurrent()));
			dataLogger.addDataProvider("Shooter.Hood.PercentOut", () -> f2(RobotContainer.shooterSubsystem.getHoodPercentOut()));
			dataLogger.addDataProvider("Shooter.Hood.Voltage", () -> f2(RobotContainer.shooterSubsystem.getHoodVoltage()));
	
			dataLogger.addDataProvider("Shooter.Top.Velocity.Requested", () -> f2(RobotContainer.shooterSubsystem.getRequestedTopShooterVelocity()));
			dataLogger.addDataProvider("Shooter.Top.Velocity.Actual", () -> f2(RobotContainer.shooterSubsystem.getActualTopShooterVelocity()));
			dataLogger.addDataProvider("Shooter.Top.CurrentOut", () -> f2(RobotContainer.shooterSubsystem.getTopShooterCurrent()));
			dataLogger.addDataProvider("Shooter.Top.PercentOut", () -> f2(RobotContainer.shooterSubsystem.getTopPercentOut()));
			dataLogger.addDataProvider("Shooter.Top.Voltage", () -> f2(RobotContainer.shooterSubsystem.getTopVoltage()));

			dataLogger.addDataProvider("Shooter.Bottom.Velocity.Requested", () -> f2(RobotContainer.shooterSubsystem.getRequestedBottomShooterVelocity()));
			dataLogger.addDataProvider("Shooter.Bottom.Velocity.Actual", () -> f2(RobotContainer.shooterSubsystem.getActualBottomShooterVelocity()));
			dataLogger.addDataProvider("Shooter.Bottom.CurrentOut", () -> f2(RobotContainer.shooterSubsystem.getBottomShooterCurrent()));
			dataLogger.addDataProvider("Shooter.Bottom.PercentOut", () -> f2(RobotContainer.shooterSubsystem.getBottomPercentOut()));
			dataLogger.addDataProvider("Shooter.Bottom.Voltage", () -> f2(RobotContainer.shooterSubsystem.getBottomVoltage()));

			dataLogger.addDataProvider("Shooter.Feeder.CurrentOut", () -> f2(RobotContainer.beltSubsystem.getFeederCurrent()));
			dataLogger.addDataProvider("Shooter.Feeder.PercentOut", () -> f2(RobotContainer.beltSubsystem.getFeederOutput()));
			dataLogger.addDataProvider("Shooter.Feeder.Voltage", () -> f2(RobotContainer.beltSubsystem.getFeederVoltage()));
		}

		/*if(RobotContainer.intakeSubsystemSparkMax != null) {
			dataLogger.addDataProvider("Intake.CurrentOut", () -> f2(RobotContainer.intakeSubsystem.getIntakeCurrent()));
			dataLogger.addDataProvider("Intake.PercentOut", () -> f2(RobotContainer.intakeSubsystem.getIntakePercentOut()));
			dataLogger.addDataProvider("Intake.Voltage", () -> f2(RobotContainer.intakeSubsystem.getIntakeVoltage()));
		}*/

		dataLogger.addDataProvider("getTargetHeading", () -> f2(RobotContainer.driveSubsystem.getTargetHeading()));
	}

	private DecimalFormat f2Formatter = new DecimalFormat("#.##");

	private String f2(double f) {
		String rv = f2Formatter.format(f);
		return rv;
	}
}