package nebula.client.impl.module.movement.icespeed;

import nebula.client.api.eventbus.Listener;
import nebula.client.api.eventbus.Subscribe;
import nebula.client.impl.event.player.EventMoveBlockFriction;
import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleMeta;
import net.minecraft.block.BlockIce;

/**
 * @author Gavin
 * @since 08/17/23
 */
@SuppressWarnings("unused")
@ModuleMeta(name = "IceSpeed",
    description = "Makes you go zoom on ice",
    defaultState = true)
public class IceSpeedModule extends Module
{
  public static final float NCP_ICE_MAX = 0.391f; // this could prolly be faster but yk

  @Subscribe
  private final Listener<EventMoveBlockFriction> moveBlockFriction = event ->
  {
    if (event.block() instanceof BlockIce)
    {
      event.setSlipperiness(NCP_ICE_MAX);
    }
  };
}
