package nebula.client.impl.gui.module.nebula.setting;

import nebula.client.api.gui.component.Component;
import nebula.client.api.gui.font.Fonts;
import nebula.client.api.value.Setting;
import nebula.client.util.render.RenderUtils;

import java.awt.Color;
import java.util.StringJoiner;

public class EnumSettingComponent extends Component
{
  private static final double PADDING = 1.0;

  private final Setting<Enum<?>> setting;

  public EnumSettingComponent(Setting<Enum<?>> setting)
  {
    this.setting = setting;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks)
  {
    Fonts.axiforma.drawStringWithShadow(
        setting.meta().value(),
        (float) (x + (PADDING * 3)),
        (float) (y + 1.0),
        0xEEEEEE);

    String value = formatEnum(setting.value());
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
    if (mouseOver(mouseX, mouseY))
    {

      if (mouseButton == 0)
      {
        setting.nextEnum();
      } else if (mouseButton == 1)
      {
        setting.previousEnum();
      }
    }
  }

  @Override
  public void keyTyped(char typedChar, int keyCode)
  {

  }

  private String formatEnum(Enum<?> value)
  {
    String toString = value.toString();
    if (!toString.equals(value.name()))
    {
      return toString;
    }

    StringJoiner joiner = new StringJoiner(" ");
    for (String part : toString.split("_"))
    {

      String builder = part.toUpperCase().charAt(0) +
          part.substring(1).toLowerCase();
      joiner.add(builder);
    }

    return joiner.toString();
  }
}
