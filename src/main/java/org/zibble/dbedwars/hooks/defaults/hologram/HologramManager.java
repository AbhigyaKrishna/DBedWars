package org.zibble.dbedwars.hooks.defaults.hologram;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.hooks.hologram.HologramFactory;
import org.zibble.dbedwars.api.hooks.hologram.HologramLine;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.task.Task;
import org.zibble.dbedwars.api.task.Workload;
import org.zibble.dbedwars.api.util.BwItemStack;
import org.zibble.dbedwars.task.TaskQueueHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HologramManager implements HologramFactory {

    private final DBedwars plugin;
    private final Task thread;
    private final Map<String, HologramImpl> holograms;

    public HologramManager() {
        this.plugin = DBedwars.getInstance();
        this.thread = new TaskQueueHandler("Hologram Thread %d").newPool(1, 3 * 1000000L);
        this.holograms = new ConcurrentHashMap<>();
        HologramPacketListener packetListener = new HologramPacketListener(this);
        PacketEventsAPI<?> packetEventsAPI = PacketEvents.getAPI();
        packetEventsAPI.getEventManager().registerListener(packetListener);
        this.startUpdateTask();
    }

    public void spawnHologram(HologramImpl hologram, Player player) {
        if (!hologram.isVisible(player)) return;
        HologramPageImpl page = (HologramPageImpl) hologram.getHologramPages().get(hologram.getViewerPages().getOrDefault(player.getUniqueId(), 0));
        Map<HologramLineImpl<?>, Location> map = this.mapLocations(page);
        for (Map.Entry<HologramLineImpl<?>, Location> entry : map.entrySet()) {
            if (entry.getKey().getContent() instanceof HologramLine.Text) {
                PacketUtils.showFakeEntityArmorStand(player, entry.getValue(), entry.getKey().getEntityIds()[0], true, true, true);
                PacketUtils.updateFakeEntityCustomName(player, ((Message) entry.getKey().getContent()).asComponentWithPAPI(player)[0], entry.getKey().getEntityIds()[0]);
                continue;
            }
            if (entry.getKey().getContent() instanceof HologramLine.Head) {
                PacketUtils.showFakeEntityArmorStand(player, entry.getValue(), entry.getKey().getEntityIds()[0], true, false, true);
                PacketUtils.helmetFakeEntity(player, ((BwItemStack) entry.getKey().getContent()).asItemStack(player), entry.getKey().getEntityIds()[0]);
                continue;
            }
            if (entry.getKey().getContent() instanceof HologramLine.SmallHead) {
                PacketUtils.showFakeEntityArmorStand(player, entry.getValue(), entry.getKey().getEntityIds()[0], true, true, true);
                PacketUtils.helmetFakeEntity(player, ((BwItemStack) entry.getKey().getContent()).asItemStack(player), entry.getKey().getEntityIds()[0]);
                continue;
            }
            if (entry.getKey().getContent() instanceof HologramLine.Icon) {
                PacketUtils.showFakeEntityArmorStand(player, entry.getValue(), entry.getKey().getEntityIds()[0], true, true, true);
                PacketUtils.showFakeEntityItem(player, entry.getValue(), ((BwItemStack) entry.getKey().getContent()).asItemStack(player), entry.getKey().getEntityIds()[1]);
                PacketUtils.attachFakeEntity(player, entry.getKey().getEntityIds()[0], entry.getKey().getEntityIds()[1]);
                continue;
            }
            if (entry.getKey().getContent() instanceof HologramLine.Entity) {
                PacketUtils.showFakeEntityArmorStand(player, entry.getValue(), entry.getKey().getEntityIds()[0], true, true, true);
                if (((EntityType) entry.getKey().getContent()).isAlive()) {
                    PacketUtils.showFakeEntityLiving(player, entry.getValue(), EntityTypes.getByName(((EntityType) entry.getKey().getContent()).name()), entry.getKey().getEntityIds()[1]);
                } else {
                    PacketUtils.showFakeEntity(player, entry.getValue(), EntityTypes.getByName(((EntityType) entry.getKey().getContent()).name()), entry.getKey().getEntityIds()[1]);
                }
            }
        }
    }

    @Override
    public Hologram createHologram(Location location) {
        HologramImpl hologram = new HologramImpl(this, location);
        this.holograms.put(UUID.randomUUID().toString(), hologram);
        return hologram;
    }

    public void updateHologram(HologramImpl hologram) {
        for (HologramImpl value : this.holograms.values()) {
            if (!value.equals(hologram)) continue;
            for (UUID uuid : hologram.getViewerPages().keySet()) {
                Player player = this.plugin.getServer().getPlayer(uuid);
                if (player == null) continue;
                this.updateContent(hologram, player);
                this.updateLocation(hologram, player);
                hologram.setLastUpdate(System.currentTimeMillis());
            }
            hologram.setHasChangedContentType(false);
        }
    }

    public void despawnHologram(HologramImpl hologram, Player player) {
        HologramPage page = hologram.getHologramPages().get(hologram.getViewerPages().get(player.getUniqueId()));
        for (HologramLine<?> line : page.getLines()) {
            HologramLineImpl<?> lineImpl = (HologramLineImpl<?>) line;
            PacketUtils.hideFakeEntities(player, lineImpl.getEntityIds());
        }
    }

    public void respawnHologram(HologramImpl hologram) {
        for (UUID uuid : hologram.getViewerPages().keySet()) {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player == null) continue;
            this.despawnHologram(hologram, player);
            this.spawnHologram(hologram, player);
        }
        hologram.setHasChangedContentType(false);
    }

    public void respawnHologram(HologramImpl hologram, Player player) {
        this.despawnHologram(hologram, player);
        this.spawnHologram(hologram, player);
    }

    public void updateContent(HologramImpl hologram) {
        for (UUID uuid : hologram.getViewerPages().keySet()) {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player == null) continue;
            this.updateContent(hologram, player);
        }
    }

    public void updateContent(HologramImpl hologram, Player player) {
        if (!hologram.isVisible(player)) return;
        HologramPageImpl page = (HologramPageImpl) hologram.getHologramPages().get(hologram.getViewerPages().get(player.getUniqueId()));
        for (HologramLine<?> line : page.getLines()) {
            HologramLineImpl<?> lineImpl = (HologramLineImpl<?>) line;
            if (lineImpl.getContent() instanceof Component) {
                PacketUtils.updateFakeEntityCustomName(player, (Component) lineImpl.getContent(), lineImpl.getEntityIds()[0]);
            }
        }
    }

    public void updateLocation(HologramImpl hologram) {
        for (UUID uuid : hologram.getViewerPages().keySet()) {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player == null) continue;
            this.updateLocation(hologram, player);
        }
    }

    public void updateLocation(HologramImpl hologram, Player player) {
        HologramPageImpl page = (HologramPageImpl) hologram.getHologramPages().get(hologram.getViewerPages().get(player.getUniqueId()));
        Map<HologramLineImpl<?>, Location> map = this.mapLocations(page);
        for (Map.Entry<HologramLineImpl<?>, Location> entry : map.entrySet())
            for (int entityId : entry.getKey().getEntityIds())
                PacketUtils.teleportFakeEntity(player, entry.getValue(), entityId);
    }

    private void startUpdateTask() {
        this.thread.submit(new Workload() {

            private long lastRun = System.currentTimeMillis();

            @Override
            public void compute() {
                this.lastRun = System.currentTimeMillis();
                for (HologramImpl value : HologramManager.this.holograms.values()) {
                    if (!value.isUpdateRegistered()) continue;
                    if (System.currentTimeMillis() - value.getLastUpdate() < value.getUpdateInterval().toMillis())
                        continue;
                    if (value.hasChangedContentType()) {
                        HologramManager.this.updateHologram(value);
                    } else {
                        HologramManager.this.updateContent(value);
                    }
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

    private Map<HologramLineImpl<?>, Location> mapLocations(HologramPageImpl page) {
        Map<HologramLineImpl<?>, Location> returnMap = new HashMap<>();
        Location l = page.getParent().getLocation();
        List<HologramLine<?>> usedLines = new ArrayList<>(page.getLines());
        if (page.getParent().isInverted()) Collections.reverse(usedLines);
        for (HologramLine<?> line : usedLines) {
            HologramLineImpl<?> line1 = (HologramLineImpl<?>) line;
            returnMap.put(line1, l);
            l.add(0, (line1.getHeight() + page.getLineGap()) * (page.getParent().isInverted() ? 1 : -1), 0);
        }
        return returnMap;
    }

    protected Map<String, HologramImpl> getHolograms() {
        return holograms;
    }

}
