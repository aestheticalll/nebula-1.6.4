package nebula.client.module.impl.combat.burrow;

import nebula.client.Nebula;
import nebula.client.listener.bus.Listener;
import nebula.client.listener.bus.Subscribe;
import nebula.client.listener.event.net.EventPacket;
import nebula.client.listener.event.player.EventUpdate;
import nebula.client.module.Module;
import nebula.client.module.ModuleMeta;
import nebula.client.util.player.InventoryUtils;
import nebula.client.util.value.Setting;
import nebula.client.util.value.SettingMeta;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import static nebula.client.util.player.InventoryUtils.INVALID_INVENTORY;

/**
 * @author xgraza
 * @since 10/3/24
 */
@SuppressWarnings("unused")
@ModuleMeta(name = "Burrow", description = "")
public final class BurrowModule extends Module
{
  private static final double[] JUMP_HEIGHTS = { 0.42, 0.753, 1.0, 1.01 };

  @SettingMeta("Swing")
  private final Setting<Boolean> swingSetting = new Setting<>(true);
  @SettingMeta("Rotate")
  private final Setting<Boolean> rotateSetting = new Setting<>(true);
  @SettingMeta("Robot")
  private final Setting<Boolean> replaceSetting = new Setting<>(false);
  @SettingMeta("PosSpoof")
  private final Setting<Boolean> packetSetting = new Setting<>(false);

  @Subscribe
  private final Listener<EventUpdate> updateListener = event -> burrow();

  @Subscribe
  private final Listener<EventPacket.In> packetInListener = event ->
  {
    if (event.packet() instanceof S23PacketBlockChange packet && replaceSetting.value())
    {
      final Vec3 pos = Vec3.createVectorHelper(packet.getX(), packet.getY(), packet.getZ());
      if (!pos.equals(getPlayerPos()))
      {
        return;
      }
      burrow();
    }
  };

  private void burrow()
  {
    final int slot = InventoryUtils.hotbarBlockSearch(
        BlockObsidian.class, BlockEnderChest.class);
    if (slot == INVALID_INVENTORY)
    {
      macro().setEnabled(false);
      return;
    }

    final Vec3 pos = getPlayerPos();
    if (!replaceable(pos))
    {
      if (!replaceSetting.value())
      {
        macro().setEnabled(false);
      }
      return;
    }

    if (packetSetting.value())
    {
      for (final double position : JUMP_HEIGHTS)
      {
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
            pos.xCoord,
            pos.yCoord + position,
            mc.thePlayer.posY + position,
            pos.zCoord,
            false));
      }
    }

    mc.thePlayer.sendQueue.addToSendQueue(
        new C09PacketHeldItemChange(slot));

    EnumFacing placeface = EnumFacing.UP;
    Vec3 placePos = pos.addVector(0, -1, 0);

    final boolean result = mc.playerController.onPlayerRightClick(mc.thePlayer,
        mc.theWorld,
        mc.thePlayer.inventory.getStackInSlot(slot),
        (int) placePos.xCoord,
        (int) placePos.yCoord,
        (int) placePos.zCoord,
        placeface.order_a,
        pos.addVector(0.5, 0.5, 0.5));
    if (result)
    {
      if (swingSetting.value())
      {
        mc.thePlayer.swingItem();
      } else
      {
        mc.thePlayer.sendQueue.addToSendQueue(
            new C0APacketAnimation(mc.thePlayer, 1));
      }
    }
    Nebula.INSTANCE.inventory.sync();

    if (!replaceSetting.value())
    {
      macro().setEnabled(false);
    }
  }

  private Vec3 getPlayerPos()
  {
    return Vec3.createVectorHelper(
        Math.floor(mc.thePlayer.posX),
        mc.thePlayer.boundingBox.minY,
        Math.floor(mc.thePlayer.posZ));
  }

  private boolean replaceable(Vec3 pos)
  {
    return mc.theWorld.getBlock((int) pos.xCoord,
            (int) pos.yCoord,
            (int) pos.zCoord)
        .getMaterial()
        .isReplaceable();
  }
}
