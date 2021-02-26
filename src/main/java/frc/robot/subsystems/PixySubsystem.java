/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.Comparator;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2CCC;
import io.github.pseudoresonance.pixy2api.Pixy2CCC.Block;
import io.github.pseudoresonance.pixy2api.links.SPILink;

public class PixySubsystem extends SubsystemBase {

  private Pixy2 pixy;

  private ArrayList<Block> lastBlocks;

  static private ArrayList<Block> noBlocks = new ArrayList<>();

  static public Comparator<Block> byDecreasingSize = (b1, b2) -> - ((b1.getWidth() * b1.getHeight()) - (b2.getWidth() * b2.getHeight()));

  public PixySubsystem() {
    super();

    pixy = Pixy2.createInstance(new SPILink()); // Creates a new Pixy2 camera using SPILink
    pixy.init(); // Initializes the camera and prepares to send/receive data
    pixy.setLamp((byte) 1, (byte) 1); // Turns the LEDs on
    pixy.setLED(255, 255, 255); // Sets the RGB LED to full white
  }

  @Override
  public void periodic() {
    // Gets the number of "blocks", identified targets, that match signature 1 on
    // the Pixy2,
    // does not wait for new data if none is available,
    // and limits the number of returned blocks for a slight increase in efficiency
    int blockCount = pixy.getCCC().getBlocks(false, Pixy2CCC.CCC_SIG1, 25);
    if (blockCount > 0) {
      ArrayList<Block> blocks = pixy.getCCC().getBlockCache(); // Gets a list of all blocks found by the Pixy2
      blocks.sort(byDecreasingSize);
      Block largestBlock = blocks.get(0);
      SmartDashboard.putNumber("pixy.x", largestBlock.getX());
      SmartDashboard.putNumber("pixy.y", largestBlock.getY());
      SmartDashboard.putNumber("pixy.width", largestBlock.getWidth());
      SmartDashboard.putNumber("pixy.signature", largestBlock.getSignature());
      SmartDashboard.putNumber("pixy.age", largestBlock.getAge());

      lastBlocks = blocks;
    } else {
      lastBlocks = noBlocks;
    }
    SmartDashboard.putNumber("pixy.count", blockCount);
  }

  public boolean isThereAPowerCell() {
    if (lastBlocks.size() == 0) return false;
    Block lastBlock = lastBlocks.get(0);
    if (lastBlock.getSignature() != 1) return false;
    // small Y are top of frame
    if (lastBlock.getY() < 120) return false;
    return true;
  }
}
