package com.pepedevs.dbedwars.features;

import com.pepedevs.dbedwars.DBedwars;
import org.bukkit.entity.Player;

import java.util.Collection;

public class DeathAnimationFeature extends com.pepedevs.dbedwars.api.feature.custom.DeathAnimationFeature {

    private final DBedwars plugin;

    public DeathAnimationFeature(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public void play(Player player, Collection<Player> viewers) {
        this.plugin.getNMSAdaptor().sendDeathAnimation(player, viewers);
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

}
