package com.pepedevs.dbedwars.game.arena;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.events.*;
import com.pepedevs.dbedwars.api.feature.custom.ArenaEndFireworkFeature;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.ArenaStatus;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.game.settings.ArenaSettings;
import com.pepedevs.dbedwars.api.game.spawner.DropType;
import com.pepedevs.dbedwars.api.game.spawner.Spawner;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.api.messaging.member.MessagingMember;
import com.pepedevs.dbedwars.api.messaging.message.AdventureMessage;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.api.util.Color;
import com.pepedevs.dbedwars.api.util.KickReason;
import com.pepedevs.dbedwars.api.util.LocationXYZ;
import com.pepedevs.dbedwars.configuration.Lang;
import com.pepedevs.dbedwars.configuration.PluginFiles;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableArena;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableScoreboard;
import com.pepedevs.dbedwars.features.BedWarsFeatures;
import com.pepedevs.dbedwars.game.TeamAssigner;
import com.pepedevs.dbedwars.game.arena.view.shoptest.ShopView;
import com.pepedevs.dbedwars.listeners.ArenaListener;
import com.pepedevs.dbedwars.listeners.GameListener;
import com.pepedevs.dbedwars.messaging.AbstractMessaging;
import com.pepedevs.dbedwars.task.DefaultWorldAdaptor;
import com.pepedevs.dbedwars.utils.DatabaseUtils;
import com.pepedevs.dbedwars.utils.Debugger;
import com.pepedevs.dbedwars.utils.ScoreboardWrapper;
import com.pepedevs.dbedwars.utils.Utils;
import com.pepedevs.radium.task.Workload;
import com.pepedevs.radium.utils.Acceptor;
import com.pepedevs.radium.utils.StringUtils;
import com.pepedevs.radium.utils.math.collision.BoundingBox;
import com.pepedevs.radium.utils.scheduler.SchedulerUtils;
import com.pepedevs.radium.utils.world.GameRuleDisableDaylightCycle;
import com.pepedevs.radium.utils.world.GameRuleType;
import com.pepedevs.radium.utils.world.WorldUtils;
import net.kyori.adventure.text.Component;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

public class Arena extends AbstractMessaging implements com.pepedevs.dbedwars.api.game.Arena {

    private final DBedwars plugin;
    private ConfigurableArena cfgArena;
    private ArenaSettings settings;
    private World world;
    private ArenaStatus status;
    private boolean enabled;
    private Instant startTime;
    private ScoreboardWrapper scoreboard;
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
            if (this.plugin.getGeneratorHandler().getWorldAdaptor().saveExist(this.settings.getName())) {
                if (overwriteCache) return this.plugin.getGeneratorHandler().getWorldAdaptor().saveWorld(worldFolder, this.settings.getName());
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
        File file = new File(PluginFiles.ARENA_DATA_SETTINGS, this.settings.getName() + ".yml");
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
        for (Team team : this.settings.getAvailableTeams()) {
            this.reloadData();
        }
    }

