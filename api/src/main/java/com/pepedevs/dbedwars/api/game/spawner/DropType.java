package com.pepedevs.dbedwars.api.game.spawner;

import com.pepedevs.dbedwars.api.hologram.Hologram;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.api.util.*;
import com.pepedevs.dbedwars.api.util.properies.PropertySerializable;
import com.pepedevs.radium.particles.ParticleEffect;

import java.util.Collection;

public interface DropType extends Cloneable, Keyed<String> {

    Key<BwItemStack> getIcon();

    void setIcon(BwItemStack icon);

    SoundVP getSoundEffect();

    void setSoundEffect(SoundVP sound);

    ParticleEffectASC getParticleEffect();

    void setParticleEffect(ParticleEffectASC effect);

    int getSpawnRadius();

    void setSpawnRadius(int radius);

    Hologram getHologram();

    void setHologram(Hologram hologram);

    boolean isTeamSpawner();

    void setTeamSpawner(boolean flag);

    boolean isMerging();

    void setMerging(boolean flag);

    boolean isSplitable();

    void setSplitable(boolean flag);

    Tier getTier(int level);

    boolean hasTier(int level);

    Collection<Tier> getTiers();

    DropType clone();

    String toString();

    interface Tier extends Cloneable, PropertySerializable, Keyed<Integer> {

        double getUpgradeDelay();

        void setUpgradeDelay(double delay);

        SoundVP getUpgradeSound();

        void setUpgradeSound(SoundVP sound);

        ParticleEffect getUpgradeEffect();

        void setUpgradeEffect(ParticleEffect effect);

        Message getUpgradeMessage();

        void setUpgradeMessage(Message message);

        Collection<Drop> getDrops();

        Tier clone();

        String toString();

    }

    interface Drop extends Cloneable, PropertySerializable, Keyed<String> {

        BwItemStack getItem();

        void setItem(BwItemStack stack);

        double getDelay();

        void setDelay(double delay);

        int getMaxSpawn();

        void setMaxSpawn(int maxSpawn);

        Drop clone();

        String toString();
    }
}
