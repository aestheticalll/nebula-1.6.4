package nebula.client.impl.module.player.novoid;

import nebula.client.api.eventbus.Listener;
import nebula.client.api.eventbus.Subscribe;
import nebula.client.impl.event.player.EventMove;
import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleMeta;
import nebula.client.api.value.Setting;
import nebula.client.api.value.SettingMeta;

/**
 * @author Gavin
 * @since 03/20/24
 */
@ModuleMeta(name = "NoVoid",
    description = "Prevents you from falling into the void")
public final class NoVoidModule extends Module
{

  @SettingMeta("Mode")
  private final Setting<NoVoidMode> mode = new Setting<>(
      NoVoidMode.FLOAT);

  @Subscribe
  private final Listener<EventMove> move = event ->
  {
    if (mc.thePlayer.boundingBox.minY <= 0.0)
    {
      event.setY(0.0);
      mc.thePlayer.motionY = 0.0;
    }
  };
}
