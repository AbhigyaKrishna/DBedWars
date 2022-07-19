package org.zibble.dbedwars.hooks.defaults.npc;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnLivingEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.hooks.npc.EntityNPC;
import org.zibble.dbedwars.api.util.key.Key;

import java.util.Collections;

public class EntityNPCImpl extends BedWarsNPCImpl implements EntityNPC {

    private final EntityType entityType;

    public EntityNPCImpl(NPCFactoryImpl factory, Key key, EntityType entityType, Location location) {
        super(factory, key, location);
        this.entityType = entityType;
    }

    @Override
    public EntityType getEntityType() {
        return this.entityType;
    }

    @Override
    public ActionFuture<EntityNPC> setBaby(boolean baby) {
        return ActionFuture.supplyAsync(() -> {
            this.sendMetaDataPacket(Collections.singletonList(new EntityData(16, EntityDataTypes.BOOLEAN, baby)));
            return this;
        });
    }

    @Override
    protected void viewPacket(Player player) {
        WrapperPlayServerSpawnLivingEntity packet = new WrapperPlayServerSpawnLivingEntity(this.getEntityID(),
                this.getUUID(),
                SpigotConversionUtil.fromBukkitEntityType(this.entityType),
                SpigotConversionUtil.fromBukkitLocation(this.getLocation()),
                this.getLocation().getYaw(),
                Vector3d.zero(),
                Collections.emptyList()
        );
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

}
