package org.zibble.dbedwars.configuration.configurable;

import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.api.util.properies.PropertySerializable;
import org.zibble.dbedwars.configuration.framework.Loadable;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;
import org.zibble.dbedwars.configuration.framework.annotations.Defaults;
import org.zibble.dbedwars.task.implementations.HologramRotateTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurableHologram implements Loadable, PropertySerializable {

    private final Map<String, HologramConfig> configs = new HashMap<>();

    @Override
    public void load(ConfigurationSection section) {
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
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public Map<String, HologramConfig> getConfigs() {
        return configs;
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

        @ConfigPath("animation-cycle-end-control")
        private HologramRotateTask.TaskEndAction animationEndTask;

        @ConfigPath
        private String text;

        @ConfigPath("keyframes")
        private List<String> frames;
        
        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        public HologramConfigMode getMode() {
            return HologramConfigMode.ADVANCED;
        }

        public HologramRotateTask.TaskEndAction getAnimationEndTask() {
            return animationEndTask;
        }

        public List<String> getFrames() {
            return frames;
        }

        @Override
        public NamedProperties toProperties() {
            return NamedProperties.builder()
                    .add("mode", this.getMode())
                    .add("animation-end", animationEndTask)
                    .add("text", text)
                    .add("frames", frames)
                    .build();
        }

    }

    public static class ConfigurableBabyHologram implements HologramConfig {

        @ConfigPath("animation-cycle-end-control")
        private HologramRotateTask.TaskEndAction animationEndTask;

        @ConfigPath
        private String text;

        @Defaults.Double(540)
        @ConfigPath("rotation.degree-rotated-per-cycle")
        private double degreeRotatedPerCycle;

        @Defaults.Double(0.5)
        @ConfigPath("rotation.vertical-bobbing-per-cycle")
        private double verticalDisplacement;

        @Defaults.Integer(60)
        @ConfigPath("rotation.ticks-per-animation-cycle")
        private int ticksPerAnimationCycle;

        @Defaults.Boolean(true)
        @ConfigPath("rotation.ease-in-out")
        private boolean slowAtEndEnabled;

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public HologramConfigMode getMode() {
            return HologramConfigMode.BABY;
        }

        public HologramRotateTask.TaskEndAction getAnimationEndTask() {
            return animationEndTask;
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
        public NamedProperties toProperties() {
            return NamedProperties.builder()
                    .add("mode", this.getMode())
                    .add("animation-end", animationEndTask)
                    .add("text", text)
                    .add("degreeRotatedPerCycle", degreeRotatedPerCycle)
                    .add("verticalBobbingPerCycle", verticalDisplacement)
                    .add("ticksPerAnimationCycle", ticksPerAnimationCycle)
                    .add("easeInOut", slowAtEndEnabled)
                    .build();
        }

    }

}
