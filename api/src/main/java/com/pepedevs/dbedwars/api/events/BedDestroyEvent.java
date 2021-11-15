package com.pepedevs.dbedwars.api.events;

import me.Abhigya.core.events.CustomEventCancellable;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BedDestroyEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private final Block bed;
    private ArenaPlayer destroyer;
    private Team affectedTeam;
    private String bedBrokenMessage;
    private String bedBrokenTeamMessage;

    public BedDestroyEvent(
            ArenaPlayer destroyer,
            Team affectedTeam,
            Block bed,
            Arena arena,
            String msg,
            String teamMsg) {
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

    public String getBedBrokenMessage() {
        return bedBrokenMessage;
    }

    public void setBedBrokenMessage(String bedBrokenMessage) {
        this.bedBrokenMessage = bedBrokenMessage;
    }

    public String getBedBrokenTeamMessage() {
        return bedBrokenTeamMessage;
    }

    public void setBedBrokenTeamMessage(String bedBrokenTeamMessage) {
        this.bedBrokenTeamMessage = bedBrokenTeamMessage;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
