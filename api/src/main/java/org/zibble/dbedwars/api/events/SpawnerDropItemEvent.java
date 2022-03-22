package org.zibble.dbedwars.api.events;

import com.pepedevs.radium.events.CustomEventCancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.spawner.DropInfo;
import org.zibble.dbedwars.api.game.spawner.ResourceItem;
import org.zibble.dbedwars.api.game.spawner.Spawner;

public class SpawnerDropItemEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private final DropInfo dropType;
    private final Spawner spawner;
    private final DropInfo.Drop drop;
    private ResourceItem resourceItem;

    public SpawnerDropItemEvent(
            Arena arena,
            DropInfo dropType,
            DropInfo.Drop drop,
            ResourceItem resourceItem,
            Spawner spawner) {
        this.arena = arena;
        this.dropType = dropType;
        this.drop = drop;
        this.resourceItem = resourceItem;
        this.spawner = spawner;
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

    public DropInfo.Drop getDrop() {
        return drop;
    }

    public ResourceItem getResourceItem() {
        return resourceItem;
    }

    public void setResourceItem(ResourceItem resourceItem) {
        this.resourceItem = resourceItem;
    }

    public Spawner getSpawner() {
        return spawner;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public String toString() {
        return "SpawnerDropItemEvent{" +
                "arena=" + arena +
                ", dropType=" + dropType +
                ", spawner=" + spawner +
                ", drop=" + drop +
                ", resourceItem=" + resourceItem +
                ", cancelled=" + cancelled +
                '}';
    }
}
