package nebula.client.module.impl.movement.noslow;

import nebula.client.listener.bus.Listener;
import nebula.client.listener.bus.Subscribe;
import nebula.client.listener.event.EventStage;
import nebula.client.listener.event.player.EventMoveUpdate;
import nebula.client.listener.event.player.EventSlowdown;
import nebula.client.module.Module;
import nebula.client.module.ModuleMeta;
import nebula.client.util.value.Setting;
import nebula.client.util.value.SettingMeta;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

/**
 * @author Gavin
 * @since 08/18/23
 */
@SuppressWarnings("unused")
@ModuleMeta(name = "NoSlow",
    description = "Prevents you from getting slowed down")
public class NoSlowModule extends Module
{
  @SettingMeta("NCPSword")
  private final Setting<Boolean> ncpSwordSetting = new Setting<>(false);

  @Subscribe
  private final Listener<EventSlowdown> slowdown = event ->
  {
    event.input().moveForward *= 5.0f;
    event.input().moveStrafe *= 5.0f;
  };

  @Subscribe
  private final Listener<EventMoveUpdate> moveUpdateListener = event ->
  {
    if (mc.thePlayer.isBlocking())
    {
      if (event.stage() == EventStage.POST)
      {
        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
            5, 0, 0, 0, 255));
      } else
      {
        mc.thePlayer.sendQueue.addToSendQueue(
            new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
      }
    }
  };
}
