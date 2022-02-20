package org.zibble.dbedwars.api.events;

import com.pepedevs.radium.events.CustomEventCancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.DeathCause;
import org.zibble.dbedwars.api.messaging.message.Message;

public class PlayerKillEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private final ArenaPlayer victim;
    private ArenaPlayer attacker;
    private DeathCause deathCause;
    private Message killMessage;

    public PlayerKillEvent(
            ArenaPlayer victim,
            ArenaPlayer attacker,
            Arena arena,
            DeathCause deathCause,
            Message killMessage) {
        this.arena = arena;
        this.attacker = attacker;
        this.victim = victim;
        this.deathCause = deathCause;
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

    public Message getKillMessage() {
        return killMessage;
    }

    public void setKillMessage(Message killMessage) {
        this.killMessage = killMessage;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public String toString() {
        return "PlayerKillEvent{" +
                "arena=" + arena +
                ", victim=" + victim +
                ", attacker=" + attacker +
                ", deathCause=" + deathCause +
                ", killMessage=" + killMessage +
                ", cancelled=" + cancelled +
                '}';
    }
}