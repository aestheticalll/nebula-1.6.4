package nebula.client.impl.module.movement.autowalk;

import nebula.client.api.eventbus.Listener;
import nebula.client.api.eventbus.Subscribe;
import nebula.client.impl.event.player.EventUpdate;
import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleMeta;

/**
 * @author Gavin
 * @since 08/09/23
 */
@SuppressWarnings("unused")
@ModuleMeta(name = "AutoWalk", description = "Automatically walks for you")
public class AutoWalkModule extends Module
{

  @Subscribe
  private final Listener<EventUpdate> update = event ->
  {
    mc.gameSettings.keyBindForward.pressed = true;
  };

  @Override
  public void disable()
  {
    super.disable();
    mc.gameSettings.keyBindForward.pressed = false;
  }
}
