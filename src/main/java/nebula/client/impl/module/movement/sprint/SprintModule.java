package nebula.client.impl.module.movement.sprint;

import nebula.client.api.eventbus.Listener;
import nebula.client.api.eventbus.Subscribe;
import nebula.client.impl.event.player.EventUpdate;
import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleMeta;
import nebula.client.api.value.Setting;
import nebula.client.api.value.SettingMeta;

/**
 * @author Gavin
 * @since 08/09/23
 */
@SuppressWarnings("unused")
@ModuleMeta(name = "Sprint", defaultState = true)
public class SprintModule extends Module
{

  @SettingMeta("Rage")
  private final Setting<Boolean> rage = new Setting<>(
      false);

  @Subscribe
  private final Listener<EventUpdate> update = event ->
  {
    mc.gameSettings.keyBindSprint.pressed = true;

    if (rage.value() && !mc.thePlayer.isSprinting())
    {
      mc.thePlayer.setSprinting(true);
    }
  };
}
