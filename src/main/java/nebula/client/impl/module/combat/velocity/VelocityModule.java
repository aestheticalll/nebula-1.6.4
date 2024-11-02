package nebula.client.impl.module.combat.velocity;

import nebula.client.api.eventbus.Listener;
import nebula.client.api.eventbus.Subscribe;
import nebula.client.impl.event.net.EventPacket;
import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleMeta;
import nebula.client.api.value.Setting;
import nebula.client.api.value.SettingMeta;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

/**
 * @author Gavin
 * @since 08/18/23
 */
@SuppressWarnings("unused")
@ModuleMeta(name = "Velocity",
    description = "Prevents you from taking server velocity")
public class VelocityModule extends Module
{

  @SettingMeta("Horizontal")
  private final Setting<Double> horizontal = new Setting<>(
      0.0, 0.0, 200.0, 0.01);
  @SettingMeta("Vertical")
  private final Setting<Double> vertical = new Setting<>(
      0.0, 0.0, 200.0, 0.01);
  @Subscribe
  private final Listener<EventPacket.In> packetIn = event ->
  {
    if (mc.thePlayer == null) return;

    if (event.packet() instanceof S12PacketEntityVelocity packet)
    {
      if (packet.getEntityId() != mc.thePlayer.getEntityId()) return;

      if (horizontal.value() == 0.0 && vertical.value() == 0.0)
      {
        event.setCanceled(true);
        return;
      }

      packet.setX((int) (packet.getX() * (horizontal.value() / 100.0)));
      packet.setY((int) (packet.getY() * (vertical.value() / 100.0)));
      packet.setZ((int) (packet.getZ() * (horizontal.value() / 100.0)));
    } else if (event.packet() instanceof S27PacketExplosion packet)
    {
      if (horizontal.value() == 0.0 && vertical.value() == 0.0)
      {
        event.setCanceled(true);
        return;
      }

      packet.setX((float) (packet.getX() * (horizontal.value() / 100.0)));
      packet.setY((float) (packet.getY() * (vertical.value() / 100.0)));
      packet.setZ((float) (packet.getZ() * (horizontal.value() / 100.0)));
    }
  };

  @Override
  public String info()
  {
    return horizontal.value() + "% " + vertical.value() + "%";
  }
}
