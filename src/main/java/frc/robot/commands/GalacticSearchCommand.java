package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.PixySubsystem;

public class GalacticSearchCommand extends SequentialCommandGroup {
    public GalacticSearchCommand(PixySubsystem pixySubsystem, DriveSubsystem driveSubsystem, IntakeSubsystem intakeSubsystem) {
        super(
            new AutoIntakeArmDownCommand(intakeSubsystem),
                new WaitCommand(1.0),
                new GalacticSearchPickerCommand(pixySubsystem, driveSubsystem, intakeSubsystem)
        );
    }
}