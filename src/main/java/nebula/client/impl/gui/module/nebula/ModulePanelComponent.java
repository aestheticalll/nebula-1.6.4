package nebula.client.impl.gui.module.nebula;

import nebula.client.api.gui.component.Component;
import nebula.client.api.gui.component.Draggable;
import nebula.client.api.gui.font.Fonts;
import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleCategory;
import nebula.client.util.render.RenderUtils;

import java.awt.Color;
import java.util.List;

public class ModulePanelComponent extends Draggable
{

  public static final int HEADER = new Color(33, 33, 33).getRGB();
  public static final int BACKGROUND = new Color(48, 48, 48).getRGB();
  public static final int FOREGROUND = new Color(66, 66, 66).getRGB();
  private static final double PADDING = 1.0;
  private final ModuleCategory category;

  public ModulePanelComponent(ModuleCategory category, List<Module> modules)
  {
    //super(1.0);
    this.category = category;

    setWidth(120);
    setHeight(16);

    for (Module module : modules)
    {
      children().add(new ModuleComponent(module));
    }
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks)
  {

    RenderUtils.scissor(x, y - 1, width, height() + 1);

    RenderUtils.roundRect(x, y, width, height(), 6, HEADER);
    RenderUtils.roundRect(x + PADDING, y + height, width - (PADDING * 2), height() - height - PADDING, 2.8, BACKGROUND);

    {
      Fonts.axiforma.drawStringWithShadow(
          category.display(),
          (float) ((x + PADDING * 2) + 14),
          (float) (y + 1.5),
          -1);
            Fonts.icons.drawStringWithShadow(category.icon(),
                    (float) (x + PADDING * 2), (float) (y + 4), -1);
    }

    double posY = y + height + PADDING;
    for (Component component : children())
    {
      component.setX(x + PADDING);
      component.setY(posY);

      component.setWidth(width - (PADDING * 2));
      component.setHeight(15);

      component.render(mouseX, mouseY, partialTicks);

      posY += component.height() + PADDING;
    }

    RenderUtils.endScissor();
  }

  @Override
  public void mouseClicked(int mouseX, int mouseY, int mouseButton)
  {

    for (Component component : children())
    {
      component.mouseClicked(mouseX, mouseY, mouseButton);
    }
  }

  @Override
  public void keyTyped(char typedChar, int keyCode)
  {
    for (Component component : children())
    {
      component.keyTyped(typedChar, keyCode);
    }
  }

  @Override
  public double height()
  {
    double original = height + PADDING;
    for (Component component : children())
    {
      original += component.height() + PADDING;
    }
    return original + (PADDING * 2);
  }
}
