package org.zibble.dbedwars.configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.api.exceptions.IllegalConfigException;
import org.zibble.dbedwars.configuration.framework.Loadable;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;
import org.zibble.dbedwars.configuration.framework.annotations.Defaults;

import java.util.ArrayList;
import java.util.List;

public class MainConfiguration implements Loadable {

    @Defaults.Boolean(false)
    @ConfigPath
    private boolean debug;

    @Defaults.Boolean(false)
    @ConfigPath
    private boolean preciseLocation;

    @ConfigPath("arena")
    private ArenaSection arenaSection;

    @ConfigPath("respawn-items")
    private RespawnItemsSection respawnItemsSection;

    @ConfigPath("traps")
    private TrapSection trapSection;

    @ConfigPath("lang")
    private LangSection langSection;

    public MainConfiguration() {
    }

    @Override
    public void load(ConfigurationSection section) {
        this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isPreciseLocation() {
        return preciseLocation;
    }

    public ArenaSection getArenaSection() {
        return this.arenaSection;
    }

    public RespawnItemsSection getRespawnItemsSection() {
        return respawnItemsSection;
    }

    public TrapSection getTrapSection() {
        return this.trapSection;
    }

    public LangSection getLangSection() {
        return this.langSection;
    }

    public static class ArenaSection implements Loadable {

        @Defaults.Integer(-1)
        @ConfigPath("start-timer")
        private int startTimer;

        @Defaults.Integer(-1)
        @ConfigPath("respawn-delay")
        private int respawnTime;

        @Defaults.Integer(-1)
        @ConfigPath("island-radius")
        private int islandRadius;

        @Defaults.Integer(Integer.MIN_VALUE)
        @ConfigPath("min-y-axis")
        private int minYAxis;

        @Defaults.Integer(-1)
        @ConfigPath("player-hit-tag-length")
        private int playerHitTagLength;

        @Defaults.Integer(-1)
        @ConfigPath("game-end-delay")
        private int gameEndDelay;

        @Defaults.Integer(Integer.MIN_VALUE)
        @ConfigPath("points.bed-destroy")
        private int bedDestroyPoint;

        @Defaults.Integer(0)
        @ConfigPath("points.kill")
        private int killPoint;

        @Defaults.Integer(0)
        @ConfigPath("points.final-kill")
        private int finalKillPoint;

        @Defaults.Integer(0)
        @ConfigPath("points.death")
        private int deathPoint;

        @Defaults.Boolean(true)
        @ConfigPath("disable-hunger")
        private boolean disableHunger;

        @ConfigPath("game-lobby-scoreboard")
        private String gameLobbyScoreboard;

        public ArenaSection() {
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            if (this.startTimer <= -1)
                throw new IllegalConfigException("arena.start-timer", "lower than 0", "config.yml");
            else if (this.respawnTime <= -1)
                throw new IllegalConfigException(
                        "arena.respawn-delay", "lower than 0", "config.yml");
            else if (this.islandRadius <= -1)
                throw new IllegalConfigException(
                        "arena.island-radius", "lower than 0", "config.yml");
            else if (this.minYAxis == Integer.MIN_VALUE)
                throw new IllegalConfigException(
                        "arena.min-y-axis", "Integer#MIN_VALUE", "config.yml");
            else if (this.playerHitTagLength <= -1)
                throw new IllegalConfigException(
                        "arena.player-hit-tag-length", "lower than 0", "config.yml");
            else if (this.gameEndDelay <= -1)
                throw new IllegalConfigException(
                        "arena.game-end-delay", "lower than 0", "config.yml");
            return true;
        }

        public int getStartTimer() {
            return startTimer;
        }

        public int getRespawnTime() {
            return respawnTime;
        }

        public int getIslandRadius() {
            return islandRadius;
        }

        public int getMinYAxis() {
            return minYAxis;
        }

        public int getPlayerHitTagLength() {
            return playerHitTagLength;
        }

        public int getGameEndDelay() {
            return gameEndDelay;
        }

        public int getBedDestroyPoint() {
            return bedDestroyPoint;
        }

        public int getKillPoint() {
            return killPoint;
        }

        public int getFinalKillPoint() {
            return finalKillPoint;
        }

        public int getDeathPoint() {
            return deathPoint;
        }

        public boolean isDisableHunger() {
            return disableHunger;
        }

        public String getGameLobbyScoreboard() {
            return gameLobbyScoreboard;
        }

    }

    public static class RespawnItemsSection implements Loadable {

        @ConfigPath
        private List<String> inventory;

        @ConfigPath
        private String helmet;

        @ConfigPath
        private String chestplate;

        @ConfigPath
        private String leggings;

        @ConfigPath
        private String boots;

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        public List<String> getInventory() {
            return inventory;
        }

        public String getHelmet() {
            return helmet;
        }

        public String getChestplate() {
            return chestplate;
        }

        public String getLeggings() {
            return leggings;
        }

        public String getBoots() {
            return boots;
        }

    }

    public static class TrapSection implements Loadable {

        @Defaults.Integer(5)
        @ConfigPath
        private int trapTriggerMeantime;

        @ConfigPath("trap-queue.enabled")
        private boolean trapQueueEnabled;

        @ConfigPath("trap-queue.queue-limit")
        private int trapQueueLimit;

        @ConfigPath("trap-queue.queued-trap-cost")
        private List<String> queueCost;

        public TrapSection() {
            this.queueCost = new ArrayList<>();
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        public int getTrapTriggerMeantime() {
            return trapTriggerMeantime;
        }

        public boolean isTrapQueueEnabled() {
            return this.trapQueueEnabled;
        }

        public int getTrapQueueLimit() {
            return this.trapQueueLimit;
        }

        public List<String> getQueueCost() {
            return this.queueCost;
        }

    }

    public static class LangSection implements Loadable {

        private final LegacySettingsSection legacySettings;
        private final ModernSettingsSection modernSettings;
        @Defaults.String("modern")
        @ConfigPath("parse-type")
        private String parseType;
        @Defaults.String("en_US")
        @ConfigPath("server-language")
        private String serverLanguage;

        public LangSection() {
            this.legacySettings = new LegacySettingsSection();
            this.modernSettings = new ModernSettingsSection();
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
            if (this.parseType.equalsIgnoreCase("legacy")) {
                this.legacySettings.load(section.getConfigurationSection("legacy-settings"));
            } else {
                this.modernSettings.load(section.getConfigurationSection("modern-settings"));
            }
        }

        @Override
        public boolean isValid() {
            return true;
        }

        public String getParseType() {
            return parseType;
        }

        public String getServerLanguage() {
            return serverLanguage;
        }

        public LegacySettingsSection getLegacySettings() {
            return legacySettings;
        }

        public ModernSettingsSection getModernSettings() {
            return modernSettings;
        }

        public static class LegacySettingsSection implements Loadable {

            @Defaults.Character('&')
            @ConfigPath("translation-char")
            private char translationChar;

            private char character;

            @Override
            public void load(ConfigurationSection section) {
                this.loadEntries(section);
            }

            @Override
            public boolean isValid() {
                return true;
            }

            public char getTranslationChar() {
                return this.character;
            }

        }

        public static class ModernSettingsSection implements Loadable {

            @ConfigPath("enabled-transformations")
            private final TransformationsSection transformations;

            @ConfigPath("discord-type-formatting")
            private boolean discordFlavour;

            public ModernSettingsSection() {
                this.transformations = new TransformationsSection();
            }

            @Override
            public void load(ConfigurationSection section) {
                this.transformations.load(section.getConfigurationSection("enabled-transformations"));
                this.loadEntries(section);
            }

            @Override
            public boolean isValid() {
                return true;
            }

            public TransformationsSection getTransformations() {
                return transformations;
            }

            public boolean isDiscordFlavour() {
                return discordFlavour;
            }

            public static class TransformationsSection implements Loadable {

                @ConfigPath("click-event")
                private boolean clickEvent;

                @ConfigPath("color")
                private boolean color;

                @ConfigPath("decoration")
                private boolean decoration;

                @ConfigPath("font")
                private boolean font;

                @ConfigPath("gradient")
                private boolean gradient;

                @ConfigPath("hover-event")
                private boolean hoverEvent;

                @ConfigPath("insertion")
                private boolean insertion;

                @ConfigPath("keybind")
                private boolean keybind;

                @ConfigPath("rainbow")
                private boolean rainbow;

                @ConfigPath("translatable")
                private boolean translatable;

                @Override
                public void load(ConfigurationSection section) {
                    this.loadEntries(section);
                }

                @Override
                public boolean isValid() {
                    return true;
                }

                public boolean isClickEvent() {
                    return clickEvent;
                }

                public boolean isColor() {
                    return color;
                }

                public boolean isDecoration() {
                    return decoration;
                }

                public boolean isFont() {
                    return font;
                }

                public boolean isGradient() {
                    return gradient;
                }

                public boolean isHoverEvent() {
                    return hoverEvent;
                }

                public boolean isInsertion() {
                    return insertion;
                }

                public boolean isKeybind() {
                    return keybind;
                }

                public boolean isRainbow() {
                    return rainbow;
                }

                public boolean isTranslatable() {
                    return translatable;
                }

            }

        }

    }

}
