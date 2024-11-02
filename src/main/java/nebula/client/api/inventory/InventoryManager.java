package nebula.client.api.inventory;

import nebula.client.Nebula;
import nebula.client.api.eventbus.Listener;
import nebula.client.api.eventbus.Subscribe;
import nebula.client.impl.event.net.EventPacket;
import nebula.client.impl.event.render.EventRenderHotbarSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S09PacketHeldItemChange;

/**
 * @author Gavin
 * @since 08/18/23
 */
public class InventoryManager
{

  /**
   * The minecraft game instance
   */
  private final Minecraft mc = Minecraft.getMinecraft();

  private int slot;

  @Subscribe
  private final Listener<EventPacket.Out> packetOut = event ->
  {
    if (event.packet() instanceof C09PacketHeldItemChange packet)
    {
      final int clientSlot = packet.getSlotIndex();
      if (clientSlot < 0 || clientSlot > 8)
      {
        event.setCanceled(true);
      }

      slot = clientSlot;
    }
  };

  @Subscribe
  private final Listener<EventPacket.In> packetIn = event ->
  {
    if (event.packet() instanceof S09PacketHeldItemChange packet)
    {
      slot = packet.func_149385_c();
    }
  };

  @Subscribe
  private final Listener<EventRenderHotbarSlot> renderHotbarSlot = event ->
  {
    event.setSlot(slot);
  };

  public InventoryManager()
  {
    Nebula.BUS.subscribe(this);
  }

  public void setSlot(final int slot)
  {
    if (this.slot != slot && (slot >= 0 && slot <= 8))
    {
      setSlotInstant(slot);
    }
  }

  public void setSlotInstant(final int slot)
  {
    mc.thePlayer.sendQueue.addToSendQueue(
        new C09PacketHeldItemChange(slot));
  }

  public int slot()
  {
    return slot;
  }

  public void sync()
  {
    if (mc.thePlayer == null)
    {
      return;
    }

    if (mc.thePlayer.inventory.currentItem != slot)
    {
      setSlotInstant(mc.thePlayer.inventory.currentItem);
    }
  }
}
