package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.RobotContainer;
import frc.robot.subsystems.BeltSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import org.usfirst.frc3620.logger.FastDataLoggerCollections;
import org.usfirst.frc3620.logger.IFastDataLogger;

import java.util.Date;

public class ShootingDataLogger {
    public static IFastDataLogger getShootingDataLogger (String name) {
        return getShootingDataLogger(name, 15.0);
    }

    public static IFastDataLogger getShootingDataLogger (String name, double length) {
        TalonFX rightTalonFX = RobotContainer.shooterSubsystemFalcon1;
        TalonFX leftTalonFX = RobotContainer.shooterSubsystemFalcon2;
        TalonFX bottomTalonFX = RobotContainer.shooterSubsystemFalcon3;
        ShooterSubsystem shooterSubsystem = RobotContainer.shooterSubsystem;
        BeltSubsystem beltSubsystem = RobotContainer.beltSubsystem;

        IFastDataLogger dataLogger = new FastDataLoggerCollections();
        dataLogger.setInterval(0.005);
        dataLogger.setMaxLength(length);
        dataLogger.setFilename(name);
        Date timestamp = new Date();
        dataLogger.setFilenameTimestamp(timestamp);

        dataLogger.addMetadata("timestamp", timestamp);
        if (rightTalonFX != null) {
            dataLogger.addDataProvider("right_setpoint", () -> rightTalonFX.getClosedLoopTarget());
            dataLogger.addDataProvider("right_rpm", () -> rightTalonFX.getSelectedSensorVelocity());
            dataLogger.addDataProvider("right_outputCurrent", () -> rightTalonFX.getStatorCurrent());
            dataLogger.addDataProvider("right_supplyCurrent", () -> rightTalonFX.getSupplyCurrent());
            dataLogger.addDataProvider("right_outputVoltage", () -> rightTalonFX.getMotorOutputVoltage());
            dataLogger.addDataProvider("right_supplyVoltage", () -> rightTalonFX.getBusVoltage());
            dataLogger.addDataProvider("right_outputPercent", () -> rightTalonFX.getMotorOutputPercent());
            dataLogger.addDataProvider("right_error", () -> rightTalonFX.getClosedLoopError());
            dataLogger.addDataProvider("right_integral_accumulator", () -> rightTalonFX.getIntegralAccumulator());
        }
        if (bottomTalonFX != null) {
            dataLogger.addDataProvider("bottom_setpoint", () -> bottomTalonFX.getClosedLoopTarget());
            dataLogger.addDataProvider("bottom_rpm", () -> bottomTalonFX.getSelectedSensorVelocity());
            dataLogger.addDataProvider("bottom_outputCurrent", () -> bottomTalonFX.getStatorCurrent());
            dataLogger.addDataProvider("bottom_supplyCurrent", () -> bottomTalonFX.getSupplyCurrent());
            dataLogger.addDataProvider("bottom_outputVoltage", () -> bottomTalonFX.getMotorOutputVoltage());
            dataLogger.addDataProvider("bottom_supplyVoltage", () -> bottomTalonFX.getBusVoltage());
            dataLogger.addDataProvider("bottom_outputPercent", () -> bottomTalonFX.getMotorOutputPercent());
            dataLogger.addDataProvider("bottom_error", () -> bottomTalonFX.getClosedLoopError());
        }
        if (leftTalonFX != null) {
            dataLogger.addDataProvider("left_setpoint", () -> leftTalonFX.getClosedLoopTarget());
            dataLogger.addDataProvider("left_rpm", () -> leftTalonFX.getSelectedSensorVelocity());
            dataLogger.addDataProvider("left_outputCurrent", () -> leftTalonFX.getStatorCurrent());
            dataLogger.addDataProvider("left_supplyCurrent", () -> leftTalonFX.getSupplyCurrent());
            dataLogger.addDataProvider("left_outputVoltage", () -> leftTalonFX.getMotorOutputVoltage());
            dataLogger.addDataProvider("left_supplyVoltage", () -> leftTalonFX.getBusVoltage());
            dataLogger.addDataProvider("left_outputPercent", () -> leftTalonFX.getMotorOutputPercent());
            dataLogger.addDataProvider("left_error", () -> leftTalonFX.getClosedLoopError());
        }
        dataLogger.addDataProvider("top_shooter_actual", () -> shooterSubsystem.getActualTopShooterVelocity());
        dataLogger.addDataProvider("top_shooter_requested", () -> shooterSubsystem.getRequestedTopShooterVelocity());
        dataLogger.addDataProvider("bottom_shooter_actual", () -> shooterSubsystem.getActualBottomShooterVelocity());
        dataLogger.addDataProvider("bottom_shooter_requested", () -> shooterSubsystem.getRequestedBottomShooterVelocity());
        dataLogger.addDataProvider("hood_actual", () -> shooterSubsystem.getActualHoodPosition());
        dataLogger.addDataProvider("hood_setpoint", () -> shooterSubsystem.getRequestedHoodPosition());
        dataLogger.addDataProvider("belt_power", () -> beltSubsystem.getBeltPower());
        dataLogger.addDataProvider("battery_voltage", () -> RobotController.getBatteryVoltage());

        return dataLogger;
    }
}
