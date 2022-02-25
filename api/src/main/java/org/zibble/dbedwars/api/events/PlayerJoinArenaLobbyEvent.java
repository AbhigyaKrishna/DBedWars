package org.zibble.dbedwars.api.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.events.player.CustomPlayerEventCancellable;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.messaging.message.Message;

public class PlayerJoinArenaLobbyEvent extends CustomPlayerEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private Location location;
    private Message joinMessage;

    public PlayerJoinArenaLobbyEvent(Player player, Arena arena, Location location, Message joinMessage) {
        super(player);
        this.arena = arena;
        this.location = location;
        this.joinMessage = joinMessage;
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

    public Message getJoinMessage() {
        return joinMessage;
    }

    public void setJoinMessage(Message joinMessage) {
        this.joinMessage = joinMessage;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