    @Override
    public World loadWorld() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (Arena.this.plugin.getServer().getWorld(Arena.this.settings.getName()) == null) {
            Debugger.debug("World is null");
            if (this.plugin.getGeneratorHandler().getWorldAdaptor() instanceof DefaultWorldAdaptor) {
                Debugger.debug("Default World generator");
                SchedulerUtils.runTask(new Runnable() {
                    @Override
                    public void run() {
                        Debugger.debug("Loading world: " + Arena.this.settings.getName());
                        Arena.this.plugin.getGeneratorHandler().getWorldAdaptor().createWorld(Arena.this.settings.getName(), Arena.this.settings.getWorldEnv());
                        Debugger.debug("World loaded: " + Arena.this.settings.getName());
                        future.complete(null);
                    }
                }, this.plugin);
            } else {
                this.plugin.getGeneratorHandler().getWorldAdaptor().createWorld(this.settings.getName(), this.settings.getWorldEnv());
                future.complete(null);
            }
        } else {
            future.complete(null);
        }
        Debugger.debug("Waiting for world to load...");
        future.join();
        Debugger.debug("World loaded!");
        return this.plugin.getGeneratorHandler().getWorldAdaptor().loadWorldFromSave(this.settings.getName());
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
        this.plugin.getThreadHandler().submitAsync(new Workload() {
            @Override
            public void compute() {
                File file = new File(PluginFiles.ARENA_DATA_SETTINGS,Arena.this.settings.getName() + ".yml");
                FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                configuration.set("enabled", flag);
                try {
                    configuration.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Arena.this.cfgArena.update();
            }
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
        // TODO: rework
        List<ArenaPlayer> players = new ArrayList<>();
//        for (ArenaPlayer player : this.getPlayers()) {
//            if (player.isSpectator()) players.add(player);
//        }
        return players;
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
            for (Spawner spawner : this.spawners) {
                if (spawner.getBoundingBox().contains(location.toVector())) return Optional.of(spawner);
            }
            return Optional.empty();
        }

        BoundingBox box = new BoundingBox(
                    location.getX() - range,
                    location.getY() - range,
                    location.getZ() - range,
                    location.getX() + range,
                    location.getY() + range,
                    location.getZ() + range);
        for (Spawner spawner : this.spawners) {
            if (spawner.getBoundingBox().intersects(box)) return Optional.of(spawner);
        }
        return Optional.empty();
    }

    @Override
    public boolean isCurrentlyRegenerating() {
        return this.status == ArenaStatus.REGENERATING;
    }

    @Override
    public boolean isConfigured() {
        boolean returnVal = true;
        for (Team team : this.settings.getAvailableTeams()) {
            returnVal = returnVal && team.isConfigured();
        }
        return this.settings.getName() != null
                && !this.settings.getAvailableTeams().isEmpty()
                && this.settings.getLobby() != null
                && !this.settings.getDrops().isEmpty()
                && this.plugin.getGeneratorHandler().getWorldAdaptor().saveExist(this.settings.getName())
                && returnVal;
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
        for (ArenaPlayer player : this.players) {
            this.teams.add(player.getTeam());
        }

        ArenaStartEvent event = new ArenaStartEvent(this);
        event.call();

        if (event.isCancelled()) return false;

        this.gameHandler.register();
        this.arenaHandler.unregister();

        // TODO manage scoreboard
        ConfigurableScoreboard scoreboard = new ArrayList<>(Arena.this.plugin.getConfigHandler().getScoreboards()).get(0);
        Arena.this.scoreboard = ScoreboardWrapper.from(scoreboard.getTitle(), scoreboard.getContent());
        for (ArenaPlayer player : Arena.this.players) {
            Arena.this.scoreboard.show(player.getPlayer());
        }

        for (Team team : this.teams) {
            List<String> names = new ArrayList<>();
            for (ArenaPlayer player : team.getPlayers()) {
                names.add(player.getName());
            }

            WrapperPlayServerTeams.ScoreBoardTeamInfo info = new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                    Component.text(team.getColor().getName(), team.getColor().getColorComponent()),
                    Component.text("[" + team.getColor().getName() + "] ", team.getColor().getColorComponent()),
                    null,
                    WrapperPlayServerTeams.NameTagVisibility.ALWAYS,
                    WrapperPlayServerTeams.CollisionRule.ALWAYS,
                    team.getColor().getColorComponent(),
                    WrapperPlayServerTeams.OptionData.NONE
            );
            WrapperPlayServerTeams teams = new WrapperPlayServerTeams(team.getName(), WrapperPlayServerTeams.TeamMode.CREATE,
                    Optional.of(info), names);

            for (ArenaPlayer player : this.getPlayers()) {
                PacketEvents.getAPI().getPlayerManager().sendPacket(player.getPlayer(), teams);
            }
            for (Map.Entry<DropType, LocationXYZ> entry : team.getSpawners().entries()) {
                new com.pepedevs.dbedwars.game.arena.Spawner(this.plugin, entry.getKey(), entry.getValue().toBukkit(this.getWorld()), this, team).init();
            }
            team.spawnShopNpc(team.getShopNpc());
            team.spawnUpgradesNpc(team.getUpgradesNpc());
        }

        Debugger.debug("Setting spawners");
        for (Map.Entry<DropType, LocationXYZ> entry : this.settings.getDrops().entries()) {
            new com.pepedevs.dbedwars.game.arena.Spawner(this.plugin, entry.getKey(), entry.getValue().toBukkit(this.getWorld()), this, null).init();
        }

        Debugger.debug("Setting lobby");
        for (ArenaPlayer player : this.players) {
            player.spawn(player.getTeam().getSpawn().toBukkit(this.world));
            ((ShopView) player.getShopView()).loadFromConfig(this.plugin.getConfigHandler().getShop());
            player.getPlayer().getEnderChest().clear();
        }
        Debugger.debug("Arena started!");

        this.status = ArenaStatus.RUNNING;
        this.startTime = Instant.now();
        return true;
    }

