package com.pepedevs.dbedwars.configuration;

import com.pepedevs.corelib.utils.configuration.Loadable;
import com.pepedevs.corelib.utils.configuration.annotations.LoadableEntry;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.exceptions.IllegalConfigException;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class MainConfiguration implements Loadable {

    private final DBedwars plugin;

    private final ArenaSection arenaSection;
    private final TrapSection trapSection;
    private final LangSection langSection;

    public MainConfiguration(DBedwars plugin) {
        this.plugin = plugin;
        this.arenaSection = new ArenaSection();
        this.trapSection = new TrapSection();
        this.langSection = new LangSection();
    }

    @Override
    public Loadable load(ConfigurationSection section) {
        this.arenaSection.load(section.getConfigurationSection("arena"));
        this.trapSection.load(section.getConfigurationSection("traps"));
        this.langSection.load(section.getConfigurationSection("lang"));
        return this;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

    public ArenaSection getArenaSection() {
        return this.arenaSection;
    }

    public TrapSection getTrapSection() {
        return this.trapSection;
    }

    public LangSection getLangSection() {
        return this.langSection;
    }

    public static class ArenaSection implements Loadable {

        @LoadableEntry(key = "start-timer")
        private int startTimer;

        @LoadableEntry(key = "respawn-delay")
        private int respawnTime;

        @LoadableEntry(key = "island-radius")
        private int islandRadius;

        @LoadableEntry(key = "min-y-axis")
        private int minYAxis;

        @LoadableEntry(key = "player-hit-tag-length")
        private int playerHitTagLength;

        @LoadableEntry(key = "game-end-delay")
        private int gameEndDelay;

        @LoadableEntry(key = "points.bed-destroy")
        private int bedDestroyPoint;

        @LoadableEntry(key = "points.kill")
        private int killPoint;

        @LoadableEntry(key = "points.final-kill")
        private int finalKillPoint;

        @LoadableEntry(key = "points.death")
        private int deathPoint;

        @LoadableEntry(key = "disable-hunger")
        private boolean disableHunger;

        public ArenaSection() {
            this.startTimer = -1;
            this.respawnTime = -1;
            this.islandRadius = -1;
            this.minYAxis = Integer.MIN_VALUE;
            this.playerHitTagLength = -1;
            this.gameEndDelay = -1;
            this.bedDestroyPoint = Integer.MIN_VALUE;
            this.killPoint = Integer.MIN_VALUE;
            this.finalKillPoint = Integer.MIN_VALUE;
            this.deathPoint = Integer.MIN_VALUE;
            this.disableHunger = true;
        }

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
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
            else if (this.bedDestroyPoint == Integer.MIN_VALUE)
                throw new IllegalConfigException(
                        "arena.points.bed-destroy", "Integer#MIN_VALUE", "config.yml");
            else if (this.killPoint == Integer.MIN_VALUE)
                throw new IllegalConfigException(
                        "arena.points.kill", "Integer#MIN_VALUE", "config.yml");
            else if (this.finalKillPoint == Integer.MIN_VALUE)
                throw new IllegalConfigException(
                        "arena.points.final-kill", "Integer#MIN_VALUE", "config.yml");
            else if (this.deathPoint == Integer.MIN_VALUE)
                throw new IllegalConfigException(
                        "arena.points.death", "Integer#MIN_VALUE", "config.yml");
            return true;
        }

        @Override
        public boolean isInvalid() {
            return !this.isValid();
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
    }

    public static class TrapSection implements Loadable {

        @LoadableEntry(key = "trap-queue.enabled")
        private boolean trapQueueEnabled;

        @LoadableEntry(key = "trap-queue.queue-limit")
        private int trapQueueLimit;

        @LoadableEntry(key = "trap-queue.queued-trap-cost")
        private List<String> queueCost;

        public TrapSection() {
            this.queueCost = new ArrayList<>();
        }

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
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

        @LoadableEntry(key = "parse-type")
        private String parseType;

        @LoadableEntry(key = "server-language")
        private String serverLanguage;

        private final LegacySettingsSection legacySettings;
        private final ModernSettingsSection modernSettings;

        public LangSection() {
            this.legacySettings = new LegacySettingsSection();
            this.modernSettings = new ModernSettingsSection();
        }

        @Override
        public Loadable load(ConfigurationSection section) {
            this.loadEntries(section);
            if (this.parseType.equalsIgnoreCase("legacy")) {
                this.legacySettings.load(section.getConfigurationSection("legacy-settings"));
            } else {
                this.modernSettings.load(section.getConfigurationSection("modern-settings"));
            }
            return this;
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

            @LoadableEntry(key = "translation-char")
            private String translationChar;

            private char character;

            @Override
            public Loadable load(ConfigurationSection section) {
                this.loadEntries(section);
                this.character = this.translationChar.charAt(0);
                return this;
            }

            @Override
            public boolean isValid() {
                return !this.translationChar.isEmpty();
            }

            public char getTranslationChar() {
                return this.character;
            }

        }

        public static class ModernSettingsSection implements Loadable {

            private final TransformationsSection transformations;

            @LoadableEntry(key = "discord-type-formatting")
            private boolean discordFlavour;

            public ModernSettingsSection() {
                this.transformations = new TransformationsSection();
            }

            @Override
            public Loadable load(ConfigurationSection section) {
                this.transformations.load(section.getConfigurationSection("enabled-transformations"));
                return this.loadEntries(section);
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

                @LoadableEntry(key = "click-event")
                private boolean clickEvent;

                @LoadableEntry(key = "color")
                private boolean color;

                @LoadableEntry(key = "decoration")
                private boolean decoration;

                @LoadableEntry(key = "font")
                private boolean font;

                @LoadableEntry(key = "gradient")
                private boolean gradient;

                @LoadableEntry(key = "hover-event")
                private boolean hoverEvent;

                @LoadableEntry(key = "insertion")
                private boolean insertion;

                @LoadableEntry(key = "keybind")
                private boolean keybind;

                @LoadableEntry(key = "rainbow")
                private boolean rainbow;

                @LoadableEntry(key = "translatable")
                private boolean translatable;

                @Override
                public Loadable load(ConfigurationSection section) {
                    return this.loadEntries(section);
                }

                @Override
                public boolean isValid() {
                    return false;
                }

                @Override
                public boolean isInvalid() {
                    return false;
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
