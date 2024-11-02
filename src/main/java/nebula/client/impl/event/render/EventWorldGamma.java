package nebula.client.impl.event.render;

/**
 * @author Gavin
 * @since 03/20/24
 */
public final class EventWorldGamma
{
  private float gamma;

  public EventWorldGamma(float gamma)
  {
    this.gamma = gamma;
  }

  public float getGamma()
  {
    return gamma;
  }

  public void setGamma(float gamma)
  {
    this.gamma = gamma;
  }
}
