package org.zibble.dbedwars.hooks.defaults.npc;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnLivingEntity;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.hooks.npc.EntityNPC;
import org.zibble.dbedwars.api.hooks.npc.NPCData;

import java.util.ArrayList;

public class EntityNPCImpl extends BedwarsNPCImpl implements EntityNPC {

    private final EntityType entityType;

    public EntityNPCImpl(String ID, EntityType entityType, Component name, Location location, NPCData npcData) {
        super(ID, location, npcData, name);
        this.entityType = entityType;
    }

    @Override
    protected void viewPacket(Player player) {
        WrapperPlayServerSpawnLivingEntity packet = new WrapperPlayServerSpawnLivingEntity(this.getEntityID(),
                this.getUUID(),
                EntityTypes.getByName(this.entityType.name().toLowerCase()),
                super.convert(this.getLocation()),
                this.getLocation().getYaw(),
                this.getLocation().getPitch(),
                this.getLocation().getYaw(),
                Vector3d.zero(),
                new ArrayList<>()
        );
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

    @Override
    public EntityType getEntityType() {
        return this.entityType;
    }
}
