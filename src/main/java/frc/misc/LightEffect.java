package frc.misc;

import edu.wpi.first.wpilibj.util.Color8Bit;

import frc.misc.ColorPattern;

/**
 * @author Nick Zimanski (SlippStream)
 * @version 11 January 2020
 */
public class LightEffect {
    public static final Color8Bit WHITE = new Color8Bit(255,255,255);
    public static final Color8Bit BLACK = new Color8Bit(0,0,0);

    public boolean override;
    public boolean returnsPrevious;
    public Color8Bit color;
    public ColorPattern.Pattern pattern;
    public int milliseconds;
    
    public int m_rainbowFirstPixelHue;

    public int m_shotTrailLength;
    public int m_shotIntervalLength;

    public int m_blinkFlashRate;

    /**
     * Creates a new lighting effect. Mainly for use in {@link frc.robot.subsystems.LightSubsystem LightSubsystem}, advanced users only.
     * @param color the Color8Bit code for the color to display
     * @param override Whether or not this effect should skip the queue
     * @param pattern The {@link ColorPattern} enum of the pattern displayed
     * @param milliseconds The amount of time, in milliseconds to run the effect for
     */
    public LightEffect(Color8Bit color, boolean override, ColorPattern.Pattern pattern, int milliseconds, boolean returnsPrevious) {
        this.color = color;
        this.override = override;
        this.pattern = pattern;
        this.milliseconds = milliseconds;
        this.returnsPrevious = returnsPrevious;
    }

    public Color8Bit getColor() {return this.color;}
    public void setColor(Color8Bit color) {this.color = color;}

    public int[] getRGB() {return new int[]{this.color.red, this.color.green, this.color.blue};}
    public void setRGB(int[] rgb) {}

    public int[] getHSV() {
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