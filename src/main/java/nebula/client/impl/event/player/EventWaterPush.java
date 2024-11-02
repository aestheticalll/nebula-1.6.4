package nebula.client.impl.event.player;

import nebula.client.api.eventbus.Cancelable;
import net.minecraft.entity.Entity;

/**
 * @author Gavin
 * @since 03/20/24
 */
public final class EventWaterPush extends Cancelable
{
  private final Entity entity;

  public EventWaterPush(Entity entity)
  {
    this.entity = entity;
  }

  public Entity getEntity()
  {
    return entity;
  }
}
