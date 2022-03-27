package org.zibble.dbedwars.api.hooks.npc;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.zibble.dbedwars.api.hooks.Hook;

public interface NPCFactory extends Hook {

    EntityNPC createEntityNPC(Location location, EntityType type);

    PlayerNPC createPlayerNPC(Location location);

}
