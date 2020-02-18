package org.usfirst.frc3620.logger;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.DriverStation;

public class DriverStationLoggingHandler extends Handler {
	int sequence = 1;
	
	DriverStationFormatter formatter = new DriverStationFormatter();
    public DriverStationLoggingHandler() {
		super();
	}

	@Override
    public void publish(LogRecord record) {
		String s = formatter.format(record);
		int level = record.getLevel().intValue();
		StringBuilder sb = new StringBuilder();
		sb.append(sequence++);
		sb.append(" ");
		sb.append(s);
		if (level >= Level.SEVERE.intValue()) {
			//DriverStation.reportError(sb.toString(), false);
			HAL.sendError(true, 2, false, sb.toString(), "", "", false);
		} else if (level >= Level.WARNING.intValue()) {
			//DriverStation.reportWarning(sb.toString(), false);
			HAL.sendError(false, 2, false, sb.toString(), "", "", false);
		} else {
			System.out.print(sb.toString()); // NOPMD
		}
    }

	@Override
	public void close() throws SecurityException {
	}

	@Override
	public void flush() {
	}
	
    static class DriverStationFormatter extends java.util.logging.Formatter {
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder(1000);
            builder.append('[').append(record.getLoggerName()).append("] ");
            builder.append(record.getLevel()).append(" - ");
            builder.append(formatMessage(record));
            builder.append('\n');
            return builder.toString();
        }
    }



}
