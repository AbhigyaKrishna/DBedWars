package org.zibble.dbedwars.configuration.configurable;

import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.spawner.DropInfo;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.api.util.BwItemStack;
import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.api.util.properies.PropertySerializable;
import org.zibble.dbedwars.configuration.MainConfiguration;
import org.zibble.dbedwars.configuration.framework.Loadable;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;
import org.zibble.dbedwars.game.arena.spawner.DropInfoImpl;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.List;

public class ConfigurableArenaOverride implements Loadable, PropertySerializable {

    @ConfigPath("spawners")
    private SpawnerOverride spawnerOverride;

    @ConfigPath("config")
    private ConfigOverride configOverride;

    public ConfigurableArenaOverride() {
        this.spawnerOverride = new SpawnerOverride();
        this.configOverride = new ConfigOverride();
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

    public void apply(Arena arena) {
        this.spawnerOverride.apply(arena);
        this.configOverride.apply(arena);
    }

    @Override
    public NamedProperties toProperties() {
        return null;
    }

    public class SpawnerOverride implements Loadable {

        private List<ConfigurableItemSpawner> spawners;

        protected SpawnerOverride() {
            this.spawners = new ArrayList<>();
        }

        @Override
        public void load(ConfigurationSection section) {
            if (!section.isConfigurationSection(SECTION_KEY)) return this;

            for (String key : section.getConfigurationSection(SECTION_KEY).getKeys(false)) {
                this.spawners.add(
                        new ConfigurableItemSpawner(ConfigurableArenaOverride.this.plugin, key));
            }

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

        protected List<ConfigurableItemSpawner> getSpawners() {
            return this.spawners;
        }

        public void apply(Arena arena) {
            for (ConfigurableItemSpawner s : this.spawners) {
                if (s.getId() == null) continue;
                SoundVP sound = s.getSpawnSound();
                ParticleEffect effect = s.getSpawnEffect();
                for (DropInfo d : arena.getSettings().getDrops().asMap().keySet()) {
                    if (!d.getId().equals(s.getId())) continue;
                    if (sound != null) d.setSpawnSound(sound);
                    if (effect != null) d.setSpawnEffect(effect);
                    s.getTiers().forEach((i, ct) -> {
                        boolean bTier = false;
                        if (d.hasTier(i)) {
                            DropInfo.Tier t = d.getTier(i);
                            double delay = ct.getSeconds();
                            SoundVP upgradeSound = ct.getUpgradeSound();
                            ParticleEffect upgradeEffect = ct.getUpgradeEffect();
                            String message = ct.getMessage();

                            if (delay != -1) t.setUpgradeDelay(delay);
                            if (upgradeSound != null) t.setUpgradeSound(sound);
                            if (upgradeEffect != null) t.setUpgradeEffect(effect);
                            if (message != null) t.setUpgradeMessage(message);
                            ct.getActions().forEach((str, cDrop) -> {
                                boolean bDrop = false;
                                for (DropInfo.Drop drop : t.getDropMap().values()) {
                                    if (drop.getKey().equals(cDrop.getKey())) {
                                        if (cDrop.getDelay() != -1) drop.setDelay(cDrop.getDelay());
                                        if (cDrop.getLimit() != -1) drop.setMaxSpawn(cDrop.getLimit());
                                        BwItemStack stack = cDrop.getMaterial();
                                        if (stack != null) drop.setItem(stack);
                                        bDrop = true;
                                    }
                                }
                                if (!bDrop && cDrop.isValid()) {
                                    t.getDropMap().put(str, new DropInfoImpl.Drop(cDrop));
                                }
                            });

                            bTier = true;
                        }
                        if (!bTier) {
                            if (ct.isValid()) {
                                d.getTiers().put(i, new DropInfoImpl.Tier(ct));
                            }
                        }
                    });
                }
            }
        }

        @Override
        public String toString() {
            return "SpawnerOverride{" +
                    "spawners=" + spawners +
                    '}';
        }
    }

    public class ConfigOverride implements Loadable {

        private static final String SECTION_KEY = "config";

        private MainConfiguration.ArenaSection config;

        protected ConfigOverride() {
            this.config = new MainConfiguration.ArenaSection();
        }

        @Override
        public Loadable load(ConfigurationSection section) {
            if (!section.isConfigurationSection(SECTION_KEY)) return this;

            this.config.load(section.getConfigurationSection(SECTION_KEY));
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

        public void apply(Arena arena) {
            if (this.config.getStartTimer() != -1)
                arena.getSettings().setStartTimer(this.config.getStartTimer());
            if (this.config.getRespawnTime() != -1)
                arena.getSettings().setRespawnTime(this.config.getRespawnTime());
            if (this.config.getIslandRadius() != -1)
                arena.getSettings().setIslandRadius(this.config.getIslandRadius());
            if (this.config.getMinYAxis() != Integer.MAX_VALUE)
                arena.getSettings().setMinYAxis(this.config.getMinYAxis());
            if (this.config.getPlayerHitTagLength() != -1)
                arena.getSettings().setPlayerHitTagLength(this.config.getPlayerHitTagLength());
            if (this.config.getGameEndDelay() != -1)
                arena.getSettings().setGameEndDelay(this.config.getGameEndDelay());
            if (this.config.getBedDestroyPoint() != Integer.MIN_VALUE)
                arena.getSettings().setBedDestroyPoint(this.config.getBedDestroyPoint());
            if (this.config.getKillPoint() != Integer.MIN_VALUE)
                arena.getSettings().setKillPoint(this.config.getKillPoint());
            if (this.config.getFinalKillPoint() != Integer.MIN_VALUE)
                arena.getSettings().setFinalKillPoint(this.config.getFinalKillPoint());
            if (this.config.getDeathPoint() != Integer.MIN_VALUE)
                arena.getSettings().setDeathPoint(this.config.getDeathPoint());
        }

        @Override
        public String toString() {
            return "ConfigOverride{" +
                    "config=" + config +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ConfigurableArenaOverride{" +
                "plugin=" + plugin +
                ", spawnerOverride=" + spawnerOverride +
                ", configOverride=" + configOverride +
                '}';
    }
}
