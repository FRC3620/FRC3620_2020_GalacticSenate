package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class Figure23PathARedCommand extends SlalomCommandGroup {

    public Figure23PathARedCommand(DriveSubsystem driveSubsystem, IntakeSubsystem intakeSubsystem) {
        super(driveSubsystem);
        addCommands(
            new DeployIntakeCommand(intakeSubsystem)
            ,
            new WaitCommand(.2)
            ,
                  new AutoDriveCommand(1*12,70,90,180, driveSubsystem)
                  ,
                  new AutoDriveCommand(3*12,45,90,180, driveSubsystem)
                  ,
                  new AutoDriveCommand(2.5*12,90,90,180, driveSubsystem)
                  ,
                  new AutoDriveCommand(2.41*12,160,90,180, driveSubsystem)
                  ,
                  new AutoDriveCommand(4*12,230,90,180, driveSubsystem)
                  ,
                  new AutoDriveCommand(2.41*12,150,90,180, driveSubsystem)
                  ,
                  new AutoDriveCommand(13*12, 85,90,180, driveSubsystem)
                  ,
                  new AutoIntakeArmUpCommand (intakeSubsystem)
        );
    }

    
}
