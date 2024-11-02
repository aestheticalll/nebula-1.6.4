package nebula.client.api.module;

/**
 * @author Gavin
 * @since 08/09/23
 */
public enum ModuleCategory
{
  COMBAT("Combat", "a"),
  EXPLOIT("Exploit", "b"),
  MOVEMENT("Movement", "E"),
  PLAYER("Player", "d"),
  RENDER("Render", "c");

  private final String displayName, icon;

  ModuleCategory(String displayName, String icon) {
    this.displayName = displayName;
    this.icon = icon;
  }

  public String display() {
    return displayName;
  }

  public String icon() {
    return icon;
  }
}
