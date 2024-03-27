package nebula.client.pathfinding;

import net.minecraft.util.MovementInput;

/**
 * @author Gavin
 * @since 03/25/24
 */
public final class PathfinderMovementInput extends MovementInput {

  private int forward = 0;
  private int strafe = 0;

  private boolean jumping, sneaking;

  public void forward() {
    forward = 1;
  }

  public void backward() {
    forward = -1;
  }

  public void right() {
    strafe = -1;
  }

  public void left() {
    strafe = 1;
  }

  public void stopMoving() {
    forward = 0;
    strafe = 0;
  }

  public void setJumping(boolean jumping) {
    this.jumping = jumping;
  }

  public boolean isJumping() {
    return jumping;
  }

  public void setSneaking(boolean sneaking) {
    this.sneaking = sneaking;
  }

  public boolean isSneaking() {
    return sneaking;
  }

  public void reset() {
    forward = 0;
    strafe = 0;
    jumping = false;
    sneaking = false;
  }
}
