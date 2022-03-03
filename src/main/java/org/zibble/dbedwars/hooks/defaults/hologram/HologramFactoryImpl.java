package org.zibble.dbedwars.hooks.defaults.hologram;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.hooks.hologram.HologramFactory;
import org.zibble.dbedwars.api.hooks.hologram.HologramLine;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;
import org.zibble.dbedwars.api.task.Task;
import org.zibble.dbedwars.api.task.Workload;
import org.zibble.dbedwars.task.TaskQueueHandler;

import java.util.*;

public class HologramFactoryImpl implements HologramFactory {

    private static final HologramFactoryImpl INSTANCE = new HologramFactoryImpl();

    private final DBedwars plugin;
    private final Task thread;
    private final Map<String, HologramDataHolder> holograms;

    public HologramFactoryImpl() {
        this.plugin = DBedwars.getInstance();
        this.thread = new TaskQueueHandler("Hologram Thread %d").newPool(1, 3 * 1000000L);
        this.holograms = Collections.synchronizedMap(new HashMap<>());
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
                PacketUtils.updateFakeEntityCustomName(player, (Component) entry.getKey().getContent(), entry.getKey().getEntityIds()[0]);
                continue;
            }
            if (entry.getKey().getContent() instanceof HologramLine.Head) {
                PacketUtils.showFakeEntityArmorStand(player, entry.getValue(), entry.getKey().getEntityIds()[0], true, false, true);
                PacketUtils.helmetFakeEntity(player, (ItemStack) entry.getKey().getContent(), entry.getKey().getEntityIds()[0]);
                continue;
            }
            if (entry.getKey().getContent() instanceof HologramLine.SmallHead) {
                PacketUtils.showFakeEntityArmorStand(player, entry.getValue(), entry.getKey().getEntityIds()[0], true, true, true);
                PacketUtils.helmetFakeEntity(player, (ItemStack) entry.getKey().getContent(), entry.getKey().getEntityIds()[0]);
                continue;
            }
            if (entry.getKey().getContent() instanceof HologramLine.Icon) {
                PacketUtils.showFakeEntityArmorStand(player, entry.getValue(), entry.getKey().getEntityIds()[0], true, true, true);
                PacketUtils.showFakeEntityItem(player, entry.getValue(), (ItemStack) entry.getKey().getContent(), entry.getKey().getEntityIds()[1]);
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

    @Override
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

    public void updateHologram(HologramImpl hologram) {
        HologramDataHolder holder = null;
        for (HologramDataHolder value : this.holograms.values()) {
            if (value.getHologram().equals(hologram)) {
                holder = value;
                break;
            }
        }
        if (holder == null) throw new IllegalArgumentException("Hologram not found! Try creating it first");
        for (UUID uuid : hologram.getViewerPages().keySet()) {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player == null) continue;
            this.updateContent(hologram, player);
            this.updateLocation(hologram, player);
        }
        holder.setLastUpdateTime(System.currentTimeMillis());
    }

    public void despawnHologram(HologramImpl hologram, Player player) {
        HologramPage page = hologram.getHologramPages().get(hologram.getViewerPages().get(player.getUniqueId()));
        for (HologramLine<?> line : page.getLines()) {
            HologramLineImpl<?> lineImpl = (HologramLineImpl<?>) line;
            PacketUtils.hideFakeEntities(player, lineImpl.getEntityIds());
        }
    }

    public void respawnHologram(HologramImpl hologram, Player player) {
        this.despawnHologram(hologram, player);
        this.spawnHologram(hologram, player);
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
                for (HologramDataHolder value : HologramFactoryImpl.this.holograms.values()) {
                    if (value.isUpdateRegistered() && System.currentTimeMillis() - value.getLastUpdateTime() >= value.getHologram().getUpdateInterval().toMillis())
                        HologramFactoryImpl.this.updateHologram(value.getHologram());

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

    protected Map<String, HologramDataHolder> getHolograms() {
        return holograms;
    }

    public static HologramFactoryImpl getInstance() {
        return INSTANCE;
    }

}
