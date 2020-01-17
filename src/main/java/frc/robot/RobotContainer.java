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
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);

  public static WPI_TalonFX shooterSubsystemFalcon1 = new WPI_TalonFX(1);
  public static WPI_TalonFX shooterSubsystemFalcon2 = new WPI_TalonFX(2);

  //subsystems
  public static ShooterSubsystem shooterSubsystem = new ShooterSubsystem();

  private XboxController driverJoystick;
  private XboxController operatorJoystick;
  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    setupMotors();
    // Configure the button bindings
    driverJoystick = new XboxController(0);
    operatorJoystick = new XboxController(1);
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

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    JoystickButton shootButton = new JoystickButton(operatorJoystick, XBoxConstants.BUTTON_A);

    shootButton.toggleWhenPressed(new ShootingCommand(shooterSubsystem));
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }
}
