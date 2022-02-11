package com.pepedevs.dbedwars.api.npc;

import org.bukkit.entity.EntityType;

public abstract class EntityNPC implements BedwarsNPC {

    private final EntityType entityType;
    private final String ID;

    public EntityNPC(String ID, EntityType entityType) {
        this.ID = ID;
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    @Override
    public String getID() {
        return this.ID;
    }
}
