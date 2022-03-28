package org.zibble.dbedwars.api.game.statistics;

import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.Team;

import java.util.Collection;
import java.util.HashSet;

public class BedBrokenStatistics extends Statistics<Arena, ArenaPlayer, Collection<Team>> {

    public BedBrokenStatistics(Arena tracker) {
        super(tracker);
    }

    public void add(ArenaPlayer player, Team team) {
        if (this.containsKey(player)) {
            this.get(player).add(team);
        } else {
            this.put(player, new HashSet<>());
            this.get(player).add(team);
        }
    }

}
