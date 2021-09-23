package me.abhigya.dbedwars.game.arena.view;

public enum AttributeType {

    PURCHASABLE("cost", "item"),
    PERMANENT(),
    UPGRADEABLE("previous-slot"),
    AUTO_EQUIP("item:auto-equip"),
    UPGRADEABLE_TIER("next-tier"),
    DOWNGRADEABLE_TIER("previous-tier", "downgrade-event"),
    CHANGE_PAGE("page"),
    COMMAND("command"),
    ;

    public static final AttributeType[] values = values();

    private String[] configKeys;

    AttributeType(String... configKeys) {
        this.configKeys = configKeys;
    }

    public String[] getConfigKeys() {
        return this.configKeys;
    }

    public static AttributeType matchAttribute(String str) {
        for (AttributeType t : values) {
            if (t.name().equalsIgnoreCase(str) || t.name().replace("_", " ").equalsIgnoreCase(str) ||
                    t.name().replace("_", "-").equalsIgnoreCase(str)) {
                return t;
            }
        }

        return null;
    }

}