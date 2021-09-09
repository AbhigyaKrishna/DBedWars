package me.abhigya.dbedwars.api.events.game;

import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.game.DeathCause;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerFinalKillEvent extends PlayerKillEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public PlayerFinalKillEvent(ArenaPlayer victim, ArenaPlayer attacker, Arena arena, DeathCause deathCause, String killMessage) {
        super(victim, attacker, arena, deathCause, killMessage);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
