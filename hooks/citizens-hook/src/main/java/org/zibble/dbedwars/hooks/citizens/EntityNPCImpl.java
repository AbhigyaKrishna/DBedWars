package org.zibble.dbedwars.hooks.citizens;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.hooks.npc.EntityNPC;

public class EntityNPCImpl extends BedWarsNPCImpl implements EntityNPC {

    private final EntityType type;

    public EntityNPCImpl(CitizensHook hook, Location location, EntityType type) {
        super(hook, location);
        this.type = type;
    }

    @Override
    public EntityType getEntityType() {
        return this.type;
    }

    @Override
    public ActionFuture<EntityNPC> setBaby(boolean baby) {
        // TODO: 06-07-2022
        return ActionFuture.completedFuture(this);
    }

    @Override
    public NPC createNPC() {
        return CitizensAPI.getNPCRegistry().createNPC(this.type, "");
    }

}
