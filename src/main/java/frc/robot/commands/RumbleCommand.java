package frc.robot.commands;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.RumbleSubsystem;

/**
 * @author Nick Zimanski (SlippStream)
 * @version 15 January 2020
 * 
 * Finalised command -- rules in subsystem
 */
public class RumbleCommand extends CommandBase {

    private final boolean logRumbleDetails = false;

    public enum Hand {
        LEFT,
        RIGHT,
        BOTH
    }

    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);
    private RumbleSubsystem subsystem;
    private Double intensity; // 0.1 to 1.0
    private Hand hand;
    private Double duration; // In seconds

    private final Hand handDefault = Hand.BOTH;
    private final Double intensityDefault = 1.0;
    private final Double durationDefault = 1.0;

    private boolean disabled, continuous, disableChange = false;

    private Timer timer = new Timer();
    
    /**
     * @param subsystem Which controller to rumble. Either Robot.rumbleSubsystemDriver or Robot.rumbleSubsystemOperator
     * @param hand Which side of the controller to rumble. Either Hand.LEFT, Hand.RIGHT, or Hand.BOTH
     * @param intensity How hard to rumble the controller. Between 0.1 and 1.0
     * @param duration How long the controller will rumble in seconds
     */
    public RumbleCommand(RumbleSubsystem subsystem, Hand hand, Double intensity, Double duration) {
        addRequirements(subsystem);

        this.subsystem = subsystem;
        this.duration = duration;
        this.hand = hand;
        this.intensity = intensity;

        continuous = false;
    }

    /**
     * @param subsystem Which controller to rumble. Either Robot.rumbleSubsystemDriver or Robot.rumbleSubsystemOperator
     * @param intensity How hard to rumble the controller. Between 0.1 and 1.0
     * @param duration How long the controller will rumble in seconds
     * 
     * @see Hand defaults to Hand.BOTH
     */
    public RumbleCommand(RumbleSubsystem subsystem, Double intensity, Double duration) {
        this(subsystem, null, intensity, duration);
    }

    /**
     * @param subsystem Which controller to rumble. Either Robot.rumbleSubsystemDriver or Robot.rumbleSubsystemOperator
     * 
     * @see Hand defaults to Hand.BOTH
     * @see Intensity defaults to 1.0
     * @see Duration defaults to 1.0
     */
    public RumbleCommand(RumbleSubsystem subsystem) {
        this(subsystem, null, null, null);
    }

    /**
     * @param subsystem Which controller to rumble. Either Robot.rumbleSubsystemDriver or Robot.rumbleSubsystemOperator
     * @param hand Which side of the controller to rumble. Either Hand.LEFT, Hand.RIGHT, or Hand.BOTH
     * @param intensity How hard to rumble the controller. Between 0.1 and 1.0
     * 
     * @see This command should ONLY be used if you plan on interrupting it with .cancel()
     */
    public RumbleCommand(RumbleSubsystem subsystem, Hand hand, Double intensity) {
        this(subsystem, hand, intensity, null);

        continuous = true;
    }

    /**
     * @param subsystem Which controller to disable/enable rumble for
     * @param disabled Which state to set the controller to
     * 
     * @see This command will stop any and all rumble from happening on the controller until it's enabled
     */
    public RumbleCommand(RumbleSubsystem subsystem, Boolean disabled) {
        this.subsystem = subsystem;
        this.disabled = disabled;
        this.disableChange = true;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        //Finishes the command when the timer is up
        if (disableChange) {
            return true;
        }
        if (!continuous) {
            if (timer.get() >= duration) {return true;}
        }
        return false;
    }

    @Override
    public void initialize() {
        if (!disableChange) {
            //System.out.println("Rumble Init");
            //sets the defaults
            if (duration == null) {duration = durationDefault;}
            if (hand == null) {hand = handDefault;}
            if (intensity == null) {intensity = intensityDefault;}

            //logs info
            if (logRumbleDetails) {
                if (subsystem == RobotContainer.rumbleSubsystemDriver) {
                    logger.info("Rumbling driver controller");
                }
                else {
                    logger.info("Rumbling operator controller");
                }
            }

            //Clears the rumble
            subsystem.clearRumble();

            //Sets the rumble and starts the timer
            subsystem.setRumble(hand, intensity);
            if (!continuous) 
                timer.start();
        }
        else {
            subsystem.setDisabled(this.disabled);
        }
        
        super.initialize();
    }


    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        if (logRumbleDetails) {
            //EventLogging.commandMessage(logger);
            if (!disabled) {
                if (!interrupted) {
                    if (subsystem == RobotContainer.rumbleSubsystemDriver)
                        logger.info("Driver rumble finished");
                    else
                        logger.info("Operator rumble finished");
                }
                else {
                    //EventLogging.commandMessage(logger);
                    if (subsystem == RobotContainer.rumbleSubsystemDriver)
                        logger.info("Driver rumble interrupted");
                    else
                        logger.info("Operator rumble interrupted");
                }
            }
        }

        subsystem.clearRumble();
    }
}