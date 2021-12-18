package com.pepedevs.dbedwars.configuration.configurable;

import com.pepedevs.corelib.utils.configuration.Loadable;
import com.pepedevs.corelib.utils.configuration.annotations.LoadableEntry;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigurableMessaging implements Loadable {

    @LoadableEntry(key = "parser-type")
    private String parserType;

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

    public String getParserType() {
        return parserType;
    }

    public static class ConfigurableModernSettings implements Loadable {

        private ConfigurableTransformations transformations;

        @LoadableEntry(key = "compulsory-closing-tags")
        private boolean strict;

        @LoadableEntry(key = "discord-type-formatting")
        private boolean discordFlavour;

        public ConfigurableModernSettings() {
            this.transformations = new ConfigurableTransformations();
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

        @Override
        public boolean isInvalid() {
            return false;
        }

        public ConfigurableTransformations getTransformations() {
            return transformations;
        }

        public boolean isStrict() {
            return strict;
        }

        public boolean isDiscordFlavour() {
            return discordFlavour;
        }

        public static class ConfigurableTransformations implements Loadable {

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

            @LoadableEntry(key = "pre")
            private boolean pre;

            @LoadableEntry(key = "rainbow")
            private boolean rainbow;

            @LoadableEntry(key = "reset")
            private boolean reset;

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

            public boolean isPre() {
                return pre;
            }

            public boolean isRainbow() {
                return rainbow;
            }

            public boolean isReset() {
                return reset;
            }

            public boolean isTranslatable() {
                return translatable;
            }
        }
    }

    public static class ConfigurableHistory implements Loadable {

        @LoadableEntry(key = "cache-time-seconds")
        private long cacheTime;

        @LoadableEntry(key = "max-messages")
        private int maxMessages;

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
        }

        @Override
        public boolean isInvalid() {
            return Loadable.super.isInvalid();
        }

        @Override
        public boolean isValid() {
            return false;
        }

        public int getMaxMessages() {
            return maxMessages;
        }

        public long getCacheTime() {
            return cacheTime;
        }
    }
}
