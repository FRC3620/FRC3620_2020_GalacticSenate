package org.usfirst.frc3620.logger;

public interface IFastDataLogger extends IDataLogger {
    public void setMaxLength(Double seconds);

    public void done();

    public boolean isDone();
}
