package org.usfirst.frc3620.misc;

/**
 * Enum variables to control color pattern in LightSubsystem
 * @author Nick Zimanski (SlippStream)
 * @version 15 January 2020
 * @see {@link frc.robot.subsystems.LightSubsystem LightSubsystem}
 */
public class ColorPattern {
    public enum Pattern {
        SOLID,
        RAINBOW,
        BLINK,
        TWINKLE,
        SHOT,
        KEY
    }

    public enum Preset {
        INIT,
        DISABLED,
        TEST,
        TELEOP,
        AUTO,
        LOW_VOLTAGE
    }
}