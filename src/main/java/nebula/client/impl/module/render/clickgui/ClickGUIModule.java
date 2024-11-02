package nebula.client.impl.module.render.clickgui;

import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleMeta;
import nebula.client.api.value.Setting;
import nebula.client.api.value.SettingMeta;

import static org.lwjgl.input.Keyboard.KEY_RSHIFT;

/**
 * @author Gavin
 * @since 08/09/23
 */
@SuppressWarnings("unused")
@ModuleMeta(name = "ClickGUI",
    description = "Displays a configuration menu for modules",
    defaultMacro = KEY_RSHIFT)
public class ClickGUIModule extends Module
{

  @SettingMeta("Pause")
  public final Setting<Boolean> pause = new Setting<>(
      false);
  @SettingMeta("Mode")
  private final Setting<ClickGUIMode> mode = new Setting<>(
      ClickGUIMode.FUTURE);

  @Override
  public void enable()
  {
    super.enable();

    if (mc.thePlayer == null || mc.theWorld == null) return;
    mc.displayGuiScreen(mode.value().instance(this));
  }
}
