package nebula.client.impl.module.render.infviewer;

import nebula.client.api.eventbus.Listener;
import nebula.client.api.eventbus.Subscribe;
import nebula.client.impl.event.render.EventRenderStackCount;
import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleMeta;
import nebula.client.impl.module.render.hud.HUDModule;
import nebula.client.api.value.Setting;
import nebula.client.api.value.SettingMeta;
import nebula.client.util.player.InventoryUtils;

/**
 * @author Gavin
 * @since 08/18/23
 */
@SuppressWarnings("unused")
@ModuleMeta(name = "InfViewer",
    description = "Allows you to view infinite item stack counts")
public class InfViewerModule extends Module
{

  @SettingMeta("Simple")
  private final Setting<Boolean> simple = new Setting<>(
      false);

  @Subscribe
  private final Listener<EventRenderStackCount> renderStackCount = event ->
  {
    if (!InventoryUtils.infinite(event.itemStack())) return;

    event.setColor(HUDModule.primary.value().getRGB());
    event.setText(simple.value()
        ? "-"
        : String.valueOf(event.itemStack().stackSize));
    event.setCanceled(true);
  };
}
