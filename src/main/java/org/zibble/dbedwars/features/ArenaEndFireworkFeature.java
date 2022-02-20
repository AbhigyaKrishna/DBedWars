package org.zibble.dbedwars.features;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.FeaturePriority;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.ArenaStatus;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.objects.serializable.FireworkEffectC;
import org.zibble.dbedwars.utils.Utils;

import java.util.Collections;
import java.util.function.Predicate;

public class ArenaEndFireworkFeature extends org.zibble.dbedwars.api.feature.custom.ArenaEndFireworkFeature {

    private final DBedwars plugin;

    public ArenaEndFireworkFeature(DBedwars plugin) {
        this.plugin = plugin;
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
        FireworkEffectC effect = new FireworkEffectC(FireworkEffect.Type.BALL, true, false, Collections.singletonList(winner.getColor().getColor()), Collections.singletonList(Color.YELLOW));
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (arena.getStatus() == ArenaStatus.ENDING && arena.getWorld() != null) {
                    for (ArenaPlayer player : arena.getPlayers()) {
                        if (player.isFinalKilled())
                            continue;
                        Location loc = Utils.getRandomPointAround(player.getPlayer().getLocation(), 30, new Predicate<Location>() {
                            @Override
                            public boolean test(Location location) {
                                return location.getY() > player.getPlayer().getLocation().getY() + 10;
                            }
                        });
                        effect.spawn(loc);
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
