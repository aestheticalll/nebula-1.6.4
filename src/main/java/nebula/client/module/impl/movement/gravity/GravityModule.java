package nebula.client.module.impl.movement.gravity;

import nebula.client.listener.bus.Listener;
import nebula.client.listener.bus.Subscribe;
import nebula.client.listener.event.player.EventGravity;
import nebula.client.module.Module;
import nebula.client.module.ModuleMeta;
import nebula.client.util.value.Setting;
import nebula.client.util.value.SettingMeta;
import net.minecraft.client.entity.EntityClientPlayerMP;

/**
 * @author Gavin
 * @since 03/22/24
 */
@ModuleMeta(name = "Gravity")
public final class GravityModule extends Module {

  @SettingMeta("Drag")
  private final Setting<Double> drag = new Setting<>(
      0.08, 0.01, 0.2, 0.01);

  @SettingMeta("Constant")
  private final Setting<Float> constant = new Setting<>(
      0.98f, 0.01f, 1.0f, 0.01f);

  @Subscribe
  private final Listener<EventGravity> gravityListener = event -> {
    if (event.getEntity() instanceof EntityClientPlayerMP) {
      event.setAirResistance(drag.value());
      event.setGravity(constant.value());
    }
  };
}
