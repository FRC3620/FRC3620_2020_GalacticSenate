/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2CCC;
import io.github.pseudoresonance.pixy2api.Pixy2CCC.Block;
import io.github.pseudoresonance.pixy2api.links.SPILink;

public class PixySubsystem extends SubsystemBase {

  private Pixy2 pixy;

  private BlockDescription lastBlock;

  /**
   * Creates a new PixySubsystem.
   */
  public PixySubsystem() {
    super();

    lastBlock = new BlockDescription();

    pixy = Pixy2.createInstance(new SPILink()); // Creates a new Pixy2 camera using SPILink
    pixy.init(); // Initializes the camera and prepares to send/receive data
    pixy.setLamp((byte) 1, (byte) 1); // Turns the LEDs on
    pixy.setLED(255, 255, 255); // Sets the RGB LED to full white
  }

  public static class BlockDescription implements Cloneable {
    double x, y, height, width;
    int blockCount; // if 0, we don't see anything!
    int age, signature;

    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }

  @Override
  public void periodic() {
    // Gets the number of "blocks", identified targets, that match signature 1 on
    // the Pixy2,
    // does not wait for new data if none is available,
    // and limits the number of returned blocks to 25, for a slight increase in
    // efficiency
    lastBlock.blockCount = pixy.getCCC().getBlocks(false, Pixy2CCC.CCC_SIG1, 5);
    Block largestBlock = null;
    if (lastBlock.blockCount > 0) {
      ArrayList<Block> blocks = pixy.getCCC().getBlockCache(); // Gets a list of all blocks found by the Pixy2
      for (Block block : blocks) { // Loops through all blocks and finds the widest one
        if (largestBlock == null) {
          largestBlock = block;
        } else if (block.getWidth() > largestBlock.getWidth()) {
          largestBlock = block;
        }
      }
      lastBlock.x = largestBlock.getX();
      lastBlock.y = largestBlock.getY();
      lastBlock.width = largestBlock.getWidth();
      lastBlock.height = largestBlock.getHeight();
      lastBlock.age = largestBlock.getAge();
      lastBlock.signature = largestBlock.getSignature();
    }
    SmartDashboard.putNumber("pixy.count", lastBlock.blockCount);
    SmartDashboard.putNumber("pixy.x", lastBlock.x);
    SmartDashboard.putNumber("pixy.y", lastBlock.y);
    SmartDashboard.putNumber("pixy.width", lastBlock.width);
    SmartDashboard.putNumber("pixy.signature", lastBlock.signature);
    SmartDashboard.putNumber("pixy.age", lastBlock.age);
  }

  public BlockDescription getBlockDescription() {
    try {
      return (BlockDescription) lastBlock.clone();
    } catch (CloneNotSupportedException ex) {
      throw new RuntimeException ("could not clone", ex);
    }
  }

  public boolean isThereAPowerCell() {
    if (lastBlock.blockCount == 0) return false;
    if (lastBlock.signature != 1) return false;
    // small Y are top of frame
    if (lastBlock.y < 120) return false;
    return true;
  }
}
