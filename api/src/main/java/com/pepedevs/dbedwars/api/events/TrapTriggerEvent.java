package com.pepedevs.dbedwars.api.events;

import me.Abhigya.core.events.CustomEventCancellable;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.game.Trap;
import org.bukkit.event.HandlerList;

public class TrapTriggerEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private Trap trap;
    private ArenaPlayer target;
    private Team team;

    public TrapTriggerEvent(Trap trap, ArenaPlayer target, Team team) {
        this.trap = trap;
        this.target = target;
        this.team = team;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Trap getTrap() {
        return trap;
    }

    public ArenaPlayer getTarget() {
        return target;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}