    // TODO: revamp this
    @Override
    public boolean end() {
        if (this.status != ArenaStatus.RUNNING) return false;

        List<ArenaPlayer> list = new ArrayList<>(this.players);
        list.removeIf(new Predicate<ArenaPlayer>() {
            @Override
            public boolean test(ArenaPlayer arenaPlayer) {
                return arenaPlayer.isFinalKilled();
            }
        });

        ArenaEndEvent event = new ArenaEndEvent(this, list);
        event.call();

        if (event.isCancelled()) return false;

        this.status = ArenaStatus.ENDING;
        DatabaseUtils.saveGameData(this);
        this.plugin.getThreadHandler().runTaskLater(new Runnable() {
            @Override
            public void run() {
                Arena.this.clearCache();
                CompletableFuture<Void> teleported = new CompletableFuture<>();
                SchedulerUtils.runTask(new Runnable() {
                    @Override
                    public void run() {
                        //TODO TAKE SPAWN LOCATION FROM CONFIG
                        for (Player player : Arena.this.world.getPlayers()) {
                            player.teleport(Arena.this.plugin.getServer().getWorlds().get(0).getSpawnLocation());
                        }
                        teleported.complete(null);
                    }
                }, Arena.this.plugin);
                try {
                    teleported.get();
                } catch (InterruptedException | ExecutionException ignored) {}
                Arena.this.load();
            }
        }, (long) Arena.this.getSettings().getGameEndDelay() * 20 * 50);

        for (Team team : this.teams) {
            List<String> names = new ArrayList<>();
            for (ArenaPlayer player : team.getPlayers()) {
                names.add(player.getName());
            }
            WrapperPlayServerTeams teams = new WrapperPlayServerTeams(team.getName(), WrapperPlayServerTeams.TeamMode.REMOVE,
                    Optional.empty(), names);

            for (ArenaPlayer player : this.getPlayers()) {
                PacketEvents.getAPI().getPlayerManager().sendPacket(player.getPlayer(), teams);
            }
        }

        this.plugin.getFeatureManager().runFeature(BedWarsFeatures.ARENA_END_FIREWORK_FEATURE, ArenaEndFireworkFeature.class, new Acceptor<ArenaEndFireworkFeature>() {
            @Override
            public boolean accept(ArenaEndFireworkFeature feature) {
                feature.spawn(list.get(0).getTeam(), Arena.this);
                return true;
            }
        });

        // TODO: give config?
        LinkedHashMap<ArenaPlayer, Integer> leaderboard = Utils.getGameLeaderBoard(this.players);
        StringBuilder builder = new StringBuilder("<gold>" + StringUtils.repeat("⬛", 35));
        //TODO IMPLEMENT FROM LANG
        byte b = 0;
        for (Map.Entry<ArenaPlayer, Integer> entry : leaderboard.entrySet()) {
            if (b++ == 4) break;
            builder.append("\n<green>").append(b).append(". ").append(entry.getKey().getPlayer().getName()).append("   ").append(entry.getValue()).append("pts");
        }
        builder.append("\n<gold>").append(StringUtils.repeat("⬛", 35));
        /*String message = ""*/
        String message = builder.toString();
        for (ArenaPlayer player : this.players) {
            if (player.getArena().getWorld().equals(player.getPlayer().getWorld())) {
                player.sendMessage(AdventureMessage.from(message));
            }
        }

        this.gameHandler.unregister();
        return true;
    }

    @Override
    public void addPlayer(Player player) {
        ArenaPlayer arenaPlayer = new com.pepedevs.dbedwars.game.arena.ArenaPlayer(this.plugin, player, this);
        this.addPlayer(arenaPlayer);
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
        ArenaPlayer aPlayer = new com.pepedevs.dbedwars.game.arena.ArenaPlayer(this.plugin, player, this);
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

            this.scheduleMessage(event.getArena(), event.getPlayer(), event.getArena().getPlayers().size(), false);
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
        for (ArenaPlayer player : this.players) {
            kickPlayer(player);
        }
    }

    @Override
    public void kickAllPlayers(KickReason reason) {
        for (ArenaPlayer player : this.players) {
            this.kickPlayer(player, reason);
        }
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
        if (!block.getWorld().getName().equals(this.world.getName())) return null;

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
        Set<Team> teams = new HashSet<>();
        for (Team team : this.teams) {
            if (!team.isEliminated()) teams.add(team);
        }
        return teams;
    }

    @Override
    public Set<ArenaPlayer> getPlayers() {
        return Collections.unmodifiableSet(this.players);
    }

