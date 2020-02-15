/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.function.Consumer;

import org.slf4j.Logger;

import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import org.usfirst.frc3620.misc.ColorPattern;
import org.usfirst.frc3620.misc.RobotMode;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  private Logger logger;
  static RobotMode currentRobotMode = RobotMode.INIT, previousRobotMode;

  DriverStation driverStation;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    logger = EventLogging.getLogger(Robot.class, Level.INFO);

    driverStation = DriverStation.getInstance();

    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    RobotContainer.lightSubsystem.setPreset(ColorPattern.Preset.INIT);

    CommandScheduler.getInstance().onCommandInitialize(new Consumer<Command>() {//whenever a command initializes, the function declared bellow will run.
      public void accept(Command command) {
        logger.info("Initialized {}", command.getClass().getSimpleName());//I scream at people
      }
    });

    CommandScheduler.getInstance().onCommandFinish(new Consumer<Command>() {//whenever a command ends, the function declared bellow will run.
      public void accept(Command command) {
        logger.info("Ended {}", command.getClass().getSimpleName());//I, too, scream at people
      }
    });

    CommandScheduler.getInstance().onCommandInterrupt(new Consumer<Command>() {//whenever a command ends, the function declared bellow will run.
      public void accept(Command command) {
        logger.info("Interrupted {}", command.getClass().getSimpleName());//I, in addition, as well, scream.
      }
    });
  }


  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }
  

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
    processRobotModeChange(RobotMode.DISABLED);
    RobotContainer.lightSubsystem.setPreset(ColorPattern.Preset.DISABLED);
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    processRobotModeChange(RobotMode.AUTONOMOUS);
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }

    RobotContainer.lightSubsystem.setPreset(ColorPattern.Preset.AUTO);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    processRobotModeChange(RobotMode.TELEOP);
    logMatchInfo();

    RobotContainer.lightSubsystem.setPreset(ColorPattern.Preset.TELEOP);
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();

    processRobotModeChange(RobotMode.TEST);
    RobotContainer.lightSubsystem.setPreset(ColorPattern.Preset.TEST);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  /*
	 * this routine gets called whenever we change modes
	 */
	void processRobotModeChange(RobotMode newMode) {
		logger.info("Switching from {} to {}", currentRobotMode, newMode);
		
		previousRobotMode = currentRobotMode;
		currentRobotMode = newMode;

		// if any subsystems need to know about mode changes, let
		// them know here.
		// exampleSubsystem.processRobotModeChange(newMode);
		
  }
  
  public static RobotMode getCurrentRobotMode(){
    return currentRobotMode;
  }

  void logMatchInfo() {
    if (driverStation.isFMSAttached()) {
      logger.info("FMS attached. Event name {}, match type {}, match number {}, replay number {}", 
        driverStation.getEventName(),
        driverStation.getMatchType(),
        driverStation.getMatchNumber(),
        driverStation.getReplayNumber());
    }
    logger.info("Alliance {}, position {}", driverStation.getAlliance(), driverStation.getLocation());
  }
}
