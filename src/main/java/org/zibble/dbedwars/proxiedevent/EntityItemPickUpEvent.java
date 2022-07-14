package org.zibble.dbedwars.proxiedevent;

import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.events.CustomEventCancellable;

public class EntityItemPickUpEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final LivingEntity entity;
    private final Item item;
    private final int remaining;

    public EntityItemPickUpEvent(PlayerPickupItemEvent event) {
        this.entity = event.getPlayer();
        this.item = event.getItem();
        this.remaining = event.getRemaining();
    }

    public EntityItemPickUpEvent(EntityPickupItemEvent event) {
        this.entity = event.getEntity();
        this.item = event.getItem();
        this.remaining = event.getRemaining();
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public Item getItem() {
        return this.item;
    }

    public int getRemaining() {
        return this.remaining;
    }

    public void applyTo(PlayerPickupItemEvent event) {
        event.setCancelled(this.isCancelled());
    }

    public void applyTo(EntityPickupItemEvent event) {
        event.setCancelled(this.isCancelled());
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
