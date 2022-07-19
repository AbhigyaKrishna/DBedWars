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
import org.zibble.dbedwars.api.task.Task;
import org.zibble.dbedwars.api.util.key.Key;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NPCFactoryImpl implements NPCFactory {

    private final Map<Key, BedWarsNPCImpl> npcs = new ConcurrentHashMap<>();
    private NPCListener listener;
    Task thread;

    @Override
    public void init() {
        this.thread = DBedwars.getInstance().getThreadHandler().getTaskHandler().newPool(1, 3 * 1000000L);
        PacketEvents.getAPI().getEventManager().registerListener(listener = new NPCListener(this));
        Bukkit.getPluginManager().registerEvents(this.listener, DBedwars.getInstance());
    }

    @Override
    public EntityNPC createEntityNPC(Location location, EntityType type) {
        EntityNPCImpl npc = new EntityNPCImpl(this, Key.of(UUID.randomUUID().toString().substring(0, 8)), type, location);
        npcs.put(npc.getKey(), npc);
        return npc;
    }

    @Override
    public PlayerNPC createPlayerNPC(Location location) {
        PlayerNPCImpl npc = new PlayerNPCImpl(this, Key.of(UUID.randomUUID().toString().substring(0, 8)), location);
        npcs.put(npc.getKey(), npc);
        return npc;
    }

    public Map<Key, BedWarsNPCImpl> getNpcs() {
        return npcs;
    }

    @Override
    public void disable() {
        for (BedWarsNPCImpl value : this.npcs.values()) {
            value.destroy();
        }
        this.npcs.clear();
        this.thread.cancel();
        PacketEvents.getAPI().getEventManager().unregisterListener(this.listener);
        HandlerList.unregisterAll(this.listener);
    }

}
