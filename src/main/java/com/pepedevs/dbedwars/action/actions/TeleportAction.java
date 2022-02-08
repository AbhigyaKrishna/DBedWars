package com.pepedevs.dbedwars.action.actions;

import com.pepedevs.dbedwars.api.action.Action;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class TeleportAction implements Action<Entity> {

    private final Entity entity;
    private final Location location;

    public TeleportAction(Location location, Entity entity) {
        this.entity = entity;
        this.location = location;
    }

    @Override
    public void execute() {
        this.getHandle().teleport(this.location);
    }

    @Override
    public Entity getHandle() {
        return this.entity;
    }

    public Location getLocation() {
        return this.location;
    }

}
