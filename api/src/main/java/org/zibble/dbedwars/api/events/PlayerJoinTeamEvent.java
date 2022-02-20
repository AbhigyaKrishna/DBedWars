package org.zibble.dbedwars.api.events;

import com.pepedevs.radium.events.player.CustomPlayerEventCancellable;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.Team;

public class PlayerJoinTeamEvent extends CustomPlayerEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private final ArenaPlayer arenaPlayer;
    private Team team;

    public PlayerJoinTeamEvent(Player player, ArenaPlayer arenaPlayer, Arena arena, Team team) {
        super(player);
        this.arena = arena;
        this.arenaPlayer = arenaPlayer;
        this.team = team;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Arena getArena() {
        return arena;
    }

    public ArenaPlayer getAsArenaPlayer() {
        return arenaPlayer;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public String toString() {
        return "PlayerJoinTeamEvent{" +
                "arena=" + arena +
                ", arenaPlayer=" + arenaPlayer +
                ", team=" + team +
                ", cancelled=" + cancelled +
                ", player=" + player +
                '}';
    }
}