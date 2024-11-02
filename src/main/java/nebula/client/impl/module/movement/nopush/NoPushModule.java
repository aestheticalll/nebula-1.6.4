package nebula.client.impl.module.movement.nopush;

import nebula.client.api.eventbus.Listener;
import nebula.client.api.eventbus.Subscribe;
import nebula.client.impl.event.player.EventBlockPush;
import nebula.client.impl.event.player.EventWaterPush;
import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleMeta;
import nebula.client.api.value.Setting;
import nebula.client.api.value.SettingMeta;

/**
 * @author Gavin
 * @since 03/19/24
 */
@SuppressWarnings("unused")
@ModuleMeta(name = "NoPush",
    description = "Prevents things from pushing you around")
public final class NoPushModule extends Module
{

  @SettingMeta("Blocks")
  private final Setting<Boolean> blocks = new Setting<>(
      true);

  @SettingMeta("Liquids")
  private final Setting<Boolean> liquids = new Setting<>(
      true);

  @Subscribe
  private final Listener<EventBlockPush> blockPushListener = (event) ->
  {
    if (event.getPlayer() != null && event.getPlayer().equals(mc.thePlayer) && blocks.value())
    {
      event.setCanceled(true);
    }
  };

  @Subscribe
  private final Listener<EventWaterPush> waterPushListener = (event) ->
  {
    if (event.getEntity() != null && event.getEntity().equals(mc.thePlayer) && liquids.value())
    {
      event.setCanceled(true);
    }
  };
}
