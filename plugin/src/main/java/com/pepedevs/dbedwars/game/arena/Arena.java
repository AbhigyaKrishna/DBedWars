package com.pepedevs.dbedwars.game.arena;

import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.math.collision.BoundingBox;
import me.Abhigya.core.util.tasks.Workload;
import me.Abhigya.core.util.tasks.workload.FixedRateWorkload;
import me.Abhigya.core.util.world.GameRuleDisableDaylightCycle;
import me.Abhigya.core.util.world.GameRuleType;
import me.Abhigya.core.util.world.WorldUtils;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.events.*;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.ArenaStatus;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.game.settings.ArenaSettings;
import com.pepedevs.dbedwars.api.game.spawner.Spawner;
import com.pepedevs.dbedwars.api.task.Regeneration;
import com.pepedevs.dbedwars.api.util.Color;
import com.pepedevs.dbedwars.api.util.KickReason;
import com.pepedevs.dbedwars.api.util.LocationXYZ;
import com.pepedevs.dbedwars.configuration.PluginFiles;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableArena;
import com.pepedevs.dbedwars.game.TeamAssigner;
import com.pepedevs.dbedwars.game.arena.view.shop.ShopView;
import com.pepedevs.dbedwars.listeners.ArenaListener;
import com.pepedevs.dbedwars.listeners.GameListener;
import com.pepedevs.dbedwars.task.WorldRegenerator;
import com.pepedevs.dbedwars.utils.ConfigurationUtils;
import com.pepedevs.dbedwars.utils.DatabaseUtils;
import com.pepedevs.dbedwars.utils.ScoreboardImpl;
import com.pepedevs.dbedwars.utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Arena implements com.pepedevs.dbedwars.api.game.Arena {

    private final DBedwars plugin;
    private ConfigurableArena cfgArena;
    private ArenaSettings settings;

    private World world;
    private Regeneration regenerator;
    private ArenaStatus status;
    private boolean enabled;
    private Instant startTime;
    private ScoreboardImpl scoreboard;
    private ArenaListener arenaHandler;
    private GameListener gameHandler;

    private Set<Team> teams;
    private Set<ArenaPlayer> players;
    private Map<ArenaPlayer, KickReason> removed;
    private List<Spawner> spawners;

    public Arena(DBedwars plugin) {
        this.plugin = plugin;
        this.settings =
                new com.pepedevs.dbedwars.game.arena.settings.ArenaSettings(this.plugin, this);
        this.teams = new HashSet<>();
        this.players = new HashSet<>();
        this.regenerator =
                new WorldRegenerator(this.plugin, this.settings.getRegenerationType(), this);
        this.status = ArenaStatus.STOPPED;
        this.arenaHandler = new ArenaListener(this.plugin, this);
        this.gameHandler = new GameListener(this.plugin, this);
    }

    public Arena(DBedwars plugin, ConfigurableArena cfg) {
        this(plugin);
        this.cfgArena = cfg;
        this.settings =
                new com.pepedevs.dbedwars.game.arena.settings.ArenaSettings(this.plugin, this, cfg);
        this.enabled = this.isConfigured() && cfg.isEnabled();
    }

    @Override
    public ArenaSettings getSettings() {
        return this.settings;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public boolean saveWorld(String worldFolder, boolean overwriteCache) {
        Validate.notNull(worldFolder, "World folder cannot be null!");

        File file = new File(worldFolder);
        if (file.isDirectory() && WorldUtils.worldFolderCheck(file)) {
            if (this.plugin
                    .getGeneratorHandler()
                    .getWorldAdaptor()
                    .saveExist(this.settings.getName())) {
                if (overwriteCache)
                    return this.plugin
                            .getGeneratorHandler()
                            .getWorldAdaptor()
                            .saveWorld(worldFolder, this.settings.getName());
            } else {
                return this.plugin
                        .getGeneratorHandler()
                        .getWorldAdaptor()
                        .saveWorld(worldFolder, this.settings.getName());
            }
        } else {
            throw new IllegalStateException("World folder missing or was deleted before saving!");
        }

        return true;
    }

    @Override
    public boolean saveData(boolean overwriteData) {
        File file =
                new File(
                        PluginFiles.ARENA_DATA_SETTINGS.getFile(),
                        this.settings.getName() + ".yml");
        if (this.cfgArena == null) {
            this.cfgArena = new ConfigurableArena(this);
            this.plugin.getConfigHandler().getArenas().add(this.cfgArena);
        }

        this.cfgArena.update();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        boolean saved = false;

        if (!file.exists()) {
            try {
                file.createNewFile();
                cfgArena.save(config);
                saved = true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        if (!saved && overwriteData) {
            cfgArena.save(config);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void reloadData() {
        if (this.cfgArena == null) return;

        ((com.pepedevs.dbedwars.game.arena.settings.ArenaSettings) this.settings)
                .update(this.cfgArena);
        this.settings.getAvailableTeams().forEach(Team::reloadData);
    }

    @Override
    public World loadWorld() {
        try {
            return this.regenerator.regenerate().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("World regen interrupted!");
        }
    }

    @Override
    public void load() {
        if (!this.enabled)
            throw new IllegalStateException("Tried loading arena which isn't enabled!");

        this.setStatus(ArenaStatus.REGENERATING);
        World world = this.loadWorld();
        GameRuleType.SHOW_DEATH_MESSAGES.apply(world, false);
        GameRuleType.MOB_SPAWNING.apply(world, false);
        GameRuleType.KEEP_INVENTORY.apply(world, true);
        GameRuleType.SHOW_DEATH_MESSAGES.apply(world, false);
        GameRuleType.SPECTATORS_GENERATE_CHUNKS.apply(world, false);
        GameRuleDisableDaylightCycle gameRule = new GameRuleDisableDaylightCycle(8000);
        gameRule.apply(world);
        this.setWorld(world);
        this.arenaHandler.register();
        this.setStatus(ArenaStatus.IDLING);
    }

    @Override
    public void enable(boolean flag) {
        this.enabled = flag;
        this.plugin
                .getThreadHandler()
                .addAsyncWork(
                        () -> {
                            File file =
                                    new File(
                                            PluginFiles.ARENA_DATA_SETTINGS.getFile(),
                                            this.settings.getName() + ".yml");
                            FileConfiguration configuration =
                                    YamlConfiguration.loadConfiguration(file);
                            configuration.set("enabled", flag);
                            try {
                                configuration.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            this.cfgArena.update();
                        });
    }

    @Override
    public Team getTeam(Color color) {
        for (Team team : teams) {
            if (team.getColor().equals(color)) {
                return team;
            }
        }
        return null;
    }

    @Override
    public List<Team> getTeams() {
        return new ArrayList<>(this.teams);
    }

    @Override
    public List<ArenaPlayer> getSpectators() {
        return this.getPlayers().stream()
                .filter(ArenaPlayer::isSpectator)
                .collect(Collectors.toList());
    }

    @Override
    public ArenaStatus getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(ArenaStatus status) {
        this.status = status;
    }

    @Override
    public void addSpawner(Spawner spawner) {
        this.spawners.add(spawner);
    }

    @Override
    public void removeSpawner(Spawner spawner) {
        this.spawners.remove(spawner);
    }

    @Override
    public List<Spawner> getSpawners() {
        return Collections.unmodifiableList(this.spawners);
    }

    @Override
    public Optional<Spawner> getSpawner(LocationXYZ location, float range) {
        if (range <= 1) {
            return this.spawners.stream()
                    .filter(s -> s.getBoundingBox().contains(location.toVector()))
                    .findFirst();
        }

        BoundingBox box =
                new BoundingBox(
                        location.getX() - range,
                        location.getY() - range,
                        location.getZ() - range,
                        location.getX() + range,
                        location.getY() + range,
                        location.getZ() + range);
        return this.spawners.stream().filter(s -> s.getBoundingBox().intersects(box)).findFirst();
    }

    @Override
    public boolean isCurrentlyRegenerating() {
        return this.status == ArenaStatus.REGENERATING;
    }

    @Override
    public boolean isConfigured() {
        return this.settings.getName() != null
                && !this.settings.getAvailableTeams().isEmpty()
                && this.settings.getLobby() != null
                && !this.settings.getDrops().isEmpty()
                && this.plugin
                        .getGeneratorHandler()
                        .getWorldAdaptor()
                        .saveExist(this.settings.getName())
                && this.settings.getAvailableTeams().stream().allMatch(Team::isConfigured);
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean start() {
        if (this.players.size() < this.settings.getMinPlayers()) {
            return false;
        }

        this.removed = new LinkedHashMap<>();
        this.spawners = new ArrayList<>();

        TeamAssigner ta = new TeamAssigner(this);
        ta.assign();
        this.players.forEach(p -> this.teams.add(p.getTeam()));

        ArenaStartEvent event = new ArenaStartEvent(this);
        event.call();

        if (event.isCancelled()) return false;

        this.gameHandler.register();
        this.arenaHandler.unregister();

        // TODO manage scoreboard
        this.plugin
                .getThreadHandler()
                .addSyncWork(
                        () -> {
                            Arena.this.scoreboard =
                                    new ScoreboardImpl(
                                            Arena.this.plugin,
                                            new ArrayList<>(
                                                            Arena.this
                                                                    .plugin
                                                                    .getConfigHandler()
                                                                    .getScoreboards())
                                                    .get(0));
                            Arena.this.scoreboard.createScoreboard();
                            Arena.this.players.forEach(
                                    p -> Arena.this.scoreboard.show(p.getPlayer()));
                            Arena.this.scoreboard.getHandle().update();
                            Arena.this.teams.forEach(
                                    t ->
                                            t.registerTeam(
                                                    Arena.this.scoreboard.getHandle().getHandle()));
                        });

        this.teams.forEach(
                t -> {
                    t.getSpawners()
                            .entries()
                            .forEach(
                                    e ->
                                            new com.pepedevs.dbedwars.game.arena.Spawner(
                                                            this.plugin,
                                                            e.getKey(),
                                                            e.getValue().toBukkit(this.getWorld()),
                                                            this,
                                                            t)
                                                    .init());
                    t.spawnShopNpc(t.getShopNpc());
                    t.spawnUpgradesNpc(t.getUpgradesNpc());
                });

        this.settings
                .getDrops()
                .entries()
                .forEach(
                        e ->
                                new com.pepedevs.dbedwars.game.arena.Spawner(
                                                this.plugin,
                                                e.getKey(),
                                                e.getValue().toBukkit(this.getWorld()),
                                                this,
                                                null)
                                        .init());

        this.players.forEach(
                p -> {
                    p.spawn(p.getTeam().getSpawn().toBukkit(this.world));
                    ((ShopView) p.getShopView()).load();
                    p.getPlayer().getEnderChest().clear();
                });

        this.status = ArenaStatus.RUNNING;
        this.startTime = Instant.now();
        return true;
    }

    // TODO: revamp this
    @Override
    public boolean end() {
        if (this.status != ArenaStatus.RUNNING) return false;

        ArenaEndEvent event =
                new ArenaEndEvent(
                        this,
                        this.players.stream()
                                .filter(p -> !p.isFinalKilled())
                                .collect(Collectors.toSet()));
        event.call();

        if (event.isCancelled()) return false;

        this.status = ArenaStatus.ENDING;
        DatabaseUtils.saveGameData(this);
        this.plugin
                .getThreadHandler()
                .addSyncWork(
                        new Workload() {
                            final long time = System.currentTimeMillis();
                            final int delay = Arena.this.getSettings().getGameEndDelay() * 20;
                            boolean b = false;

                            @Override
                            public void compute() {
                                b = true;
                                Arena.this.clearCache();
                                Arena.this
                                        .world
                                        .getPlayers()
                                        .forEach(
                                                p ->
                                                        p.teleport(
                                                                Arena.this
                                                                        .plugin
                                                                        .getServer()
                                                                        .getWorlds()
                                                                        .get(0)
                                                                        .getSpawnLocation()));
                                Arena.this.plugin.getThreadHandler().addAsyncWork(Arena.this::load);
                            }

                            @Override
                            public boolean shouldExecute() {
                                return System.currentTimeMillis() - this.time >= this.delay;
                            }

                            @Override
                            public boolean reSchedule() {
                                return !b;
                            }
                        });

        // TODO: give config?
        LinkedHashMap<ArenaPlayer, Integer> leaderboard = Utils.getGameLeaderBoard(this.players);
        StringBuilder builder = new StringBuilder("&6" + StringUtils.repeat("⬛", 35));
        byte b = 0;
        for (Map.Entry<ArenaPlayer, Integer> entry : leaderboard.entrySet()) {
            if (b == 4) break;

            b++;
            builder.append("\n&a")
                    .append(b)
                    .append(". ")
                    .append(entry.getKey().getPlayer().getName())
                    .append("   ")
                    .append(entry.getValue())
                    .append("pts");
        }
        builder.append("\n&6").append(StringUtils.repeat("⬛", 35));
        for (ArenaPlayer player : this.players) {
            if (player.getArena().getWorld().equals(player.getPlayer().getWorld())) {
                player.sendMessage(StringUtils.translateAlternateColorCodes(builder.toString()));
            }
        }

        this.gameHandler.unregister();
        return true;
    }

    @Override
    public void addPlayer(Player player) {
        ArenaPlayer aplayer = new com.pepedevs.dbedwars.game.arena.ArenaPlayer(player, this);
        this.addPlayer(aplayer);
    }

    @Override
    public void addPlayer(ArenaPlayer player) {
        PlayerJoinArenaLobbyEvent event =
                new PlayerJoinArenaLobbyEvent(
                        player.getPlayer(), this, this.settings.getLobby().toBukkit(this.world));
        event.call();
        if (event.isCancelled()) return;

        if (event.getArena().getPlayers().size() == event.getArena().getSettings().getMaxPlayer())
            return;

        this.players.add(player);
        player.getPlayer().teleport(event.getLocation());

        if (event.getArena().getStatus() == ArenaStatus.IDLING)
            event.getArena().setStatus(ArenaStatus.WAITING);

        this.scheduleMessage(
                event.getArena(), event.getPlayer(), event.getArena().getPlayers().size(), true);

        if (event.getArena().getPlayers().size() >= event.getArena().getSettings().getMinPlayers()
                && event.getArena().getStatus() != ArenaStatus.STARTING) {
            this.plugin.getGameManager().startArenaSequence(event.getArena());
        }
    }

    @Override
    public void addPlayer(Player player, Team team) {
        ArenaPlayer aPlayer = new com.pepedevs.dbedwars.game.arena.ArenaPlayer(player, this);
        this.addPlayer(aPlayer);
    }

    @Override
    public void addPlayer(ArenaPlayer player, Team team) {
        PlayerJoinArenaLobbyEvent event =
                new PlayerJoinArenaLobbyEvent(
                        player.getPlayer(), this, this.settings.getLobby().toBukkit(this.world));
        event.call();
        if (event.isCancelled()) return;

        if (event.getArena().getPlayers().size() == event.getArena().getSettings().getMaxPlayer())
            return;

        this.players.add(player);
        player.getPlayer().teleport(event.getLocation());

        team.addPlayer(player);

        if (event.getArena().getStatus() == ArenaStatus.IDLING)
            event.getArena().setStatus(ArenaStatus.WAITING);

        this.scheduleMessage(
                event.getArena(), event.getPlayer(), event.getArena().getPlayers().size(), true);

        if (event.getArena().getPlayers().size() >= event.getArena().getSettings().getMinPlayers()
                && event.getArena().getStatus() != ArenaStatus.STARTING) {
            this.plugin.getGameManager().startArenaSequence(event.getArena());
        }
    }

    @Override
    public boolean kickPlayer(ArenaPlayer player) {
        this.kickPlayer(player, KickReason.UNKNOWN);
        return false;
    }

    @Override
    public boolean kickPlayer(ArenaPlayer player, KickReason reason) {
        if (this.status == ArenaStatus.WAITING) {
            PlayerLeaveArenaLobbyEvent event =
                    new PlayerLeaveArenaLobbyEvent(player.getPlayer(), player, this, reason);
            event.call();

            if (event.isCancelled() && reason != KickReason.DISCONNECT) return false;

            if (player.getTeam() != null) player.getTeam().removePlayer(player);

            this.scheduleMessage(
                    event.getArena(),
                    event.getPlayer(),
                    event.getArena().getPlayers().size(),
                    false);
            this.players.remove(player);

            if (event.getArena().getPlayers().size() == 0)
                event.getArena().setStatus(ArenaStatus.IDLING);
        } else {
            PlayerRemoveFromArenaEvent event =
                    new PlayerRemoveFromArenaEvent(
                            player.getPlayer(), player, this, player.getTeam(), reason);
            event.call();

            if (event.isCancelled() && reason != KickReason.DISCONNECT) return false;

            player.getTeam().removePlayer(player);

            this.players.remove(player);
            this.removed.put(player, reason);
        }

        // TODO: teleport to spawn
        return true;
    }

    @Override
    public void kickAllPlayers() {
        this.players.forEach(this::kickPlayer);
    }

    @Override
    public void kickAllPlayers(KickReason reason) {
        this.players.forEach(p -> this.kickPlayer(p, reason));
    }

    @Override
    public void broadcast(String msg) {
        this.broadcast(msg, null);
    }

    @Override
    public void broadcast(String msg, Predicate<ArenaPlayer> condition) {
        this.players.forEach(
                p -> {
                    if (condition != null && !condition.test(p)) return;

                    p.sendMessage(
                            ConfigurationUtils.parseMessage(
                                    ConfigurationUtils.parsePlaceholder(msg, p.getPlayer())));
                });
    }

    @Override
    public void broadcast(BaseComponent[] components) {
        this.players.forEach(p -> p.getPlayer().spigot().sendMessage(components));
    }

    @Override
    public Block setBlock(LocationXYZ location, Material material) {
        Block block = this.world.getBlockAt(location.toBukkit(this.world));
        block.setType(material);
        block.setMetadata(
                "placed", new FixedMetadataValue(this.plugin, this.getSettings().getName()));
        return block;
    }

    @Override
    public Block setBlock(Block block, Material material) {
        if (!block.getWorld().getName().equals(this.world.getName())) return null;

        block.setType(material);
        block.setMetadata(
                "placed", new FixedMetadataValue(this.plugin, this.getSettings().getName()));
        return block;
    }

    @Override
    public long getGameStartTime() {
        return this.startTime.toEpochMilli();
    }

    @Override
    public long getRunningTime() {
        return System.currentTimeMillis() - this.getGameStartTime();
    }

    @Override
    public boolean isSleeping() {
        return this.status == ArenaStatus.IDLING;
    }

    @Override
    public Set<ArenaPlayer> getPlayersInTeam(Team team) {
        return team.getPlayers();
    }

    @Override
    public Set<Team> getRemainingTeams() {
        return this.teams.stream().filter(t -> !t.isEliminated()).collect(Collectors.toSet());
    }

    @Override
    public Set<ArenaPlayer> getPlayers() {
        return Collections.unmodifiableSet(this.players);
    }

    @Override
    public Optional<ArenaPlayer> getAsArenaPlayer(Player player) {
        return this.players.stream().filter(p -> p.getPlayer().equals(player)).findFirst();
    }

    @Override
    public boolean isArenaPlayer(Player player) {
        return this.players.stream().anyMatch(p -> p.getPlayer().equals(player));
    }

    @Override
    public void destroyBed(ArenaPlayer player, Team affected) {
        if (affected.isBedBroken()) return;

        Block bed = affected.getBedLocation().toBukkit(this.world).getBlock();
        if (!Utils.isBed(bed)) return;

        // TODO: change message with placeholder and configuration
        BedDestroyEvent event =
                new BedDestroyEvent(
                        player,
                        affected,
                        bed,
                        this,
                        "&"
                                + affected.getColor().getChatSymbol()
                                + StringUtils.capitalize(affected.getName())
                                + "'s Bed &7 was destroyed by &"
                                + player.getTeam().getColor().getChatSymbol()
                                + player.getPlayer().getName(),
                        "&7Your bed was destroyed by &"
                                + player.getTeam().getColor().getChatSymbol()
                                + player.getPlayer().getName());
        event.call();

        if (event.isCancelled()) return;

        bed.getDrops().clear();
        bed.breakNaturally();
        event.getAffectedTeam().setBedBroken(true);
        event.getDestroyer().addBedDestroy();
        // TODO: change message
        // TODO: Add more effect
        this.broadcast(
                event.getBedBrokenMessage(), p -> !p.getTeam().equals(event.getAffectedTeam()));
        event.getAffectedTeam().sendMessage(event.getBedBrokenTeamMessage());
    }

    @Override
    public void setIngameScoreboard(ArenaPlayer player) {}

    @Override
    public void setLobbyScoreboard(Player player) {}

    @Override
    public boolean stop() {
        return false;
    }

    private void scheduleMessage(
            com.pepedevs.dbedwars.api.game.Arena arena, Player player, int size, boolean join) {
        this.plugin
                .getThreadHandler()
                .addAsyncWork(
                        new FixedRateWorkload(20) {
                            private boolean hasRun = false;

                            @Override
                            public void compute() {
                                // TODO: change message
                                arena.broadcast(
                                        StringUtils.translateAlternateColorCodes(
                                                "&5"
                                                        + player.getName()
                                                        + " &7"
                                                        + (join ? "joined" : "left")
                                                        + " the arena. &5("
                                                        + size
                                                        + "/"
                                                        + arena.getSettings().getMaxPlayer()
                                                        + ")"));
                                this.hasRun = true;
                            }

                            @Override
                            public boolean reSchedule() {
                                return !hasRun;
                            }
                        });
    }

    private void clearCache() {
        for (Team team : this.teams) {
            ((com.pepedevs.dbedwars.game.arena.Team) team).clearCache();
            team = null;
        }
        for (ArenaPlayer player : this.players) {
            player = null;
        }
        for (Spawner spawner : this.spawners) {
            spawner = null;
        }
        this.teams.clear();
        this.players.clear();
        this.spawners.clear();
        this.removed.clear();
        this.plugin
                .getThreadHandler()
                .addSyncWork(
                        () -> {
                            for (Player player :
                                    new ArrayList<>(
                                            Arena.this.scoreboard.getHandle().getViewers())) {
                                Arena.this.scoreboard.hide(player);
                            }
                            Arena.this.scoreboard.getHandle().update();
                        });
    }
}
