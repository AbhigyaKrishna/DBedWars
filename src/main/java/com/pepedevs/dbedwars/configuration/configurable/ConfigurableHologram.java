package com.pepedevs.dbedwars.configuration.configurable;

import com.pepedevs.dbedwars.api.util.properies.NamedProperties;
import com.pepedevs.dbedwars.api.util.properies.PropertySerializable;
import com.pepedevs.dbedwars.task.HologramRotateTask;
import com.pepedevs.dbedwars.utils.ConfigurationUtils;
import com.pepedevs.dbedwars.configuration.util.Loadable;
import com.pepedevs.dbedwars.configuration.util.annotations.LoadableEntry;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurableHologram implements Loadable, PropertySerializable {

    private Map<String, HologramConfig> configs = new HashMap<>();

    @Override
    public Loadable load(ConfigurationSection section) {
        for (String key : section.getKeys(false)) {
            ConfigurationSection config = section.getConfigurationSection(key);
            if (config.getString("mode").equalsIgnoreCase("baby")) {
                ConfigurableBabyHologram hologram = new ConfigurableBabyHologram();
                hologram.load(config);
                configs.put(key, hologram);
            } else {
                ConfigurableAdvancedHologram hologram = new ConfigurableAdvancedHologram();
                hologram.load(config);
                configs.put(key, hologram);
            }
        }
        return this;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public Map<String, HologramConfig> getConfigs() {
        return configs;
    }

    @Override
    public String toString() {
        return "ConfigurableHologram{" +
                "configs=" + configs +
                '}';
    }

    @Override
    public NamedProperties toProperties() {
        return NamedProperties.builder()
                .add("configs", configs)
                .build();
    }

    public interface HologramConfig extends Loadable, PropertySerializable {

        HologramConfigMode getMode();

    }

    public enum HologramConfigMode {
        ADVANCED, BABY;
    }

    public static class ConfigurableAdvancedHologram implements HologramConfig {

        @LoadableEntry(key = "animation-cycle-end-control")
        private String animationEndTask;

        @LoadableEntry(key = "text")
        private String text;

        @LoadableEntry(key = "keyframes")
        private List<String> frames;

        public ConfigurableAdvancedHologram() {
        }

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        public HologramConfigMode getMode() {
            return HologramConfigMode.ADVANCED;
        }

        public String getAnimationEndTask() {
            return animationEndTask;
        }

        public List<String> getFrames() {
            return frames;
        }

        @Override
        public String toString() {
            return "ConfigurableAdvancedHologram{" +
                    "mode=" + this.getMode() +
                    ", animationEndTask='" + animationEndTask + '\'' +
                    ", text='" + text + '\'' +
                    ", frames=" + frames +
                    '}';
        }

        @Override
        public NamedProperties toProperties() {
            return NamedProperties.builder()
                    .add("mode", this.getMode())
                    .add("animation-end", ConfigurationUtils.matchEnum(animationEndTask, HologramRotateTask.TaskEndAction.values()))
                    .add("text", text)
                    .add("frames", frames)
                    .build();
        }

    }

    public static class ConfigurableBabyHologram implements HologramConfig {

        @LoadableEntry(key = "animation-cycle-end-control")
        private String animationEndTask;

        @LoadableEntry(key = "text")
        private String text;

        @LoadableEntry(key = "degree-rotated-per-cycle", subsection = "rotation")
        private double degreeRotatedPerCycle;

        @LoadableEntry(key = "vertical-bobbing-per-cycle", subsection = "rotation")
        private double verticalDisplacement;

        @LoadableEntry(key = "ticks-per-animation-cycle", subsection = "rotation")
        private int ticksPerAnimationCycle;

        @LoadableEntry(key = "ease-in-out", subsection = "rotation")
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
            return true;
        }

        @Override
        public HologramConfigMode getMode() {
            return HologramConfigMode.BABY;
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
                    "mode=" + this.getMode() +
                    ", animationEndTask='" + animationEndTask + '\'' +
                    ", text='" + text + '\'' +
                    ", degreeRotatedPerCycle=" + degreeRotatedPerCycle +
                    ", verticalDisplacement=" + verticalDisplacement +
                    ", ticksPerAnimationCycle=" + ticksPerAnimationCycle +
                    ", slowAtEndEnabled=" + slowAtEndEnabled +
                    '}';
        }

        @Override
        public NamedProperties toProperties() {
            return NamedProperties.builder()
                    .add("mode", this.getMode())
                    .add("animation-end", ConfigurationUtils.matchEnum(animationEndTask, HologramRotateTask.TaskEndAction.values()))
                    .add("text", text)
                    .add("degreeRotatedPerCycle", degreeRotatedPerCycle)
                    .add("verticalBobbingPerCycle", verticalDisplacement)
                    .add("ticksPerAnimationCycle", ticksPerAnimationCycle)
                    .add("easeInOut", slowAtEndEnabled)
                    .build();
        }

    }

}
