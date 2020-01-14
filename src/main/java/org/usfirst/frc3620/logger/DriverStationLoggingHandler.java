package org.usfirst.frc3620.logger;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

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
		if (level >= Level.SEVERE.intValue()) {
			DriverStation.reportError(s, false);
		} else if (level >= Level.WARNING.intValue()) {
			DriverStation.reportWarning(s, false);
		} else {
			System.out.print("" + sequence + " " + s); // NOPMD
			sequence++;
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
