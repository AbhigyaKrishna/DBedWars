package org.zibble.dbedwars.features;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.game.arena.ArenaPlayerImpl;

import java.util.Collection;

public class DeathAnimationFeature extends org.zibble.dbedwars.api.feature.custom.DeathAnimationFeature {

    private final DBedwars plugin;

    public DeathAnimationFeature(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public void play(ArenaPlayer player, Collection<Player> viewers) {
        ((ArenaPlayerImpl) player).getVanishPlayer().vanishWithDeathAnimation(viewers.toArray(new Player[0]));
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

}
