package com.pepedevs.dbedwars.game.arena.spawner;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.events.SpawnerDropItemEvent;
import com.pepedevs.dbedwars.api.events.SpawnerUpgradeEvent;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.game.spawner.DropType;
import com.pepedevs.dbedwars.api.game.spawner.ResourceItem;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.api.util.NBTUtils;
import com.pepedevs.dbedwars.api.util.Initializable;
import com.pepedevs.radium.utils.math.collision.BoundingBox;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Spawner implements com.pepedevs.dbedwars.api.game.spawner.Spawner, Initializable {

    private final DBedwars plugin;
    private final Key<DropType> key;
    private final Arena arena;
    private final Optional<Team> team;
    private Location location;
    private BoundingBox box;

    private DropType.Tier currentTier;

    private boolean initialized;
    private Instant start;
    private Instant lastUpgrade;
    private final Map<DropType.Drop, Instant> dropTime = Collections.synchronizedMap(new HashMap<>());

    public Spawner(DBedwars plugin, DropType dropType, Arena arena, Optional<Team> team) {
        this.plugin = plugin;
        this.key = Key.of(dropType);
        this.arena = arena;
        this.team = team;
    }

    public void init(Location location, int defaultLevel) {
        this.location = location;
        this.box = new BoundingBox(
                this.location.getX() - this.getDropType().getSpawnRadius(),
                this.location.getY() - 2,
                this.location.getZ() - this.getDropType().getSpawnRadius(),
                this.location.getX() + this.getDropType().getSpawnRadius(),
                this.location.getY() + 2,
                this.location.getZ() + this.getDropType().getSpawnRadius());

        this.currentTier = this.getDropType().getTier(defaultLevel);

        if (this.getDropType().getHologram() != null) {
            // Do hologram stuff
        }

        this.initialized = true;
        this.start = Instant.now();
        this.lastUpgrade = this.start;
        for (DropType.Drop drop : this.currentTier.getDrops()) {
            this.dropTime.put(drop, this.start);
        }
    }

    @Override
    public void tick() {
        if (!this.initialized) {
            return;
        }

        for (Map.Entry<DropType.Drop, Instant> entry : this.dropTime.entrySet()) {
            if (entry.getValue().plusMillis((long) entry.getKey().getDelay() * 1000).isBefore(Instant.now())) {
                entry.setValue(Instant.now());
                if (entry.getKey().getMaxSpawn() != -1) {
                    int count = 0;
                    for (Entity entity : this.location.getWorld().getNearbyEntities(this.location, this.getDropType().getSpawnRadius(), this.getDropType().getSpawnRadius(), this.getDropType().getSpawnRadius())) {
                        if (!(entity instanceof Item)) continue;
                        Item item = (Item) entity;
                        if (!item.getItemStack().getType().equals(entry.getKey().getItem().getType())) continue;
                        if (!NBTUtils.hasPluginData(item.getItemStack())) continue;
                        count += item.getItemStack().getAmount();
                    }
                    if (count >= entry.getKey().getMaxSpawn()) return;
                }
                this.spawn(entry.getKey());
            }
        }
    }

    @Override
    public void spawn(DropType.Drop drop) {
        ResourceItem item = com.pepedevs.dbedwars.game.arena.spawner.ResourceItem.builder()
                .item(drop.getItem())
                .mergeable(this.getDropType().isMerging())
                .splittable(this.getDropType().isSplitable())
                .build();
        SpawnerDropItemEvent event = new SpawnerDropItemEvent(this.arena, this.getDropType(), drop, item, this);
        event.call();

        if (event.isCancelled()) return;

        if (!event.getResourceItem().isDropped())
            event.getResourceItem().drop(this.location);

        if (this.getDropType().getSoundEffect() != null) {
            this.getDropType().getSoundEffect().play(this.location);
        }
        if (this.getDropType().getParticleEffect() != null) {
            this.getDropType().getParticleEffect().build()
                    .display();
        }
    }

    @Override
    public boolean upgrade(int level) {
        DropType.Tier nextTier = this.getDropType().getTier(level);
        SpawnerUpgradeEvent event = new SpawnerUpgradeEvent(this.arena, this.getDropType(), this, this.currentTier, nextTier);
        event.call();

        if (event.isCancelled()) return false;

        this.lastUpgrade = Instant.now();

        Map<DropType.Drop, Instant> time = new HashMap<>(this.dropTime);
        this.dropTime.clear();

        for (DropType.Drop drop : event.getNextTier().getDrops()) {
            if (time.containsKey(drop)) continue;
            time.put(drop, Instant.now());
        }

        this.dropTime.putAll(time);

        if (nextTier.getUpgradeMessage() != null) {
            // TODO add placeholders and some checks
            // TODO maybe add actions here also?
            this.arena.sendMessage(nextTier.getUpgradeMessage());
        }

        return true;
    }

    public DropType getDropType() {
        return key.get();
    }

    @Override
    public Instant getStartTime() {
        return Instant.from(this.start);
    }

    @Override
    public Location getLocation() {
        return this.location.clone();
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Arena getArena() {
        return this.arena;
    }

    @Override
    public Optional<Team> getTeam() {
        return this.team;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.box;
    }

    @Override
    public DropType.Tier getTier() {
        return this.currentTier;
    }

    @Override
    public Instant getLastUpgrade() {
        return this.lastUpgrade;
    }

    @Override
    public boolean remove() {
        return false;
    }

    @Override
    public Key<DropType> getKey() {
        return key;
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

}
