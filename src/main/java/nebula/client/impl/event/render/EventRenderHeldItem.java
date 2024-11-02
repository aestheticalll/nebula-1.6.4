package nebula.client.impl.event.render;

import nebula.client.api.eventbus.Cancelable;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

/**
 * @author Gavin
 * @since 03/27/24
 */
public final class EventRenderHeldItem extends Cancelable
{
  private final ItemStack stack;
  private EnumAction action;

  public EventRenderHeldItem(ItemStack stack, EnumAction action)
  {
    this.stack = stack;
    this.action = action;
  }

  public ItemStack getStack()
  {
    return stack;
  }

  public EnumAction getAction()
  {
    return action;
  }

  public void setAction(EnumAction action)
  {
    this.action = action;
  }
}
