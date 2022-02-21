package org.zibble.dbedwars.hooks.defaults.hologram;

import com.pepedevs.radium.utils.version.Version;

public enum HologramEntities {

    CREEPER {

        @Override
        public float getHeight(Version version) {
        
        }

    },
    SKELETON(1.8F),
    SPIDER(0.9F),
    GIANT(10.8F),
    ZOMBIE(1.95F),
    SLIME(1.02F),
    GHAST(4.0F),
    PIG_ZOMBIE(1.95F),
    ENDERMAN(3.0F),
    CAVE_SPIDER(1.8F),
    SILVERFISH(0.3F),
    BLAZE(1.8F),
    MAGMA_CUBE(0.51F),
    ENDER_DRAGON(8.0F),
    WITHER(3.5F),
    BAT(0.9F),
    WITCH(1.95F),
    ENDERMITE(0.3F),
    GUARDIAN(0.85F),
    PIG(0.9F),
    SHEEP(1.3F),
    COW(1.3F),
    CHICKEN(0.7F),
    SQUID(0.95F),
    WOLF(0.8F),
    MUSHROOM_COW(1.3F),
    SNOWMAN(1.9F),
    OCELOT(0.7F),
    IRON_GOLEM(2.9F),
    HORSE(1.8F),
    RABBIT(0.7F),
    VILLAGER(1.8F),
    ;

    private final float height;

    HologramEntities(float height) {
        this.height = height;
    }

    public abstract float getHeight(Version version);
}
