package me.abhigya.dbedwars.api.events;

import me.Abhigya.core.events.CustomEventCancellable;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public class ArenaStartEvent extends CustomEventCancellable {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  private Arena arena;
  private Set<ArenaPlayer> players;

  public ArenaStartEvent(Arena arena) {
    this.arena = arena;
    this.players = arena.getPlayers();
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  public Arena getArena() {
    return this.arena;
  }

  public Set<ArenaPlayer> getPlayers() {
    return Collections.unmodifiableSet(this.players);
  }

  @NotNull
  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
