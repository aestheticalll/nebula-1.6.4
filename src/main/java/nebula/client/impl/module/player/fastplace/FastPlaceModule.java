package nebula.client.impl.module.player.fastplace;

import nebula.client.api.eventbus.Listener;
import nebula.client.api.eventbus.Subscribe;
import nebula.client.impl.event.player.EventUpdate;
import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleMeta;
import nebula.client.api.value.Setting;
import nebula.client.api.value.SettingMeta;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

/**
 * @author Gavin
 * @since 08/21/23
 */
@SuppressWarnings("unused")
@ModuleMeta(name = "FastPlace",
    description = "Places shit faster than u can")
public class FastPlaceModule extends Module
{

  @SettingMeta("Speed")
  private final Setting<Integer> speed = new Setting<>(
      4, 0, 4, 1);
  @SettingMeta("Auto Use")
  private final Setting<Boolean> autoUse = new Setting<>(
      false);
  @Subscribe
  private final Listener<EventUpdate> update = event ->
  {
    if (autoUse.value())
    {
      mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(
          -1, -1, -1, 255,
          mc.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
    }

    mc.rightClickDelayTimer = 4 - speed.value();
  };

  @Override
  public String info()
  {
    return String.valueOf(speed.value());
  }
}
