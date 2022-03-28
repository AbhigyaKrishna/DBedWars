package org.zibble.dbedwars.game;

import org.zibble.dbedwars.api.util.RandomList;
import org.zibble.dbedwars.game.arena.ArenaImpl;
import org.zibble.dbedwars.game.arena.ArenaPlayerImpl;
import org.zibble.dbedwars.game.arena.TeamImpl;

public class TeamAssigner {

    private final ArenaImpl arena;

    public TeamAssigner(ArenaImpl arena) {
        this.arena = arena;
    }

    public void assign() {
        int teamNumber = this.arena.getTeams().size();
        int playerNumber = this.arena.getPlayers().size();

        int min = Math.min(playerNumber / teamNumber, this.arena.getDataHolder().getMaxPlayersPerTeam());
        int finalMin = min == 0 ? 1 : min;

        RandomList<TeamImpl> teams = new RandomList<>(this.arena.getTeams());
        for (ArenaPlayerImpl player : this.arena.getPlayers()) {
            if (player.getTeam() != null) {
                TeamImpl team = player.getTeam();
                team.addPlayer(player);
                continue;
            }

            TeamImpl team = teams.randomValue(t -> t.getPlayers().size() < finalMin);
            team.addPlayer(player);
        }
    }
}
