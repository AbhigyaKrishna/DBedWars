package org.zibble.dbedwars.script.action.impl;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.zibble.dbedwars.api.script.action.Action;

public class TeleportAction implements Action {

    private final Entity entity;
    private final Location location;

    public TeleportAction(Location location, Entity entity) {
        this.entity = entity;
        this.location = location;
    }

    @Override
    public void execute() {
        this.getEntity().teleport(this.location);
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Location getLocation() {
        return this.location;
    }

}
