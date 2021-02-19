package org.usfirst.frc3620.logger;

public interface IFastDataLogger extends IDataLogger {
    public void setMaxLength(Double seconds);

    public void doLog();

    public void done();

    public boolean isDone();
}
