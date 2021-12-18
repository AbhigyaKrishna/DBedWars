package com.pepedevs.dbedwars.game;

import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;

import java.util.LinkedList;
import java.util.List;

public class TeamAssigner {

    private final Arena arena;

    public TeamAssigner(Arena arena) {
        this.arena = arena;
    }

    public void assign() {
        byte teamNumber = (byte) this.arena.getSettings().getAvailableTeams().size();
        int playerNumber = this.arena.getPlayers().size();

        int min = Math.min(playerNumber / teamNumber, this.arena.getSettings().getTeamPlayers());
        min = min == 0 ? 1 : min;

        byte i = 0;
        for (ArenaPlayer player : this.arena.getPlayers()) {
            if (player.getTeam() != null) continue;

            List<Team> teams = new LinkedList<>(this.arena.getSettings().getAvailableTeams());
            Team team = teams.get(i);
            if (team.getArena() == null) team.init(this.arena);
            if (team.getPlayers().size() >= min) {
                i++;
                team = teams.get(i);
                team.init(this.arena);
            }
            team.addPlayer(player);
        }
    }

}
