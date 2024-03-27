package nebula.client.pathfinding;

import net.minecraft.util.EnumFacing;

public record Position(int x, int y, int z) {

  public Position offest(EnumFacing facing) {
    return new Position(
        x + facing.getFrontOffsetX(),
        y + facing.getFrontOffsetY(),
        z + facing.getFrontOffsetZ());
  }
}
