package me.abhigya.dbedwars.game.arena;

import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.math.collision.BoundingBox;
import me.Abhigya.core.util.tasks.workload.FixedRateWorkload;
import me.Abhigya.core.util.world.GameRuleDisableDaylightCycle;
import me.Abhigya.core.util.world.GameRuleType;
import me.Abhigya.core.util.world.WorldUtils;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.events.game.*;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.game.ArenaStatus;
import me.abhigya.dbedwars.api.game.Team;
import me.abhigya.dbedwars.api.game.settings.ArenaSettings;
import me.abhigya.dbedwars.api.game.spawner.Spawner;
import me.abhigya.dbedwars.api.task.Regeneration;
import me.abhigya.dbedwars.api.util.Color;
import me.abhigya.dbedwars.api.util.KickReason;
import me.abhigya.dbedwars.api.util.LocationXYZ;
import me.abhigya.dbedwars.configuration.PluginFiles;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableArena;
import me.abhigya.dbedwars.game.TeamAssigner;
import me.abhigya.dbedwars.game.arena.view.ShopView;
import me.abhigya.dbedwars.listeners.ArenaListener;
import me.abhigya.dbedwars.listeners.GameListener;
import me.abhigya.dbedwars.task.WorldRegenerator;
import me.abhigya.dbedwars.utils.ConfigurationUtils;
import me.abhigya.dbedwars.utils.Utils;
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

public class Arena implements me.abhigya.dbedwars.api.game.Arena {

    private final DBedwars plugin;
    private ConfigurableArena cfgArena;
    private ArenaSettings settings;

    private World world;
    private Regeneration regenerator;
    private ArenaStatus status;
    private boolean enabled;
    private Instant startTime;
    private ArenaListener arenaHandler;
    private GameListener gameHandler;

    private Set<Team> teams;
    private Set<ArenaPlayer> players;
    private Map<ArenaPlayer, KickReason> removed;
    private List<Spawner> spawners;

    public Arena(DBedwars plugin) {
        this.plugin = plugin;
        this.settings = new me.abhigya.dbedwars.game.arena.settings.ArenaSettings(this);
        this.teams = new HashSet<>();
        this.players = new HashSet<>();
        this.regenerator = new WorldRegenerator(this.plugin, this.settings.getRegenerationType(), this);
        this.status = ArenaStatus.STOPPED;
        this.arenaHandler = new ArenaListener(this.plugin, this);
        this.gameHandler = new GameListener(this.plugin, this);
    }

