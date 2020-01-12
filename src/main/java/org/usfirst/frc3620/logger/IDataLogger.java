package org.usfirst.frc3620.logger;

import java.io.File;
import java.util.Date;

public interface IDataLogger {
    public void setLoggingDirectory(File loggingDirectory);

    public void setFilename(String filename);

    public void setFilenameTimestamp(Date ts);

    public void setInterval(double seconds);

    public void addDataProvider(String name, 
            IDataLoggerDataProvider iDataLoggerDataProvider);

    public void addMetadata(String s, double d);

    public String start();

}
