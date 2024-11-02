package nebula.client.impl.gui.module.nebula.setting;

import nebula.client.api.gui.component.Component;
import nebula.client.api.gui.font.Fonts;
import nebula.client.api.macro.Macro;
import nebula.client.api.macro.MacroType;
import nebula.client.util.render.RenderUtils;
import org.lwjgl.input.Keyboard;

import java.awt.Color;

public class MacroSettingComponent extends Component
{
  private static final double PADDING = 1.0;

  private final String name;
  private final Macro macro;
  private boolean listening;

  public MacroSettingComponent(String name, Macro macro)
  {
    this.name = name;
    this.macro = macro;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks)
  {
    if (listening)
    {
      Fonts.axiforma.drawStringWithShadow(
          "Awaiting Input...",
          (float) (x + (PADDING * 3)),
          (float) (y + 1.0),
          0xEEEEEE);
      return;
    }

    Fonts.axiforma.drawStringWithShadow(
        name,
        (float) (x + (PADDING * 3)),
        (float) (y + 1.0),
        0xEEEEEE);

    String value = switch (macro.type())
    {
      case KEYBOARD ->
      {
        if (macro.key() == -1) yield "NONE";
        yield Keyboard.getKeyName(macro.key());
      }
      case MOUSE ->
      {
        if (macro.key() == -1) yield "NONE";
        yield "MB " + (macro.key() + 1);
      }
    };
    int size = Fonts.axiforma.getStringWidth(value) + 4;

    RenderUtils.roundRect(
        (float) (x + width - size - (PADDING * 4)),
        (float) (y + PADDING),
        size + (PADDING * 2),
        height - (PADDING * 2),
        5.0f,
        new Color(39, 39, 39).getRGB()
    );

    Fonts.axiforma.drawStringWithShadow(
        value,
        (float) (x + width - size - (PADDING * 2)),
        (float) (y + PADDING),
        0xBBBBBB);

  }

  @Override
  public void mouseClicked(int mouseX, int mouseY, int mouseButton)
  {
    if (listening)
    {
      listening = false;

      macro.setKey(mouseButton);
      macro.setType(MacroType.MOUSE);
    }

    if (mouseOver(mouseX, mouseY))
    {

      if (mouseButton == 0)
      {
        listening = !listening;
      } else if (mouseButton == 1)
      {
        listening = false;
        macro.setKey(-1);
      }
    }
  }

  @Override
  public void keyTyped(char typedChar, int keyCode)
  {
    if (listening)
    {
      listening = false;
      macro.setKey(keyCode);
      macro.setType(MacroType.KEYBOARD);
    }
  }
}