    public Arena(DBedwars plugin, ConfigurableArena cfg) {
        this(plugin);
        this.cfgArena = cfg;
        this.settings = new me.abhigya.dbedwars.game.arena.settings.ArenaSettings(this, cfg);
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
    public void setStatus(ArenaStatus status) {
        this.status = status;
    }

    @Override
    public boolean saveWorld(String worldFolder, boolean overwriteCache) {
        Validate.notNull(worldFolder, "World folder cannot be null!");

        File file = new File(worldFolder);
        if (file.isDirectory() && WorldUtils.worldFolderCheck(file)) {
            if (this.plugin.getGeneratorHandler().getWorldAdaptor().saveExist(this.settings.getName())) {
                if (overwriteCache)
                    return this.plugin.getGeneratorHandler().getWorldAdaptor().saveWorld(worldFolder, this.settings.getName());
            } else {
                return this.plugin.getGeneratorHandler().getWorldAdaptor().saveWorld(worldFolder, this.settings.getName());
            }
        } else {
            throw new IllegalStateException("World folder missing or was deleted before saving!");
        }

        return true;
    }

    @Override
    public boolean saveData(boolean overwriteData) {
        File file = new File(PluginFiles.ARENA_DATA_SETTINGS.getFile(), this.settings.getName() + ".yml");
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
        if (this.cfgArena == null)
            return;

        ((me.abhigya.dbedwars.game.arena.settings.ArenaSettings) this.settings).update(this.cfgArena);
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
        GameRuleType.MOB_GRIEFING.apply(world, false);
        GameRuleType.MOB_SPAWNING.apply(world, false);
        GameRuleType.FIRE_TICK.apply(world, false);
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
        this.plugin.getThreadHandler().addAsyncWork(() -> {
            File file = new File(PluginFiles.ARENA_DATA_SETTINGS.getFile(), this.settings.getName() + ".yml");
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
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
        return this.getPlayers().stream().filter(ArenaPlayer::isSpectator).collect(Collectors.toList());
    }

    @Override
    public ArenaStatus getStatus() {
        return this.status;
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
            return this.spawners.stream().filter(s -> s.getBoundingBox().contains(location.toVector())).findFirst();
        }

        BoundingBox box = new BoundingBox(location.getX() - range, location.getY() - range, location.getZ() - range,
                location.getX() + range, location.getY() + range, location.getZ() + range);
        return this.spawners.stream().filter(s -> s.getBoundingBox().intersects(box)).findFirst();
    }

    @Override
    public boolean isCurrentlyRegenerating() {
        return this.status == ArenaStatus.REGENERATING;
    }

    @Override
    public boolean isConfigured() {
        return this.settings.getName() != null && !this.settings.getAvailableTeams().isEmpty() && this.settings.getLobby() != null &&
                !this.settings.getDrops().isEmpty() && this.plugin.getGeneratorHandler().getWorldAdaptor().saveExist(this.settings.getName()) &&
                this.settings.getAvailableTeams().stream().allMatch(Team::isConfigured);
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

        if (event.isCancelled())
            return false;

        this.gameHandler.register();
        this.arenaHandler.unregister();
        this.players.forEach(p -> {
            p.spawn(p.getTeam().getSpawn().toBukkit(this.world));
            ((ShopView) p.getShopView()).load();
        });
        this.teams.forEach(t -> t.getSpawners().entries().forEach(e ->
                new me.abhigya.dbedwars.game.arena.Spawner(this.plugin, e.getKey(), e.getValue().toBukkit(this.getWorld()), this, t).init()));
        this.settings.getDrops().entries().forEach(e ->
                new me.abhigya.dbedwars.game.arena.Spawner(this.plugin, e.getKey(), e.getValue().toBukkit(this.getWorld()), this, null).init());

        this.teams.forEach(t -> {
            t.spawnShopNpc(t.getShopNpc());
            t.spawnUpgradesNpc(t.getUpgradesNpc());
        });

        this.status = ArenaStatus.RUNNING;
        this.startTime = Instant.now();
        return true;
    }

    // TODO: revamp this
    @Override
    public boolean end() {
        if (this.status != ArenaStatus.RUNNING)
            return false;

        ArenaEndEvent event = new ArenaEndEvent(this, this.players.stream().filter(p -> !p.isFinalKilled()).collect(Collectors.toSet()));
        event.call();

        this.status = ArenaStatus.ENDING;
        this.plugin.getThreadHandler().addSyncWork(new FixedRateWorkload(this.plugin.getMainConfiguration().getArenaSection().getGameEndDelay() * 20) {
            @Override
            public void compute() {
                Arena.this.world.getPlayers().forEach(p -> p.teleport(Arena.this.plugin.getServer().getWorlds().get(0).getSpawnLocation()));
                Arena.this.plugin.getThreadHandler().addAsyncWork(Arena.this::loadWorld);
            }
        });
        this.world.getPlayers().forEach(p -> p.sendMessage("You won!"));
        this.gameHandler.unregister();
        return true;
    }

    @Override
    public void addPlayer(Player player) {
        ArenaPlayer aplayer = new me.abhigya.dbedwars.game.arena.ArenaPlayer(player, this);
        this.addPlayer(aplayer);
    }

    @Override
    public void addPlayer(ArenaPlayer player) {
        PlayerJoinArenaLobbyEvent event = new PlayerJoinArenaLobbyEvent(player.getPlayer(), this, this.settings.getLobby().toBukkit(this.world));
        event.call();
        if (event.isCancelled())
            return;

        if (event.getArena().getPlayers().size() == event.getArena().getSettings().getMaxPlayer())
            return;

        this.players.add(player);
        player.getPlayer().teleport(event.getLocation());

        if (event.getArena().getStatus() == ArenaStatus.IDLING)
            event.getArena().setStatus(ArenaStatus.WAITING);

        this.scheduleMessage(event.getArena(), event.getPlayer(), event.getArena().getPlayers().size(), true);

        if (event.getArena().getPlayers().size() >= event.getArena().getSettings().getMinPlayers()
                && event.getArena().getStatus() != ArenaStatus.STARTING) {
            this.plugin.getGameManager().startArenaSequence(event.getArena());
        }
    }

    @Override
    public void addPlayer(Player player, Team team) {
        ArenaPlayer aPlayer = new me.abhigya.dbedwars.game.arena.ArenaPlayer(player, this);
        this.addPlayer(aPlayer);
    }

    @Override
    public void addPlayer(ArenaPlayer player, Team team) {
        PlayerJoinArenaLobbyEvent event = new PlayerJoinArenaLobbyEvent(player.getPlayer(), this, this.settings.getLobby().toBukkit(this.world));
        event.call();
        if (event.isCancelled())
            return;

        if (event.getArena().getPlayers().size() == event.getArena().getSettings().getMaxPlayer())
            return;

        this.players.add(player);
        player.getPlayer().teleport(event.getLocation());

        team.addPlayer(player);

        if (event.getArena().getStatus() == ArenaStatus.IDLING)
            event.getArena().setStatus(ArenaStatus.WAITING);

        this.scheduleMessage(event.getArena(), event.getPlayer(), event.getArena().getPlayers().size(), true);

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
            PlayerLeaveArenaLobbyEvent event = new PlayerLeaveArenaLobbyEvent(player.getPlayer(), player, this, reason);
            event.call();

            if (event.isCancelled() && reason != KickReason.DISCONNECT)
                return false;

            if (player.getTeam() != null)
                player.getTeam().removePlayer(player);

            this.scheduleMessage(event.getArena(), event.getPlayer(), event.getArena().getPlayers().size(), false);
            this.players.remove(player);

            if (event.getArena().getPlayers().size() == 0)
                event.getArena().setStatus(ArenaStatus.IDLING);
        } else {
            PlayerRemoveFromArenaEvent event = new PlayerRemoveFromArenaEvent(player.getPlayer(), player, this, player.getTeam(), reason);
            event.call();

            if (event.isCancelled() && reason != KickReason.DISCONNECT)
                return false;

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
        this.players.forEach(p -> {
            if (condition != null && !condition.test(p))
                return;

            p.sendMessage(ConfigurationUtils.parseMessage(ConfigurationUtils.parsePlaceholder(msg, p.getPlayer())));
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
        block.setMetadata("placed", new FixedMetadataValue(this.plugin, this.getSettings().getName()));
        return block;
    }

    @Override
    public Block setBlock(Block block, Material material) {
        if (!block.getWorld().getName().equals(this.world.getName()))
            return null;

        block.setType(material);
        block.setMetadata("placed", new FixedMetadataValue(this.plugin, this.getSettings().getName()));
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
        if (affected.isBedBroken())
            return;

        Block bed = affected.getBedLocation().toBukkit(this.world).getBlock();
        if (!Utils.isBed(bed))
            return;

        // TODO: change message with placeholder and configuration
        BedDestroyEvent event = new BedDestroyEvent(player, affected, bed, this,
                "&" + affected.getColor().getChatSymbol() + StringUtils.capitalize(affected.getName())
                + "'s Bed &7 was destroyed by &" + player.getTeam().getColor().getChatSymbol() + player.getPlayer().getName(),
                "&7Your bed was destroyed by &" + player.getTeam().getColor().getChatSymbol() + player.getPlayer().getName());
        event.call();

        if (event.isCancelled())
            return;

        bed.breakNaturally();
        bed.getDrops().clear();
        event.getAffectedTeam().setBedBroken(true);
        // TODO: change message
        // TODO: Add more effect
        this.broadcast(event.getBedBrokenMessage(), p -> !p.getTeam().equals(event.getAffectedTeam()));
        event.getAffectedTeam().sendMessage(event.getBedBrokenTeamMessage());
    }

    @Override
    public void setIngameScoreboard(ArenaPlayer player) {

    }

    @Override
    public void setLobbyScoreboard(Player player) {

    }

    @Override
    public boolean stop() {
        return false;
    }

    private void scheduleMessage(me.abhigya.dbedwars.api.game.Arena arena, Player player, int size, boolean join) {
        this.plugin.getThreadHandler().addAsyncWork(new FixedRateWorkload(20) {
            private boolean hasRun = false;

            @Override
            public void compute() {
                //TODO: change message
                arena.broadcast(StringUtils.translateAlternateColorCodes("&5" + player.getName() + " &7" + (join ? "joined" : "left") + " the arena. &5("
                        + size + "/" + arena.getSettings().getMaxPlayer() + ")"));
                this.hasRun = true;
            }

            @Override
            public boolean reSchedule() {
                return !hasRun;
            }
        });
    }

}
