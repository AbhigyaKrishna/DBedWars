package com.pepedevs.dbedwars.api.events;

import me.Abhigya.core.events.player.CustomPlayerEventCancellable;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.util.KickReason;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerRemoveFromArenaEvent extends CustomPlayerEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ArenaPlayer arenaPlayer;
    private final Arena arena;
    private final Team team;
    private KickReason reason;

    public PlayerRemoveFromArenaEvent(
            Player player, ArenaPlayer arenaPlayer, Arena arena, Team team, KickReason reason) {
        super(player);
        this.arenaPlayer = arenaPlayer;
        this.arena = arena;
        this.team = team;
        this.reason = reason;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public ArenaPlayer getAsArenaPlayer() {
        return arenaPlayer;
    }

    public Arena getArena() {
        return arena;
    }

    public Team getTeam() {
        return team;
    }

    public KickReason getReason() {
        return reason;
    }

    public void setReason(KickReason reason) {
        this.reason = reason;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
