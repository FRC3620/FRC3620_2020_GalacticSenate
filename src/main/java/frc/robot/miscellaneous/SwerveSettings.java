/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.miscellaneous;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Add your docs here.
 */
public class SwerveSettings {
    NetworkTableEntry leftFrontEntry;
    NetworkTableEntry rightFrontEntry;
    NetworkTableEntry leftBackEntry;
    NetworkTableEntry rightBackEntry;
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    public SwerveSettings(String name) {
        NetworkTable networkTable = NetworkTableInstance.getDefault().getTable(name);
        leftFrontEntry = networkTable.getEntry("leftFront");
        rightFrontEntry = networkTable.getEntry("rightFront");
        leftBackEntry = networkTable.getEntry("leftBack");
        rightBackEntry = networkTable.getEntry("rightBack");
    }

    public SwerveSettingsContainer get(SwerveSettingsContainer defaults) {
        return null;
    }

    public void set(SwerveSettingsContainer settings) {
        logger.info("SwerveSettings is saving {}", settings);
        leftFrontEntry.setDouble(settings.leftFront);
        rightFrontEntry.setDouble(settings.rightFront);
        leftBackEntry.setDouble(settings.leftBack);
        rightBackEntry.setDouble(settings.rightBack);
    }
}
