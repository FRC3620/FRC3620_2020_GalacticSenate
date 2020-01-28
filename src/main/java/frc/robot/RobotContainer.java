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
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.XboxController;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;
import org.usfirst.frc3620.misc.CANDeviceFinder;
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
  public static SpeedController m_armMotor;
  public static WPI_TalonFX shooterSubsystemFalcon1;
  public static WPI_TalonFX shooterSubsystemFalcon2;
  public static WPI_TalonFX shooterSubsystemFalcon3;
  public static WPI_TalonFX intakeSubsystemFalcon1;
  public static WPI_TalonFX liftSubsystemWinch;
  public static Solenoid liftSubsystemRelease;

  // subsystems here...
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
  }

  void setupMotors() {
    int kTimeoutMs = 0;
    if (shooterSubsystemFalcon1 != null) {
      shooterSubsystemFalcon1.setInverted(InvertType.InvertMotorOutput);
      // undocumented current measurement status frame
      shooterSubsystemFalcon1.setStatusFramePeriod(0x1240, 1, kTimeoutMs);
      shooterSubsystemFalcon1.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 1, kTimeoutMs);
      shooterSubsystemFalcon1.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 1, kTimeoutMs);
      shooterSubsystemFalcon1.setStatusFramePeriod(StatusFrameEnhanced.Status_4_AinTempVbat, 1, kTimeoutMs);
    }

    if (shooterSubsystemFalcon2 != null) {
      shooterSubsystemFalcon2.setInverted(InvertType.InvertMotorOutput);
      // undocumented current measurement status frame
      shooterSubsystemFalcon2.setStatusFramePeriod(0x1240, 1, kTimeoutMs);
      shooterSubsystemFalcon2.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 1, kTimeoutMs);
      shooterSubsystemFalcon2.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 1, kTimeoutMs);
      shooterSubsystemFalcon2.setStatusFramePeriod(StatusFrameEnhanced.Status_4_AinTempVbat, 1, kTimeoutMs);
    }

    if (shooterSubsystemFalcon3 != null) {
      shooterSubsystemFalcon3.setInverted(InvertType.OpposeMaster);
      // undocumented current measurement status frame
      shooterSubsystemFalcon3.setStatusFramePeriod(0x1240, 1, kTimeoutMs);
      shooterSubsystemFalcon3.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 1, kTimeoutMs);
      shooterSubsystemFalcon3.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 1, kTimeoutMs);
      shooterSubsystemFalcon3.setStatusFramePeriod(StatusFrameEnhanced.Status_4_AinTempVbat, 1, kTimeoutMs);
    }
  }

  void makeHardware() {
    m_armMotor = new Victor(8);

    // we don't need to use the canDeviceFinder for CAN Talons because
    // they do not put up unreasonable amounts of SPAM
    if (canDeviceFinder.isDevicePresent(CANDeviceType.TALON, 1)) {
      shooterSubsystemFalcon1 = new WPI_TalonFX(1);
    }
    if (canDeviceFinder.isDevicePresent(CANDeviceType.TALON, 2)) { 
      shooterSubsystemFalcon2 = new WPI_TalonFX(2);
    }
    if (canDeviceFinder.isDevicePresent(CANDeviceType.TALON, 5)) { 
      shooterSubsystemFalcon3 = new WPI_TalonFX(5);
    }
    if (canDeviceFinder.isDevicePresent(CANDeviceType.TALON, 3)) {
      intakeSubsystemFalcon1 = new WPI_TalonFX(3); 
    }
    if (canDeviceFinder.isDevicePresent(CANDeviceType.TALON, 4)) {
      liftSubsystemWinch = new WPI_TalonFX(4);
    }
    if (canDeviceFinder.isDevicePresent(CANDeviceType.PCM, 0)) {
      liftSubsystemRelease = new Solenoid(0);
    }
  }

  void makeSubsystems() {
    armSubsystem = new ArmSubsystem();
    shooterSubsystem = new ShooterSubsystem();
    intakeSubsystem = new IntakeSubsystem();
    lightSubsystem = new LightSubsystem();
    rumbleSubsystemDriver = new RumbleSubsystem(DRIVER_JOYSTICK_PORT);
    rumbleSubsystemOperator = new RumbleSubsystem(OPERATOR_JOYSTICK_PORT);
    liftSubsystem = new LiftSubsystem();
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

    //Driver Controller
    JoystickButton spin4Button = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_A);
    spin4Button.whenPressed (new SpinControlPanel4TimesCommand());

    JoystickButton stopForColor = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_B);
    stopForColor.whenPressed (new SpinControlPanelUntilColor());
   
    JoystickButton rumbButton = new JoystickButton(driverJoystick, XBoxConstants.BUTTON_X);
    rumbButton.whenPressed(new RumbleCommand(rumbleSubsystemDriver));

    //Operator Controller
    JoystickButton shootButton = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_A);
    shootButton.toggleWhenPressed(new ShootingCommand(shooterSubsystem));

    JoystickButton intakeJoystickButton = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_B);
    intakeJoystickButton.whileHeld(new IntakeCommand(intakeSubsystem)); 

    JoystickButton liftRaiseButton = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_X);
    liftRaiseButton.whileHeld(new LiftRaiseCommand(liftSubsystem));

    JoystickButton liftLowerButton = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_Y);
    liftLowerButton.toggleWhenPressed(new LiftLowerCommand(liftSubsystem)); 
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
}
