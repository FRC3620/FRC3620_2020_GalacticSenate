package frc.robot.commands;

public interface IAutonomousLogger {
    public void setInitialDrivePositions (double lf, double rf, double lr, double rr);
    public void setCurrentDrivePositions (double lf, double rf, double lr, double rr);
    public void setLegName (String s);
    public void setElapsed (double d);
    public void doLog();
}
