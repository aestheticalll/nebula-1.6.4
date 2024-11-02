package nebula.client.impl.module.player.autorespawn;

import nebula.client.api.eventbus.Listener;
import nebula.client.api.eventbus.Subscribe;
import nebula.client.impl.event.player.EventUpdate;
import nebula.client.impl.event.render.EventChangeGui;
import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleMeta;
import nebula.client.util.player.chat.Printer;
import nebula.client.api.value.Setting;
import nebula.client.api.value.SettingMeta;
import net.minecraft.client.gui.GuiGameOver;

import static java.lang.String.format;

/**
 * @author Gavin
 * @since 08/24/23
 */
@SuppressWarnings("unused")
@ModuleMeta(name = "AutoRespawn",
    description = "Automatically respawns for you")
public class AutoRespawnModule extends Module
{

  @SettingMeta("Instant")
  private final Setting<Boolean> instant = new Setting<>(
      false);
  @SettingMeta("Death Coords")
  private final Setting<Boolean> deathCoords = new Setting<>(
      true);

  private boolean respawn;
  @Subscribe
  private final Listener<EventChangeGui> changeGui = event ->
  {
    if (event.screen() instanceof GuiGameOver && !respawn)
    {
      respawn = true;

      if (deathCoords.value())
      {
        Printer.print(format("You died at XYZ: %.1f, %.1f, %.1f",
            mc.thePlayer.posX,
            mc.thePlayer.boundingBox.minY,
            mc.thePlayer.posZ));
      }

      if (instant.value())
      {
        mc.thePlayer.respawnPlayer();
        event.setCanceled(true);
      }
    } else if (event.screen() == null)
    {
      respawn = false;
    }
  };
  @Subscribe
  private final Listener<EventUpdate> update = event ->
  {
    if (respawn)
    {
      respawn = false;
      mc.thePlayer.respawnPlayer();
    }
  };

  @Override
  public void disable()
  {
    super.disable();
    respawn = false;
  }

}
