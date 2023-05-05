package lol.nebula.module.visual;

import lol.nebula.Nebula;
import lol.nebula.listener.bus.Listener;
import lol.nebula.listener.events.render.gui.overlay.EventRender2D;
import lol.nebula.module.Module;
import lol.nebula.module.ModuleCategory;
import lol.nebula.setting.Setting;
import lol.nebula.util.render.ColorUtils;
import lol.nebula.util.render.font.Fonts;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.client.resources.I18n.format;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author aesthetical
 * @since 04/27/23
 */
public class Interface extends Module {
    /**
     * The location of the inventory textures
     * Used for potion effects
     */
    private static final ResourceLocation CONTAINER_LOCATION = new ResourceLocation("textures/gui/container/inventory.png");

    public static final Setting<Color> color = new Setting<>(new Color(162, 108, 222), "Color");
    private final Setting<Boolean> coordinates = new Setting<>(true, "Coordinates");

    public Interface() {
        super("Interface", "Renders an overlay over the vanilla HUD", ModuleCategory.VISUAL);

        // by default, set on
        setState(true);

        // do not draw to array list by default
        setDrawn(false);
    }

    @Listener
    public void onRender2D(EventRender2D event) {

        // if the F3 debug menu is open, do not render over it
        if (mc.gameSettings.showDebugInfo) return;

        Fonts.axiforma.drawStringWithShadow(Nebula.getFormatted(), 3.0f, 3.0f, color.getValue().getRGB());

        List<Module> enabled = Nebula.getInstance().getModules().getModules()
                .stream()
                .filter((m) -> m.isDrawn() && (m.isToggled() || m.getAnimation().getFactor() > 0.0))
                .sorted(Comparator.comparingInt((m) -> -Fonts.axiforma.getStringWidth(formatModule(m))))
                .collect(Collectors.toList());

        if (!enabled.isEmpty()) {
            double y = 3.0;
            for (int i = 0; i < enabled.size(); ++i) {
                Module module = enabled.get(i);
                String tag = formatModule(module);

                double x = event.getRes().getScaledWidth_double() - 4.0
                        - (Fonts.axiforma.getStringWidth(tag) * module.getAnimation().getFactor());

                Fonts.axiforma.drawStringWithShadow(tag, (float) x, (float) y, ColorUtils.rainbowCycle(i * 100, 5.0));

                y += (Fonts.axiforma.FONT_HEIGHT + 2) * module.getAnimation().getFactor();
            }
        }

        // render held item and armor
        {
            double x = event.getRes().getScaledWidth_double() / 2.0 + 9.0;
            double y = event.getRes().getScaledHeight() - 56.0;

            if (mc.thePlayer.getHeldItem() != null) {
                glPushMatrix();
                RenderHelper.enableGUIStandardItemLighting();
                RenderItem renderItem = (RenderItem) RenderManager.instance.getEntityClassRenderObject(EntityItem.class);

                renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), mc.thePlayer.getHeldItem(), (int) x, (int) y);
                renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), mc.thePlayer.getHeldItem(), (int) x, (int) y);
                glPopMatrix();

                x += 16.0;
            } else {
                x += 6.0;
            }

            for (int i = 3; i >= 0; --i) {
                ItemStack stack = mc.thePlayer.inventory.armorInventory[i];
                if (stack != null) {
                    glPushMatrix();
                    RenderHelper.enableGUIStandardItemLighting();
                    RenderItem renderItem = (RenderItem) RenderManager.instance.getEntityClassRenderObject(EntityItem.class);

                    renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, (int) x, (int) y);
                    renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, (int) x, (int) y);

                    RenderHelper.disableStandardItemLighting();
                    glPopMatrix();

                    x += 16;
                }
            }
        }

        double yOffset = mc.currentScreen instanceof GuiChat ? 14.0 : 0.0;

        // render potion effects
        potionRender: {
            double y = event.getRes().getScaledHeight_double() - 3.0 - Fonts.axiforma.FONT_HEIGHT - yOffset;
            Collection<PotionEffect> activeEffects = mc.thePlayer.getActivePotionEffects();

            // do not continue if there are no potion effects
            if (activeEffects.isEmpty()) break potionRender;

            for (PotionEffect potionEffect : activeEffects) {
                String formatted = format("%s %s: %s",
                        I18n.format(potionEffect.getEffectName()),
                        String.valueOf(potionEffect.getAmplifier() + 1),
                        EnumChatFormatting.GRAY + Potion.getDurationString(potionEffect));
                double x = event.getRes().getScaledWidth_double() - 3.0 - Fonts.axiforma.getStringWidth(formatted);
                Potion potion = Potion.potionTypes[potionEffect.getPotionID()];

                Fonts.axiforma.drawStringWithShadow(formatted, (float) x, (float) y, potion.getLiquidColor());

                if (potion.hasStatusIcon()) {
                    glPushMatrix();
                    glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    glDisable(GL_LIGHTING);
                    int var9 = potion.getStatusIconIndex();
                    mc.getTextureManager().bindTexture(CONTAINER_LOCATION);
                    glTranslated(x - (18.0 / 2.0), y, 0.0);
                    glScaled(0.5, 0.5, 0.5);
                    Gui.drawTexturedModalRectX(0, 0, var9 % 8 * 18, 198 + var9 / 8 * 18, 18, 18);
                    glPopMatrix();
                }

                y -= (Fonts.axiforma.FONT_HEIGHT + 2.0);
            }
        }

        if (coordinates.getValue()) {

            Fonts.axiforma.drawStringWithShadow(
                    "XYZ: " + EnumChatFormatting.GRAY +
                            String.format("%.1f", mc.thePlayer.posX) + ", " +
                            String.format("%.1f", mc.thePlayer.boundingBox.minY) + ", " +
                            String.format("%.1f", mc.thePlayer.posZ),
                    3.0f,
                    (float) (event.getRes().getScaledHeight_double() - Fonts.axiforma.FONT_HEIGHT - 3.0 - yOffset),
                    color.getValue().getRGB()
            );
        }
    }

    private String formatModule(Module module) {
        String tag = module.getTag();
        if (module.getMetadata() != null) {
            tag += " " + EnumChatFormatting.GRAY + module.getMetadata();
        }
        return tag;
    }
}
