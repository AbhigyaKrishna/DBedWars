package org.zibble.dbedwars.api.hooks.hologram;

import com.pepedevs.radium.utils.version.Version;

public enum HologramEntityType {

    CREEPER {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_9_R1)) return 1.7F;
            return 1.8F;
        }
    },
    SKELETON {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_11_R1)) return 1.99F;
            return 1.8F;
        }
    },
    SPIDER {
        @Override
        public float height(Version version) {
            return 0.9F;
        }
    },
    GIANT {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_14_R1)) return 12F;
            return 10.8F;
        }
    },
    ZOMBIE {
        @Override
        public float height(Version version) {
            return 1.95F;
        }
    },
    GHAST {
        @Override
        public float height(Version version) {
            return 4.0F;
        }
    },
    ENDERMAN {
        @Override
        public float height(Version version) {
            return 2.9F;
        }
    },
    CAVE_SPIDER {
        @Override
        public float height(Version version) {
            return 0.5F;
        }
    },
    SILVERFISH {
        @Override
        public float height(Version version) {
            return 0.3F;
        }
    },
    BLAZE {
        @Override
        public float height(Version version) {
            return 1.8F;
        }
    },
    WITHER {
        @Override
        public float height(Version version) {
            return 3.5F;
        }
    },
    BAT {
        @Override
        public float height(Version version) {
            return 0.9F;
        }
    },
    WITCH {
        @Override
        public float height(Version version) {
            return 1.95F;
        }
    },
    ENDERMITE {
        @Override
        public float height(Version version) {
            return 0.3F;
        }
    },
    GUARDIAN {
        @Override
        public float height(Version version) {
            return 0.85F;
        }
    },
    ELDER_GUARDIAN {
        @Override
        public float height(Version version) {
            return 2.0F;
        }
    },
    PIG {
        @Override
        public float height(Version version) {
            return 0.9F;
        }
    },
    SHEEP {
        @Override
        public float height(Version version) {
            return 1.3F;
        }
    },
    COW {
        @Override
        public float height(Version version) {
            return 1.3F;
        }
    },
    CHICKEN {
        @Override
        public float height(Version version) {
            return 0.7F;
        }
    },
    SQUID {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_9_R1)) return 0.8F;
            return 0.95F;
        }
    },
    WOLF {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_9_R1)) return 0.95F;
            return 0.8F;
        }
    },
    MUSHROOM_COW {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_9_R1)) return 1.4F;
            return 1.3F;
        }
    },
    SNOWMAN {
        @Override
        public float height(Version version) {
            return 1.9F;
        }
    },
    OCELOT {
        @Override
        public float height(Version version) {
            return 0.7F;
        }
    },
    IRON_GOLEM {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_9_R1)) return 2.7F;
            return 2.9F;
        }
    },
    HORSE {
        @Override
        public float height(Version version) {
            return 1.6F;
        }
    },
    RABBIT {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_9_R1)) return 0.5F;
            return 0.7F;
        }
    },
    VILLAGER {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_9_R1)) return 1.95F;
            return 1.8F;
        }
    },
    POLAR_BEAR {
        @Override
        public float height(Version version) {
            return 1.4F;
        }
    },
    WITHER_SKELETON {
        @Override
        public float height(Version version) {
            return 2.4F;
        }
    },
    HUSK {
        @Override
        public float height(Version version) {
            return 1.95F;
        }
    },
    VINDICATOR {
        @Override
        public float height(Version version) {
            return 1.95F;
        }
    },
    EVOKER {
        @Override
        public float height(Version version) {
            return 1.95F;
        }
    },
    LLAMA {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_13_R1)) return 1.87F;
            return 1.6F;
        }
    },
    ZOMBIE_VILLAGER {
        @Override
        public float height(Version version) {
            return 1.95F;
        }
    },

    BABY_PIG {
        @Override
        public float height(Version version) {
            return 0.45F;
        }
    },
    BABY_SHEEP {
        @Override
        public float height(Version version) {
            return 0.65F;
        }
    },
    BABY_COW {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_9_R1)) return 0.7F;
            return 0.65F;
        }
    },
    BABY_CHICKEN {
        @Override
        public float height(Version version) {
            return 0.35F;
        }
    },
    BABY_WOLF {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_9_R1)) return 0.4025F; //FUCK U MOJANG
            return 0.4F;
        }
    },
    BABY_MUSHROOM_COW {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_9_R1)) return 0.7F;
            return 0.65F;
        }
    },
    BABY_OCELOT {
        @Override
        public float height(Version version) {
            return 0;
        }
    },
    BABY_HORSE {
        @Override
        public float height(Version version) {
            return 0.8F;
        }
    },
    BABY_RABBIT {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_9_R1)) return 0.25F;
            return 0.35F;
        }
    },
    BABY_VILLAGER {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_9_R1)) return 0.9075F; //FUCK U MOJANG
            return 0.9F;
        }
    },
    BABY_POLAR_BEAR {
        @Override
        public float height(Version version) {
            return 0.7F;
        }
    },
    DONKEY {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_14_R1)) return 1.6F;
            return 1.5F;
        }
    },
    BABY_DONKEY {
        @Override
        public float height(Version version) {
            if (version.isNewerEquals(Version.v1_14_R1)) return 0.8F;
            return 0.795F;
        }
    },
    MULE {
        @Override
        public float height(Version version) {
            return 1.6F;
        }
    },
    BABY_MULE {
        @Override
        public float height(Version version) {
            return 0.8F;
        }
    },
    SKELETON_HORSE {
        @Override
        public float height(Version version) {
            return 1.6F;
        }
    },
    BABY_SKELETON_HORSE {
        @Override
        public float height(Version version) {
            return 0.8F;
        }
    },
    VEX {
        @Override
        public float height(Version version) {
            return 0.8F;
        }
    },
    BABY_LLAMA {
        @Override
        public float height(Version version) {
            return 0.935F;
        }
    },
    TRADER_LLAMA {
        @Override
        public float height(Version version) {
            return 1.87F;
        }
    },
    BABY_TRADER_LLAMA {
        @Override
        public float height(Version version) {
            return 0.935F;
        }
    },
    ILLUSIONER {
        @Override
        public float height(Version version) {
            return 0;
        }
    },
    PARROT {
        @Override
        public float height(Version version) {
            return 0.9F;
        }
    },
    BABY_PARROT {
        @Override
        public float height(Version version) {
            return 0.45F;
        }
    },
    DROWNED {
        @Override
        public float height(Version version) {
            return 1.95F;
        }
    },
    BABY_DROWNED {
        @Override
        public float height(Version version) {
            return 0.975F;
        }
    },
    SALMON {
        @Override
        public float height(Version version) {
            return 0.4F;
        }
    },
    COD {
        @Override
        public float height(Version version) {
            return 0.3F;
        }
    },
    PHANTOM {
        @Override
        public float height(Version version) {
            return 0.5F;
        }
    },
    TURTLE {
        @Override
        public float height(Version version) {
            return 0.4F;
        }
    },
    BABY_TURTLE {
        @Override
        public float height(Version version) {
            return 0.12F;
        }
    },
    TROPICAL_FISH {
        @Override
        public float height(Version version) {
            return 0.4F;
        }
    },
    PUFFERFISH {
        @Override
        public float height(Version version) {
            return 0.35F;
        }
    },
    DOLPHIN {
        @Override
        public float height(Version version) {
            return 0.6F;
        }
    },
    PANDA {
        @Override
        public float height(Version version) {
            return 1.25F;
        }
    },
    PANDA_BABY {
        @Override
        public float height(Version version) {
            return 0.625F;
        }
    },
    PILLAGER {
        @Override
        public float height(Version version) {
            return 1.95F;
        }
    },
    RAVAGER {
        @Override
        public float height(Version version) {
            return 2.2F;
        }
    },
    WANDERING_TRADER {
        @Override
        public float height(Version version) {
            return 1.95F;
        }
    },
    FOX {
        @Override
        public float height(Version version) {
            return 0.7F;
        }
    },
    BABY_FOX {
        @Override
        public float height(Version version) {
            return 0.35F;
        }
    },
    CAT {
        @Override
        public float height(Version version) {
            return 0.7F;
        }
    },
    BABY_CAT {
        @Override
        public float height(Version version) {
            return 0.35F;
        }
    },
    STRIDER {
        @Override
        public float height(Version version) {
            return 1.7F;
        }
    },
    BABY_STRIDER {
        @Override
        public float height(Version version) {
            return 0.85F;
        }
    },
    HOGLIN {
        @Override
        public float height(Version version) {
            return 1.4F;
        }
    },
    BABY_HOGLIN {
        @Override
        public float height(Version version) {
            return 0.7F;
        }
    },
    ZOGLIN {
        @Override
        public float height(Version version) {
            return 1.4F;
        }
    },
    BABY_ZOGLIN {
        @Override
        public float height(Version version) {
            return 0.7F;
        }
    },
    ZOMBIFIED_PIGLIN {
        @Override
        public float height(Version version) {
            return 1.95F;
        }
    },
    PIGLIN {
        @Override
        public float height(Version version) {
            return 1.95F;
        }
    },
    PIGLIN_BRUTE {
        @Override
        public float height(Version version) {
            return 1.95F;
        }
    },
    BABY_ZOMBIFIED_PIGLIN {
        @Override
        public float height(Version version) {
            return 0.975F;
        }
    },
    BABY_PIGLIN {
        @Override
        public float height(Version version) {
            return 0.975F;
        }
    },
    BABY_ZOMBIE_VILLAGER {
        @Override
        public float height(Version version) {
            return 0.975F;
        }
    },
    GOAT {
        @Override
        public float height(Version version) {
            return 1.3F;
        }
    },
    BABY_GOAT {
        @Override
        public float height(Version version) {
            return 0.65F;
        }
    },
    AXOLOTL {
        @Override
        public float height(Version version) {
            return 0.42F;
        }
    },
    BABY_AXOLOTL {
        @Override
        public float height(Version version) {
            return 0.21F;
        }
    },
    GLOW_SQUID {
        @Override
        public float height(Version version) {
            return 0.8F;
        }
    };

    private final float height;

    HologramEntityType() {
        this.height = height(Version.SERVER_VERSION);
    }

    public float getHeight() {
        return height;
    }

    protected abstract float height(Version version);
}
