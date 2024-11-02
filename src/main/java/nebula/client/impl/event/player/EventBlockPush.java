package nebula.client.impl.event.player;

import nebula.client.api.eventbus.Cancelable;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Gavin
 * @since 03/20/24
 */
public final class EventBlockPush extends Cancelable
{
  private final EntityPlayer player;

  public EventBlockPush(EntityPlayer player)
  {
    this.player = player;
  }

  public EntityPlayer getPlayer()
  {
    return player;
  }
}
