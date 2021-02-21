
package frc.robot.commands;


import frc.robot.commands.AutoDriveCommand;
import frc.robot.subsystems.DriveSubsystem;

public class Slalom25Command extends SlalomCommandGroup {
    private static final double SPEED = 0.25;
    
    public  Slalom25Command(DriveSubsystem driveSubsystem) {
       // command for slalom 25 - Barrel Race Challenge
       super(driveSubsystem);
       
       addCommands(
                // new AutoDriveCommand(distance, strafeAngle, speed, heading, driveSubsystem)
               

               // new AutoSteerCommand(90, driveSubsystem),
               // new AutoSemiElipseCommand(6 * 4, 3, SPEED, driveSubsystem)
                
                new AutoSteerCommand(90, driveSubsystem), // angle wheels
                new AutoDriveCommand(7.5 * 12, 90, SPEED, 180, driveSubsystem, "alpha", this), // Straight
                
                new AutoDriveCommand(3 * 12, 0, SPEED, 180, driveSubsystem, "beta", this), //right
                
                new AutoDriveCommand(3 * 12, -90, SPEED, 180, driveSubsystem, "charlie", this), //straight
                
                new AutoDriveCommand(6 * 12, 180, SPEED, 180, driveSubsystem, "delta", this), //left
                //new barrel
                new AutoDriveCommand(8 * 12, 90, SPEED, 180, driveSubsystem, "ehco", this), //foward
               
                new AutoDriveCommand(5 * 12, 180, SPEED, 180, driveSubsystem, "foxtrot", this), //left
             
                new AutoDriveCommand(5 * 12, -90, SPEED, 180, driveSubsystem, "gamma", this), //back
                
                new AutoDriveCommand(10 * 12, 0, SPEED, 180, driveSubsystem,"hotel", this), //right
                //new barrel
                new AutoDriveCommand(10 * 12, 90, SPEED, 180, driveSubsystem, "india", this), //foward
               
                new AutoDriveCommand(5 * 12, 180, SPEED, 180, driveSubsystem, "juliet", this), //left
            
                new AutoDriveCommand(22 * 14, -90, SPEED, 180, driveSubsystem, "kilo", this) //return
            
                
        );
    }
}