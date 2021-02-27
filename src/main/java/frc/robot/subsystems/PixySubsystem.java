/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.*;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2CCC;
import io.github.pseudoresonance.pixy2api.Pixy2CCC.Block;
import io.github.pseudoresonance.pixy2api.links.SPILink;

public class PixySubsystem extends SubsystemBase {

  private Pixy2 pixy;
  private Pixy2CCC ccc;

  private ArrayList<Block> lastBlocks = new ArrayList<>();

  // large Y are bottom of frame and closest
  static public Comparator<PixyBlockPlus> lowestCenterFirst = (b1, b2) -> - (b1.getY() - b2.getY());

  static public Comparator<PixyBlockPlus> lowestBottomFirst = (b1, b2) -> - (int) (b1.getBottom() - b2.getBottom());

  static public Comparator<PixyBlockPlus> byDecreasingSize = (b1, b2) -> - ((b1.getWidth() * b1.getHeight()) - (b2.getWidth() * b2.getHeight()));

  private static Map<Byte, String> pixyErrorTexts= new HashMap<>();
  static {
    pixyErrorTexts.put (Pixy2.PIXY_RESULT_OK, "OK");
    pixyErrorTexts.put (Pixy2.PIXY_RESULT_ERROR, "ERROR");
    pixyErrorTexts.put (Pixy2.PIXY_RESULT_BUSY, "BUSY");
    pixyErrorTexts.put (Pixy2.PIXY_RESULT_CHECKSUM_ERROR, "CHECKSUM_ERROR");
    pixyErrorTexts.put (Pixy2.PIXY_RESULT_TIMEOUT, "TIMEOUT");
    pixyErrorTexts.put (Pixy2.PIXY_RESULT_BUTTON_OVERRIDE, "BUTTON_OVERRIDE");
    pixyErrorTexts.put (Pixy2.PIXY_RESULT_PROG_CHANGING, "PROG_CHANGING");
  }

  public class PixyBlockPlus {
    Block block;
    Double aspectRatio = null;
    Integer area = null;
    Double bottom = null;

    Set<String> tags = new LinkedHashSet<>();

    PixyBlockPlus(Block block) {
      this.block = block;
      this.area = block.getWidth() * block.getHeight();
      this.aspectRatio = ((double) block.getWidth()) / block.getHeight();
      this.bottom = block.getY() + (block.getHeight() / 2.0);
    }

    public int getArea() {
      return area;
    }

    public double getAspectRatio() {
      return aspectRatio;
    }

    public int getSignature() {
      return block.getSignature();
    }

    public int getX() {
      return block.getX();
    }

    public int getY() {
      return block.getY();
    }

    public int getWidth() {
      return block.getWidth();
    }

    public int getHeight() {
      return block.getHeight();
    }

    public int getAngle() {
      return block.getAngle();
    }

    public int getIndex() {
      return block.getIndex();
    }

    public int getAge() {
      return block.getAge();
    }

    public double getBottom() {
      return bottom;
    }

    public Set<String> getTags() {
      return tags;
    }

    public void addTag(String tag) {
      this.tags.add(tag);
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("PixyBlockPlus{");
      sb.append("signature=").append(getSignature());
      if (tags.size() != 0) sb.append(", tag=").append(tags.toString());
      sb.append(", index=").append(getIndex());
      sb.append(", area=").append(area);
      sb.append(", x=").append(getX());
      sb.append(", y=").append(getY());
      sb.append(", bottom=").append(bottom);
      sb.append(", aspectRatio=").append(aspectRatio);
      sb.append(", width=").append(getWidth());
      sb.append(", height=").append(getHeight());
      sb.append(", angle=").append(getAngle());
      sb.append(", age=").append(getAge());
      sb.append('}');
      return sb.toString();
    }
  }

  public static String getPixyErrorText (int code) {
    if (code > 0) code = 0;
    String rv = pixyErrorTexts.get((byte) code);
    if (rv == null) {
      rv = Integer.toString(code);
    }
    return rv;
  }

  public PixySubsystem() {
    super();

    pixy = Pixy2.createInstance(new SPILink()); // Creates a new Pixy2 camera using SPILink
    ccc = pixy.getCCC();
    pixy.init(); // Initializes the camera and prepares to send/receive data
    pixy.setLamp((byte) 0, (byte) 0); // Turns the LEDs off
    // pixy.setLED(255, 255, 255); // Sets the RGB LED to full white

    // get ready to run the update periodically, waiting for pixy data
    Notifier follower = new Notifier(() -> {
      update(true);
    });
    // and do so every 0.500s
    follower.startPeriodic(0.500);
  }

  public int update() {
    return update(false);
  }

  public int update(boolean wait) {
    // Gets the number of "blocks", identified targets, that match signature 1 on
    // the Pixy2,
    // wait for new data if asked to,
    // and limits the number of returned blocks for a slight increase in efficiency
    int pixyReturnCode = ccc.getBlocks(wait, Pixy2CCC.CCC_SIG1 | Pixy2CCC.CCC_SIG2, 8);
    SmartDashboard.putNumber("pixy.rc", pixyReturnCode);
    SmartDashboard.putString("pixy.status", getPixyErrorText(pixyReturnCode));
    if (pixyReturnCode >= 0) {
      ArrayList<Block> blocks = ccc.getBlockCache(); // Gets a list of all blocks found by the Pixy2
      SmartDashboard.putNumber("pixy.count", blocks.size());
      if (blocks.size() > 0) {
        Optional<Block> oMostImportantBlock = blocks.stream()
                // sort largest first
                .sorted ((b1, b2) -> - (b1.getWidth()*b1.getHeight() - b2.getWidth()*b2.getHeight()))
                // then put smallest sigs at front
                .sorted ((b1, b2) -> (b1.getSignature() - b2.getSignature()))
                // and keep the first one
                .findFirst();
        if (oMostImportantBlock.isPresent()) {
          Block mostImportantBlock = oMostImportantBlock.get();
          SmartDashboard.putNumber("pixy.x", mostImportantBlock.getX());
          SmartDashboard.putNumber("pixy.y", mostImportantBlock.getY());
          SmartDashboard.putNumber("pixy.height", mostImportantBlock.getHeight());
          SmartDashboard.putNumber("pixy.width", mostImportantBlock.getWidth());
          SmartDashboard.putNumber("pixy.signature", mostImportantBlock.getSignature());
          SmartDashboard.putNumber("pixy.index", mostImportantBlock.getIndex());
          SmartDashboard.putNumber("pixy.age", mostImportantBlock.getAge());
        }
      }
      lastBlocks = blocks;
    }
    return pixyReturnCode;
  }

  public List<PixyBlockPlus> getLastBlockPluses() {
    List<PixyBlockPlus> rv = new ArrayList<>();
    for (Block b : lastBlocks) {
      rv.add(new PixyBlockPlus(b));
    }
    return rv;
  }
}
