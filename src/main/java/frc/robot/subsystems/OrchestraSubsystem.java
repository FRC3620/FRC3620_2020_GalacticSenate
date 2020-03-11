package frc.robot.subsystems;

import com.ctre.phoenix.music.Orchestra;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

public class OrchestraSubsystem extends SubsystemBase {

  private Orchestra orchestra = RobotContainer.imperialOrchestra;

  public OrchestraSubsystem() {
    
  }

  @Override
  public void periodic() {
     
  }

  public void startMusic() {
    orchestra.play();
  }

  public void stopMusic() {
    orchestra.stop();
  }
}