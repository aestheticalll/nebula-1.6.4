package nebula.client.impl.module.render.clickgui;

import nebula.client.impl.gui.module.future.FutureModuleConfigScreen;
import nebula.client.impl.gui.module.nebula.NebulaModuleConfigScreen;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author Gavin
 * @since 08/13/23
 */
public enum ClickGUIMode
{
  NEBULA(NebulaModuleConfigScreen.class),
  FUTURE(FutureModuleConfigScreen.class);

  private final Class<? extends GuiScreen> clazz;
  private GuiScreen instance;

  ClickGUIMode(Class<? extends GuiScreen> clazz)
  {
    this.clazz = clazz;
  }

  public GuiScreen instance(ClickGUIModule module)
  {
    if (instance == null)
    {
      try
      {
        instance = (GuiScreen) clazz.getConstructors()[0].newInstance(module);
      } catch (Exception e)
      {
        e.printStackTrace();
      }
    }

    return instance;
  }
}
