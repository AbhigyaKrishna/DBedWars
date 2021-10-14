package me.abhigya.dbedwars.api.events;

import me.Abhigya.core.events.player.CustomPlayerEventCancellable;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.util.KickReason;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerLeaveArenaLobbyEvent extends CustomPlayerEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList( );

    private final ArenaPlayer arenaPlayer;
    private final Arena arena;
    private KickReason reason;

    public PlayerLeaveArenaLobbyEvent( Player player, ArenaPlayer arenaPlayer, Arena arena, KickReason reason ) {
        super( player );
        this.arenaPlayer = arenaPlayer;
        this.arena = arena;
    }

    public static HandlerList getHandlerList( ) {
        return HANDLER_LIST;
    }

    public ArenaPlayer getAsArenaPlayer( ) {
        return arenaPlayer;
    }

    public Arena getArena( ) {
        return arena;
    }

    public KickReason getReason( ) {
        return reason;
    }

    public void setReason( KickReason reason ) {
        this.reason = reason;
    }

    @NotNull
    @Override
    public HandlerList getHandlers( ) {
        return HANDLER_LIST;
    }

}
