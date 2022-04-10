package org.zibble.dbedwars.api.events;

import org.bukkit.event.HandlerList;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.trap.Trap;

public class TrapTriggerEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private Trap trap;
    private ArenaPlayer target;

    public TrapTriggerEvent(Trap trap, ArenaPlayer target) {
        super(true);
        this.trap = trap;
        this.target = target;
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

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public String toString() {
        return "TrapTriggerEvent{" +
                "trap=" + trap +
                ", target=" + target +
                ", cancelled=" + cancelled +
                '}';
    }

}
