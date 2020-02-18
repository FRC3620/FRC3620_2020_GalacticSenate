package org.usfirst.frc3620.misc;

import edu.wpi.first.wpilibj.util.Color8Bit;

/**
 * @author Nick Zimanski (SlippStream)
 * @version 11 February 2020
 */
public class LightEffect {
    public static enum Color {
        WHITE(new Color8Bit(255,255,255)),
        BLACK(new Color8Bit(0,0,0)),
        RED(new Color8Bit(255,0,0)),
        BLUE(new Color8Bit(0,0,255)),
        GREEN(new Color8Bit(0,255,0)),
        GRAY(new Color8Bit(128,128,128)),
        PURPLE(new Color8Bit(132,56,214)),
        MAIZE(new Color8Bit(255,210,0));

        public Color8Bit value;
        private Color(Color8Bit color) {this.value = color;}
    }

    private boolean override;
    private boolean returnsPrevious;
    private Color8Bit color;
    private ColorPattern.Pattern pattern;
    private int milliseconds;
    private String password = null;
    
    public int m_rainbowFirstPixelHue;

    public int m_shotTrailLength;
    public int m_shotIntervalLength;
    public Color8Bit[] m_shotColors;

    public int m_blinkFlashRate;

    public boolean m_presetEffect;

    /**
     * Creates a new lighting effect. Mainly for use in {@link frc.robot.subsystems.LightSubsystem LightSubsystem}, advanced users only.
     * @param color the Color8Bit code for the color to display
     * @param override Whether or not this effect should skip the queue
     * @param pattern The {@link ColorPattern} enum of the pattern displayed
     * @param milliseconds The amount of time, in milliseconds to run the effect for
     * @param returnsPrevious Whether or not to requeue the interrupted effect
     * @param password the lock (or key) to attach to this effect
     */
    public LightEffect(Color8Bit color, boolean override, ColorPattern.Pattern pattern, int milliseconds, boolean returnsPrevious, String password) {
        this.color = color;
        this.override = override;
        this.pattern = pattern;
        this.milliseconds = milliseconds;
        this.returnsPrevious = returnsPrevious;
        this.password = password;
    }

    public String getPassword() {return this.password;}
    public void setPassword(String password) {this.password = password;}

    public int getMilliseconds() {return this.milliseconds;}
    public void setMilliseconds(int milliseconds) {this.milliseconds = milliseconds;}

    public boolean getReturnsPrevious() {return this.returnsPrevious;}
    public void setReturnsPrevious(boolean returnsPrevious) {this.returnsPrevious = returnsPrevious;}

    public Color8Bit getColor() {return this.color;}
    public void setColor(Color8Bit color) {this.color = color;}

    public int[] getRGB() {return new int[]{this.color.red, this.color.green, this.color.blue};}
    public void setRGB(int[] rgb) {}

    public ColorPattern.Pattern getPattern() {return this.pattern;}
    public void setPattern(ColorPattern.Pattern pattern) {this.pattern = pattern;}

    public boolean getOverride() {return this.override;}
    public void setOverride(boolean override) {this.override = override;}

    public int[] getHSV() {return getHSV(this.color);}

    public static int[] getHSV(Color8Bit color) {
        double r = color.red / 255.0;
        double g = color.green / 255.0;
        double b = color.blue / 255.0;

        double cmax = Math.max(r, Math.max(g, b)); // maximum of r, g, b 
        double cmin = Math.min(r, Math.min(g, b)); // minimum of r, g, b 
        double diff = cmax - cmin; // diff of cmax and cmin. 
        double h = -1, s = -1; 
            
        if (cmax == cmin) h = 0; 

        else if (cmax == r) h = (60 * ((g - b) / diff) + 360) % 360; 
        else if (cmax == g) h = (60 * ((b - r) / diff) + 120) % 360; 
        else if (cmax == b) h = (60 * ((r - g) / diff) + 240) % 360; 

        if (cmax == 0) s = 0; 
        else
            s = (diff / cmax) * 255; 

        // compute v 
        double v = cmax * 255; 

        return new int[]{(int)h/2, (int)s, (int)v};
    }
}