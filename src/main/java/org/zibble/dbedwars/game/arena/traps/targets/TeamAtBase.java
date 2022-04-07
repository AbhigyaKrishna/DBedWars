package org.zibble.dbedwars.game.arena.traps.targets;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.trap.Target;
import org.zibble.dbedwars.api.game.trap.Trap;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.key.LenientKey;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TeamAtBase implements Target {

    @Override
    public Collection<? extends ArenaPlayer> getTargets(Trap trap, ArenaPlayer trigger) {
        Set<ArenaPlayer> set = new HashSet<>();
        for (ArenaPlayer player : trap.getTrapOwner().getPlayers()) {
            if (trap.getTrapOwner().getIslandArea().contains(player.getPlayer().getLocation().toVector()))
                set.add(player);
        }
        return set;
    }

    @Override
    public Key getKey() {
        return LenientKey.of("team_at_base");
    }

}
