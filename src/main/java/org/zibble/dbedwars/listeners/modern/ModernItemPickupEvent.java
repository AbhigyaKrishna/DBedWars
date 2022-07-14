package org.zibble.dbedwars.listeners.modern;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.zibble.dbedwars.proxiedevent.EntityItemPickUpEvent;

public class ModernItemPickupEvent implements Listener {

    @EventHandler
    public void handlePickup(EntityPickupItemEvent event) {
        EntityItemPickUpEvent e = new EntityItemPickUpEvent(event);
        e.call();

        e.applyTo(event);
    }

}
