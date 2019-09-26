package net.k773.smm;

import java.util.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;

public class Change
{
    static Random rand;
    public double value;
    public double result;
    public int state;
    public double ox;
    public double oy;
    public long end;
    
    public Change(final double result, final double value) {
        this.result = result;
        this.value = value;
        this.state = ((value < 0.0) ? 1 : 0);
        this.ox = Change.rand.nextInt(100) - 5.0;
        this.oy = 100.0;
        this.end = ((this.value > -10.0 && this.value < 0.0) ? ((long)(System.currentTimeMillis() + 100.0 * -this.value)) : (System.currentTimeMillis() + 1000L));
    }
    
    public double draw(final double x, final double y) {
        final long time = System.currentTimeMillis();
        double cpos = 0.0;
        double value = this.value;
        switch (this.state) {
            case 0: {
                cpos = (this.end - time) / 1000.0;
                if (cpos < 0.0 || cpos > 1.0) {
                    this.state = ((this.value < 0.0) ? 2 : 1);
                    this.end = ((this.value < 10.0 && this.value > 0.0) ? ((long)(System.currentTimeMillis() + 100.0 * this.value)) : (time + 1000L));
                    return 0.0;
                }
                break;
            }
            case 1: {
                cpos = (this.end - time) / 1000.0;
                value = this.value * ((this.value < 0.0) ? (1.0 - cpos) : cpos);
                if (cpos < 0.0 || cpos > 1.0) {
                    this.state = ((this.value < 0.0) ? 0 : 2);
                    this.end = time + 1000L;
                }
                cpos = 0.0;
                break;
            }
            case 2: {
                return 0.0;
            }
        }
        double cx = 0.0;
        double cy = 0.0;
        if (this.value <= 0.0 && this.state != 1) {
            cx = this.ox - this.ox * cpos;
            cy = this.oy - this.oy * cpos;
        }
        else {
            cx = this.ox * cpos;
            cy = this.oy * cpos;
        }
        GL11.glTranslated(x + cx, y + cy, 0.0);
        Minecraft.getMinecraft().fontRenderer.drawString(((this.value > 0.0) ? "§d" : "§c") + (int)value, 0, 0, Integer.MAX_VALUE);
        GL11.glTranslated(-(x + cx), -(y + cy), 0.0);
        return (this.value < 0.0) ? value : (this.value - value);
    }
    
    public double getValue() {
        return this.value;
    }
    
    static {
        Change.rand = new Random();
    }
}
