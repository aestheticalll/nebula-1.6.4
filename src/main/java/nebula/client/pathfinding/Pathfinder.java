package nebula.client.pathfinding;

import nebula.client.util.chat.Printer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Vec3;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Gavin
 * @since 03/25/24
 */
public final class Pathfinder {

  private static final Minecraft mc = Minecraft.getMinecraft();

  private final PathfinderMovementInput input = new PathfinderMovementInput();

  private MovementInput defaultInput = null;
  private Position startPosition, goalPosition;
  private Node startNode, currentNode;

  public void find(final int x, final int y, final int z) {
    defaultInput = mc.thePlayer.movementInput;
    mc.thePlayer.movementInput = input;

    startPosition = getPosition();
    goalPosition = new Position(x, y, z);

    Printer.print("Starting position: " + startPosition);
    startNode = new Node(startPosition);
    currentNode = startNode;
    calcCost(currentNode);
  }

  public void stop() {
    mc.thePlayer.movementInput = defaultInput;
    defaultInput = null;
    input.reset();
    startNode = null;
    currentNode = null;
  }

  public void onUpdate() {
    if (startNode == null) {
      Printer.print("No start node?");
      return;
    }

    Node bestNode = currentNode;
    for (final EnumFacing facing : EnumFacing.values()) {
      if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
        continue;
      }
      final Position position = getPosition().offest(facing);
      final Node node = new Node(position);
      calcCost(node);
      if (node.f < bestNode.f && node.g >= bestNode.g) {
        Printer.print("Better node found");
        bestNode = node;
      }
    }

    if (bestNode.hashCode() != currentNode.hashCode()) {
      
    }
  }

  private void calcCost(final Node node) {
    node.g = Math.abs(getDistance(startPosition, goalPosition));
    node.h = Math.abs(getDistance(node.position, goalPosition));
    node.f = node.g + node.h;
  }

  private Position getPosition() {
    return new Position(MathHelper.floor_double(mc.thePlayer.posX), MathHelper.floor_double(mc.thePlayer.posY), MathHelper.floor_double(mc.thePlayer.posZ));
  }

  private double getDistance(final Position current, final Position ending) {
    double diffX = current.x() - ending.x();
    double diffY = current.y() - ending.y();
    double diffZ = current.z() - ending.z();
    return Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
  }
}
