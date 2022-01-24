package com.pepedevs.dbedwars.configuration.configurable;

import com.pepedevs.radium.utils.configuration.Loadable;
import com.pepedevs.radium.utils.configuration.annotations.LoadableCollectionEntry;
import com.pepedevs.radium.utils.configuration.annotations.LoadableEntry;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ConfigurableParticleImages implements Loadable {

    private ConfigurableParticleImagesGlobalSettings globalSettings;

    @LoadableCollectionEntry(subsection = "images")
    private Map<String, ConfigurableParticleImageSettings> imageSettings;

    public ConfigurableParticleImages() {
        this.globalSettings = new ConfigurableParticleImagesGlobalSettings();
        this.imageSettings = new HashMap<>();
    }

    @Override
    public Loadable load(ConfigurationSection section) {
        this.globalSettings.load(section.getConfigurationSection("global-settings"));
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

    public ConfigurableParticleImagesGlobalSettings getGlobalSettings() {
        return globalSettings;
    }

    public Map<String, ConfigurableParticleImageSettings> getImageSettings() {
        return imageSettings;
    }

    public static class ConfigurableParticleImagesGlobalSettings implements Loadable {

        @LoadableEntry(key = "downscaling")
        private String downscaling;

        public ConfigurableParticleImagesGlobalSettings() {
            this.downscaling = "50x50";
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

        public String getDownscaling() {
            return downscaling;
        }
    }

    public static class ConfigurableParticleImageSettings implements Loadable {

        @LoadableEntry(key = "format")
        private String format;

        @LoadableEntry(key = "dimensions")
        private String dimensions;

        public ConfigurableParticleImageSettings() {
            this.format = "png";
            this.dimensions = "2.5x2.5";
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

        public String getDimensions() {
            return dimensions;
        }

        public String getFormat() {
            return format;
        }
    }
}
