package com.pepedevs.dbedwars.api.game.spawner;

import com.pepedevs.radium.particles.ParticleEffect;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import com.pepedevs.dbedwars.api.util.SoundVP;

import java.util.List;
import java.util.Map;

public interface DropType extends Cloneable {

    String getConfigId();

    String getId();

    String getSimpleName();

    BwItemStack getIcon();

    SoundVP getSpawnSound();

    void setSpawnSound(SoundVP sound);

    ParticleEffect getSpawnEffect();

    void setSpawnEffect(ParticleEffect effect);

    int getSpawnRadius();

    void setSpawnRadius(int radius);

    boolean isTeamSpawner();

    void setTeamSpawner(boolean flag);

    boolean isMerging();

    void setMerging(boolean flag);

    boolean isSplitable();

    void setSplitable(boolean flag);

    boolean isHologramEnabled();

    void setHologramEnabled(boolean flag);

    BwItemStack getHologramMaterial();

    void setHologramMaterial(BwItemStack stack);

    List<String> getHologramText();

    Tier getTier(int level);

    boolean hasTier(int level);

    Map<Integer, Tier> getTiers();

    boolean isRegistered();

    DropType clone();

    String toString();

    interface Tier extends Cloneable {

        double getUpgradeDelay();

        void setUpgradeDelay(double delay);

        SoundVP getUpgradeSound();

        void setUpgradeSound(SoundVP sound);

        ParticleEffect getUpgradeEffect();

        void setUpgradeEffect(ParticleEffect effect);

        String getUpgradeMessage();

        void setUpgradeMessage(String message);

        List<Drop> getDrops();

        Map<String, Drop> getDropMap();

        Tier clone();

        String toString();

    }

    interface Drop extends Cloneable {

        String getKey();

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
