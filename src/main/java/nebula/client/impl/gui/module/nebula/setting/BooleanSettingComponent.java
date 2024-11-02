package nebula.client.impl.gui.module.nebula.setting;

import nebula.client.api.gui.component.Component;
import nebula.client.api.gui.font.Fonts;
import nebula.client.api.value.Setting;
import nebula.client.util.render.RenderUtils;
import net.minecraft.util.ResourceLocation;

import java.awt.Color;

public class BooleanSettingComponent extends Component
{
  private static final double PADDING = 1.0;

  private static final ResourceLocation CHECKMARK_RESOURCE = new ResourceLocation(
      "nebula/texture/checkmark.png");

  private final Setting<Boolean> setting;

  public BooleanSettingComponent(Setting<Boolean> setting)
  {
    this.setting = setting;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks)
  {

    //RenderUtils.roundRect(x, y, width, height, 1.0f, Color.red.getRGB());

    Fonts.axiforma.drawStringWithShadow(
        setting.meta().value(),
        (float) (x + (PADDING * 3)),
        (float) (y + 1.0),
        0xEEEEEE);

    double d = height - PADDING;
    RenderUtils.roundRect(
        x + width - d - PADDING,
        y + (PADDING / 2.0),
        d, d,
        5f,
        new Color(39, 39, 39).getRGB()
    );

    if (setting.value())
    {
      RenderUtils.renderTexture(CHECKMARK_RESOURCE,
          x + width - d + PADDING,
          y + (PADDING * 4),
          10, 8, Color.white);
    }
  }

  @Override
  public void mouseClicked(int mouseX, int mouseY, int mouseButton)
  {
    if (mouseOver(mouseX, mouseY))
    {
      setting.setValue(!setting.value());
    }
  }

  @Override
  public void keyTyped(char typedChar, int keyCode)
  {

  }
}
