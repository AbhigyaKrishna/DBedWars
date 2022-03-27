package org.zibble.dbedwars.hooks.defaults.npc;

import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.hooks.npc.EntityNPC;
import org.zibble.dbedwars.api.hooks.npc.NPCFactory;
import org.zibble.dbedwars.api.hooks.npc.PlayerNPC;
import org.zibble.dbedwars.api.messaging.message.Message;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NPCFactoryImpl implements NPCFactory {

    private final Map<String, BedWarsNPCImpl> npcs = new ConcurrentHashMap<>();
    private NPCListener listener;

    @Override
    public void init() {
        PacketEvents.getAPI().getEventManager().registerListener(listener = new NPCListener(this));
        Bukkit.getPluginManager().registerEvents(this.listener, DBedwars.getInstance());
    }

    @Override
    public EntityNPC createEntityNPC(Location location, EntityType type) {
        EntityNPCImpl npc = new EntityNPCImpl(UUID.randomUUID().toString().substring(0, 8), type, location, new NPCDataImpl());
        npcs.put(npc.getID(), npc);
        return npc;
    }

    @Override
    public PlayerNPC createPlayerNPC(Location location) {
        PlayerNPCImpl npc = new PlayerNPCImpl(UUID.randomUUID().toString().substring(0, 8), location, new NPCDataImpl(), new SkinDataImpl());
        npcs.put(npc.getID(), npc);
        return npc;
    }

    public Map<String, BedWarsNPCImpl> getNpcs() {
        return npcs;
    }

    @Override
    public void disable() {
        for (BedWarsNPCImpl value : this.npcs.values()) {
            value.destroy();
        }
        this.npcs.clear();
        PacketEvents.getAPI().getEventManager().unregisterListener(this.listener);
        HandlerList.unregisterAll(this.listener);
    }

}
