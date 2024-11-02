package nebula.client.impl.event.player.rotate;

import nebula.client.api.eventbus.Cancelable;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author Gavin
 * @since 08/18/23
 */
public class EventRotateBody extends Cancelable
{
  private final EntityLivingBase entity;

  public EventRotateBody(EntityLivingBase entity)
  {
    this.entity = entity;
  }

  public EntityLivingBase entity()
  {
    return entity;
  }
}
