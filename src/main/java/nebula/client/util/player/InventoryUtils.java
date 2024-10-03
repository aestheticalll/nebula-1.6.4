package nebula.client.util.player;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

/**
 * @author Gavin
 * @since 08/18/23
 */
public class InventoryUtils
{
  private static final Minecraft mc = Minecraft.getMinecraft();

  /**
   * The size of the local player inventory
   */
  public static final int PLAYER_INVENTORY_SIZE = 36;

  /**
   * The number of hotbar slots
   */
  public static final int HOTBAR_SIZE = 9;

  public static final int INVALID_INVENTORY = -1;

  public static int hotbarBlockSearch(final Class<? extends Block>... blocks)
  {
    for (int slot = 0; slot < HOTBAR_SIZE; ++slot)
    {
      final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(slot);
      if (itemStack == null || !(itemStack.getItem() instanceof ItemBlock itemBlock))
      {
        continue;
      }
      for (final Class<? extends Block> itemClass : blocks)
      {
        if (itemClass.isInstance(itemBlock.getBlock()))
        {
          return slot;
        }
      }
    }
    return INVALID_INVENTORY;
  }

  public static int hotbarSearch(final Class<? extends Item>... items)
  {
    for (int slot = 0; slot < HOTBAR_SIZE; ++slot)
    {
      final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(slot);
      if (itemStack == null)
      {
        continue;
      }
      for (final Class<? extends Item> itemClass : items)
      {
        if (itemClass.isInstance(itemStack.getItem()))
        {
          return slot;
        }
      }
    }
    return INVALID_INVENTORY;
  }

  public static int search(final int start, final int end, final Predicate<ItemStack> filter)
  {
    if (end < start)
    {
      return INVALID_INVENTORY;
    }
    for (int slot = start; slot < end; ++slot)
    {
      final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(slot);
      if (filter.test(itemStack))
      {
        return slot;
      }
    }
    return INVALID_INVENTORY;
  }
}
