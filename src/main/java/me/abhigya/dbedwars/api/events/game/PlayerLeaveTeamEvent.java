package me.abhigya.dbedwars.api.events.game;

import me.Abhigya.core.events.player.CustomPlayerEventCancellable;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.game.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerLeaveTeamEvent extends CustomPlayerEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private final ArenaPlayer arenaPlayer;
    private final Team team;

    public PlayerLeaveTeamEvent(Player player, ArenaPlayer arenaPlayer, Arena arena, Team team) {
        super(player);
        this.arena = arena;
        this.arenaPlayer = arenaPlayer;
        this.team = team;
    }

    public Arena getArena() {
        return arena;
    }

    public ArenaPlayer getArenaPlayer() {
        return arenaPlayer;
    }

    public Team getTeam() {
        return team;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
