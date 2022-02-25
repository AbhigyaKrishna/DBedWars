package org.zibble.dbedwars.hooks.citizens.npc;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.zibble.dbedwars.api.hooks.npc.EntityNPC;
import org.zibble.dbedwars.api.hooks.npc.NPCData;

public class EntityNPCImpl extends BedwarsNPCImpl implements EntityNPC {

    private final EntityType type;

    public EntityNPCImpl(Location location, EntityType type, NPCData npcData) {
        super(location, npcData);
        this.type = type;
    }

    @Override
    public EntityType getEntityType() {
        return this.type;
    }

    @Override
    public NPC createNPC() {
        return CitizensAPI.getNPCRegistry().createNPC(this.type, "");
    }
}
