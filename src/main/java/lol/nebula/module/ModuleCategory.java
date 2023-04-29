package lol.nebula.module;

/**
 * @author aesthetical
 * @since 04/27/23
 */
public enum ModuleCategory {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    VISUAL("Visual");

    private final String display;

    ModuleCategory(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}