package com.pepedevs.dbedwars.configuration.configurable;

import com.pepedevs.dbedwars.api.util.properies.NamedProperties;
import com.pepedevs.dbedwars.api.util.properies.PropertySerializable;
import com.pepedevs.radium.utils.configuration.Loadable;
import com.pepedevs.radium.utils.configuration.annotations.LoadableEntry;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigurableHologram implements Loadable {

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

    public static class HologramConfig implements Loadable {

        @LoadableEntry(key = "mode")
        private String mode;

        @LoadableEntry(key = "animation-cycle-end-control")
        private String animationEndTask;

        private ConfigurationSection section;

        public HologramConfig() {
            animationEndTask = "reverse";
            mode = "baby";
        }

        @Override
        public Loadable load(ConfigurationSection section) {
            this.section = section;
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

        public String getMode() {
            return mode;
        }

        public boolean isBabyMode() {
            return mode.trim().equalsIgnoreCase("baby");
        }

        public ConfigurableBabyHologram loadAsBabyHologram() {
            ConfigurableBabyHologram hologram = new ConfigurableBabyHologram();
            hologram.load(section);
            return hologram;
        }

        public String getAnimationEndTask() {
            return animationEndTask;
        }

        @Override
        public String toString() {
            return "HologramConfig{" +
                    "mode='" + mode + '\'' +
                    ", animationEndTask='" + animationEndTask + '\'' +
                    ", section=" + section +
                    '}';
        }
    }

    public static class ConfigurableBabyHologram implements Loadable, PropertySerializable {

        @LoadableEntry(key = "degree-rotated-per-cycle")
        private double degreeRotatedPerCycle;

        @LoadableEntry(key = "vertical-bobbing-per-cycle")
        private double verticalDisplacement;

        @LoadableEntry(key = "ticks-per-animation-cycle")
        private int ticksPerAnimationCycle;

        @LoadableEntry(key = "ease-in-out")
        private boolean slowAtEndEnabled;

        protected ConfigurableBabyHologram() {
            degreeRotatedPerCycle = 540;
            verticalDisplacement = 0.5;
            ticksPerAnimationCycle = 60;
            slowAtEndEnabled = true;
        }

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

        public boolean isSlowAtEndEnabled() {
            return slowAtEndEnabled;
        }

        public double getDegreeRotatedPerCycle() {
            return degreeRotatedPerCycle;
        }

        public double getVerticalDisplacement() {
            return verticalDisplacement;
        }

        public int getTicksPerAnimationCycle() {
            return ticksPerAnimationCycle;
        }

        @Override
        public String toString() {
            return "ConfigurableBabyHologram{" +
                    "degreeRotatedPerCycle=" + degreeRotatedPerCycle +
                    ", verticalDisplacement=" + verticalDisplacement +
                    ", ticksPerAnimationCycle=" + ticksPerAnimationCycle +
                    ", slowAtEndEnabled=" + slowAtEndEnabled +
                    '}';
        }

        @Override
        public NamedProperties toProperties() {
            return NamedProperties.builder()
                    .add("degreeRotatedPerCycle", degreeRotatedPerCycle)
                    .add("verticalBobbingPerCycle", verticalDisplacement)
                    .add("ticksPerAnimationCycle", ticksPerAnimationCycle)
                    .add("easeInOut", slowAtEndEnabled)
                    .build();
        }
    }

}
