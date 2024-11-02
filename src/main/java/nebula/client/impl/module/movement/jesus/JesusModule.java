package nebula.client.impl.module.movement.jesus;

import nebula.client.api.eventbus.Listener;
import nebula.client.api.eventbus.Subscribe;
import nebula.client.impl.event.EventStage;
import nebula.client.impl.event.player.EventMoveUpdate;
import nebula.client.impl.event.world.EventCollision;
import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleMeta;
import nebula.client.util.player.chat.Printer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;

/**
 * @author Gavin
 * @since 08/24/23
 */
@SuppressWarnings("unused")
@ModuleMeta(name = "Jesus",
    description = "Allows you to walk on water")
public class JesusModule extends Module
{

  /**
   * The bounding box offset for a liquid block
   */
  private static final AxisAlignedBB JESUS_AABB = new AxisAlignedBB(
      0, 0, 0, 1, 0.99, 1);

  private boolean floatingToTop;

  @Subscribe
  private final Listener<EventCollision> collision = event ->
  {
    if (event.entity() != null
        && event.entity().equals(mc.thePlayer)
        && event.block().getMaterial().isLiquid()
        && !mc.thePlayer.isInWater()
        && !floatingToTop
        && mc.thePlayer.fallDistance < 3.1f)
    {
      event.setBox(JESUS_AABB.copy().offset(event.x(), event.y(), event.z()));
    }
  };

  @Subscribe
  private final Listener<EventMoveUpdate> moveUpdate = event ->
  {
    if (event.stage() == EventStage.PRE)
    {
      // Allow water to catch fall
      if (mc.thePlayer.fallDistance > 3.1f)
      {
        return;
      }

      // Floating above water handling
      if (mc.thePlayer.isInWater())
      {
        mc.thePlayer.motionY = 0.13;
        floatingToTop = true;
      } else
      {
        if (floatingToTop)
        {
          mc.thePlayer.motionY = 0.3;
          floatingToTop = false;
          return;
        }
      }

      // TODO: jumping on water??
      if (isAboveWater() && mc.thePlayer.groundTicks != 0 && mc.thePlayer.ticksExisted % 2 == 0)
      {
        event.setY(event.y() + 0.01);
        event.setStance(event.stance() + 0.01);
      }
    }
  };

  public static boolean isAboveWater()
  {
    double x = Math.floor(mc.thePlayer.posX);
    double y = Math.round(mc.thePlayer.boundingBox.minY) - 1;
    double z = Math.floor(mc.thePlayer.posZ);
    return mc.theWorld.getBlock((int) x, (int) y, (int) z).getMaterial().isLiquid();
  }
}
