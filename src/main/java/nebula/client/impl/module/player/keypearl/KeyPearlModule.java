package nebula.client.impl.module.player.keypearl;

import nebula.client.Nebula;
import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleMeta;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

/**
 * @author Gavin
 * @since 08/25/23
 */
@SuppressWarnings("unused")
@ModuleMeta(name = "KeyPearl",
    description = "Pearls on a key press")
public class KeyPearlModule extends Module
{

  @Override
  public void enable()
  {
    super.enable();
    macro().setEnabled(false);

    if (mc.thePlayer == null) return;

    int slot = -1;
    for (int i = 0; i < 9; ++i)
    {
      ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
      if (itemStack != null
          && itemStack.getItem() instanceof ItemEnderPearl)
      {
        slot = i;
        break;
      }
    }

    if (slot == -1) return;

    Nebula.INSTANCE.inventory.setSlot(slot);
    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(
        -1, -1, -1, 255,
        mc.thePlayer.inventory.getStackInSlot(slot), 0.0f, 0.0f, 0.0f));
    Nebula.INSTANCE.inventory.sync();
  }
}
