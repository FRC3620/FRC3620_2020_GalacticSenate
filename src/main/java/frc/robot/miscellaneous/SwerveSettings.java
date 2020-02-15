/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.miscellaneous;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableType;

/**
 * Add your docs here.
 */
public class SwerveSettings {
    NetworkTable networkTable;
    NetworkTableEntry leftFrontEntry;
    NetworkTableEntry rightFrontEntry;
    NetworkTableEntry leftBackEntry;
    NetworkTableEntry rightBackEntry;
    Logger logger = EventLogging.getLogger(getClass(), Level.INFO);

    public SwerveSettings(String name) {
        networkTable = NetworkTableInstance.getDefault().getTable(name);
        leftFrontEntry = networkTable.getEntry("leftFront");
        rightFrontEntry = networkTable.getEntry("rightFront");
        leftBackEntry = networkTable.getEntry("leftBack");
        rightBackEntry = networkTable.getEntry("rightBack");
    }

    Set<String> defaulted = new TreeSet<>();
    double getOneSetting (NetworkTableEntry nte, double defaultValue) {
        if (!nte.exists()) {
            logger.info ("Network table entry {} not in network table, using default {}",
              nte.getName(), defaultValue);
            defaulted.add(nte.getName());
        }
        return nte.getDouble(defaultValue);
    }

    public SwerveSettingsContainer get(SwerveSettingsContainer defaultValues) {
        defaulted.clear();
        SwerveSettingsContainer returnValue = new SwerveSettingsContainer();
        returnValue.leftFront = getOneSetting(leftFrontEntry, defaultValues.leftFront);
        returnValue.rightFront = getOneSetting(rightFrontEntry, defaultValues.rightFront);
        returnValue.leftBack = getOneSetting(leftBackEntry, defaultValues.leftBack);
        returnValue.rightBack = getOneSetting(rightBackEntry, defaultValues.rightBack);
        if (defaulted.size() == 4) {
            logger.warn ("all of the {} settings were missing", networkTable.getPath());
        } else if (defaulted.size() > 0) {
            logger.warn ("some of the {} settings were missing: {}", networkTable.getPath(), defaulted.toString());
        }
        logger.info ("read {}: {}", networkTable.getPath(), returnValue.toString());
        return returnValue;
    }

    public void set(SwerveSettingsContainer settings) {
        logger.info("SwerveSettings {} is saving {}", networkTable.getPath(), settings);
        leftFrontEntry.setDouble(settings.leftFront);
        rightFrontEntry.setDouble(settings.rightFront);
        leftBackEntry.setDouble(settings.leftBack);
        rightBackEntry.setDouble(settings.rightBack);
        leftFrontEntry.setPersistent();
        rightFrontEntry.setPersistent();
        leftBackEntry.setPersistent();
        rightBackEntry.setPersistent();

    }
}
