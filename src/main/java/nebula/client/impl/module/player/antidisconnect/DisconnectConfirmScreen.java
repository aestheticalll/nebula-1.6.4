package nebula.client.impl.module.player.antidisconnect;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;

public class DisconnectConfirmScreen extends GuiYesNo
{
  public DisconnectConfirmScreen(GuiScreen par1GuiScreen)
  {
    super(par1GuiScreen, "Are you sure you want to disconnect?", "", 69420);
  }
}
