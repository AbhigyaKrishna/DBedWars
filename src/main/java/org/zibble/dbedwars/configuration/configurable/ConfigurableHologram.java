package org.zibble.dbedwars.configuration.configurable;

import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.api.util.properies.PropertySerializable;
import org.zibble.dbedwars.configuration.framework.Loadable;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;
import org.zibble.dbedwars.configuration.framework.annotations.Defaults;
import org.zibble.dbedwars.api.objects.hologram.HologramRotateTask;

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

    public enum HologramConfigMode {
        ADVANCED, BABY;
    }

    public interface HologramConfig extends Loadable, PropertySerializable {

        HologramConfigMode getMode();

        List<String> getContent();

        long getUpdateDelay();

        HologramRotateTask.TaskEndAction getAnimationEndTask();

    }

    public static class ConfigurableAdvancedHologram implements HologramConfig {

        @ConfigPath("animation-cycle-end-control")
        private HologramRotateTask.TaskEndAction animationEndTask;

        @ConfigPath
        private long updateDelay;

        @ConfigPath
        private List<String> content;

        @ConfigPath("keyframes")
        private List<String> frames;

        @ConfigPath
        private short frameTickDelay;

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

        @Override
        public List<String> getContent() {
            return this.content;
        }

        @Override
        public long getUpdateDelay() {
            return this.updateDelay;
        }

        @Override
        public HologramRotateTask.TaskEndAction getAnimationEndTask() {
            return animationEndTask;
        }

        public List<String> getFrames() {
            return frames;
        }

        public short getFrameTickDelay() {
            return frameTickDelay;
        }

        @Override
        public NamedProperties toProperties() {
            return NamedProperties.builder()
                    .add("mode", this.getMode())
                    .add("animation-end", animationEndTask)
                    .add("content", content)
                    .add("frames", frames)
                    .build();
        }

    }

    public static class ConfigurableBabyHologram implements HologramConfig {

        @ConfigPath("animation-cycle-end-control")
        private HologramRotateTask.TaskEndAction animationEndTask;

        @ConfigPath
        private long updateDelay;

        @ConfigPath
        private List<String> content;

        @Defaults.Double(540)
        @ConfigPath("rotation.degree-rotated-per-cycle")
        private float degreeRotatedPerCycle;

        @Defaults.Double(0.5)
        @ConfigPath("rotation.vertical-bobbing-per-cycle")
        private float verticalDisplacement;

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

        @Override
        public List<String> getContent() {
            return this.content;
        }

        @Override
        public long getUpdateDelay() {
            return this.updateDelay;
        }

        @Override
        public HologramRotateTask.TaskEndAction getAnimationEndTask() {
            return animationEndTask;
        }

        public boolean isSlowAtEndEnabled() {
            return slowAtEndEnabled;
        }

        public float getDegreeRotatedPerCycle() {
            return degreeRotatedPerCycle;
        }

        public float getVerticalDisplacement() {
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
                    .add("content", content)
                    .add("degreeRotatedPerCycle", degreeRotatedPerCycle)
                    .add("verticalBobbingPerCycle", verticalDisplacement)
                    .add("ticksPerAnimationCycle", ticksPerAnimationCycle)
                    .add("easeInOut", slowAtEndEnabled)
                    .build();
        }

    }

}
