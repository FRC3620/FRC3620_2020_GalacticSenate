/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.CANDeviceFinder;
import org.usfirst.frc3620.misc.DPad;
import org.usfirst.frc3620.misc.XBoxConstants;
import org.usfirst.frc3620.misc.CANDeviceId.CANDeviceType;

import frc.robot.commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * 
 * @version 11 February 2020
 * 
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  public final static Logger logger = EventLogging.getLogger(RobotContainer.class, Level.INFO);
  final static int DRIVER_JOYSTICK_PORT = 0;
  final static int OPERATOR_JOYSTICK_PORT = 1;

  // need this
  CANDeviceFinder canDeviceFinder;

  // hardware here...
  public static CANSparkMax driveSubsystemRightFrontDrive;
  public static CANSparkMax driveSubsystemRightFrontAzimuth;
  public static CANEncoder driveSubsystemRightFrontDriveEncoder;
  public static CANEncoder driveSubsystemRightFrontAzimuthEncoder;
  public static AnalogInput driveSubsystemRightFrontHomeEncoder;
  
  public static CANSparkMax driveSubsystemLeftFrontDrive;
  public static CANSparkMax driveSubsystemLeftFrontAzimuth;
  public static CANEncoder driveSubsystemLeftFrontDriveEncoder;
  public static CANEncoder driveSubsystemLeftFrontAzimuthEncoder;
  public static AnalogInput driveSubsystemLeftFrontHomeEncoder;
  
  public static CANSparkMax driveSubsystemLeftBackDrive;
  public static CANSparkMax driveSubsystemLeftBackAzimuth;
  public static CANEncoder driveSubsystemLeftBackDriveEncoder;
  public static CANEncoder driveSubsystemLeftBackAzimuthEncoder;
  public static AnalogInput driveSubsystemLeftBackHomeEncoder;
  
  public static CANSparkMax driveSubsystemRightBackDrive;
  public static CANSparkMax driveSubsystemRightBackAzimuth;
  public static CANEncoder driveSubsystemRightBackDriveEncoder;
  public static CANEncoder driveSubsystemRightBackAzimuthEncoder;
  public static AnalogInput driveSubsystemRightBackHomeEncoder;
  
  public static SpeedController m_armMotor;
  public static WPI_TalonFX shooterSubsystemFalcon1;
  public static WPI_TalonFX shooterSubsystemFalcon2;
  public static WPI_TalonFX shooterSubsystemFalcon3;
  public static WPI_TalonSRX shooterSubsystemBallFeeder; 
  public static CANSparkMax shooterSubsystemHoodMax;
  public static CANEncoder shooterSubsystemHoodEncoder;
  public static CANSparkMax intakeSubsystemSparkMax;
  public static CANSparkMax liftSubsystemWinch;
  public static Solenoid solenoidArmUp;
  public static Solenoid intakeSubsystemArmDown;

  private static DigitalInput practiceBotJumper;

  public static Compressor theCompressor;

  // subsystems here...
  public static DriveSubsystem driveSubsystem;
  public static ArmSubsystem armSubsystem;
  public static ShooterSubsystem shooterSubsystem;
  public static LightSubsystem lightSubsystem;
  public static RumbleSubsystem rumbleSubsystemDriver;
  public static RumbleSubsystem rumbleSubsystemOperator;
  public static IntakeSubsystem intakeSubsystem;
  public static LiftSubsystem liftSubsystem;
  public static VisionSubsystem visionSubsystem;

  // joysticks here....
  public static Joystick driverJoystick;
  public static Joystick operatorJoystick;

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    canDeviceFinder = new CANDeviceFinder();
    logger.info ("CAN bus: " + canDeviceFinder.getDeviceSet());

    makeHardware();
    setupMotors();
    makeSubsystems();
    // Configure the button bindings
    configureButtonBindings();
    setupSmartDashboardCommands();
  }

  void setupMotors() {
    int kTimeoutMs = 0;

    if (driveSubsystemRightFrontDrive != null){

      resetMaxToKnownState(driveSubsystemRightFrontDrive);
      driveSubsystemRightFrontDrive.setOpenLoopRampRate(0.3);

      resetMaxToKnownState(driveSubsystemRightFrontAzimuth);

      resetMaxToKnownState(driveSubsystemLeftFrontDrive);
      driveSubsystemLeftFrontDrive.setOpenLoopRampRate(0.3);

      resetMaxToKnownState(driveSubsystemLeftFrontAzimuth);

      resetMaxToKnownState(driveSubsystemLeftBackDrive);
      driveSubsystemLeftBackDrive.setOpenLoopRampRate(0.3);

      resetMaxToKnownState(driveSubsystemLeftBackAzimuth);

      resetMaxToKnownState(driveSubsystemRightBackDrive);
      driveSubsystemRightBackDrive.setOpenLoopRampRate(0.3);
      
      resetMaxToKnownState(driveSubsystemRightBackAzimuth);
    }

    if (shooterSubsystemFalcon1 != null) {
      shooterSubsystemFalcon1.configFactoryDefault();
      shooterSubsystemFalcon1.setInverted(InvertType.InvertMotorOutput);
    }

    if (shooterSubsystemFalcon2 != null) {
      shooterSubsystemFalcon2.configFactoryDefault();
      shooterSubsystemFalcon2.follow(shooterSubsystemFalcon1);
      shooterSubsystemFalcon2.setInverted(InvertType.OpposeMaster);
    }

    if (shooterSubsystemFalcon3 != null) {
      shooterSubsystemFalcon3.configFactoryDefault();
      shooterSubsystemFalcon3.setInverted(InvertType.InvertMotorOutput);
    }

    if(shooterSubsystemBallFeeder != null) {
      shooterSubsystemBallFeeder.setInverted(InvertType.None);
    }
  }

  void makeHardware() {
    practiceBotJumper = new DigitalInput(0);
    boolean iAmACompetitionRobot = amIACompBot();
    if (!iAmACompetitionRobot) {
      logger.warn ("this is a test chassis, will try to deal with missing hardware!");
    }

    m_armMotor = new Victor(8);

    // we don't need to use the canDeviceFinder for CAN Talons because
    // they do not put up unreasonable amounts of SPAM
    if (canDeviceFinder.isDevicePresent(CANDeviceType.SPARK_MAX, 1, "Swerve") || iAmACompetitionRobot){

      driveSubsystemRightFrontDrive = new CANSparkMax(1, MotorType.kBrushless);
      driveSubsystemRightFrontDriveEncoder = driveSubsystemRightFrontDrive.getEncoder();

      driveSubsystemRightFrontAzimuth = new CANSparkMax(2, MotorType.kBrushless);
      driveSubsystemRightFrontAzimuthEncoder = driveSubsystemRightFrontAzimuth.getEncoder();

      driveSubsystemRightFrontHomeEncoder = new AnalogInput(0);
              
      driveSubsystemLeftFrontDrive = new CANSparkMax(3, MotorType.kBrushless);
      driveSubsystemLeftFrontDriveEncoder = driveSubsystemLeftFrontDrive.getEncoder();
              
      driveSubsystemLeftFrontAzimuth = new CANSparkMax(4, MotorType.kBrushless);
      driveSubsystemLeftFrontAzimuthEncoder = driveSubsystemLeftFrontAzimuth.getEncoder();

      driveSubsystemLeftFrontHomeEncoder = new AnalogInput(1);
      
      driveSubsystemLeftBackDrive = new CANSparkMax(5, MotorType.kBrushless);
      driveSubsystemLeftBackDriveEncoder = driveSubsystemLeftBackDrive.getEncoder();
              
      driveSubsystemLeftBackAzimuth = new CANSparkMax(6, MotorType.kBrushless);
      driveSubsystemLeftBackAzimuthEncoder = driveSubsystemLeftBackAzimuth.getEncoder();

      driveSubsystemLeftBackHomeEncoder = new AnalogInput(2);
              
      driveSubsystemRightBackDrive = new CANSparkMax(7, MotorType.kBrushless);
      driveSubsystemRightBackDriveEncoder = driveSubsystemRightBackDrive.getEncoder();
      
      driveSubsystemRightBackAzimuth = new CANSparkMax(8, MotorType.kBrushless);
      driveSubsystemRightBackAzimuthEncoder = driveSubsystemRightBackAzimuth.getEncoder();

      driveSubsystemRightBackHomeEncoder = new AnalogInput(3);
    }
    
    if (canDeviceFinder.isDevicePresent(CANDeviceType.SPARK_MAX, 9)){
      intakeSubsystemSparkMax = new CANSparkMax(9, MotorType.kBrushless);
      intakeSubsystemSparkMax.setIdleMode(IdleMode.kCoast);
      intakeSubsystemSparkMax.setOpenLoopRampRate(.3);
      intakeSubsystemSparkMax.setClosedLoopRampRate(.3);
    }

    if (canDeviceFinder.isDevicePresent(CANDeviceType.SPARK_MAX, 10)){
      shooterSubsystemHoodMax = new CANSparkMax(10, MotorType.kBrushless);
      shooterSubsystemHoodMax.setIdleMode(IdleMode.kCoast);
      shooterSubsystemHoodMax.setOpenLoopRampRate(.3);
      shooterSubsystemHoodMax.setClosedLoopRampRate(.3);
      shooterSubsystemHoodMax.setSmartCurrentLimit(30);
    }

    if (canDeviceFinder.isDevicePresent(CANDeviceType.TALON, 1, "Left Shooter") || iAmACompetitionRobot) {
      shooterSubsystemFalcon1 = new WPI_TalonFX(1);
    }

    if (canDeviceFinder.isDevicePresent(CANDeviceType.TALON, 2, "Pre Shooter") || iAmACompetitionRobot) { 
      shooterSubsystemFalcon2 = new WPI_TalonFX(2);
    }

    if (canDeviceFinder.isDevicePresent(CANDeviceType.TALON, 3, "Right Shooter") || iAmACompetitionRobot) { 
      shooterSubsystemFalcon3 = new WPI_TalonFX(3);
    }
    
    if (canDeviceFinder.isDevicePresent(CANDeviceType.TALON, 4, "Ball Feeder") || iAmACompetitionRobot) { 
      shooterSubsystemBallFeeder = new WPI_TalonSRX(4);
    } 

    if (canDeviceFinder.isDevicePresent(CANDeviceType.TALON, 5, "Lift Winch") || iAmACompetitionRobot) {
      liftSubsystemWinch = new CANSparkMax(10, MotorType.kBrushless);
    }
    
    if (canDeviceFinder.isDevicePresent(CANDeviceType.PCM, 0) || iAmACompetitionRobot) {
      theCompressor = new Compressor(0);
      solenoidArmUp = new Solenoid(0);
      intakeSubsystemArmDown = new Solenoid(1);
    }
  }

  void makeSubsystems() {
    driveSubsystem = new DriveSubsystem();
    armSubsystem = new ArmSubsystem();
    shooterSubsystem = new ShooterSubsystem();
    intakeSubsystem = new IntakeSubsystem();
    lightSubsystem = new LightSubsystem();
    rumbleSubsystemDriver = new RumbleSubsystem(DRIVER_JOYSTICK_PORT);
    rumbleSubsystemOperator = new RumbleSubsystem(OPERATOR_JOYSTICK_PORT);
    liftSubsystem = new LiftSubsystem();
    visionSubsystem = new VisionSubsystem();
  }

  void setupSmartDashboardCommands() {
    SmartDashboard.putData(new ZeroDriveEncodersCommand(driveSubsystem));
    SmartDashboard.putData(new ResetNavXCommand(driveSubsystem));
    SmartDashboard.putData(new LoggingTestCommand(null));
    SmartDashboard.putData(new TestTargetHeadingCommand(driveSubsystem));
  }

  static void resetMaxToKnownState(CANSparkMax x) {
    x.setInverted(false);
    x.setIdleMode(IdleMode.kCoast);
    x.setOpenLoopRampRate(1);
    x.setClosedLoopRampRate(1);
    x.setSmartCurrentLimit(50);
    // x.setSecondaryCurrentLimit(100, 0);
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    driverJoystick = new Joystick(DRIVER_JOYSTICK_PORT);
    operatorJoystick = new Joystick(OPERATOR_JOYSTICK_PORT);

    DPad driverDPad = new DPad(driverJoystick, 0);
    DPad operatorDPad = new DPad(operatorJoystick, 0);

    //Driver Controller

    driverDPad.up().whenPressed(new SnapToHeadingCommand(180, driveSubsystem));
    driverDPad.down().whenPressed(new SnapToHeadingCommand(0, driveSubsystem));
    driverDPad.right().whenPressed(new SnapToHeadingCommand(-90, driveSubsystem));
    driverDPad.left().whenPressed(new SnapToHeadingCommand(90, driveSubsystem));

    JoystickButton zeroDriveButton = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_A);
    zeroDriveButton.whenPressed(new ZeroDriveEncodersCommand(driveSubsystem));

    JoystickButton toggleFieldRelative = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_START); 
    toggleFieldRelative.whenPressed(new ToggleFieldRelativeCommand(driveSubsystem));

    JoystickButton intakeButton = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_LEFT_BUMPER);
    intakeButton.toggleWhenPressed(new IntakeCommand(intakeSubsystem));

    JoystickButton driveAndAlignButton = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_RIGHT_BUMPER);
    driveAndAlignButton.whileHeld(new DriveAndAlignCommand(driveSubsystem, visionSubsystem));

    //Operator Controller

    operatorDPad.up().whenPressed(new PopupArmCommand()); 
    operatorDPad.down().whenPressed(new PopDownArmCommand());
    operatorDPad.left().whenPressed(new SpinControlPanel4TimesCommand());
    operatorDPad.right().whenPressed(new SpinControlPanelUntilColor());

    JoystickButton shootButton = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_A);
    shootButton.toggleWhenPressed(new ShootingCommand(shooterSubsystem));

    JoystickButton beltDriver = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_B);
    beltDriver.toggleWhenPressed(new BeltDriverCommand(shooterSubsystem));

    JoystickButton intakeArmButton = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_X);
    intakeArmButton.toggleWhenPressed(new IntakeArmFireCommand(intakeSubsystem));
  }

  public static double getDriveVerticalJoystick() {
    double axisValue = driverJoystick.getRawAxis(XBoxConstants.AXIS_LEFT_Y);
    if (axisValue < 0.2 && axisValue > -0.2) {
      return 0;
    }
    return -axisValue;
  }

  public static double getDriveHorizontalJoystick() {
    double axisValue = driverJoystick.getRawAxis(XBoxConstants.AXIS_LEFT_X);
    if (axisValue < 0.2 && axisValue > -0.2) {
      return 0;
    }
    return axisValue;
  }

  public static double getDriveSpinJoystick() {
    double axisValue = driverJoystick.getRawAxis(XBoxConstants.AXIS_RIGHT_X);
    if (axisValue < 0.2 && axisValue > -0.2) {
      return 0;
    }
    return axisValue;
  }
    
  public static double getOperatorSpinJoystick() {
    double axisValue = operatorJoystick.getRawAxis(XBoxConstants.AXIS_LEFT_X);
    if (axisValue < 0.2 && axisValue > -0.2) {
      return 0;
    }
    return -axisValue;
  }

  /**
   * Return the climbing joystick position. Return positive values if
   * the operator pushes the joystick up.
   * @return
   */
  public static double getClimbingJoystick() {
    double axisValue = operatorJoystick.getRawAxis(XBoxConstants.AXIS_RIGHT_Y); //Grabs the joystick value
    if (axisValue < 0.1 && axisValue > -0.1) { //Since the joystick doesnt stay at zero, make it not give a false value
      return 0;
    } 
    return -axisValue;
  }

  public static double getColorJoystick() {
    double axisValue = operatorJoystick.getRawAxis(XBoxConstants.AXIS_LEFT_X); //Grabs the joystick value
    if (axisValue < 0.1 && axisValue > -0.1) { //Since the joystick doesnt stay at zero, make it not give a false value
      return 0;
    } 
    return -axisValue;
  }
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return null;
  }

  /**
   * Determine if this robot is a completition robot. It is if
   * it's connected to an FMS.
   * 
   * We should probably also check for an "I am a test" file or jumper
   * and return true if those are missing.
   * 
   * @return true if this robot is a competition robot.
   */
  public static boolean amIACompBot() {
    if (DriverStation.getInstance().isFMSAttached()) {
      return true;
    }

    if(practiceBotJumper.get() == true){
      return true;
    }

    return false;
  }
}
