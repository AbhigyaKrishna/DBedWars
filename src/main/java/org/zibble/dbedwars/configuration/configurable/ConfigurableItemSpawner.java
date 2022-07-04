package org.zibble.dbedwars.configuration.configurable;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.objects.serializable.ParticleEffectASC;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.api.util.properies.PropertySerializable;
import org.zibble.dbedwars.configuration.framework.Loadable;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;
import org.zibble.dbedwars.configuration.framework.annotations.Defaults;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigurableItemSpawner implements Loadable, PropertySerializable {

    private final DBedwars plugin;

    private String key;

    @ConfigPath
    private String id;

    @ConfigPath
    private String icon;

    @ConfigPath
    private String spawnSound;

    @ConfigPath
    private String spawnEffect;

    @ConfigPath
    private int radius;

    @ConfigPath
    private boolean merge;

    @ConfigPath
    private boolean split;

    @ConfigPath
    private boolean timedUpgrade;

    @ConfigPath
    private boolean teamSpawner;

    @ConfigPath("hologram.enabled")
    private boolean hologramEnabled;

    @ConfigPath("hologram.id")
    private String hologramId;

    @ConfigPath
    private Map<String, ConfigurableTiers> tiers;

    public ConfigurableItemSpawner(DBedwars plugin, String key) {
        this.plugin = plugin;
        this.key = key;
        this.tiers = new LinkedHashMap<>();
        this.radius = 1;
    }

    @Override
    public void load(ConfigurationSection section) {
        this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return this.id != null
                && !this.tiers.isEmpty()
                && this.tiers.containsKey("1")
                && !this.tiers.get("1").getActions().isEmpty();
    }

    public String getKey() {
        return key;
    }

    public String getId() {
        return id;
    }

    public BwItemStack getIcon() {
        return this.icon != null
                ? BwItemStack.valueOf(this.icon)
                : new BwItemStack(XMaterial.STONE.parseItem());
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

    public Map<String, ConfigurableTiers> getTiers() {
        return tiers;
    }

    @Override
    public String toString() {
        return "ConfigurableItemSpawner{" +
                "key='" + key + '\'' +
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

    public static class ConfigurableTiers implements Loadable, PropertySerializable {

        @ConfigPath("time")
        private double seconds;

        @ConfigPath
        private String upgradeEffect;

        @ConfigPath
        private String upgradeSound;

        @ConfigPath
        private String message;

        private Map<String, ConfigurableDrop> actions;

        protected ConfigurableTiers() {
            this.seconds = -1;
            this.actions = new HashMap<>();
        }

        @Override
        public void load(ConfigurationSection section) {
            ConfigurationSection actionSection = section.getConfigurationSection("action");
            if (actionSection != null) {
                for (String key : actionSection.getKeys(false)) {
                    ConfigurableDrop action = new ConfigurableDrop(key);
                    action.load(actionSection.getConfigurationSection(key));
                    if (action.isValid()) this.actions.put(key, action);
                }
            }
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return !this.isValid();
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
                    .add("time", seconds)
                    .add("upgradeEffect", upgradeEffect)
                    .add("upgradeSound", upgradeSound)
                    .add("message", message)
                    .add("actions", actions)
                    .build();
        }

        @Override
        public String toString() {
            return "ConfigurableTiers{" +
                    "seconds=" + seconds +
                    ", upgradeEffect='" + upgradeEffect + '\'' +
                    ", upgradeSound='" + upgradeSound + '\'' +
                    ", message='" + message + '\'' +
                    ", actions=" + actions +
                    '}';
        }

        public static class ConfigurableDrop implements Loadable, PropertySerializable {

            private String key;

            @ConfigPath
            private String item;

            @Defaults.Double(-1)
            @ConfigPath
            private double delay;

            @Defaults.Integer(-1)
            @ConfigPath
            private int limit;

            protected ConfigurableDrop(String key) {
                this.key = key;
            }

            @Override
            public void load(ConfigurationSection section) {
                this.loadEntries(section);
            }

            @Override
            public boolean isValid() {
                return this.item != null;
            }

            @Override
            public boolean isInvalid() {
                return !this.isValid();
            }

            public String getKey() {
                return key;
            }

            public String getItem() {
                return item;
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
                        ", material='" + item + '\'' +
                        ", delay=" + delay +
                        ", limit=" + limit +
                        '}';
            }

            @Override
            public NamedProperties toProperties() {
                return NamedProperties.builder()
                        .add("key", key)
                        .add("material", item)
                        .add("delay", delay)
                        .add("limit", limit)
                        .build();
            }

        }

    }

}
