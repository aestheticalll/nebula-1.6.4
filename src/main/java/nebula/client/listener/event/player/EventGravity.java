package nebula.client.listener.event.player;

import net.minecraft.entity.Entity;

/**
 * @author Gavin
 * @since 03/22/24
 */
public final class EventGravity
{

  private final Entity entity;

  private double gravity, airResistance;

  public EventGravity(final Entity entity, final double gravity, final double airResistance)
  {
    this.entity = entity;
    this.gravity = gravity;
    this.airResistance = airResistance;
  }

  public Entity getEntity()
  {
    return entity;
  }

  public double getGravity()
  {
    return gravity;
  }

  public void setGravity(double gravity)
  {
    this.gravity = gravity;
  }

  public double getAirResistance()
  {
    return airResistance;
  }

  public void setAirResistance(double airResistance)
  {
    this.airResistance = airResistance;
  }
}
