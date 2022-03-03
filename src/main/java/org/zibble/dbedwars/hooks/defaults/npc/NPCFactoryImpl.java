package org.zibble.dbedwars.hooks.defaults.npc;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.zibble.dbedwars.api.hooks.npc.EntityNPC;
import org.zibble.dbedwars.api.hooks.npc.NPCFactory;
import org.zibble.dbedwars.api.hooks.npc.PlayerNPC;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NPCFactoryImpl implements NPCFactory {

    private final Map<String, BedwarsNPCImpl> npcs = new ConcurrentHashMap<>();
    private PacketListenerAbstract listener;

    @Override
    public void init() {
        PacketEvents.getAPI().getEventManager().registerListener(listener = new NPCPacketListener(this));
    }

    @Override
    public EntityNPC createEntityNPC(Location location, Component name, EntityType type) {
        EntityNPCImpl npc = new EntityNPCImpl(UUID.randomUUID().toString().substring(0, 8), type, location, new NPCDataImpl(), name);
        npcs.put(npc.getID(), npc);
        return npc;
    }

    @Override
    public PlayerNPC createPlayerNPC(Location location, Component name) {
        PlayerNPCImpl npc = new PlayerNPCImpl(UUID.randomUUID().toString().substring(0, 8), location, new NPCDataImpl(), new SkinDataImpl(), name);
        npcs.put(npc.getID(), npc);
        return npc;
    }

    public Map<String, BedwarsNPCImpl> getNpcs() {
        return npcs;
    }

    @Override
    public void disable() {
        for (BedwarsNPCImpl value : this.npcs.values()) {
            value.destroy();
        }
        this.npcs.clear();
        PacketEvents.getAPI().getEventManager().unregisterListener(this.listener);
    }

}
