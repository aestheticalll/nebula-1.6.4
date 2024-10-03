package nebula.client.module.impl.combat.aura;

import nebula.client.Nebula;
import nebula.client.listener.bus.Listener;
import nebula.client.listener.bus.Subscribe;
import nebula.client.listener.event.EventStage;
import nebula.client.listener.event.player.EventMove;
import nebula.client.listener.event.player.EventMoveUpdate;
import nebula.client.listener.event.player.EventUpdate;
import nebula.client.listener.event.render.EventRenderHeldItem;
import nebula.client.module.Module;
import nebula.client.module.ModuleMeta;
import nebula.client.rotate.Rotation;
import nebula.client.util.math.AngleUtils;
import nebula.client.util.value.Setting;
import nebula.client.util.value.SettingMeta;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

import java.util.Comparator;
import java.util.List;

import static net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK;

/**
 * @author Gavin
 * @since 03/27/24
 */
@ModuleMeta(name = "Aura",
    description = "Attacks entities in your aura")
public final class AuraModule extends Module
{

  private static final int AURA_ROTATION_PRIORITY = 20;

  @SettingMeta("Mode")
  private final Setting<AuraMode> mode = new Setting<>(
      AuraMode.SWITCH);
  @SettingMeta("Range")
  private final Setting<Double> range = new Setting<>(
      4.0, 1.0, 6.0, 0.1);
  @SettingMeta("Through Walls")
  private final Setting<Boolean> walls = new Setting<>(
      true);
  @SettingMeta("Rotate")
  private final Setting<Boolean> rotate = new Setting<>(
      true);
  @SettingMeta("Auto Block")
  private final Setting<Boolean> autoBlock = new Setting<>(
      true);
  @SettingMeta("Keep Sprint")
  private final Setting<Boolean> keepSprint = new Setting<>(
      true);
  @SettingMeta("Auto Disable")
  private final Setting<Boolean> autoDisable = new Setting<>(
      true);

  private EntityLivingBase target;
  private boolean blockState;

  @Subscribe
  private final Listener<EventMoveUpdate> moveUpdateListener = event ->
  {

    // safety so we dont crash :skull:
    if (mc.thePlayer.ticksExisted < 5)
    {
      target = null;
      blockState = false;

      if (autoDisable.value())
      {
        macro().setEnabled(false);
      }

      return;
    }

    if (!isValidTarget(target) || mode.value() == AuraMode.SWITCH)
    {
      target = searchForTarget();
    }

    if (target == null)
    {

      if (blockState && isHoldingSword())
      {
        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
            5, 0, 0, 0, 255));
      }

      blockState = false;
      return;
    }

    if (rotate.value())
    {
      Nebula.INSTANCE.rotation.submit(Rotation.from(
          AngleUtils.entity(target),
          AURA_ROTATION_PRIORITY,
          5));
    }

    if (event.stage() == EventStage.PRE)
    {
      if (isHoldingSword() && blockState && autoBlock.value())
      {
        blockState = false;
        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
            5, 0, 0, 0, 255));
      }

      // I have tested this aura against 2.0.0 (which has the best Aura out of all the Nebula versions..)
      // It fucking DESTROYED 2.0.0, and fucked its armor quicker in similar conditions on a LAN world
      // This aura is goated
      mc.thePlayer.swingItem();
      if (keepSprint.value())
      {
        mc.thePlayer.sendQueue.addToSendQueue(
            new C02PacketUseEntity(target, ATTACK));
      } else
      {
        mc.playerController.attackEntity(mc.thePlayer, target);
      }

    } else
    {
      if (isHoldingSword() && !blockState && autoBlock.value())
      {
        blockState = true;
        mc.thePlayer.sendQueue.addToSendQueue(
            new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
      }
    }
  };

  @Subscribe
  private final Listener<EventRenderHeldItem> renderHeldItemListener = event ->
  {
    if (autoBlock.value() && target != null && event.getStack().getItem() instanceof ItemSword)
    {
      event.setAction(blockState ? EnumAction.block : EnumAction.none);
      event.setCanceled(true);
    }
  };

  @Override
  public void disable()
  {
    super.disable();

    if (mc.thePlayer != null && blockState && isHoldingSword())
    {
      mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
          5, 0, 0, 0, 255));
    }

    blockState = false;
    target = null;

    Nebula.INSTANCE.rotation.reset(AURA_ROTATION_PRIORITY);
  }

  private EntityLivingBase searchForTarget()
  {
    return (EntityLivingBase) ((List<Entity>) mc.theWorld.loadedEntityList)
        .stream()
        .filter((entity) -> entity instanceof EntityLivingBase && !entity.equals(mc.thePlayer))
        .filter((entity) -> isValidTarget((EntityLivingBase) entity))
        .min(Comparator.comparingDouble((entity) -> ((EntityLivingBase) entity).hurtTime))
        .orElse(null);
  }

  private boolean isValidTarget(final EntityLivingBase entity)
  {

    if (entity == null || entity.isDead || entity.getHealth() <= 0.0f || entity.equals(mc.thePlayer))
    {
      return false;
    }

    if (!walls.value() && !mc.thePlayer.canEntityBeSeen(entity))
    {
      return false;
    }

    final double distance = mc.thePlayer.getDistanceSqToEntity(entity);
    return distance <= Math.pow(range.value(), 2);
  }

  private boolean isHoldingSword()
  {
    final ItemStack itemStack = mc.thePlayer.getHeldItem();
    return itemStack != null && itemStack.getItem() instanceof ItemSword;
  }
}
