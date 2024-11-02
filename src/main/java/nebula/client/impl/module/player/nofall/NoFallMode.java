package nebula.client.impl.module.player.nofall;

/**
 * @author Gavin
 * @since 08/18/23
 */
public enum NoFallMode
{
  SPOOF,
  LAG_BACK,
  NAN
      {
        @Override
        public String toString()
        {
          return "NaN";
        }
      }
}
