package com.pepedevs.dbedwars.api.npc;

import org.bukkit.entity.EntityType;

public abstract class EntityNPC <T extends EntityType> implements BedwarsNPC {

    private final T entityType;
    private final String ID;

    public EntityNPC(String ID, T entityType) {
        this.ID = ID;
        this.entityType = entityType;
    }

    public T getEntityType(){
        return this.entityType;
    }

    @Override
    public String getID() {
        return this.ID;
    }
}
