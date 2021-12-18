package com.pepedevs.dbedwars.game.arena;

import com.pepedevs.corelib.particles.ParticleEffect;
import com.pepedevs.corelib.utils.StringUtils;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.spawner.Spawner;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import com.pepedevs.dbedwars.api.util.SoundVP;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableArenaOverride;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableItemSpawner;

import java.util.*;

public class DropType implements com.pepedevs.dbedwars.api.game.spawner.DropType {

    private final DBedwars plugin;
    private ConfigurableItemSpawner cfgSpawner;
    private Spawner spawner;

    private String configId;
    private String id;
    private BwItemStack icon;
    private SoundVP sound;
    private ParticleEffect effect;
    private int radius;
    private boolean teamSpawner;
    private boolean merge;
    private boolean split;
    private boolean hologramEnabled;
    private BwItemStack hologramItem;
    private List<String> hologramText;
    private Map<Integer, com.pepedevs.dbedwars.api.game.spawner.DropType.Tier> tiers;

    public DropType(DBedwars plugin, ConfigurableItemSpawner cfgSpawner) {
        this.plugin = plugin;
        this.cfgSpawner = cfgSpawner;
        this.tiers = new LinkedHashMap<>();

        this.configId = this.cfgSpawner.getKey();
        this.id = this.cfgSpawner.getId();
        this.icon = this.cfgSpawner.getIcon();
        this.sound = this.cfgSpawner.getSpawnSound();
        this.effect = this.cfgSpawner.getSpawnEffect();
        this.radius = this.cfgSpawner.getRadius();
        this.teamSpawner = this.cfgSpawner.isTeamSpawner();
        this.merge = this.cfgSpawner.isMerge();
        this.split = this.cfgSpawner.isSplit();
        this.hologramEnabled = this.cfgSpawner.isHologramEnabled();
        this.hologramItem = this.cfgSpawner.getHologramMaterial();
        this.hologramText = this.cfgSpawner.getHologramText();
        this.cfgSpawner.getTiers().forEach((i, t) -> this.tiers.put(i, new Tier(t)));
    }

    @Override
    public String getConfigId() {
        return this.configId;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getSimpleName() {
        return StringUtils.capitalize(this.id);
    }

    @Override
    public BwItemStack getIcon() {
        return this.icon;
    }

    @Override
    public SoundVP getSpawnSound() {
        return this.sound;
    }

    @Override
    public void setSpawnSound(SoundVP sound) {
        this.sound = sound;
    }

    @Override
    public ParticleEffect getSpawnEffect() {
        return this.effect;
    }

    @Override
    public void setSpawnEffect(ParticleEffect effect) {
        this.effect = effect;
    }

    @Override
    public int getSpawnRadius() {
        return this.radius;
    }

    @Override
    public void setSpawnRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public boolean isTeamSpawner() {
        return this.teamSpawner;
    }

    @Override
    public void setTeamSpawner(boolean flag) {
        this.teamSpawner = flag;
    }

    @Override
    public boolean isMerging() {
        return this.merge;
    }

    @Override
    public void setMerging(boolean flag) {
        this.merge = flag;
    }

    @Override
    public boolean isSplitable() {
        return split;
    }

    @Override
    public void setSplitable(boolean flag) {
        this.split = flag;
    }

    @Override
    public boolean isHologramEnabled() {
        return this.hologramEnabled;
    }

    @Override
    public void setHologramEnabled(boolean flag) {
        this.hologramEnabled = flag;
    }

    @Override
    public BwItemStack getHologramMaterial() {
        return this.hologramItem;
    }

    @Override
    public void setHologramMaterial(BwItemStack stack) {
        this.hologramItem = stack;
    }

    @Override
    public List<String> getHologramText() {
        return this.hologramText;
    }

    @Override
    public com.pepedevs.dbedwars.api.game.spawner.DropType.Tier getTier(int level) {
        return this.tiers.get(level);
    }

    @Override
    public boolean hasTier(int level) {
        return this.tiers.containsKey(level);
    }

    @Override
    public Map<Integer, com.pepedevs.dbedwars.api.game.spawner.DropType.Tier> getTiers() {
        return this.tiers;
    }

    @Override
    public boolean isRegistered() {
        return this.spawner != null;
    }

    public void override(ConfigurableArenaOverride.SpawnerOverride o) {}

    @Override
    public DropType clone() {
        try {
            return (DropType) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class Tier implements com.pepedevs.dbedwars.api.game.spawner.DropType.Tier {

        private double delay;
        private SoundVP upgradeSound;
        private ParticleEffect upgradeEffect;
        private String upgradeMessage;
        private Map<String, com.pepedevs.dbedwars.api.game.spawner.DropType.Drop> drops;

        public Tier(ConfigurableItemSpawner.ConfigurableTiers tiers) {
            this.delay = tiers.getSeconds();
            this.upgradeSound = tiers.getUpgradeSound();
            this.upgradeEffect = tiers.getUpgradeEffect();
            this.upgradeMessage = tiers.getMessage();
            this.drops = new HashMap<>();
            tiers.getActions().forEach((s, d) -> this.drops.put(s, new Drop(d)));
        }

        @Override
        public double getUpgradeDelay() {
            return this.delay;
        }

        @Override
        public void setUpgradeDelay(double delay) {
            this.delay = delay;
        }

        @Override
        public SoundVP getUpgradeSound() {
            return this.upgradeSound;
        }

        @Override
        public void setUpgradeSound(SoundVP sound) {
            this.upgradeSound = sound;
        }

        @Override
        public ParticleEffect getUpgradeEffect() {
            return this.upgradeEffect;
        }

        @Override
        public void setUpgradeEffect(ParticleEffect effect) {
            this.upgradeEffect = effect;
        }

        @Override
        public String getUpgradeMessage() {
            return this.upgradeMessage;
        }

        @Override
        public void setUpgradeMessage(String message) {
            this.upgradeMessage = message;
        }

        @Override
        public List<com.pepedevs.dbedwars.api.game.spawner.DropType.Drop> getDrops() {
            return new ArrayList<>(this.drops.values());
        }

        @Override
        public Map<String, com.pepedevs.dbedwars.api.game.spawner.DropType.Drop> getDropMap() {
            return this.drops;
        }

        @Override
        public Tier clone() {
            try {
                return (Tier) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }

    public static class Drop implements com.pepedevs.dbedwars.api.game.spawner.DropType.Drop {

        private final String key;
        private BwItemStack item;
        private double delay;
        private int maxSpawn;

        public Drop(ConfigurableItemSpawner.ConfigurableDrop a) {
            this.key = a.getKey();
            this.item = a.getMaterial();
            this.delay = a.getDelay();
            this.maxSpawn = a.getLimit();
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public BwItemStack getItem() {
            return this.item;
        }

        @Override
        public void setItem(BwItemStack stack) {
            this.item = item;
        }

        @Override
        public double getDelay() {
            return this.delay;
        }

        @Override
        public void setDelay(double delay) {
            this.delay = delay;
        }

        @Override
        public int getMaxSpawn() {
            return this.maxSpawn;
        }

        @Override
        public void setMaxSpawn(int maxSpawn) {
            this.maxSpawn = maxSpawn;
        }

        @Override
        public Drop clone() {
            try {
                return (Drop) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }
}
