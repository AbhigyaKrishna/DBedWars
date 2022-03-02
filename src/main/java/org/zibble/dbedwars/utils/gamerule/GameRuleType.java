package org.zibble.dbedwars.utils.gamerule;

import org.apache.commons.lang.Validate;
import org.bukkit.World;
import org.bukkit.metadata.MetadataValue;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.utils.reflection.DataType;

public enum GameRuleType {
    FIRE_TICK {
        public String getName() {
            return "doFireTick";
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return true;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    MOB_GRIEFING {
        public String getName() {
            return "mobGriefing";
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return true;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    KEEP_INVENTORY {
        public String getName() {
            return "keepInventory";
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return false;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    MOB_SPAWNING {
        public String getName() {
            return "doMobSpawning";
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return true;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    MOB_LOOT {
        public String getName() {
            return "doMobLoot";
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return true;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    TILE_DROPS {
        public String getName() {
            return "doTileDrops";
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return true;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    ENTITY_DROPS {
        public String getName() {
            return "doEntityDrops";
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return true;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    COMMAND_BLOCK_OUTPUT {
        public String getName() {
            return "commandBlockOutput";
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return true;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    NATURAL_REGENERATION {
        public String getName() {
            return "naturalRegeneration";
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return true;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    DAYLIGHT_CYCLE {
        public String getName() {
            return "doDaylightCycle";
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return true;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    LOG_ADMIN_COMMANDS {
        public String getName() {
            return "logAdminCommands";
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return true;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    SHOW_DEATH_MESSAGES {
        public String getName() {
            return "showDeathMessages";
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return true;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    RANDOM_TICK_SPEED {
        public String getName() {
            return "randomTickSpeed";
        }

        public boolean isBooleanValue() {
            return false;
        }

        public boolean isNumericalValue() {
            return true;
        }

        public int getDefaultNumericalValue() {
            return 3;
        }
    },

    SEND_COMMAND_FEEDBACK {
        public String getName() {
            return "sendCommandFeedback";
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return true;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    REDUCED_DEBUG_INFO {
        public String getName() {
            return "reducedDebugInfo";
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return false;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    SPECTATORS_GENERATE_CHUNKS {
        public String getName() {
            return "spectatorsGenerateChunks";
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return true;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    SPAWN_RADIUS {
        public String getName() {
            return "randomTickSpeed";
        }

        public boolean isBooleanValue() {
            return false;
        }

        public boolean isNumericalValue() {
            return true;
        }

        public int getDefaultNumericalValue() {
            return 10;
        }
    },

    DISABLE_ELYTRA_MOVEMENT_CHECK {
        public String getName() {
            return "disableElytraMovementCheck";
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return false;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    DISABLE_BLOCK_PLACING {
        public String getName() {
            return "disableBlockPlacing";
        }

        public GameRulePresentationMode getPresentationMode() {
            return GameRulePresentationMode.METADATA;
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return false;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    DISABLE_BLOCK_BREAKING {
        public String getName() {
            return "disableBlockBreaking";
        }

        public GameRulePresentationMode getPresentationMode() {
            return GameRulePresentationMode.METADATA;
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return false;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    INVULNERABLE_ENTITIES {
        public String getName() {
            return "invulnerableEntities";
        }

        public GameRulePresentationMode getPresentationMode() {
            return GameRulePresentationMode.METADATA;
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return false;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    DISALLOW_PVP {
        public String getName() {
            return "disallowPvP";
        }

        public GameRulePresentationMode getPresentationMode() {
            return GameRulePresentationMode.METADATA;
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return false;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },

    DISABLE_HUNGER {
        public String getName() {
            return "disableHunger";
        }

        public GameRulePresentationMode getPresentationMode() {
            return GameRulePresentationMode.METADATA;
        }

        public boolean isBooleanValue() {
            return true;
        }

        public boolean getDefaultBooleanValue() {
            return false;
        }

        public boolean isNumericalValue() {
            return false;
        }
    },
    ;

    public String getName() {
        throw new UnsupportedOperationException();
    }

    public DataType getDataType() {
        return isBooleanValue() ? DataType.BOOLEAN : DataType.INTEGER;
    }

    public GameRulePresentationMode getPresentationMode() {
        return GameRulePresentationMode.ORDINARY;
    }

    @SuppressWarnings("deprecation")
    public Object getValue(World world) {
        if (!isPresent(world)) {
            return null;
        }

        if (getPresentationMode() == GameRulePresentationMode.ORDINARY) {
            try {
                // this class was implemented on spigot 1.14.
                Class.forName("org.bukkit.GameRule");
                return world.getGameRuleValue(org.bukkit.GameRule.getByName(this.getName()));
            } catch (ClassNotFoundException | NoClassDefFoundError ex) {
                // deprecated on 1.14, but works for older versions.
                return world.getGameRuleValue(this.getName());
            }
        } else {
            return getGameRuleMetadata(world) != null ? getGameRuleMetadata(world).value() : null;
        }
    }

    public GameRuleMetadata getGameRuleMetadata(World world) {
        Validate.isTrue(this.getPresentationMode() == GameRulePresentationMode.METADATA,
                "Wrong presentation mode!");

        for (MetadataValue metadata : world.getMetadata(this.getName())) {
            if (metadata instanceof GameRuleMetadata && ((GameRuleMetadata) metadata).getType() == this) {
                return (GameRuleMetadata) metadata;
            }
        }
        return null;
    }

    public boolean isPresent(World world) {
        return this.getPresentationMode() != GameRulePresentationMode.METADATA || this.getGameRuleMetadata(world) != null;
    }

    public boolean isSameDataType(Object value) {
        return value != null && (this.getDataType() == DataType.fromClass(value.getClass()));
    }

    public boolean isBooleanValue() {
        throw new UnsupportedOperationException();
    }

    public boolean getDefaultBooleanValue() {
        throw new IllegalStateException("Wrong value type!");
    }

    public boolean isNumericalValue() {
        throw new UnsupportedOperationException();
    }

    public int getDefaultNumericalValue() {
        throw new IllegalStateException("Wrong value type!");
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    public void apply(World world, Object value) {
        Validate.isTrue(isSameDataType(value), "wrong value type!");

        // ordinary presentation mode
        if (getPresentationMode() == GameRulePresentationMode.ORDINARY) {
            try {
                // this class was implemented on spigot 1.14.
                Class.forName("org.bukkit.GameRule");
                switch (getDataType()) {
                    case BOOLEAN:
                    default:
                        world.setGameRule((org.bukkit.GameRule<Boolean>) org.bukkit.GameRule.getByName(getName()), (Boolean) value);
                        break;
                    case INTEGER:
                        world.setGameRule((org.bukkit.GameRule<Integer>) org.bukkit.GameRule.getByName(getName()), (Integer) value);
                        break;
                }
            } catch (ClassNotFoundException ex) {
                // deprecated on 1.14, but works for older versions.
                world.setGameRuleValue(getName(), value.toString());
            }
        } else { // metadata presentation mode
            world.setMetadata(this.getName(), new GameRuleMetadata(DBedwars.getInstance(), this, value));
        }
    }
}
