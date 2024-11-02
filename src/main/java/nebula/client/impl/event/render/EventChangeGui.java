package nebula.client.impl.event.render;

import nebula.client.api.eventbus.Cancelable;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author Gavin
 * @since 08/24/23
 */
public class EventChangeGui extends Cancelable
{
  private final GuiScreen screen;

  public EventChangeGui(GuiScreen screen)
  {
    this.screen = screen;
  }

  public GuiScreen screen()
  {
    return screen;
  }
}