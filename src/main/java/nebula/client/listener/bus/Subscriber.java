/* chronos.dev - Aesthetical © 2023 */
package nebula.client.listener.bus;

/**
 * @author aesthetical
 * @since 06/11/23
 */
public class Subscriber
{

  public final Listener listener;
  private final Object parent;
  private final int eventPriority;
  private final boolean ignoreCanceled;

  public Subscriber(
      Object parent,
      Listener listener,
      int eventPriority,
      boolean ignoreCanceled
  )
  {
    this.parent = parent;
    this.listener = listener;
    this.eventPriority = eventPriority;
    this.ignoreCanceled = ignoreCanceled;
  }

  public Object getParent()
  {
    return parent;
  }

  public int getEventPriority()
  {
    return eventPriority;
  }

  public boolean isIgnoreCanceled()
  {
    return ignoreCanceled;
  }
}
