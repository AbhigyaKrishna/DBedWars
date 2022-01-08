package com.pepedevs.dbedwars.api.events;

import com.pepedevs.corelib.events.CustomEventCancellable;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BedDestroyEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private final Block bed;
    private ArenaPlayer destroyer;
    private Team affectedTeam;
    private Message bedBrokenMessage;
    private Message bedBrokenTeamMessage;

    public BedDestroyEvent(
            ArenaPlayer destroyer,
            Team affectedTeam,
            Block bed,
            Arena arena,
            Message msg,
            Message teamMsg) {
        this.arena = arena;
        this.bed = bed;
        this.destroyer = destroyer;
        this.affectedTeam = affectedTeam;
        this.bedBrokenMessage = msg;
        this.bedBrokenTeamMessage = teamMsg;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Arena getArena() {
        return arena;
    }

    public Block getBed() {
        return bed;
    }

    public ArenaPlayer getDestroyer() {
        return destroyer;
    }

    public void setDestroyer(ArenaPlayer destroyer) {
        this.destroyer = destroyer;
    }

    public Team getAffectedTeam() {
        return affectedTeam;
    }

    public void setAffectedTeam(Team affectedTeam) {
        this.affectedTeam = affectedTeam;
    }

    public Message getBedBrokenMessages() {
        return bedBrokenMessage;
    }

    public void setBedBrokenMessages(Message bedBrokenMessages) {
        this.bedBrokenMessage = bedBrokenMessages;
    }

    public Message getBedBrokenTeamMessages() {
        return bedBrokenTeamMessage;
    }

    public void setBedBrokenTeamMessages(Message bedBrokenTeamMessage) {
        this.bedBrokenTeamMessage = bedBrokenTeamMessage;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
