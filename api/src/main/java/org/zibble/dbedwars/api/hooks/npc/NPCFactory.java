package org.zibble.dbedwars.api.hooks.npc;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.zibble.dbedwars.api.hooks.Hook;

public interface NPCFactory extends Hook {

    EntityNPC createEntityNPC(Location location, Component name, EntityType type);

    PlayerNPC createPlayerNPC(Location location, Component name);

}
