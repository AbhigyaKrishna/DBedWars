package org.zibble.dbedwars.api.game.spawner;

import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.hologram.AnimatedHologramModel;
import org.zibble.dbedwars.api.objects.serializable.ParticleEffectASC;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.api.util.key.Keyed;
import org.zibble.dbedwars.api.util.properies.PropertySerializable;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.Collection;

public interface DropInfo extends Cloneable, Keyed {

    BwItemStack getIcon();

    void setIcon(BwItemStack icon);

    SoundVP getSoundEffect();

    void setSoundEffect(SoundVP sound);

    ParticleEffectASC getParticleEffect();

    void setParticleEffect(ParticleEffectASC effect);

    int getSpawnRadius();

    void setSpawnRadius(int radius);

    AnimatedHologramModel getHologram();

    void setHologram(AnimatedHologramModel hologram);

    boolean isTeamSpawner();

    void setTeamSpawner(boolean flag);

    boolean isMerging();

    void setMerging(boolean flag);

    boolean isSplitable();

    void setSplitable(boolean flag);

    Tier getTier(int level);

    boolean hasTier(int level);

    Collection<Tier> getTiers();

    DropInfo clone();

    String toString();

    interface Tier extends Cloneable, PropertySerializable, Keyed {

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

    interface Drop extends Cloneable, PropertySerializable, Keyed {

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
