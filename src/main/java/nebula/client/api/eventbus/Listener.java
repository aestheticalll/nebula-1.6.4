/* chronos.dev - Aesthetical © 2023 */
package nebula.client.api.eventbus;

/**
 * @author aesthetical
 * @since 06/11/23
 */
@FunctionalInterface
public interface Listener<T>
{
  void invoke(T event);
}
