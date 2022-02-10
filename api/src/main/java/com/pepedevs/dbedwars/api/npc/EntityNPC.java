package com.pepedevs.dbedwars.api.npc;

import org.bukkit.entity.EntityType;

public abstract class EntityNPC <T extends EntityType> extends BedwarsNPC{

    private final T entityType;

    public EntityNPC(String ID, T entityType) {
        super(ID);
        this.entityType = entityType;
    }

    public T getEntityType(){
        return this.entityType;
    }

}
