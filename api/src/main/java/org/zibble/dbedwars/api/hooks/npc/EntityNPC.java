package org.zibble.dbedwars.api.hooks.npc;

import org.bukkit.entity.EntityType;
import org.zibble.dbedwars.api.future.ActionFuture;

public interface EntityNPC extends BedwarsNPC {

    EntityType getEntityType();

    ActionFuture<EntityNPC> setBaby(boolean baby);

}
