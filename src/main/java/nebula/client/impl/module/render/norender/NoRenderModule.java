package nebula.client.impl.module.render.norender;

import nebula.client.api.eventbus.Listener;
import nebula.client.api.eventbus.Subscribe;
import nebula.client.impl.event.render.EventRender3DItem;
import nebula.client.impl.event.render.EventRenderEXPOrb;
import nebula.client.impl.event.render.overlay.EventRenderBurning;
import nebula.client.impl.event.render.overlay.EventRenderHurtCamera;
import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleMeta;
import nebula.client.api.value.Setting;
import nebula.client.api.value.SettingMeta;

/**
 * @author Gavin
 * @since 08/24/23
 */
@SuppressWarnings("unused")
@ModuleMeta(name = "NoRender",
    description = "Prevents annoying things from rendering")
public class NoRenderModule extends Module
{

  @SettingMeta("Hurt Camera")
  private final Setting<Boolean> hurtCamera = new Setting<>(
      true);
  @SettingMeta("Burning")
  private final Setting<Boolean> burning = new Setting<>(
      true);
  @SettingMeta("Items")
  private final Setting<Boolean> items = new Setting<>(
      false);
  @SettingMeta("EXP")
  private final Setting<Boolean> exp = new Setting<>(
      false);

  // listeners

  @Subscribe
  private final Listener<EventRenderHurtCamera> renderHurtCamera
      = event -> event.setCanceled(hurtCamera.value());

  @Subscribe
  private final Listener<EventRenderBurning> renderBurning
      = event -> event.setCanceled(burning.value());

  @Subscribe
  private final Listener<EventRender3DItem> render3DItem
      = event -> event.setCanceled(items.value());

  @Subscribe
  private final Listener<EventRenderEXPOrb> renderEXPOrb
      = event -> event.setCanceled(exp.value());
}
