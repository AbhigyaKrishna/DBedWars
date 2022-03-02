package org.zibble.dbedwars.hooks.defaults.npc;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.zibble.dbedwars.api.hooks.npc.EntityNPC;
import org.zibble.dbedwars.api.hooks.npc.NPCFactory;
import org.zibble.dbedwars.api.hooks.npc.PlayerNPC;

import java.util.UUID;

public class NPCFactoryImpl implements NPCFactory {

    @Override
    public EntityNPC createEntityNPC(Location location, Component name, EntityType type) {
        return new EntityNPCImpl(UUID.randomUUID().toString().substring(0, 8), type, location, new NPCDataImpl(), name);
    }

    @Override
    public PlayerNPC createPlayerNPC(Location location, Component name) {
        return new PlayerNPCImpl(UUID.randomUUID().toString().substring(0, 8), location, new NPCDataImpl(), new SkinDataImpl(), name);
    }

}
