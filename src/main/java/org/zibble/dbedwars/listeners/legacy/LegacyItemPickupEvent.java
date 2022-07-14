package org.zibble.dbedwars.listeners.legacy;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.zibble.dbedwars.proxiedevent.EntityItemPickUpEvent;

public class LegacyItemPickupEvent implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void handlePickup(PlayerPickupItemEvent event) {
        EntityItemPickUpEvent e = new EntityItemPickUpEvent(event);
        e.call();

        e.applyTo(event);
    }

}
