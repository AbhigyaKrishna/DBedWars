package com.pepedevs.dbedwars.game.arena.spawner;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.spawner.DropType;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.api.util.Keyed;
import com.pepedevs.dbedwars.api.util.SoundVP;
import com.pepedevs.dbedwars.api.util.properies.NamedProperties;
import com.pepedevs.dbedwars.api.util.properies.PropertyName;
import com.pepedevs.dbedwars.configuration.Lang;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableItemSpawner;
import com.pepedevs.radium.holograms.object.Hologram;
import com.pepedevs.radium.particles.ParticleEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropTypeNew implements Keyed<String> {

    private final DBedwars plugin;
    private final Key<String> key;

    private Key<BwItemStack> icon;
    private int radius;
    private SoundVP soundEffect;
    private ParticleEffect particleEffect;
    private Hologram hologram;

    private Map<Integer, DropType.Tier> tiers = new HashMap<>();

    public static DropTypeNew fromConfig(DBedwars plugin, ConfigurableItemSpawner cfg) {
        DropTypeNew dropType = new DropTypeNew(plugin, cfg.getId());
        dropType.radius = cfg.getRadius();
        dropType.icon = Key.of(cfg.getIcon());
        dropType.soundEffect = cfg.getSpawnSound();
        dropType.particleEffect = cfg.getSpawnEffect();

        for (Map.Entry<Integer, ConfigurableItemSpawner.ConfigurableTiers> entry : cfg.getTiers().entrySet()) {
            dropType.tiers.put(entry.getKey(), Tier.fromConfig(entry.getKey(), entry.getValue()));
        }
        return dropType;
    }

    public DropTypeNew(DBedwars plugin, String key) {
        this.plugin = plugin;
        this.key = Key.of(key);
    }

    public Key<BwItemStack> getIcon() {
        return this.icon.clone();
    }

    public void setIcon(BwItemStack viewItem) {
        this.icon = Key.of(viewItem);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public Key<String> getKey() {
        return key;
    }

    public static class Tier implements DropType.Tier {

        @PropertyName("key")
        private final Key<Integer> key;
        @PropertyName("delay")
        private double delay;
        @PropertyName("upgrade-sound")
        private SoundVP upgradeSound;
        @PropertyName("upgrade-effect")
        private ParticleEffect upgradeEffect;
        @PropertyName("upgrade-message")
        private Message upgradeMessage;
        @PropertyName("upgrade-item")
        private Map<String, com.pepedevs.dbedwars.api.game.spawner.DropType.Drop> drops = new HashMap<>();

        public static Tier fromConfig(int key, ConfigurableItemSpawner.ConfigurableTiers cfg) {
            Tier tier = new Tier(key);
            tier.delay = cfg.getSeconds();
            tier.upgradeSound = cfg.getUpgradeSound();
            tier.upgradeEffect = cfg.getUpgradeEffect();
            tier.upgradeMessage = Lang.getTranslator().asMessage(cfg.getMessage());
            for (Map.Entry<String, ConfigurableItemSpawner.ConfigurableDrop> entry : cfg.getActions().entrySet()) {
                tier.drops.put(entry.getKey(), Drop.fromConfig(entry.getValue()));
            }
            return tier;
        }

        public Tier(int key) {
            this.key = Key.of(key);
        }

        public Tier(NamedProperties properties) {
            this.key = properties.getValue("key");
            this.delay = properties.getValue("delay", 5);
            this.upgradeSound = properties.getValue("upgrade-sound");
            this.upgradeEffect = properties.getValue("upgrade-effect");
            this.upgradeMessage = properties.getValue("upgrade-message");
            this.drops = properties.getValue("upgrade-item");
        }

        @Override
        public Key<Integer> getKey() {
            return this.key;
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
        public Message getUpgradeMessage() {
            return this.upgradeMessage;
        }

        @Override
        public void setUpgradeMessage(Message message) {
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
            Tier tier = new Tier(this.key.get());
            tier.delay = this.delay;
            tier.upgradeSound = this.upgradeSound;
            tier.upgradeEffect = this.upgradeEffect;
            tier.upgradeMessage = this.upgradeMessage;
            tier.drops = new HashMap<>(this.drops);
            return tier;
        }

        @Override
        public String toString() {
            return "Tier{" +
                    "delay=" + delay +
                    ", upgradeSound=" + upgradeSound +
                    ", upgradeEffect=" + upgradeEffect +
                    ", upgradeMessage='" + upgradeMessage + '\'' +
                    ", drops=" + drops +
                    '}';
        }

        @Override
        public NamedProperties toProperties() {
            return NamedProperties.builder()
                    .add("key", this.key)
                    .add("delay", delay)
                    .add("upgrade-sound", upgradeSound)
                    .add("upgrade-effect", upgradeEffect)
                    .add("upgrade-message", upgradeMessage)
                    .add("upgrade-item", drops)
                    .build();
        }

    }

    public static class Drop implements DropType.Drop {

        @PropertyName("key")
        private final Key<String> key;
        @PropertyName("item")
        private BwItemStack item;
        @PropertyName("delay")
        private double delay;
        @PropertyName("max-spawn")
        private int maxSpawn;

        public static Drop fromConfig(ConfigurableItemSpawner.ConfigurableDrop cfg) {
            Drop drop = new Drop(cfg.getKey());
            drop.item = cfg.getMaterial();
            drop.delay = cfg.getDelay();
            drop.maxSpawn = cfg.getLimit();
            return drop;
        }

        public Drop(String key) {
            this.key = Key.of(key);
        }

        public Drop(NamedProperties properties) {
            this.key = properties.getValue("key");
            this.item = properties.getValue("item");
            this.delay = properties.getValue("delay");
            this.maxSpawn = properties.getValue("max-spawn");
        }

        @Override
        public Key<String> getKey() {
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
            Drop drop = new Drop(this.key.get());
            drop.item = this.item;
            drop.delay = this.delay;
            drop.maxSpawn = this.maxSpawn;
            return drop;
        }

        @Override
        public String toString() {
            return "Drop{" +
                    "key='" + key + '\'' +
                    ", item=" + item +
                    ", delay=" + delay +
                    ", maxSpawn=" + maxSpawn +
                    '}';
        }

        @Override
        public NamedProperties toProperties() {
            return NamedProperties.builder()
                    .add("key", this.key)
                    .add("item", this.item)
                    .add("delay", this.delay)
                    .add("max-spawn", this.maxSpawn)
                    .build();
        }

    }

}
