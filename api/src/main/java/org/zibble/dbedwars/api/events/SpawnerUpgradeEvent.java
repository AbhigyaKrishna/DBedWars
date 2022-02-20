package org.zibble.dbedwars.api.events;

import com.pepedevs.radium.events.CustomEventCancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.spawner.DropType;
import org.zibble.dbedwars.api.game.spawner.Spawner;

public class SpawnerUpgradeEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private final DropType dropType;
    private final Spawner spawner;
    private final DropType.Tier currentTier;
    private DropType.Tier nextTier;

    public SpawnerUpgradeEvent(
            Arena arena,
            DropType dropType,
            Spawner spawner,
            DropType.Tier currentTier,
            DropType.Tier nextTier) {
        this.arena = arena;
        this.dropType = dropType;
        this.spawner = spawner;
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

    public DropType.Tier getCurrentTier() {
        return currentTier;
    }

    public DropType.Tier getNextTier() {
        return nextTier;
    }

    public void setNextTier(DropType.Tier nextTier) {
        this.nextTier = nextTier;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public String toString() {
        return "SpawnerUpgradeEvent{" +
                "arena=" + arena +
                ", dropType=" + dropType +
                ", spawner=" + spawner +
                ", currentTier=" + currentTier +
                ", nextTier=" + nextTier +
                ", cancelled=" + cancelled +
                '}';
    }
}
