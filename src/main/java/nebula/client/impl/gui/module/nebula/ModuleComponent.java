package nebula.client.impl.gui.module.nebula;

import nebula.client.api.gui.component.Component;
import nebula.client.api.gui.font.Fonts;
import nebula.client.api.module.Module;
import nebula.client.api.value.Setting;
import nebula.client.api.value.SettingMeta;
import nebula.client.impl.gui.module.nebula.setting.MacroSettingComponent;
import nebula.client.impl.gui.module.nebula.setting.BooleanSettingComponent;
import nebula.client.impl.gui.module.nebula.setting.EnumSettingComponent;
import nebula.client.util.render.RenderUtils;

import java.awt.Color;

@SuppressWarnings("unchecked")
public class ModuleComponent extends Component
{
  private static final double PADDING = 1.0;

  private final Module module;

  // This may be the most retarded thing in this client tbh
  @SettingMeta("Hidden")
  private final Setting<Boolean> hiddenSetting = new Setting<>(false)
  {
    @Override
    public void setValue(Boolean value)
    {
      super.setValue(value);
      module.setHidden(value);
    }
  };

  private boolean expanded;

  public ModuleComponent(Module module)
  {
    this.module = module;

    // TODO: this is retarded please find a better solution
    hiddenSetting.setValue(module.hidden());
    try
    {
      hiddenSetting.setMeta(getClass().getDeclaredField("hiddenSetting").getDeclaredAnnotation(SettingMeta.class));
    } catch (NoSuchFieldException e)
    {
      throw new RuntimeException(e);
    }

    for (Setting<?> setting : module.settings())
    {
      if (setting.value() instanceof Boolean)
      {
        children().add(new BooleanSettingComponent((Setting<Boolean>) setting));
      } else if (setting.value() instanceof Enum<?>)
      {
        children().add(new EnumSettingComponent((Setting<Enum<?>>) setting));
      }
    }

    children().add(new BooleanSettingComponent(hiddenSetting));
    children().add(new MacroSettingComponent("Bind", module.macro()));
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks)
  {

    if (module.macro().toggled() || mouseOver(mouseX, mouseY))
    {
      Color color = (module.macro().toggled() ? new Color(101, 64, 152) : new Color(66, 66, 66));

      RenderUtils.roundRect(
          x + PADDING,
          y,
          width - (PADDING * 2),
          height(),
          3.5,
          color.getRGB()); // new Color(82, 54, 121).getRGB()
    }

    Fonts.axiforma.drawStringWithShadow(
        module.meta().name(),
        (float) (x + (PADDING * 3)),
        (float) (y + 1.0),
        0xEEEEEE);

    if (!module.settings().isEmpty())
    {
      Fonts.icons.drawStringWithShadow("g",
          (float) (x + width - 16),
          (float) (y + 3.0),
          0xEEEEEE);
    }

    if (expanded)
    {
      // new Color(66, 66, 66).getRGB()
      RenderUtils.roundRect(
          x + (PADDING * 2),
          y + height,
          width - (PADDING * 4),
          height() - height - PADDING,
          2.5f,
          new Color(44, 44, 44).getRGB()
      );

      double posY = y + height + PADDING;
      for (nebula.client.api.gui.component.Component component : children())
      {
        component.setX(x + PADDING);
        component.setY(posY);
        component.setHeight(height);
        component.setWidth(width - (PADDING * 2));

        component.render(mouseX, mouseY, partialTicks);

        posY += component.height();
      }
    }
  }

  @Override
  public void mouseClicked(int mouseX, int mouseY, int mouseButton)
  {
    if (mouseOver(mouseX, mouseY))
    {

      if (mouseButton == 0)
      {
        module.macro().setEnabled(!module.macro().toggled());
      } else if (mouseButton == 1)
      {
        expanded = !expanded;
      }
    }

    if (expanded)
    {
      for (nebula.client.api.gui.component.Component drawable : children())
      {
        drawable.mouseClicked(mouseX, mouseY, mouseButton);
      }
    }
  }

  @Override
  public void keyTyped(char typedChar, int keyCode)
  {
    if (expanded)
    {
      for (Component drawable : children())
      {
        drawable.keyTyped(typedChar, keyCode);
      }
    }
  }

  @Override
  public double height()
  {
    double height = super.height();
    if (expanded)
    {
      height += (PADDING * 2);
      for (Component component : children())
      {
        height += component.height();
      }
    }
    return height;
  }
}
