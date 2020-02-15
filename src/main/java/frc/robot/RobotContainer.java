/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
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
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
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
  public static CANSparkMax intakeSubsystemSparkMax;
  public static WPI_TalonSRX liftSubsystemWinch;
  public static Solenoid liftSubsystemRelease;
  public static Solenoid solenoidArmUp;
  public static Solenoid ballReleaseSolenoid;
  public static Solenoid netSolenoid;
  public static Solenoid intakeSubsystemArmDown;
  public static Solenoid intakeSubsystemHolder1;
  public static Solenoid intakeSubsystemHolder2;

  private static DigitalInput practiceBotJumper;

  // subsystems here...
  public static DriveSubsystem driveSubsystem;
  public static ArmSubsystem armSubsystem;
  public static ShooterSubsystem shooterSubsystem;
  public static LightSubsystem lightSubsystem;
  public static RumbleSubsystem rumbleSubsystemDriver;
  public static RumbleSubsystem rumbleSubsystemOperator;
  public static IntakeSubsystem intakeSubsystem;
  public static LiftSubsystem liftSubsystem;

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
      driveSubsystemRightFrontDrive.setOpenLoopRampRate(0.6);

      resetMaxToKnownState(driveSubsystemRightFrontAzimuth);

      resetMaxToKnownState(driveSubsystemLeftFrontDrive);
      driveSubsystemLeftFrontDrive.setOpenLoopRampRate(0.6);

      resetMaxToKnownState(driveSubsystemLeftFrontAzimuth);

      resetMaxToKnownState(driveSubsystemLeftBackDrive);
      driveSubsystemLeftBackDrive.setOpenLoopRampRate(0.6);

      resetMaxToKnownState(driveSubsystemLeftBackAzimuth);

      resetMaxToKnownState(driveSubsystemRightBackDrive);
      driveSubsystemRightBackDrive.setOpenLoopRampRate(0.6);
      
      resetMaxToKnownState(driveSubsystemRightBackAzimuth);

    }

    if (shooterSubsystemFalcon1 != null) {
      shooterSubsystemFalcon1.configFactoryDefault();
      shooterSubsystemFalcon1.setInverted(InvertType.InvertMotorOutput);

      /*
      shooterSubsystemFalcon1.setStatusFramePeriod(0x1240, 1, kTimeoutMs); // undocumented current measurement status frame
      shooterSubsystemFalcon1.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 1, kTimeoutMs);
      shooterSubsystemFalcon1.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 1, kTimeoutMs);
      shooterSubsystemFalcon1.setStatusFramePeriod(StatusFrameEnhanced.Status_4_AinTempVbat, 1, kTimeoutMs);
      */
    }

    if (shooterSubsystemFalcon2 != null) {
      shooterSubsystemFalcon2.configFactoryDefault();
      shooterSubsystemFalcon2.setInverted(InvertType.None);
      if (false) {
        // undocumented current measurement status frame
        shooterSubsystemFalcon2.setStatusFramePeriod(0x1240, 1, kTimeoutMs);
        shooterSubsystemFalcon2.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 1, kTimeoutMs);
        shooterSubsystemFalcon2.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 1, kTimeoutMs);
        shooterSubsystemFalcon2.setStatusFramePeriod(StatusFrameEnhanced.Status_4_AinTempVbat, 1, kTimeoutMs);
      }
    }

    if (shooterSubsystemFalcon3 != null) {
      shooterSubsystemFalcon3.configFactoryDefault();
      shooterSubsystemFalcon3.follow(shooterSubsystemFalcon1);
      shooterSubsystemFalcon3.setInverted(InvertType.OpposeMaster);
    }

    if(shooterSubsystemBallFeeder != null) {
      shooterSubsystemBallFeeder.setInverted(InvertType.None);
    }
  }

  void makeHardware() {
    boolean iAmACompetitionRobot = amIACompBot();
    if (!iAmACompetitionRobot) {
      logger.warn ("this is a test chassis, will try to deal with missing hardware!");
    }

    practiceBotJumper = new DigitalInput(0);

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
      liftSubsystemWinch = new WPI_TalonSRX(5);
    }
    
    if (canDeviceFinder.isDevicePresent(CANDeviceType.PCM, 0) || iAmACompetitionRobot) {
      liftSubsystemRelease = new Solenoid(0);
      solenoidArmUp = new Solenoid(1);
      ballReleaseSolenoid = new Solenoid(2);
      intakeSubsystemArmDown = new Solenoid(3);
      intakeSubsystemHolder1 = new Solenoid(4);
      intakeSubsystemHolder2 = new Solenoid(5);
      netSolenoid = new Solenoid(6);
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
  }

  void setupSmartDashboardCommands() {
    SmartDashboard.putData(new ZeroDriveEncodersCommand(driveSubsystem));
    SmartDashboard.putData(new ResetNavXCommand(driveSubsystem));
    SmartDashboard.putData(new LoggingTestCommand(null));
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

    DPad operatorDPad = new DPad(operatorJoystick, 0);

    //Driver Controller

    JoystickButton zeroDriveButton = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_LEFT_BUMPER);
    zeroDriveButton.whenPressed(new ZeroDriveEncodersCommand(driveSubsystem));

    JoystickButton toggleFieldRelative = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_RIGHT_BUMPER); 
    toggleFieldRelative.whenPressed(new ToggleFieldRelativeCommand(driveSubsystem));

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

    JoystickButton intakeButton = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_RIGHT_BUMPER);
    intakeButton.toggleWhenPressed(new IntakeCommand(intakeSubsystem)); 

    JoystickButton releaseBallsFromTroughButton = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_Y);
    releaseBallsFromTroughButton.toggleWhenPressed(new BallsCommand());

    JoystickButton liftReleaseButton = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_RIGHT_STICK);
    liftReleaseButton.whenPressed(new LiftReleaseCommand(liftSubsystem));

    JoystickButton holdBallsInIntakeButton = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_LEFT_BUMPER);
    holdBallsInIntakeButton.toggleWhenPressed(new IntakeBallHolderCommand(intakeSubsystem));
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
    return -axisValue;
  }
    
  public static double getOperatorSpinJoystick() {
    double axisValue = operatorJoystick.getRawAxis(XBoxConstants.AXIS_LEFT_X);
    if (axisValue < 0.2 && axisValue > -0.2) {
      return 0;
    }
    return -axisValue;
  }

  public static double getClimbingJoystick() {
    double axisValue = operatorJoystick.getRawAxis(XBoxConstants.AXIS_RIGHT_Y); //Grabs the joystick value
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
