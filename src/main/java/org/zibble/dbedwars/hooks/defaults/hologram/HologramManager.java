package org.zibble.dbedwars.hooks.defaults.hologram;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
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
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.api.task.Task;
import org.zibble.dbedwars.api.task.Workload;
import org.zibble.dbedwars.api.util.Pair;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.io.UUIDTypeAdaptor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HologramManager implements HologramFactory {

    private final DBedwars plugin;
    private final Task thread;
    private final Map<Key, HologramImpl> holograms;

    public HologramManager() {
        this.plugin = DBedwars.getInstance();
        this.thread = this.plugin.getThreadHandler().getTaskHandler().newPool(1, 3 * 1000000L);
        this.holograms = new ConcurrentHashMap<>();
        HologramPacketListener packetListener = new HologramPacketListener(this);
        PacketEventsAPI<?> packetEventsAPI = PacketEvents.getAPI();
        packetEventsAPI.getEventManager().registerListener(packetListener);
        this.startUpdateTask();
    }

    public void spawnHologram(HologramImpl hologram, Player player) {
        if (!hologram.isVisible(player)) return;
        HologramPageImpl page = hologram.getCurrentPage(player);
        if (page == null) return;
        List<Pair<HologramLine<?>, Location>> map = this.mapLocations(page);
        for (Pair<HologramLine<?>, Location> entry : map) {
            if (entry.getKey() instanceof HologramLine.Text) {
                PacketUtils.showFakeEntityArmorStand(player, entry.getValue(), ((HologramLineImpl<?>) entry.getKey()).getEntityIds()[0], true, true, true);
                PacketUtils.updateFakeEntityCustomName(player, ((Message) entry.getKey().getContent()).asComponentWithPAPI(player)[0], ((HologramLineImpl<?>) entry.getKey()).getEntityIds()[0]);
            } else if (entry.getKey() instanceof HologramLine.Head) {
                PacketUtils.showFakeEntityArmorStand(player, entry.getValue(), ((HologramLineImpl<?>) entry.getKey()).getEntityIds()[0], true, false, true);
                PacketUtils.helmetFakeEntity(player, ((BwItemStack) entry.getKey().getContent()).asItemStack(player), ((HologramLineImpl<?>) entry.getKey()).getEntityIds()[0]);
            } else if (entry.getKey() instanceof HologramLine.SmallHead) {
                PacketUtils.showFakeEntityArmorStand(player, entry.getValue(), ((HologramLineImpl<?>) entry.getKey()).getEntityIds()[0], true, true, true);
                PacketUtils.helmetFakeEntity(player, ((BwItemStack) entry.getKey().getContent()).asItemStack(player), ((HologramLineImpl<?>) entry.getKey()).getEntityIds()[0]);
            } else if (entry.getKey() instanceof HologramLine.Icon) {
                PacketUtils.showFakeEntityArmorStand(player, entry.getValue(), ((HologramLineImpl<?>) entry.getKey()).getEntityIds()[0], true, true, true);
                PacketUtils.showFakeEntityItem(player, entry.getValue(), ((BwItemStack) entry.getKey().getContent()).asItemStack(player), ((HologramLineImpl<?>) entry.getKey()).getEntityIds()[1]);
                PacketUtils.attachFakeEntity(player, ((HologramLineImpl<?>) entry.getKey()).getEntityIds()[0], ((HologramLineImpl<?>) entry.getKey()).getEntityIds()[1]);
            } else if (entry.getKey() instanceof HologramLine.Entity) {
                PacketUtils.showFakeEntityArmorStand(player, entry.getValue(), ((HologramLineImpl<?>) entry.getKey()).getEntityIds()[0], true, true, true);
                if (((EntityType) entry.getKey().getContent()).isAlive()) {
                    PacketUtils.showFakeEntityLiving(player, entry.getValue(), SpigotConversionUtil.fromBukkitEntityType((EntityType) entry.getKey().getContent()), ((HologramLineImpl<?>) entry.getKey()).getEntityIds()[1]);
                } else {
                    PacketUtils.showFakeEntity(player, entry.getValue(), SpigotConversionUtil.fromBukkitEntityType((EntityType) entry.getKey().getContent()), ((HologramLineImpl<?>) entry.getKey()).getEntityIds()[1]);
                }
            }
        }
    }

    @Override
    public Hologram createHologram(Location location) {
        Key key = Key.of(UUIDTypeAdaptor.fromUUID(UUID.randomUUID()));
        HologramImpl hologram = new HologramImpl(this, key, location);
        this.holograms.put(key, hologram);
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
        HologramPage page = hologram.getCurrentPage(player);
        if (page == null) return;
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
        List<Pair<HologramLine<?>, Location>> map = this.mapLocations(page);
        for (Pair<HologramLine<?>, Location> entry : map)
            for (int entityId : ((HologramLineImpl<?>) entry.getKey()).getEntityIds())
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

    private List<Pair<HologramLine<?>, Location>> mapLocations(HologramPageImpl page) {
        List<Pair<HologramLine<?>, Location>> returnMap = new ArrayList<>();
        Location l = page.getParent().getLocation();
        List<HologramLine<?>> usedLines = new ArrayList<>(page.getLines());
        if (page.getParent().isInverted()) Collections.reverse(usedLines);
        for (HologramLine<?> line : usedLines) {
            returnMap.add(Pair.of(line, l));
            l = l.add(0, line.getHeight() + page.getLineGap(), 0).clone();
        }
        return returnMap;
    }

    protected Map<Key, HologramImpl> getHolograms() {
        return holograms;
    }

}
