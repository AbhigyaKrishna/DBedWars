package com.pepedevs.dbedwars.hooks.defaults.hologram;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.hooks.hologram.HologramLine;
import com.pepedevs.dbedwars.api.task.Task;
import com.pepedevs.dbedwars.api.task.Workload;
import com.pepedevs.dbedwars.task.TaskQueueHandler;
import org.bukkit.Location;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HologramManager {

    private static final HologramManager INSTANCE = new HologramManager();

    private final DBedwars plugin;
    private final Task thread;
    private final Map<String, HologramDataHolder> holograms;
    private final PacketEventsAPI<?> packetEventsAPI;

    private HologramManager() {
        this.plugin = DBedwars.getInstance();
        this.thread = new TaskQueueHandler("Hologram Thread %d").newPool(1, 3 * 1000000L);
        this.holograms = Collections.synchronizedMap(new HashMap<>());
        HologramPacketListener packetListener = new HologramPacketListener(this);
        this.packetEventsAPI = PacketEvents.getAPI();
        this.packetEventsAPI.getEventManager().registerListener(packetListener);
    }

    public void spawnHologram(HologramImpl hologram) {
        HologramDataHolder holder = null;
        for (HologramDataHolder value : this.holograms.values()) {
            if (value.getHologram().equals(hologram)) {
                holder = value;
                break;
            }
        }
        if (holder == null) throw new IllegalArgumentException("Hologram not found! Try creating it first");
        holder.setSpawned(true);
    }

    public HologramImpl createHologram(Location location) {
        HologramDataHolder holder = new HologramDataHolder(new HologramImpl(location), false);
        this.holograms.put(UUID.randomUUID().toString(), holder);
        return holder.getHologram();
    }

    public void registerHologramListener(HologramImpl hologram) {
        for (HologramDataHolder value : this.holograms.values()) {
            if (value.getHologram().equals(hologram)) {
                value.setClickRegistered(true);
                break;
            }
        }
    }

    public void registerHologramUpdates(HologramImpl hologram) {
        for (HologramDataHolder value : this.holograms.values()) {
            if (value.getHologram().equals(hologram)) {
                value.setUpdateRegistered(true);
                break;
            }
        }
    }

    public void unregisterHologramUpdates(HologramImpl hologram) {
        for (HologramDataHolder value : this.holograms.values()) {
            if (value.getHologram().equals(hologram)) {
                value.setUpdateRegistered(false);
                break;
            }
        }
    }

    public void unregisterHologramListener(HologramImpl hologram) {
        for (HologramDataHolder value : this.holograms.values()) {
            if (value.getHologram().equals(hologram)) {
                value.setClickRegistered(false);
                break;
            }
        }
    }

    public void startUpdateTask() {
        this.thread.submit(new Workload() {

            private long lastRun = System.currentTimeMillis();

            @Override
            public void compute() {
                this.lastRun = System.currentTimeMillis();
                for (HologramDataHolder value : HologramManager.this.holograms.values()) {
                    if (value.isUpdateRegistered() && System.currentTimeMillis() - value.getLastUpdateTime() >= value.getHologram().getUpdateInterval().toMillis())
                        HologramManager.this.updateHologram(value.getHologram());

                }
            }

            @Override
            public boolean reSchedule() {
                return true;
            }

            @Override
            public boolean shouldExecute() {
                return System.currentTimeMillis() - this.lastRun >= 100L;
            }

        });
    }

    public void updateHologram(HologramImpl hologram) {
        HologramDataHolder holder = null;
        for (HologramDataHolder value : this.holograms.values()) {
            if (value.getHologram().equals(hologram)) {
                holder = value;
                break;
            }
        }
        if (holder == null) throw new IllegalArgumentException("Hologram not found! Try creating it first");
        holder.getHologram().update();
        holder.setLastUpdateTime(System.currentTimeMillis());
    }

    private static Map<HologramLineImpl<?>, Location> mapLocations(HologramPageImpl page) {
        Map<HologramLineImpl<?>, Location> returnMap = new HashMap<>();
        Location l = page.getParent().getLocation();
        for (HologramLine<?> line : page.getLines()) {
            HologramLineImpl<?> line1 = (HologramLineImpl<?>) line;
            returnMap.put(line1, l);
            l.add(0, (line1.getHeight() + page.getLineGap()) * (page.getParent().isInverted() ? 1 : -1), 0);
        }
        return returnMap;
    }

    protected Map<String, HologramDataHolder> getHolograms() {
        return holograms;
    }

    public static HologramManager getInstance() {
        return INSTANCE;
    }

}
