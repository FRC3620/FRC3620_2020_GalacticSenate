/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.PixySubsystem;
import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.LoggingMaster;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PixyTestCommand extends CommandBase {
  Logger logger = EventLogging.getLogger(getClass(), EventLogging.Level.INFO);

  Gson gson = new Gson();
  Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
  DriveSubsystem driveSubsystem;
  private static final String DASHBOARD_GALACTICSEARCH_PATH = "galacticsearch.path";
  private static final String DASHBOARD_GALACTICSEARCH_JSON = "galacticsearch.json";
  private static final String DASHBOARD_GALACTICSEARCH_FILE = "galacticsearch.file";
  private static final String DASHBOARD_GALACTICSEARCH_COUNT = "galacticsearch.count";

  public PixyTestCommand(DriveSubsystem driveSubsystem) {
    super();
    this.driveSubsystem = driveSubsystem;
    SmartDashboard.putString (DASHBOARD_GALACTICSEARCH_PATH, GalacticSearchPath.NOT_RUN.toString());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    List<PixySubsystem.PixyBlockPlus> blocks = RobotContainer.pixySubsystem.getLastBlockPluses();

    GalacticSearchPath whichPathToTake = PixyPathFinder.findPath(blocks);

    String json = gson.toJson(blocks);
    SmartDashboard.putString(DASHBOARD_GALACTICSEARCH_JSON, json);
    SmartDashboard.putNumber(DASHBOARD_GALACTICSEARCH_COUNT, blocks.size());

    logger.info("we recognize this as {}", whichPathToTake);
    SmartDashboard.putString(DASHBOARD_GALACTICSEARCH_PATH, whichPathToTake.toString());

    File loggingDirectory = LoggingMaster.getLoggingDirectory();
    // String ts = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
    String ts = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());

    File jsonFile = new File(loggingDirectory, "pixy_" + ts + ".json");
    logger.info("logging Pixy2 JSON to {}", jsonFile.getAbsoluteFile().toString());
    SmartDashboard.putString(DASHBOARD_GALACTICSEARCH_FILE, jsonFile.getAbsoluteFile().toString());
    PrintWriter w = null;
    try {
      w = new PrintWriter(new FileWriter(jsonFile));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    Map<String, Object> mm = new LinkedHashMap<>();
    mm.put("blocks", blocks);
    mm.put("path", whichPathToTake.toString());
    mm.put("when", ts);
    w.println(gsonPretty.toJson(mm));
    w.close();

    Command whichCommand = null;
    switch (whichPathToTake) {
      case A_BLUE:
        whichCommand = new PixyTestPathABlueCommand(driveSubsystem);
        break;
      case A_RED:
        whichCommand = new PixyTestPathARedCommand(driveSubsystem);
        break;
      case B_BLUE:
        // need to add this
        //whichCommand = new PixyTestPathBBlueCommand(driveSubsystem);
        break;
      case B_RED:
        // need to add this
        //whichCommand = new PixyTestPathBRedCommand(driveSubsystem);
        break;
      default:
        break;
    }
    if (whichCommand != null) {
      whichCommand.schedule();
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }

}
