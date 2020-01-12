package org.usfirst.frc3620.logger;

import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging.Level;

abstract public class DataLoggerBase implements IDataLogger {
	boolean started /* = false */;

	List<NamedDataProvider> namedDataProviders = new ArrayList<>();

	File loggingDirectory = LoggingMaster.getLoggingDirectory();
	String filename = null;
	Date filenameTimestamp = null;

	Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

	Map<String, Object> metadata = new TreeMap<>();

	double intervalInSeconds = 0.100;

	File outputFile;
	Timer timer;

	@Override
	public void setLoggingDirectory(File loggerDirectory) {
		this.loggingDirectory = loggerDirectory;
	}

	@Override
	public void addDataProvider(String _name, IDataLoggerDataProvider _iDataLoggerDataProvider) {
		if (!started) {
			namedDataProviders.add(new NamedDataProvider(_name, _iDataLoggerDataProvider));
		} else {
			logger.error("Cannot addDataProvider(...) after start()");
		}
	}

	@Override
	public void setFilename(String _filename) {
		logger.info("setFilename(\"{}\")", _filename);
		if (!started) {
			filename = _filename;
		} else {
			logger.error("Cannot setFilename(...) after start()");
		}
	}

	@Override
	public void setFilenameTimestamp(Date date) {
		filenameTimestamp = date;
	}

	@Override
	public void addMetadata(String s, double d) {
		if (!started) {
			metadata.put(s, d);
		} else {
			logger.error("Cannot addMetadata(...) after start()");
		}
	}

	@Override
	public void setInterval(double seconds) {
		intervalInSeconds = seconds;
	}

	double getTimeInSeconds() {
		return edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
		// return System.currentTimeMillis() / 1000.0;
	}

	@Override
	public String start() {
		started = true;

		setupOutputFile();

		startTimer();

		if (outputFile == null) {
			return "";
		} else {
			return outputFile.getAbsolutePath();
		}
	}

	void setupOutputFile() {
		if (outputFile == null) {
			synchronized (DataLoggerBase.this) {
				if (outputFile == null) {
					Date timestamp = filenameTimestamp;
					if (timestamp == null) {
						timestamp = LoggingMaster.getTimestamp();
					}
					if (timestamp != null) {
						String _timestampString = LoggingMaster.convertTimestampToString(timestamp);
						String fullFilename = _timestampString + ".csv";
						if (filename != null) {
							fullFilename = filename + "_" + fullFilename;
						}
						  logger.info("setupOutputFile filename is {}", fullFilename);
			
						outputFile = new File(loggingDirectory, fullFilename);
						logger.info("setting file to {}", outputFile);
					}
				}
			}
		}
	}

	static void writeHeader(PrintWriter w, Iterable<NamedDataProvider> namedDataProviders,
			Map<String, Object> metadata) {
		w.print("time,timeSinceStart");
		for (NamedDataProvider namedDataProvider : namedDataProviders) {
			w.print(",");
			w.print(namedDataProvider.name);
		}
		w.println();

		if (metadata != null) {
			for (String n : metadata.keySet()) {
				w.print("# ");
				w.print(n);
				w.print(" = ");
				w.print(metadata.get(n));
				w.println();
			}
		}
	}

	abstract void startTimer();

	public class NamedDataProvider {
		public NamedDataProvider(String name, IDataLoggerDataProvider iDataLoggerDataProvider) {
			super();
			this.name = name;
			this.iDataLoggerDataProvider = iDataLoggerDataProvider;
		}

		String name;
		IDataLoggerDataProvider iDataLoggerDataProvider;
	}
}
