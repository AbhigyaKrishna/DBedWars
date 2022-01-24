package com.pepedevs.dbedwars.game.arena;

import com.pepedevs.radium.holograms.hologramline.HeadHologramLine;
import com.pepedevs.radium.holograms.hologramline.TextHologramLine;
import com.pepedevs.radium.holograms.object.Hologram;
import com.pepedevs.radium.holograms.object.HologramLineType;
import com.pepedevs.radium.holograms.object.HologramPage;
import com.pepedevs.radium.particles.ParticleBuilder;
import com.pepedevs.radium.particles.ParticleEffect;
import com.pepedevs.radium.task.Workload;
import com.pepedevs.radium.utils.StringUtils;
import com.pepedevs.radium.utils.math.collision.BoundingBox;
import com.pepedevs.radium.utils.scheduler.SchedulerUtils;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.events.SpawnerDropItemEvent;
import com.pepedevs.dbedwars.api.events.SpawnerUpgradeEvent;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.game.spawner.DropType;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import com.pepedevs.dbedwars.api.util.NBTUtils;
import com.pepedevs.dbedwars.api.util.SoundVP;
import com.pepedevs.dbedwars.configuration.ConfigMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Spawner implements com.pepedevs.dbedwars.api.game.spawner.Spawner {

    private final DBedwars plugin;
    private final DropType drop;
    private final Arena arena;
    private final Team team;
    private Location location;
    private BoundingBox box;

    private boolean registered;
    private int level;
    private DropType.Tier tier;
    private Instant lastUpgrade;
    private ParticleBuilder particle;
    private Instant start;
    private Hologram hologram;

    private Map<DropType.Drop, Long> items;

    public Spawner(
            DBedwars plugin, DropType type, Location location, Arena arena, @Nullable Team team) {
        this.plugin = plugin;
        this.drop = type;
        this.location = location;
        this.box =
                new BoundingBox(
                        this.location.getX() - this.drop.getSpawnRadius(),
                        this.location.getY() - 2,
                        this.location.getZ() - this.drop.getSpawnRadius(),
                        this.location.getX() + this.drop.getSpawnRadius(),
                        this.location.getY() + 2,
                        this.location.getZ() + this.drop.getSpawnRadius());
        this.arena = arena;
        this.team = team;
        this.items = new ConcurrentHashMap<>();
    }

    @Override
    public void init() {
        this.registered = true;
        this.level = 1;
        this.tier = this.drop.getTier(this.level);
        this.particle =
                new ParticleBuilder(this.drop.getSpawnEffect(), this.location.clone().add(0, 1, 0))
                        .setSpeed(0)
                        .setAmount(5);
        if (this.drop.isHologramEnabled()) {
            // TODO
            Location loc = this.location.clone().add(0, 2, 0);
            this.hologram =
                    this.plugin
                            .getHologramManager()
                            .createHologram(
                                    this.drop.getId() + "@" + UUID.randomUUID().toString(), loc);
            HologramPage page = this.hologram.getPages().get(0);
            page.addLine(new HeadHologramLine(loc, this.drop.getHologramMaterial().toItemStack()));
            loc.subtract(0, HologramLineType.HEAD.getOffsetY(), 0);
            for (String line : this.drop.getHologramText()) {
                page.addLine(
                        new TextHologramLine(loc, StringUtils.translateAlternateColorCodes(line)));
                loc.subtract(0, HologramLineType.TEXT.getOffsetY(), 0);
            }

            this.hologram.showAll();
        }

        this.start = Instant.now();
        this.lastUpgrade = start;
        for (DropType.Drop i : this.tier.getDrops()) {
            this.items.put(i, System.currentTimeMillis());
        }
        this.arena.addSpawner(this);
    }

    @Override
    public void tick() {
        if (!this.exists()) return;

        this.rotateHologram();
        for (Map.Entry<DropType.Drop, Long> entry : this.items.entrySet()) {
            if (((double) (System.currentTimeMillis() - entry.getValue()) / (50 * 20))
                    < entry.getKey().getDelay()) continue;
            this.items.put(entry.getKey(), System.currentTimeMillis());
            if (entry.getKey().getMaxSpawn() != -1) {
                int count = 0;
                for (Entity entity : this.location.getWorld().getNearbyEntities(this.location, this.drop.getSpawnRadius(), this.drop.getSpawnRadius(), this.drop.getSpawnRadius())) {
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

    @Override
    public void spawn(DropType.Drop drop) {
        SpawnerDropItemEvent event =
                new SpawnerDropItemEvent(
                        this.arena,
                        this.getDropType(),
                        drop,
                        this,
                        this.team,
                        this.level,
                        this.tier);
        event.call();

        if (event.isCancelled()) return;

        this.plugin.getThreadHandler().submitAsync(new Workload() {
            @Override
            public void compute() {
                BwItemStack stack = event.getDrop().getItem();
                if (!Spawner.this.getDropType().isMerging()) stack.setUnMergeable();

                SchedulerUtils.runTask(
                        new Runnable() {
                            @Override
                            public void run() {
                                Item item = Spawner.this.location.getWorld().dropItemNaturally(Spawner.this.location, stack.toItemStack());
                                if (Spawner.this.getDropType().isSplitable())
                                    item.setMetadata(
                                            "split",
                                            new FixedMetadataValue(
                                                    Spawner.this.plugin, true));
                            }
                        },
                        Spawner.this.plugin);
                if (Spawner.this.drop.getSpawnSound() != null)
                    Spawner.this.drop.getSpawnSound().play(Spawner.this.location);
                if (Spawner.this.drop.getSpawnEffect() != null)
                    Spawner.this.particle.display(
                            Spawner.this.arena.getPlayers().stream()
                                    .map(ArenaPlayer::getPlayer)
                                    .collect(Collectors.toList()));

            }
        });
    }

    @Override
    public boolean upgrade(int level) {
        DropType.Tier nextTier = this.drop.getTier(level);
        SpawnerUpgradeEvent event =
                new SpawnerUpgradeEvent(
                        this.arena,
                        this.drop,
                        this,
                        this.team,
                        this.level,
                        level,
                        this.tier,
                        nextTier);
        event.call();

        if (event.isCancelled()) return false;

        this.level = event.getNextLevel();
        this.lastUpgrade = Instant.now();
        Map<String, Long> time = new HashMap<>();
        for (Map.Entry<DropType.Drop, Long> d : this.items.entrySet()) {
            time.put(d.getKey().getKey(), d.getValue());
        }
        this.items.clear();
        for (DropType.Drop i : event.getNextTier().getDrops()) {
            if (time.containsKey(i.getKey())) {
                this.items.put(i, time.get(i.getKey()));
            } else {
                this.items.put(i, System.currentTimeMillis());
            }
        }

        this.plugin
                .getThreadHandler()
                .submitAsync(new Workload() {
                    @Override
                    public void compute() {
                        if (event.getNextTier().getUpgradeMessage() != null) {
                            String message = event.getNextTier().getUpgradeMessage();
                            String key = message.substring(0, message.indexOf(" "));
                            message = message.replaceFirst(key, "").trim();
                            Spawner.this.broadcast(key, message, Spawner.this.location);
                        }
                        SoundVP sound = event.getNextTier().getUpgradeSound();
                        if (sound != null) {
                            sound.play(Spawner.this.location);
                        }
                        ParticleEffect effect = event.getNextTier().getUpgradeEffect();
                        if (effect != null) {
                            ParticleBuilder particle =
                                    new ParticleBuilder(
                                            effect, Spawner.this.location.clone().add(0, 2, 0));
                            particle.setSpeed(0)
                                    .setAmount(20)
                                    .display(
                                            Spawner.this.arena.getPlayers().stream()
                                                    .map(ArenaPlayer::getPlayer)
                                                    .collect(Collectors.toList()));
                        }
                    }
                });

        return true;
    }

    private void rotateHologram() {
        if (this.hologram == null) return;

        Location location = this.hologram.getLocation().clone();
        location.setYaw(location.getYaw() + 1F);
        //        this.hologram.teleport(location);
    }

    private void broadcast(String key, String message, @Nullable Location location) {
        if (key.equalsIgnoreCase("-all")) {
            this.arena.sendMessage(ConfigMessage.from(message));
        } else if (key.equalsIgnoreCase("-team")) {
            if (this.team != null) this.team.sendMessage(ConfigMessage.from(message));
        } else if (key.startsWith("-region")) {
            if (location != null && location.getWorld() != null) {
                double range = 50;
                try {
                    range = Double.parseDouble(key.split(":")[1]);
                } catch (NumberFormatException
                        | ArrayIndexOutOfBoundsException
                        | NullPointerException ignored) {
                }

                for (Entity entity : location.getWorld().getNearbyEntities(location, range, range, range)) {
                    if (entity instanceof Player) {
                        this.arena.getAsArenaPlayer((Player) entity).ifPresent(new Consumer<ArenaPlayer>() {
                            @Override
                            public void accept(ArenaPlayer arenaPlayer) {
                                arenaPlayer.sendMessage(ConfigMessage.from(message));
                            }
                        });
                    }
                }
            }
        } else if (key.startsWith("-player")) {
            Player player = null;
            try {
                player = Bukkit.getPlayer(key.split(":")[1]);
            } catch (ArrayIndexOutOfBoundsException | NullPointerException ignored) {
            }
            if (player != null) {
                this.arena.getAsArenaPlayer(player).ifPresent(new Consumer<ArenaPlayer>() {
                    @Override
                    public void accept(ArenaPlayer arenaPlayer) {
                        arenaPlayer.sendMessage(ConfigMessage.from(message));
                    }
                });
            }
        }
    }

    @Override
    public DropType getDropType() {
        return this.drop;
    }

    @Override
    public Instant getStartTime() {
        return this.start;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
        this.box =
                new BoundingBox(
                        this.location.getX() - this.drop.getSpawnRadius(),
                        this.location.getY() - 2,
                        this.location.getZ() - this.drop.getSpawnRadius(),
                        this.location.getX() + this.drop.getSpawnRadius(),
                        this.location.getY() + 2,
                        this.location.getZ() + this.drop.getSpawnRadius());
    }

    @Override
    public Arena getArena() {
        return arena;
    }

    @Override
    public Team getTeam() {
        return this.team;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.box;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public DropType.Tier getTier() {
        return this.tier;
    }

    @Override
    public Instant getLastUpgrade() {
        return this.lastUpgrade;
    }

    @Override
    public ParticleBuilder getParticle() {
        return this.particle;
    }

    @Override
    public boolean exists() {
        return this.registered && this.location != null;
    }

    @Override
    public boolean remove() {
        return false;
    }
}
