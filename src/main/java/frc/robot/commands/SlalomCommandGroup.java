package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.DriveSubsystem;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.FastDataLoggerCollections;
import org.usfirst.frc3620.logger.IFastDataLogger;

import java.util.Date;

abstract public class SlalomCommandGroup extends SequentialCommandGroup implements IAutonomousLogger {

    Logger logger = EventLogging.getLogger(getClass(), EventLogging.Level.INFO);
    DriveSubsystem driveSubsystem;

    public SlalomCommandGroup (DriveSubsystem driveSubsystem) {
        super();
        this.driveSubsystem = driveSubsystem;
    }

    @Override
    public void initialize() {
        setupLogger();
        driveSubsystem.resetNavX();
        driveSubsystem.setDriveToRampSlowly();
        driveSubsystem.setDriveToBrake();
        super.initialize();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        driveSubsystem.setDriveToCoast();
        driveSubsystem.setDriveToRampQuickly();
        teardownLogger();
    }

    // ======================================================

    private IFastDataLogger iFastDataLogger;

    private double i_lf, i_rf, i_lb, i_rb;
    private double c_lf, c_rf, c_lb, c_rb;
    private double elapsed;
    private String legName;

    private void setupLogger() {

        iFastDataLogger = new FastDataLoggerCollections();
        iFastDataLogger.setInterval(0.0);
        iFastDataLogger.setMaxLength(0.0);
        iFastDataLogger.setFilename("slalom");
        iFastDataLogger.setFilenameTimestamp(new Date());
        iFastDataLogger.addMetadata("course", getClass().getName());

        iFastDataLogger.addDataProvider("elapsed", () -> elapsed);
        iFastDataLogger.addDataProvider("leg", () -> legName);
        iFastDataLogger.addDataProvider("lf.ip", () -> i_lf);
        iFastDataLogger.addDataProvider("lf.cp", () -> c_lf);
        iFastDataLogger.addDataProvider("rf.ip", () -> i_rf);
        iFastDataLogger.addDataProvider("rf.cp", () -> c_rf);
        iFastDataLogger.addDataProvider("lb.ip", () -> i_lb);
        iFastDataLogger.addDataProvider("lb.cp", () -> c_lb);
        iFastDataLogger.addDataProvider("rb.ip", () -> i_rb);
        iFastDataLogger.addDataProvider("rb.cp", () -> c_rb);

        iFastDataLogger.addDataProvider("lf.aa", () -> driveSubsystem.getAzimuthLeftFront());
        iFastDataLogger.addDataProvider("rf.aa", () -> driveSubsystem.getAzimuthRightFront());
        iFastDataLogger.addDataProvider("lb.aa", () -> driveSubsystem.getAzimuthLeftBack());
        iFastDataLogger.addDataProvider("rb.aa", () -> driveSubsystem.getAzimuthRightBack());

        iFastDataLogger.addDataProvider("navx.fa", () -> driveSubsystem.getNavXFixedAngle());
        iFastDataLogger.addDataProvider("navx.aa", () -> driveSubsystem.getNavXAbsoluteAngle());

        iFastDataLogger.start();

    }

    private void teardownLogger() {
        iFastDataLogger.done();
    }

    @Override
    public void setInitialDrivePositions (double lf, double rf, double lb, double rb) {
        i_lf = lf;
        i_rf = rf;
        i_lb = lb;
        i_rb = rb;
    }

    @Override
    public void setCurrentDrivePositions (double lf, double rf, double lb, double rb) {
        c_lf = lf;
        c_rf = rf;
        c_lb = lb;
        c_rb = rb;
    }

    @Override
    public void setLegName (String s) {
        this.legName = s;
    }

    @Override
    public void setElapsed(double d) {
        this.elapsed = d;
    }

    @Override
    public void doLog() {
        iFastDataLogger.doLog();
    }

}
