package me.abhigya.dbedwars.api.events;

import me.Abhigya.core.events.CustomEventCancellable;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.Team;
import me.abhigya.dbedwars.api.game.spawner.DropType;
import me.abhigya.dbedwars.api.game.spawner.Spawner;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpawnerDropItemEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList( );

    private final Arena arena;
    private final DropType dropType;
    private final Spawner spawner;
    private final Team team;
    private final int level;
    private final DropType.Tier tier;
    private DropType.Drop drop;

    public SpawnerDropItemEvent( Arena arena, DropType dropType, DropType.Drop drop, Spawner spawner, Team team, int level, DropType.Tier tier ) {
        this.arena = arena;
        this.dropType = dropType;
        this.drop = drop;
        this.spawner = spawner;
        this.team = team;
        this.level = level;
        this.tier = tier;
    }

    public static HandlerList getHandlerList( ) {
        return HANDLER_LIST;
    }

    public Arena getArena( ) {
        return arena;
    }

    public DropType getDropType( ) {
        return dropType;
    }

    public DropType.Drop getDrop( ) {
        return drop;
    }

    public void setDrop( DropType.Drop drop ) {
        this.drop = drop;
    }

    public Spawner getSpawner( ) {
        return spawner;
    }

    public boolean isTeamSpawner( ) {
        return this.team != null;
    }

    @Nullable
    public Team getTeam( ) {
        return team;
    }

    public int getLevel( ) {
        return level;
    }

    public DropType.Tier getTier( ) {
        return tier;
    }

    @NotNull
    @Override
    public HandlerList getHandlers( ) {
        return HANDLER_LIST;
    }

}
