package nebula.client.util.render;

import nebula.client.api.gui.shader.Shader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

import java.awt.Color;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Gavin
 * @since 08/14/23
 */
public class RenderUtils
{
  private static final Minecraft mc = Minecraft.getMinecraft();

  public static Shader roundShader, espShader;

  /**
   * The cached ScaledResolution object from {@link net.minecraft.client.gui.GuiIngame}
   */
  public static ScaledResolution resolution;

  public static void loadShaders()
  {
    roundShader = new Shader(
        "/assets/minecraft/nebula/shader/vertex.vsh",
        "/assets/minecraft/nebula/shader/roundedrect.frag",
        (shader) -> {
          shader.createUniform("rectSize");
          shader.createUniform("color");
          shader.createUniform("radius");
          shader.createUniform("edgeSoftness");
        });
    espShader = new Shader(
        "/assets/minecraft/nebula/shader/vertex.vsh",
        "/assets/minecraft/nebula/shader/esp.fsh",
        (s) ->
        {
          s.createUniform("texture");
          s.createUniform("texelSize");
          s.createUniform("color");
          s.createUniform("radius");
          s.createUniform("opacity");
        });
  }

  public static void rect(double x, double y, double width, double height, int color)
  {
    glPushMatrix();

    glDisable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);
    OpenGlHelper.glBlendFunc(770, 771, 0, 1);

    color(color);

    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawingQuads();
    tessellator.addVertex(x, y, 0.0);
    tessellator.addVertex(x, y + height, 0.0);
    tessellator.addVertex(x + width, y + height, 0.0);
    tessellator.addVertex(x + width, y, 0.0);
    tessellator.draw();

