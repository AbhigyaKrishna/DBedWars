package org.zibble.dbedwars.api.events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.DeathCause;
import org.zibble.dbedwars.api.messaging.message.Message;

public class PlayerFinalKillEvent extends PlayerKillEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public PlayerFinalKillEvent(
            ArenaPlayer victim,
            ArenaPlayer attacker,
            Arena arena,
            DeathCause deathCause,
            Message killMessage) {
        super(victim, attacker, arena, deathCause, killMessage);
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public String toString() {
        return "PlayerFinalKillEvent{" +
                "arena=" + this.getArena() +
                ", victim=" + this.getVictim() +
                ", attacker=" + this.getAttacker() +
                ", deathCause=" + this.getDeathCause() +
                ", killMessage=" + this.getKillMessage() +
                ", cancelled=" + this.isCancelled() +
                '}';
    }

}
