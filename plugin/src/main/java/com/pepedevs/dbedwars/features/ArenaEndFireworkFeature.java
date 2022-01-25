package com.pepedevs.dbedwars.features;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.feature.FeaturePriority;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.ArenaStatus;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.util.FireworkEffectAT;
import com.pepedevs.dbedwars.utils.Utils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

public class ArenaEndFireworkFeature extends com.pepedevs.dbedwars.api.feature.custom.ArenaEndFireworkFeature {

    private final DBedwars plugin;

    private FireworkEffectAT effect;

    public ArenaEndFireworkFeature(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadFromConfig() {
        this.effect = new FireworkEffectAT(FireworkEffect.Type.BALL, true, false, new ArrayList<>(), Collections.singletonList(Color.YELLOW));
    }

    @Override
    public boolean isTickable() {
        return false;
    }

    @Override
    public FeaturePriority getPriority() {
        return FeaturePriority.NORMAL;
    }

    @Override
    public void spawn(Team winner, Arena arena) {
        this.effect.addBaseColors(winner.getColor().getColor());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (arena.getStatus() == ArenaStatus.ENDING) {
                    for (ArenaPlayer player : arena.getPlayers()) {
                        if (player.isFinalKilled())
                            continue;
                        Location loc = Utils.getRandomPointAround(player.getPlayer().getLocation(), 30, new Predicate<Location>() {
                            @Override
                            public boolean test(Location location) {
                                return location.getY() > player.getPlayer().getLocation().getY() + 10;
                            }
                        });
                        ArenaEndFireworkFeature.this.effect.spawn(loc);
                    }
                    ArenaEndFireworkFeature.this.plugin.getThreadHandler().runTaskLater(this, 40L);
                }
            }
        };
        this.plugin.getThreadHandler().runTaskLater(runnable, 40L);
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

}
