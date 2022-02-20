package org.zibble.dbedwars.configuration.configurable;

import com.pepedevs.radium.particles.ParticleEffect;
import com.pepedevs.radium.utils.xseries.XMaterial;
import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.objects.serializable.ParticleEffectASC;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.api.util.BwItemStack;
import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.api.util.properies.PropertySerializable;
import org.zibble.dbedwars.configuration.util.Loadable;
import org.zibble.dbedwars.configuration.util.annotations.LoadableCollectionEntry;
import org.zibble.dbedwars.configuration.util.annotations.LoadableEntry;
import org.zibble.dbedwars.utils.ConfigurationUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigurableItemSpawner implements Loadable, PropertySerializable {

    private final DBedwars plugin;

    private String key;

    @LoadableEntry(key = "id")
    private String id;

    @LoadableEntry(key = "icon")
    private String icon;

    @LoadableEntry(key = "spawn-sound")
    private String spawnSound;

    @LoadableEntry(key = "spawn-effect")
    private String spawnEffect;

    @LoadableEntry(key = "radius")
    private int radius;

    @LoadableEntry(key = "merge")
    private boolean merge;

    @LoadableEntry(key = "split")
    private boolean split;

    @LoadableEntry(key = "timed-upgrade")
    private boolean timedUpgrade;

    @LoadableEntry(key = "team-spawner")
    private boolean teamSpawner;

    @LoadableEntry(key = "enabled", subsection = "hologram")
    private boolean hologramEnabled;

    @LoadableEntry(key = "id", subsection = "hologram")
    private String hologramId;

    @LoadableCollectionEntry(subsection = "tiers")
    private Map<Integer, ConfigurableTiers> tiers;

    public ConfigurableItemSpawner(DBedwars plugin, String key) {
        this.plugin = plugin;
        this.key = key;
        this.tiers = new LinkedHashMap<>();
        this.radius = 1;
    }

    @Override
    public Loadable load(ConfigurationSection section) {
//        ConfigurationSection tierSection = section.getConfigurationSection("tiers");
//        if (tierSection != null) {
//            for (String key : tierSection.getKeys(false)) {
//                ConfigurableTiers tier = new ConfigurableTiers(key);
//                tier.load(tierSection.getConfigurationSection(key));
//                if (tier.isValid()) this.tiers.put(Integer.parseInt(key), tier);
//            }
//        }
        return this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return this.id != null
                && !this.tiers.isEmpty()
                && this.tiers.containsKey(1)
                && !this.tiers.get(1).getActions().isEmpty();
    }

    @Override
    public boolean isInvalid() {
        return !this.isValid();
    }

    public String getKey() {
        return key;
    }

    public String getId() {
        return id;
    }

    public BwItemStack getIcon() {
        return this.icon != null
                ? ConfigurationUtils.parseItem(this.icon)
                : new BwItemStack(XMaterial.IRON_INGOT.parseItem());
    }

    public SoundVP getSpawnSound() {
        return this.spawnSound != null ? SoundVP.valueOf(this.spawnSound) : null;
    }

    public ParticleEffectASC getSpawnEffect() {
        return ParticleEffectASC.valueOf(this.spawnEffect);
    }

    public int getRadius() {
        return this.radius;
    }

    public boolean isMerge() {
        return this.merge;
    }

    public boolean isSplit() {
        return split;
    }

    public boolean isTimedUpgrade() {
        return this.timedUpgrade;
    }

    public boolean isTeamSpawner() {
        return this.teamSpawner;
    }

    public boolean isHologramEnabled() {
        return this.hologramEnabled;
    }

    public String getHologramId() {
        return hologramId;
    }

    public Map<Integer, ConfigurableTiers> getTiers() {
        return tiers;
    }

    public static class ConfigurableTiers implements Loadable, PropertySerializable {

        private String key;

        @LoadableEntry(key = "time")
        private double seconds;

        @LoadableEntry(key = "upgrade-effect")
        private String upgradeEffect;

        @LoadableEntry(key = "upgrade-sound")
        private String upgradeSound;

        @LoadableEntry(key = "message")
        private String message;

        private Map<String, ConfigurableDrop> actions;

        protected ConfigurableTiers(String key) {
            this.key = key;
            this.seconds = -1;
            this.actions = new HashMap<>();
        }

        @Override
        public Loadable load(ConfigurationSection section) {
            ConfigurationSection actionSection = section.getConfigurationSection("action");
            if (actionSection != null) {
                for (String key : actionSection.getKeys(false)) {
                    ConfigurableDrop action = new ConfigurableDrop(key);
                    action.load(actionSection.getConfigurationSection(key));
                    if (action.isValid()) this.actions.put(key, action);
                }
            }
            return this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            try {
                Integer.parseInt(this.key);
            } catch (NumberFormatException e) {
                return false;
            }

            return true;
        }

        @Override
        public boolean isInvalid() {
            return !this.isValid();
        }

        public String getKey() {
            return key;
        }

        public double getSeconds() {
            return this.seconds;
        }

        public ParticleEffect getUpgradeEffect() {
            if (this.upgradeEffect != null) {
                try {
                    return ParticleEffect.valueOf(this.upgradeEffect);
                } catch (IllegalArgumentException ignored) {
                }
            }
            return null;
        }

        public SoundVP getUpgradeSound() {
            return this.upgradeSound != null ? SoundVP.valueOf(this.upgradeSound) : null;
        }

        public String getMessage() {
            return this.message;
        }

        public Map<String, ConfigurableDrop> getActions() {
            return this.actions;
        }

        @Override
        public NamedProperties toProperties() {
            return NamedProperties.builder()
                    .add("key", key)
                    .add("time", seconds)
                    .add("upgradeEffect", upgradeEffect)
                    .add("upgradeSound", upgradeSound)
                    .add("message", message)
                    .add("actions", actions)
                    .build();
        }

        public static class ConfigurableDrop implements Loadable, PropertySerializable {

            private String key;

            @LoadableEntry(key = "material")
            private String material;

            @LoadableEntry(key = "delay")
            private double delay;

            @LoadableEntry(key = "limit")
            private int limit;

            protected ConfigurableDrop(String key) {
                this.key = key;
                this.delay = -1;
                this.limit = -1;
            }

            @Override
            public Loadable load(ConfigurationSection section) {
                return this.loadEntries(section);
            }

            @Override
            public boolean isValid() {
                return this.material != null
                        && XMaterial.matchXMaterial(this.material.split(":")[0]).isPresent();
            }

            @Override
            public boolean isInvalid() {
                return !this.isValid();
            }

            public String getKey() {
                return key;
            }

            public BwItemStack getMaterial() {
                return ConfigurationUtils.parseItem(this.material);
            }

            public double getDelay() {
                return this.delay;
            }

            public int getLimit() {
                return this.limit;
            }

            @Override
            public String toString() {
                return "ConfigurableDrop{" +
                        "key='" + key + '\'' +
                        ", material='" + material + '\'' +
                        ", delay=" + delay +
                        ", limit=" + limit +
                        '}';
            }

            @Override
            public NamedProperties toProperties() {
                return NamedProperties.builder()
                        .add("key", key)
                        .add("material", ConfigurationUtils.parseItem(this.material))
                        .add("delay", delay)
                        .add("limit", limit)
                        .build();
            }
        }

        @Override
        public String toString() {
            return "ConfigurableTiers{" +
                    "key='" + key + '\'' +
                    ", seconds=" + seconds +
                    ", upgradeEffect='" + upgradeEffect + '\'' +
                    ", upgradeSound='" + upgradeSound + '\'' +
                    ", message='" + message + '\'' +
                    ", actions=" + actions +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ConfigurableItemSpawner{" +
                "plugin=" + plugin +
                ", key='" + key + '\'' +
                ", id='" + id + '\'' +
                ", icon='" + icon + '\'' +
                ", spawnSound='" + spawnSound + '\'' +
                ", spawnEffect='" + spawnEffect + '\'' +
                ", radius=" + radius +
                ", merge=" + merge +
                ", split=" + split +
                ", timedUpgrade=" + timedUpgrade +
                ", teamSpawner=" + teamSpawner +
                ", hologramEnabled=" + hologramEnabled +
                ", hologramId='" + hologramId + '\'' +
                ", tiers=" + tiers +
                '}';
    }

    @Override
    public NamedProperties toProperties() {
        return NamedProperties
                .builder()
                .add("id", id)
                .add("icon", icon)
                .add("spawnSound", SoundVP.valueOf(spawnSound))
                .add("spawnEffect", spawnEffect)
                .add("radius", radius)
                .add("merge", merge)
                .add("split", split)
                .add("timedUpgrade", timedUpgrade)
                .add("teamSpawner", teamSpawner)
                .add("hologram", NamedProperties.builder()
                        .add("enabled", hologramEnabled)
                        .add("id", hologramId)
                        .build())
                .add("tiers", tiers)
                .build();
    }
}
