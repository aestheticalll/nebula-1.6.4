package wtf.nebula.util.render;

import java.awt.*;

import static org.lwjgl.opengl.GL11.glColor4f;

public class ColorUtil {
    public static float[] getColor(int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        return new float[] { red, green, blue, alpha };
    }

    public static void setColor(int color) {
        float[] rgba = getColor(color);
        glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    public static int addAlpha(int color, int alpha) {
        // clear alpha value and add our alpha value
        return color & 0x00ffffff | alpha << 24;
    }

    public static int toHSB(float hue, float saturation, float brightness) {
        return Color.getHSBColor(hue, saturation, brightness).getRGB();
    }

    public static int rainbow(double delay, float saturation, float brightness) {
        double state = Math.ceil((System.currentTimeMillis() + delay) / 20.0) % 360;
        return ColorUtil.toHSB((float) (state / 360.f), saturation, brightness);
    }
}
