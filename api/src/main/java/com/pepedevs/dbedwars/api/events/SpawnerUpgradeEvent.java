package com.pepedevs.dbedwars.api.events;

import com.pepedevs.corelib.events.CustomEventCancellable;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.game.spawner.DropType;
import com.pepedevs.dbedwars.api.game.spawner.Spawner;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpawnerUpgradeEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private final DropType dropType;
    private final Spawner spawner;
    private final Team team;
    private final int currentLevel;
    private int nextLevel;
    private DropType.Tier currentTier;
    private DropType.Tier nextTier;

    public SpawnerUpgradeEvent(
            Arena arena,
            DropType dropType,
            Spawner spawner,
            Team team,
            int currentLevel,
            int nextLevel,
            DropType.Tier currentTier,
            DropType.Tier nextTier) {
        this.arena = arena;
        this.dropType = dropType;
        this.spawner = spawner;
        this.team = team;
        this.currentLevel = currentLevel;
        this.nextLevel = nextLevel;
        this.currentTier = currentTier;
        this.nextTier = nextTier;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Arena getArena() {
        return arena;
    }

    public DropType getDropType() {
        return dropType;
    }

    public Spawner getSpawner() {
        return spawner;
    }

    public boolean isTeamSpawner() {
        return this.team != null;
    }

    @Nullable
    public Team getTeam() {
        return team;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(int nextLevel) {
        this.nextLevel = nextLevel;
        this.nextTier = this.dropType.getTier(this.nextLevel);
    }

    public DropType.Tier getCurrentTier() {
        return currentTier;
    }

    public DropType.Tier getNextTier() {
        return nextTier;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
