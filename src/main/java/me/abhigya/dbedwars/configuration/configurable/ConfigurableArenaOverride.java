package me.abhigya.dbedwars.configuration.configurable;

import me.Abhigya.core.particle.particlelib.ParticleEffect;
import me.Abhigya.core.util.loadable.Loadable;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.spawner.DropType;
import me.abhigya.dbedwars.api.util.BwItemStack;
import me.abhigya.dbedwars.api.util.SoundVP;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ConfigurableArenaOverride implements Loadable {

    private static final String SECTION_KEY = "override";

    private final DBedwars plugin;

    private SpawnerOverride spawnerOverride;

    public ConfigurableArenaOverride(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public Loadable load(ConfigurationSection section) {
        this.spawnerOverride = new SpawnerOverride();
        if (section.isConfigurationSection(SECTION_KEY))
            this.spawnerOverride.load(section.getConfigurationSection(SECTION_KEY));

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
        this.spawnerOverride.apply(arena);
    }

    public class SpawnerOverride implements Loadable {

        private static final String SECTION_KEY = "spawners";

        private List<ConfigurableItemSpawner> spawners;

        protected SpawnerOverride() {
            this.spawners = new ArrayList<>();
        }

        @Override
        public Loadable load(ConfigurationSection section) {
            if (!section.isConfigurationSection(SECTION_KEY))
                return this;

            for (String key : section.getConfigurationSection(SECTION_KEY).getKeys(false)) {
                this.spawners.add(new ConfigurableItemSpawner(ConfigurableArenaOverride.this.plugin, key));
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
                if (s.getId() == null)
                    continue;

                SoundVP sound = s.getSpawnSound();
                ParticleEffect effect = s.getSpawnEffect();

                for (DropType d : arena.getSettings().getDrops().asMap().keySet()) {
                    if (d.getId().equals(s.getId())) {
                        if (sound != null)
                            d.setSpawnSound(sound);
                        if (effect != null)
                            d.setSpawnEffect(effect);

                        s.getTiers().forEach((i, ct) -> {
                            boolean bTier = false;
                            if (d.hasTier(i)) {
                                DropType.Tier t = d.getTier(i);
                                double delay = ct.getSeconds();
                                SoundVP upgradeSound = ct.getUpgradeSound();
                                ParticleEffect upgradeEffect = ct.getUpgradeEffect();
                                String message = ct.getMessage();

                                if (delay != -1)
                                    t.setUpgradeDelay(delay);
                                if (upgradeSound != null)
                                    t.setUpgradeSound(sound);
                                if (upgradeEffect != null)
                                    t.setUpgradeEffect(effect);
                                if (message != null)
                                    t.setUpgradeMessage(message);

                                ct.getActions().forEach((str, cDrop) -> {
                                    boolean bDrop = false;
                                    for (DropType.Drop drop : t.getDropMap().values()) {
                                        if (drop.getKey().equals(cDrop.getKey())) {
                                            if (cDrop.getDelay() != -1)
                                                drop.setDelay(cDrop.getDelay());
                                            if (cDrop.getLimit() != -1)
                                                drop.setMaxSpawn(cDrop.getLimit());
                                            BwItemStack stack = cDrop.getMaterial();
                                            if (stack != null)
                                                drop.setItem(stack);

                                            bDrop = true;
                                        }
                                    }
                                    if (!bDrop) {
                                        if (cDrop.isValid()) {
                                            t.getDropMap().put(str, new me.abhigya.dbedwars.game.arena.DropType.Drop(cDrop));
                                        }
                                    }
                                });

                                bTier = true;
                            }
                            if (!bTier) {
                                if (ct.isValid()) {
                                    d.getTiers().put(i, new me.abhigya.dbedwars.game.arena.DropType.Tier(ct));
                                }
                            }
                        });
                    }
                }
            }
        }
    }

}
