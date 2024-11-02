package nebula.client.api.gui.font;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

import static java.awt.Font.TRUETYPE_FONT;

/**
 * @author aesthetical
 * @since 03/05/23
 */
public class Fonts
{
  public static AWTFontRenderer icons;
  public static AWTFontRenderer axiforma;

  /**
   * Loads a font from an input stream, creates the font, and adds it to the Graph's Environment
   *
   * @param inputStream the input stream
   * @return the new Font object
   * @throws IOException         if the input stream could not be created/closed
   * @throws FontFormatException if the input stream does not contain the right font information
   */
  private static Font loadFont(InputStream inputStream) throws IOException, FontFormatException
  {
    GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
    Font font = Font.createFont(TRUETYPE_FONT, inputStream);
    graphics.registerFont(font);
    inputStream.close();
    return font;
  }

  /**
   * Creates a new AWTFontRenderer
   *
   * @param fontName the name of the font file
   * @param size     the size to create the font with
   * @return the AWTFontRenderer object or null
   */
  private static AWTFontRenderer create(String fontName, int size)
  {
    InputStream is = Fonts.class.getResourceAsStream("/assets/minecraft/nebula/font/" + fontName + ".ttf");
    if (is != null)
    {
      try
      {
        Font font = loadFont(is);
        return new AWTFontRenderer(font.deriveFont(Font.PLAIN, size));
      } catch (IOException | FontFormatException e)
      {
        e.printStackTrace();
        return null;
      }
    }

    return null;
  }

  /**
   * Creates the fonts the client uses
   */
  public static void loadFonts()
  {
    icons = create("Typeface", 22);
    axiforma = create("Poppins-Regular", 19);
  }
}