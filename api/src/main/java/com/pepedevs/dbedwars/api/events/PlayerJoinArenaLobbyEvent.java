package com.pepedevs.dbedwars.api.events;

import com.pepedevs.radium.events.player.CustomPlayerEventCancellable;
import com.pepedevs.dbedwars.api.game.Arena;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinArenaLobbyEvent extends CustomPlayerEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private Location location;

    public PlayerJoinArenaLobbyEvent(Player player, Arena arena, Location location) {
        super(player);
        this.arena = arena;
        this.location = location;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Arena getArena() {
        return arena;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
