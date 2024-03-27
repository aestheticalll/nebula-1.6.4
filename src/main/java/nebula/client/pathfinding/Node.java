package nebula.client.pathfinding;

/**
 * @author Gavin
 * @since 03/25/24
 */
public class Node {

  public Node parent;

  public final Position position;

  /** F, G, and H costs */
  // gcost = distance between this node & start node
  // hcost = distance between this node & end node
  // fcost = sum of g & h costs
  public double f, g, h;

  public Node(Position position) {
    this.position = position;
  }
}
