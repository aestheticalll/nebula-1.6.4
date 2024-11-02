package nebula.client.api.rotation;

/**
 * @author Gavin
 * @since 08/18/23
 */
public class Rotation
{
  private final float[] angles;
  private final int priority, ticks;

  public Rotation(float[] angles, int priority, int ticks)
  {
    this.angles = angles;
    this.priority = priority;
    this.ticks = ticks;
  }

  public Rotation(float[] angles, int priority)
  {
    this(angles, priority, 1);
  }

  public static Rotation from(float[] angles, int priority)
  {
    return new Rotation(angles, priority);
  }

  public static Rotation from(float[] angles, int priority, int ticks)
  {
    return new Rotation(angles, priority, ticks);
  }

  public float[] angles()
  {
    return angles;
  }

  public int priority()
  {
    return priority;
  }

  public int ticks()
  {
    return ticks;
  }
}
