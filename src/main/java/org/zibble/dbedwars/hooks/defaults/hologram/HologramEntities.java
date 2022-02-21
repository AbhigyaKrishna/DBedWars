package org.zibble.dbedwars.hooks.defaults.hologram;

import com.pepedevs.radium.utils.version.Version;

public enum HologramEntities {

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
            if (version.isNewerEquals(Version.v1_9_R1)) return 1.9F;
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
    PIG_ZOMBIE {
        @Override
        public float height(Version version) {
            return 1.95F;
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
    ENDER_DRAGON {
        @Override
        public float height(Version version) {
            return 8.0F;
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
    ;

    private final float height;

    HologramEntities() {
        this.height = height(Version.getServerVersion());
    }

    public float getHeight() {
        return height;
    }

    public abstract float height(Version version);
}
