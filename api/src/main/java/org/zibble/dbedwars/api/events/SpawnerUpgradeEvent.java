package org.zibble.dbedwars.api.events;

import com.pepedevs.radium.events.CustomEventCancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.spawner.DropInfo;
import org.zibble.dbedwars.api.game.spawner.Spawner;

public class SpawnerUpgradeEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private final DropInfo dropType;
    private final Spawner spawner;
    private final DropInfo.Tier currentTier;
    private DropInfo.Tier nextTier;

    public SpawnerUpgradeEvent(
            Arena arena,
            DropInfo dropType,
            Spawner spawner,
            DropInfo.Tier currentTier,
            DropInfo.Tier nextTier) {
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

    public DropInfo getDropType() {
        return dropType;
    }

    public Spawner getSpawner() {
        return spawner;
    }

    public DropInfo.Tier getCurrentTier() {
        return currentTier;
    }

    public DropInfo.Tier getNextTier() {
        return nextTier;
    }

    public void setNextTier(DropInfo.Tier nextTier) {
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