    glDisable(GL_BLEND);
    glEnable(GL_TEXTURE_2D);
    glPopMatrix();
  }

  public static void gradientRect(double x, double y, double width, double height, int topColor, int bottomColor)
  {
    glPushMatrix();

    glDisable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);
    OpenGlHelper.glBlendFunc(770, 771, 0, 1);

    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawingQuads();

    color(topColor);
    tessellator.addVertex(x, y, 0.0);
    tessellator.addVertex(x, y + height, 0.0);
    color(bottomColor);
    tessellator.addVertex(x + width, y + height, 0.0);
    tessellator.addVertex(x + width, y, 0.0);
    tessellator.draw();

    glDisable(GL_BLEND);
    glEnable(GL_TEXTURE_2D);
    glPopMatrix();
  }

  public static void color(int hex)
  {
    float alpha = (float) (hex >> 24 & 0xFF) / 255.0f;
    float red = (float) (hex >> 16 & 0xFF) / 255.0f;
    float green = (float) (hex >> 8 & 0xFF) / 255.0f;
    float blue = (float) (hex & 0xFF) / 255.0f;

    glColor4f(red, green, blue, alpha);
  }

  /**
   * Renders a texture to the screen
   *
   * @param textureId the texture id
   * @param x         the x render coordinate
   * @param y         the y render coordinate
   * @param w         the width to draw the texture
   * @param h         the height to draw the texture
   * @param color     the color/tint to give to the rendered texture
   */
  public static void renderTexture(int textureId, double x, double y, int w, int h, Color color)
  {
    glPushMatrix();
    glEnable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

    glBindTexture(GL_TEXTURE_2D, textureId);

    // Minecraft#draw
    float f = 0.00390625f;
    glColor4f(color.getRed() * f, color.getGreen() * f, color.getBlue() * f, color.getAlpha() * f);
    Gui.func_146110_a((int) x, (int) y, 0, 0, w, h, w, h);

    glBindTexture(GL_TEXTURE_2D, 0);

    glDisable(GL_BLEND);
    glPopMatrix();
  }

  /**
   * Renders a texture to the screen
   *
   * @param location the resource location object
   * @param x        the x render coordinate
   * @param y        the y render coordinate
   * @param w        the width to draw the texture
   * @param h        the height to draw the texture
   * @param color    the color/tint to give to the rendered texture
   */
  public static void renderTexture(ResourceLocation location, double x, double y, int w, int h, Color color)
  {
    glPushMatrix();
    glEnable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);
    OpenGlHelper.glBlendFunc(770, 771, 1, 0);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

    Minecraft.getMinecraft().getTextureManager().bindTexture(location);

    // Minecraft#draw
    float f = 0.00390625f;
    glColor4f(color.getRed() * f,
        color.getGreen() * f,
        color.getBlue() * f,
        color.getAlpha() * f);
    Gui.func_146110_a((int) x, (int) y, 0, 0, w, h, w, h);

    glBindTexture(GL_TEXTURE_2D, 0);

    glDisable(GL_BLEND);
    glPopMatrix();
  }

  public static void circle(double x, double y, double radius, int color) {
    glEnable(GL_BLEND);
    OpenGlHelper.glBlendFunc(770, 771, 0, 1);

    float[] argb = ColorUtils.getColor(color);
    glColor4f(argb[1], argb[2], argb[3], argb[0]);

    glLineWidth(1.5f);
    glBegin(GL_LINE_LOOP);
    {

      for (double angle = 0.0; angle <= 180.0; angle += 0.1) {
        double posX = x + Math.cos(angle) * radius;
        double posY = y - Math.sin(angle) * radius;

        glVertex2d(posX, posY);
      }
    }
    glEnd();

    glDisable(GL_BLEND);
  }

  public static void roundRect(double x, double y, double w, double h, double radius, int color) {
    glEnable(GL_TEXTURE_2D);
    glEnable(GL_BLEND);
    OpenGlHelper.glBlendFunc(770, 771, 0, 1);

    float[] argb = ColorUtils.getColor(color);
    roundShader.use();
    {
      int scaleFactor = mc.gameSettings.guiScale;
      if (resolution != null) {
        scaleFactor = resolution.getScaleFactor();
      }

      roundShader.set("rectSize",
          (float) (w * scaleFactor),
          (float) (h * scaleFactor));
      roundShader.set("color", argb[1], argb[2], argb[3], argb[0]);
      roundShader.set("radius", (float) (radius * scaleFactor));
      roundShader.set("edgeSoftness", 1.0f);
    }

    glBegin(GL_QUADS);
    {
      glTexCoord2d(0, 0);
      glVertex2d(x, y);
      glTexCoord2d(0, 1);
      glVertex2d(x, y + h);
      glTexCoord2d(1, 1);
      glVertex2d(x + w, y + h);
      glTexCoord2d(1, 0);
      glVertex2d(x + w, y);
    }
    glEnd();

    roundShader.stop();

    glDisable(GL_BLEND);
  }

  public static void scissor(double x, double y, double width, double height) {
    double scale = resolution.getScaleFactor();
    glEnable(GL_SCISSOR_TEST);
    glScissor((int) (x * scale),
        (int) (((resolution.getScaledHeight_double() - y) * scale) - (height * scale)),
        (int) (width * scale),
        (int) (height * scale));
  }

  public static void endScissor() {
    glDisable(GL_SCISSOR_TEST);
  }

  public static void filledAabb(AxisAlignedBB box)
  {
    Tessellator tessellator = Tessellator.instance;

    tessellator.startDrawing(GL_QUADS);
    tessellator.addVertex(box.minX, box.minY, box.minZ);
    tessellator.addVertex(box.maxX, box.minY, box.minZ);
    tessellator.addVertex(box.maxX, box.minY, box.maxZ);
    tessellator.addVertex(box.minX, box.minY, box.maxZ);
    tessellator.draw();

    // sides
    tessellator.startDrawing(GL_QUADS);
    tessellator.addVertex(box.minX, box.minY, box.minZ);
    tessellator.addVertex(box.minX, box.minY, box.maxZ);
    tessellator.addVertex(box.minX, box.maxY, box.maxZ);
    tessellator.addVertex(box.minX, box.maxY, box.minZ);
    tessellator.draw();

    tessellator.startDrawing(GL_QUADS);
    tessellator.addVertex(box.maxX, box.minY, box.maxZ);
    tessellator.addVertex(box.maxX, box.minY, box.minZ);
    tessellator.addVertex(box.maxX, box.maxY, box.minZ);
    tessellator.addVertex(box.maxX, box.maxY, box.maxZ);
    tessellator.draw();

    tessellator.startDrawing(GL_QUADS);
    tessellator.addVertex(box.maxX, box.minY, box.minZ);
    tessellator.addVertex(box.minX, box.minY, box.minZ);
    tessellator.addVertex(box.minX, box.maxY, box.minZ);
    tessellator.addVertex(box.maxX, box.maxY, box.minZ);
    tessellator.draw();

    tessellator.startDrawing(GL_QUADS);
    tessellator.addVertex(box.minX, box.minY, box.maxZ);
    tessellator.addVertex(box.maxX, box.minY, box.maxZ);
    tessellator.addVertex(box.maxX, box.maxY, box.maxZ);
    tessellator.addVertex(box.minX, box.maxY, box.maxZ);
    tessellator.draw();

    // top
    tessellator.startDrawing(GL_QUADS);
    tessellator.addVertex(box.minX, box.maxY, box.maxZ);
    tessellator.addVertex(box.maxX, box.maxY, box.maxZ);
    tessellator.addVertex(box.maxX, box.maxY, box.minZ);
    tessellator.addVertex(box.minX, box.maxY, box.minZ);
    tessellator.draw();
  }

  public static void outlinedAabb(AxisAlignedBB box)
  {
    Tessellator tessellator = Tessellator.instance;

    tessellator.startDrawing(GL_LINE_STRIP);
    tessellator.addVertex(box.minX, box.minY, box.minZ);
    tessellator.addVertex(box.maxX, box.minY, box.minZ);
    tessellator.addVertex(box.maxX, box.minY, box.maxZ);
    tessellator.addVertex(box.minX, box.minY, box.maxZ);
    tessellator.draw();

    // sides
    tessellator.startDrawing(GL_LINE_STRIP);
    tessellator.addVertex(box.minX, box.minY, box.minZ);
    tessellator.addVertex(box.minX, box.minY, box.maxZ);
    tessellator.addVertex(box.minX, box.maxY, box.maxZ);
    tessellator.addVertex(box.minX, box.maxY, box.minZ);
    tessellator.draw();

    tessellator.startDrawing(GL_LINE_STRIP);
    tessellator.addVertex(box.maxX, box.minY, box.maxZ);
    tessellator.addVertex(box.maxX, box.minY, box.minZ);
    tessellator.addVertex(box.maxX, box.maxY, box.minZ);
    tessellator.addVertex(box.maxX, box.maxY, box.maxZ);
    tessellator.draw();

    tessellator.startDrawing(GL_LINE_STRIP);
    tessellator.addVertex(box.maxX, box.minY, box.minZ);
    tessellator.addVertex(box.minX, box.minY, box.minZ);
    tessellator.addVertex(box.minX, box.maxY, box.minZ);
    tessellator.addVertex(box.maxX, box.maxY, box.minZ);
    tessellator.draw();

    tessellator.startDrawing(GL_LINE_STRIP);
    tessellator.addVertex(box.minX, box.minY, box.maxZ);
    tessellator.addVertex(box.maxX, box.minY, box.maxZ);
    tessellator.addVertex(box.maxX, box.maxY, box.maxZ);
    tessellator.addVertex(box.minX, box.maxY, box.maxZ);
    tessellator.draw();

    // top
    tessellator.startDrawing(GL_LINE_STRIP);
    tessellator.addVertex(box.minX, box.maxY, box.maxZ);
    tessellator.addVertex(box.maxX, box.maxY, box.maxZ);
    tessellator.addVertex(box.maxX, box.maxY, box.minZ);
    tessellator.addVertex(box.minX, box.maxY, box.minZ);
    tessellator.draw();
  }
}
