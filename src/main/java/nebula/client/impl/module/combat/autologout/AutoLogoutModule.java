package nebula.client.impl.module.combat.autologout;

import nebula.client.api.eventbus.Listener;
import nebula.client.api.eventbus.Subscribe;
import nebula.client.impl.event.player.EventUpdate;
import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleMeta;
import nebula.client.util.player.chat.ChatUtils;
import nebula.client.util.player.chat.Printer;
import nebula.client.api.value.Setting;
import nebula.client.api.value.SettingMeta;
import net.minecraft.util.ChatComponentText;

/**
 * @author Gavin
 * @since 08/24/23
 */
@SuppressWarnings("unused")
@ModuleMeta(name = "AutoLogout",
    description = "Automatically logs out of the game upon low health")
public class AutoLogoutModule extends Module
{

  @SettingMeta("Health")
  private final Setting<Float> health = new Setting<>(
      6.0f, 0.0f, 19.0f, 0.5f);
  @SettingMeta("Auto Disable")
  private final Setting<Boolean> autoDisable = new Setting<>(
      true);
  @Subscribe
  private final Listener<EventUpdate> update = event ->
  {
    if (mc.thePlayer.getHealth() <= health.value())
    {
      mc.thePlayer.sendQueue.getNetworkManager().closeChannel(
          new ChatComponentText(ChatUtils.replaceFormatting(
              Printer.PREFIX
                  + "automatically logged out at &c"
                  + String.format("%.1f", mc.thePlayer.getHealth())
                  + "\u2764")));

      if (autoDisable.value()) macro().setEnabled(false);
    }
  };

  @Override
  public String info()
  {
    return String.valueOf(health.value());
  }
}