    @Override
    public Optional<ArenaPlayer> getAsArenaPlayer(Player player) {
        for (ArenaPlayer arenaPlayer : this.players) {
            if (arenaPlayer.getPlayer().equals(player)) return Optional.of(arenaPlayer);
        }
        return Optional.empty();
    }

    @Override
    public boolean isArenaPlayer(Player player) {
        for (ArenaPlayer arenaPlayer : this.players) {
            if (arenaPlayer.getPlayer().equals(player)) return true;
        }
        return false;
    }

    @Override
    public void destroyBed(ArenaPlayer player, Team affected) {
        if (affected.isBedBroken()) return;

        Block bed = affected.getBedLocation().toBukkit(this.world).getBlock();
        if (!Utils.isBed(bed)) return;

        final PlaceholderEntry[] bedBrokenPlaceholders = new PlaceholderEntry[]{
                PlaceholderEntry.symbol("defend_team_color", Utils.getConfigCode(affected.getColor())),
                PlaceholderEntry.symbol("attack_team_color", Utils.getConfigCode(player.getTeam().getColor())),
                PlaceholderEntry.symbol("defend_team_name", affected.getName()),
                PlaceholderEntry.symbol("attack_team_name", player.getTeam().getName()),
                PlaceholderEntry.symbol("attack_name", player.getName())
        };
        Message bedBrokenOthers = Lang.BED_BROKEN_OTHERS.asMessage();
        bedBrokenOthers.addPlaceholders(bedBrokenPlaceholders);
        Message bedBrokenSelf = Lang.BED_BROKEN_SELF.asMessage();
        bedBrokenSelf.addPlaceholders(bedBrokenPlaceholders);
        BedDestroyEvent event = new BedDestroyEvent(player, affected, bed, this, bedBrokenOthers, bedBrokenSelf);
        event.call();

        if (event.isCancelled()) return;

        bed.getDrops().clear();
        bed.breakNaturally();
        event.getAffectedTeam().setBedBroken(true);
        event.getDestroyer().getPoints().getCount(ArenaPlayer.PlayerPoints.BEDS).increment();
        // TODO: Add more effect
        this.sendMessage(event.getBedBrokenMessages(), new Predicate<MessagingMember>() {
            @Override
            public boolean test(MessagingMember member) {
                return ((ArenaPlayer) member).getTeam().equals(event.getAffectedTeam());
            }
        });
        event.getAffectedTeam().sendMessage(event.getBedBrokenMessages());
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

    private void scheduleMessage(com.pepedevs.dbedwars.api.game.Arena arena, Player player, int size, boolean join) {
        this.plugin.getThreadHandler().getTaskHandler().runTaskLater(this.plugin.getThreadHandler().getTask().getID(), new Runnable() {

           private final PlaceholderEntry[] joinLeavePlaceholders = new PlaceholderEntry[]{
                    PlaceholderEntry.symbol("player_name", player.getName()),
                    PlaceholderEntry.symbol("current_players", String.valueOf(size)),
                    PlaceholderEntry.symbol("max_players", String.valueOf(arena.getSettings().getMaxPlayer()))
            };

            @Override
            public void run() {
                Message message = (join ? Lang.ARENA_JOIN_MESSAGE.asMessage() : Lang.ARENA_LEAVE_MESSAGE.asMessage());
                message.addPlaceholders(joinLeavePlaceholders);
                arena.sendMessage(message);
            }
        },1000);
    }

    private void clearCache() {
        for (Team team : this.teams) {
            ((com.pepedevs.dbedwars.game.arena.Team) team).clearCache();
            team = null;
        }
        for (ArenaPlayer player : this.players) {
            Arena.this.scoreboard.hide(player.getPlayer());
            player = null;
        }
        for (Spawner spawner : this.spawners) {
            spawner = null;
        }
        this.teams.clear();
        this.players.clear();
        this.spawners.clear();
        this.removed.clear();
    }

    @Override
    public Collection<MessagingMember> getMembers() {
        return new ArrayList<>(this.players);
    }

    @Override
    public String toString() {
        return "Arena{" +
                "plugin=" + plugin +
                ", cfgArena=" + cfgArena +
                ", settings=" + settings +
                ", world=" + world +
                ", status=" + status +
                ", enabled=" + enabled +
                ", startTime=" + startTime +
                ", scoreboard=" + scoreboard +
                ", arenaHandler=" + arenaHandler +
                ", gameHandler=" + gameHandler +
                ", teams=" + teams +
                ", players=" + players +
                ", removed=" + removed +
                ", spawners=" + spawners +
                '}';
    }
}
