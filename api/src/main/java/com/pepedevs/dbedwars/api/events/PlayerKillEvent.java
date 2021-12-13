package com.pepedevs.dbedwars.api.events;

import com.pepedevs.corelib.events.CustomEventCancellable;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.DeathCause;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerKillEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private final ArenaPlayer victim;
    private ArenaPlayer attacker;
    private DeathCause deathCause;
    private String killMessage;

    public PlayerKillEvent(
            ArenaPlayer victim,
            ArenaPlayer attacker,
            Arena arena,
            DeathCause deathCause,
            String killMessage) {
        this.arena = arena;
        this.attacker = attacker;
        this.victim = victim;
        this.killMessage = killMessage;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Arena getArena() {
        return arena;
    }

    public ArenaPlayer getVictim() {
        return victim;
    }

    public ArenaPlayer getAttacker() {
        return attacker;
    }

    public void setAttacker(ArenaPlayer attacker) {
        this.attacker = attacker;
    }

    public DeathCause getDeathCause() {
        return deathCause;
    }

    public void setDeathCause(DeathCause deathCause) {
        this.deathCause = deathCause;
    }

    public String getKillMessage() {
        return killMessage;
    }

    public void setKillMessage(String killMessage) {
        this.killMessage = killMessage;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
