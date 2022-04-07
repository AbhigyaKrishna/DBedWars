package org.zibble.dbedwars.game.arena.traps.targets;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.game.trap.Target;
import org.zibble.dbedwars.api.game.trap.Trap;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.key.LenientKey;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AllEnemy implements Target {

    @Override
    public Collection<? extends ArenaPlayer> getTargets(Trap trap, ArenaPlayer trigger) {
        Set<ArenaPlayer> players = new HashSet<>();
        for (Team team : trigger.getArena().getTeams()) {
            players.addAll(team.getPlayers());
        }
        players.removeAll(trap.getTrapOwner().getPlayers());
        return players;
    }

    @Override
    public Key getKey() {
        return LenientKey.of("all_enemy");
    }

}
