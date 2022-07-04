package org.zibble.dbedwars.game.arena.spawner;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.spawner.DropInfo;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.hologram.AnimatedHologramModel;
import org.zibble.dbedwars.api.objects.serializable.ParticleEffectASC;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.configuration.configurable.ConfigurableItemSpawner;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.utils.ConfigurationUtil;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DropInfoImpl implements DropInfo {

    private final Key key;

    private BwItemStack icon;
    private int radius;
    private SoundVP soundEffect;
    private ParticleEffectASC particleEffect;
    private AnimatedHologramModel hologram;
    private boolean teamSpawner, merging, spliting;

    private final Map<Integer, DropInfo.Tier> tiers = new HashMap<>();

    public DropInfoImpl(String key) {
        this.key = Key.of(key);
    }

    public static DropInfoImpl fromConfig(ConfigurableItemSpawner cfg) {
        DropInfoImpl dropType = new DropInfoImpl(cfg.getId());
        dropType.radius = cfg.getRadius();
        dropType.icon = cfg.getIcon();
        dropType.soundEffect = cfg.getSpawnSound();
        dropType.particleEffect = cfg.getSpawnEffect();
        dropType.teamSpawner = cfg.isTeamSpawner();
        dropType.merging = cfg.isMerge();
        dropType.spliting = cfg.isSplit();
        dropType.hologram = cfg.isHologramEnabled() ? DBedwars.getInstance().getConfigHandler().getHolograms().getConfigs().containsKey(cfg.getHologramId()) ?
                ConfigurationUtil.createHologram(DBedwars.getInstance().getConfigHandler().getHolograms().getConfigs().get(cfg.getHologramId())) : null : null;

        for (Map.Entry<String, ConfigurableItemSpawner.ConfigurableTiers> entry : cfg.getTiers().entrySet()) {
            dropType.tiers.put(Integer.parseInt(entry.getKey()), Tier.fromConfig(Integer.parseInt(entry.getKey()), entry.getValue()));
        }
        return dropType;
    }

    @Override
    public BwItemStack getIcon() {
        return this.icon.clone();
    }

    @Override
    public void setIcon(BwItemStack viewItem) {
        this.icon = viewItem;
    }

    @Override
    public int getSpawnRadius() {
        return radius;
    }

    @Override
    public void setSpawnRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public SoundVP getSoundEffect() {
        return this.soundEffect;
    }

    @Override
    public void setSoundEffect(SoundVP soundEffect) {
        this.soundEffect = soundEffect;
    }

    @Override
    public ParticleEffectASC getParticleEffect() {
        return this.particleEffect;
    }

    @Override
    public void setParticleEffect(ParticleEffectASC particleEffect) {
        this.particleEffect = particleEffect;
    }

    @Override
    public AnimatedHologramModel getHologram() {
        return this.hologram;
    }

    @Override
    public void setHologram(AnimatedHologramModel hologram) {
        this.hologram = hologram;
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
        return this.merging;
    }

    @Override
    public void setMerging(boolean flag) {
        this.merging = flag;
    }

    @Override
    public boolean isSplitable() {
        return this.spliting;
    }

    @Override
    public void setSplitable(boolean flag) {
        this.spliting = flag;
    }

    @Override
    public DropInfo.Tier getTier(int level) {
        return this.tiers.get(level);
    }

    @Override
    public boolean hasTier(int level) {
        return this.getTier(level) != null;
    }

    @Override
    public Collection<DropInfo.Tier> getTiers() {
        return this.tiers.values();
    }

    @Override
    public DropInfo clone() {
        try {
            return (DropInfoImpl) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "DropInfoImpl{" +
                "key=" + key +
                ", icon=" + icon +
                ", radius=" + radius +
                ", soundEffect=" + soundEffect +
                ", particleEffect=" + particleEffect +
                ", hologram=" + hologram +
                ", teamSpawner=" + teamSpawner +
                ", merging=" + merging +
                ", spliting=" + spliting +
                ", tiers=" + tiers +
                '}';
    }

    @Override
    public Key getKey() {
        return key;
    }

    public static class Tier implements DropInfo.Tier {

        private final Key key;
        private double delay;
        private SoundVP upgradeSound;
        private ParticleEffect upgradeEffect;
        private Message upgradeMessage;
        private Map<String, DropInfo.Drop> drops = new HashMap<>();

        public Tier(int key) {
            this.key = Key.of(String.valueOf(key));
        }

        public Tier(NamedProperties properties) {
            this.key = properties.getValue("key");
            this.delay = properties.getValue("delay", 5);
            this.upgradeSound = properties.getValue("upgrade-sound");
            this.upgradeEffect = properties.getValue("upgrade-effect");
            this.upgradeMessage = properties.getValue("upgrade-message");
            this.drops = properties.getValue("upgrade-item");
        }

        public static Tier fromConfig(int key, ConfigurableItemSpawner.ConfigurableTiers cfg) {
            Tier tier = new Tier(key);
            tier.delay = cfg.getSeconds();
            tier.upgradeSound = cfg.getUpgradeSound();
            tier.upgradeEffect = cfg.getUpgradeEffect();
            tier.upgradeMessage = ConfigLang.getTranslator().asMessage(cfg.getMessage());
            for (Map.Entry<String, ConfigurableItemSpawner.ConfigurableTiers.ConfigurableDrop> entry : cfg.getActions().entrySet()) {
                tier.drops.put(entry.getKey(), Drop.fromConfig(entry.getValue()));
            }
            return tier;
        }

        @Override
        public Key getKey() {
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
        public Collection<DropInfo.Drop> getDrops() {
            return new ArrayList<>(this.drops.values());
        }

        @Override
        public Tier clone() {
            Tier tier = new Tier(Integer.parseInt(this.key.get()));
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
                    "key=" + key +
                    ", delay=" + delay +
                    ", upgradeSound=" + upgradeSound +
                    ", upgradeEffect=" + upgradeEffect +
                    ", upgradeMessage=" + upgradeMessage +
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

    public static class Drop implements DropInfo.Drop {

        private final Key key;
        private BwItemStack item;
        private double delay;
        private int maxSpawn;

        public Drop(String key) {
            this.key = Key.of(key);
        }

        public Drop(NamedProperties properties) {
            this.key = properties.getValue("key");
            this.item = properties.getValue("item");
            this.delay = properties.getValue("delay");
            this.maxSpawn = properties.getValue("max-spawn");
        }

        public static Drop fromConfig(ConfigurableItemSpawner.ConfigurableTiers.ConfigurableDrop cfg) {
            Drop drop = new Drop(cfg.getKey());
            drop.item = BwItemStack.valueOf(cfg.getItem());
            drop.delay = cfg.getDelay();
            drop.maxSpawn = cfg.getLimit();
            return drop;
        }

        @Override
        public Key getKey() {
            return this.key;
        }

        @Override
        public BwItemStack getItem() {
            return this.item;
        }

        @Override
        public void setItem(BwItemStack stack) {
            this.item = stack;
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
                    "key=" + key +
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
