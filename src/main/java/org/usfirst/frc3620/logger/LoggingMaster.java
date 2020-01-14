package org.usfirst.frc3620.logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class LoggingMaster {
    private final static long SOME_TIME_AFTER_1970 = 523980000000L;

    private static Date _timestamp = null;

    private static File _logDirectory = null;
    
    private static String defaultLogLocation = "/home/lvuser/logs"; 

    // http://javarevisited.blogspot.com/2014/05/double-checked-locking-on-singleton-in-java.html
    public static Date getTimestamp() {
        if (_timestamp == null) { // do a quick check (no overhead from
                                        // synchonized)
            synchronized (LoggingMaster.class) {
                if (_timestamp == null) { // Double checked
                    long now = System.currentTimeMillis();
                    
                    if (now > SOME_TIME_AFTER_1970) {
                        _timestamp = new Date();
                        String logMessage = String.format(
                                "timestamp for logs is %s\n",convertTimestampToString(_timestamp));
                        EventLogging.writeWarningToDS(logMessage);
                    }
                }
            }
        }
        return _timestamp;
    }

    public static String convertTimestampToString(Date ts) {
        SimpleDateFormat formatName = new SimpleDateFormat(
                "yyyyMMdd-HHmmss");
        return formatName.format(ts);
    }
    
    public static void setDefaultLogLocation (String s) {
    	defaultLogLocation = s;
    }

    public static File getLoggingDirectory() {
        if (_logDirectory == null) { // quick check
            synchronized (LoggingMaster.class) {
                if (_logDirectory == null) {
                    // Set dataLogger and Time information
                    TimeZone.setDefault(
                            TimeZone.getTimeZone("America/Detroit"));

                    if (_logDirectory == null)
                        _logDirectory = searchForLogDirectory(new File("/u"));
                    if (_logDirectory == null)
                        _logDirectory = searchForLogDirectory(new File("/v"));
                    if (_logDirectory == null)
                        _logDirectory = searchForLogDirectory(new File("/x"));
                    if (_logDirectory == null)
                        _logDirectory = searchForLogDirectory(new File("/y"));
                    if (_logDirectory == null) {
                        _logDirectory = new File(defaultLogLocation);
                        if (!_logDirectory.exists()) {
                            _logDirectory.mkdir();
                        }
                    }
                    String logMessage = String.format("Log directory is %s\n",
                            _logDirectory);
                    System.out.print(logMessage); // NOPMD
                }
            }
        }
        return _logDirectory;
    }

    static File searchForLogDirectory(File root) {
        // does the root directory exist?
        if (!root.isDirectory())
            return null;

        File logDirectory = new File(root, "logs");
        if (!logDirectory.isDirectory())
            return null;

        return logDirectory;
    }
}