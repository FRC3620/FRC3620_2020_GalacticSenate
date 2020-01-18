/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.XboxController;
import frc.misc.XBoxConstants;
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
  // hardware here...
  public static SpeedController m_armMotor;
  public static WPI_TalonFX shooterSubsystemFalcon1;
  public static WPI_TalonFX shooterSubsystemFalcon2;
  public static WPI_TalonFX intakeSubsystemFalcon1;

  // subsystems here...
  public static ArmSubsystem armSubsystem;
  public static ShooterSubsystem shooterSubsystem;
  public static LightSubsystem lightSubsystem;
  public static RumbleSubsystem rumbleSubsystemDriver;
  public static RumbleSubsystem rumbleSubsystemOperator;
  public static IntakeSubsystem intakeSubsystem;

  // joysticks here....
  public static Joystick driverJoystick;
  public static Joystick operatorJoystick;

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    makeHardware();
    setupMotors();
    makeSubsystems();
    // Configure the button bindings
    configureButtonBindings();
  }

  void setupMotors() {
    int kTimeoutMs = 0;
    shooterSubsystemFalcon1.setInverted(InvertType.InvertMotorOutput);
    // undocumented current measurement status frame
    shooterSubsystemFalcon1.setStatusFramePeriod(0x1240, 1, kTimeoutMs);
    shooterSubsystemFalcon1.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 1, kTimeoutMs);
    shooterSubsystemFalcon1.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 1, kTimeoutMs);
    shooterSubsystemFalcon1.setStatusFramePeriod(StatusFrameEnhanced.Status_4_AinTempVbat, 1, kTimeoutMs);
    //shooterSubsystemFalcon1.configSelectedFeedbackSensor(FeedbackDevice.Tachometer, 0, kTimeoutMs);
  }

  void makeHardware() {
    m_armMotor = new Victor(8);
    shooterSubsystemFalcon1 = new WPI_TalonFX(1);
    shooterSubsystemFalcon2 = new WPI_TalonFX(2);
    intakeSubsystemFalcon1 = new WPI_TalonFX(3);
  }

  void makeSubsystems() {
    armSubsystem = new ArmSubsystem();
    shooterSubsystem = new ShooterSubsystem();
    intakeSubsystem = new IntakeSubsystem();
    lightSubsystem = new LightSubsystem();
    rumbleSubsystemDriver = new RumbleSubsystem(Constants.DRIVER_JOYSTICK_PORT);
    rumbleSubsystemOperator = new RumbleSubsystem(Constants.OPERATOR_JOYSTICK_PORT);
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    Joystick driverJoystick = new Joystick(Constants.DRIVER_JOYSTICK_PORT);
    Joystick operatorJoystick = new Joystick(Constants.OPERATOR_JOYSTICK_PORT);

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
