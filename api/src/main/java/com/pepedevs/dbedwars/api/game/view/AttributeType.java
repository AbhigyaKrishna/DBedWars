package com.pepedevs.dbedwars.api.game.view;

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

    public static final AttributeType[] VALUES = values();

    private final String[] keys;

    AttributeType(String... keys) {
        this.keys = keys;
    }

    public String[] getKeys() {
        return this.keys;
    }
}
