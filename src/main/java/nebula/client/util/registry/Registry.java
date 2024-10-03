package nebula.client.util.registry;

import java.util.Collection;

/**
 * @param <T> the type this registry holds
 * @author Gavin
 * @since 08/09/23
 */
public interface Registry<T>
{

  void init();

  void add(T... elements);

  void remove(T... elements);

  Collection<T> values();
}
