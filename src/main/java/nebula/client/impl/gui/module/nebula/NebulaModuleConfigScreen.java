package nebula.client.impl.gui.module.nebula;

import nebula.client.Nebula;
import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleCategory;
import nebula.client.impl.module.render.clickgui.ClickGUIModule;
import net.minecraft.client.gui.GuiScreen;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NebulaModuleConfigScreen extends GuiScreen
{

  private final List<ModulePanelComponent> components = new LinkedList<>();
  private final ClickGUIModule moduleClickGUI;

  public NebulaModuleConfigScreen(ClickGUIModule moduleClickGUI)
  {
    this.moduleClickGUI = moduleClickGUI;
  }

  @Override
  public void initGui()
  {
    if (components.isEmpty())
    {
      double posX = 12.0;

      for (ModuleCategory moduleCategory : ModuleCategory.values())
      {
        List<Module> modules = Nebula.INSTANCE.module.values()
            .stream()
            .filter((x) -> x.category().equals(moduleCategory))
            .collect(Collectors.toList());
        if (modules.isEmpty()) continue;

        ModulePanelComponent component = new ModulePanelComponent(moduleCategory, modules);
        component.setX(posX);
        component.setY(26.0);

        components.add(component);

        posX += component.width() + 6.0;
      }
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks)
  {
    super.drawScreen(mouseX, mouseY, partialTicks);

    for (ModulePanelComponent component : components)
    {
      component.render(mouseX, mouseY, partialTicks);
    }
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
  {
    super.mouseClicked(mouseX, mouseY, mouseButton);

    for (ModulePanelComponent component : components)
    {
      component.mouseClicked(mouseX, mouseY, mouseButton);
    }
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode)
  {
    super.keyTyped(typedChar, keyCode);

    for (ModulePanelComponent component : components)
    {
      component.keyTyped(typedChar, keyCode);
    }
  }

  @Override
  public void onGuiClosed()
  {
    super.onGuiClosed();
    moduleClickGUI.macro().setEnabled(false);
  }

  @Override
  public boolean doesGuiPauseGame()
  {
    return moduleClickGUI.pause.value();
  }
}
