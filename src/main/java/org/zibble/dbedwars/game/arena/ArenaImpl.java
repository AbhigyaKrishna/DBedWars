package org.zibble.dbedwars.game.arena;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.events.*;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.ArenaStatus;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.game.spawner.Spawner;
import org.zibble.dbedwars.api.game.statistics.BedBrokenStatistics;
import org.zibble.dbedwars.api.game.statistics.DeathStatistics;
import org.zibble.dbedwars.api.messaging.member.MessagingMember;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.objects.math.BoundingBox;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.Duration;
import org.zibble.dbedwars.api.util.KickReason;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.game.ArenaDataHolderImpl;
import org.zibble.dbedwars.game.TeamAssigner;
import org.zibble.dbedwars.game.arena.settings.ArenaSettingsImpl;
import org.zibble.dbedwars.game.arena.spawner.SpawnerImpl;
import org.zibble.dbedwars.listeners.ArenaListener;
import org.zibble.dbedwars.listeners.GameListener;
import org.zibble.dbedwars.messaging.AbstractMessaging;
import org.zibble.dbedwars.utils.Util;
import org.zibble.dbedwars.utils.gamerule.GameRuleDisableDaylightCycle;
import org.zibble.dbedwars.utils.gamerule.GameRuleType;

import java.time.Instant;
import java.util.*;

public class ArenaImpl extends AbstractMessaging implements Arena {

    private static final String WORlD_NAME_FORMAT = "bw_arena_%s";

    private final DBedwars plugin;

    private final String name;
    private final ArenaDataHolderImpl dataHolder;
    private String worldFileName;
    private final ArenaSettingsImpl settings;
    private ArenaStatus status;

    private World world;
    private Instant startTime;
    private final ArenaListener arenaHandler;
    private final GameListener gameHandler;
    private final List<Spawner> spawners;
    private final Set<TeamImpl> teams;
    private final Set<ArenaPlayerImpl> players;
    private final List<ArenaSpectatorImpl> spectators;
    private final Map<ArenaPlayerImpl, KickReason> removed;
    private final DeathStatistics deathStatistics;
    private final BedBrokenStatistics bedBrokenStatistics;

    public ArenaImpl(DBedwars plugin, String name, ArenaDataHolderImpl dataHolder) {
        this(plugin, name, dataHolder, new ArenaSettingsImpl());
    }

    public ArenaImpl(DBedwars plugin, String name, ArenaDataHolderImpl dataHolder, ArenaSettingsImpl settings) {
        this.plugin = plugin;
        this.name = name;
        this.dataHolder = dataHolder;
        this.settings = settings;
        this.status = ArenaStatus.STOPPED;
        this.teams = new HashSet<>();
        this.players = new HashSet<>();
        this.spawners = new ArrayList<>();
        this.spectators = new ArrayList<>(0);
        this.removed = new HashMap<>(0);
        this.arenaHandler = new ArenaListener(this.plugin, this);
        this.gameHandler = new GameListener(this.plugin, this);
        this.deathStatistics = new DeathStatistics(this);
        this.bedBrokenStatistics = new BedBrokenStatistics(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getGameId() {
        return this.world.getName();
    }

    @Override
    public ArenaSettingsImpl getSettings() {
        return this.settings;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public ArenaDataHolderImpl getDataHolder() {
        return dataHolder;
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
    public ActionFuture<World> loadWorld() {
        return this.plugin.getHookManager().getWorldAdaptor().loadWorldFromSave(
                worldFileName,
                String.format(WORlD_NAME_FORMAT, this.getName()),
                this.getDataHolder().getEnvironment());
    }

    @Override
    public ActionFuture<World> load() {
        this.setStatus(ArenaStatus.REGENERATING);
        return this.loadWorld().thenApply(world -> {
            GameRuleType.SHOW_DEATH_MESSAGES.apply(world, false);
            GameRuleType.MOB_SPAWNING.apply(world, false);
            GameRuleType.KEEP_INVENTORY.apply(world, true);
            GameRuleType.SHOW_DEATH_MESSAGES.apply(world, false);
            GameRuleType.SPECTATORS_GENERATE_CHUNKS.apply(world, false);
            new GameRuleDisableDaylightCycle(8000).apply(world);
            this.world = world;
            this.arenaHandler.register();
            this.setStatus(ArenaStatus.IDLING);
            return world;
        });
    }

    @Override
    public TeamImpl getTeam(Color color) {
        for (TeamImpl team : teams) {
            if (team.getColor().equals(color)) {
                return team;
            }
        }
        return null;
    }

    @Override
    public Set<TeamImpl> getTeams() {
        return Collections.unmodifiableSet(this.teams);
    }

    @Override
    public List<ArenaSpectatorImpl> getSpectators() {
        return Collections.unmodifiableList(this.spectators);
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
    public boolean isSleeping() {
        return this.status == ArenaStatus.SLEEPING;
    }

    @Override
    public boolean isRunning() {
        return this.status == ArenaStatus.RUNNING;
    }

    @Override
    public boolean start(boolean force) {
        if (!force && this.players.size() < this.getDataHolder().getMinPlayersToStart()) {
            return false;
        }

        for (Map.Entry<Color, ArenaDataHolderImpl.TeamDataHolderImpl> entry : this.getDataHolder().getTeamData().entrySet()) {
            TeamImpl team = new TeamImpl(this.plugin, entry.getKey(), this);
            team.init(entry.getValue());
            this.teams.add(team);
        }

        TeamAssigner ta = new TeamAssigner(this);
        ta.assign();

        ArenaStartEvent event = new ArenaStartEvent(this);
        event.call();

        if (event.isCancelled()) return false;

        this.gameHandler.register();
        this.arenaHandler.unregister();

        for (ArenaPlayerImpl player : this.players) {
            player.initScoreboard(null); // FIXME: 26-03-2022 
        }

        this.teams.removeIf(team -> {
            boolean empty = team.getPlayers().isEmpty();
            team.clearCache();
            return empty;
        });

        for (TeamImpl team : this.teams) {
            ArenaDataHolderImpl.TeamDataHolderImpl teamData = this.getDataHolder().getTeamData().get(team.getColor());
            for (ArenaDataHolderImpl.SpawnerDataHolderImpl spawnerData : teamData.getSpawners()) {
                SpawnerImpl spawner = new SpawnerImpl(this.plugin, spawnerData.getDropType(), this, team);
                spawner.init(spawnerData.getLocation().toBukkit(this.world), 1);
                this.spawners.add(spawner);
            }
            team.complete(teamData);
        }

        this.startTime = Instant.now();

        return true;
    }

    @Override
    public boolean end() {
        if (!this.checkState(ArenaStatus.RUNNING)) {
            return false;
        }

        List<ArenaPlayerImpl> list = new ArrayList<>(this.players);
        list.removeIf(ArenaPlayerImpl::isFinalKilled);

        ArenaEndEvent event = new ArenaEndEvent(this, list);
        event.call();

        if (event.isCancelled()) return false;

        this.status = ArenaStatus.ENDING;
        return true;
    }

    @Override
    public boolean joinGame(Player player) {
        if (!this.checkState(ArenaStatus.IDLING, ArenaStatus.WAITING, ArenaStatus.SLEEPING, ArenaStatus.STARTING))
            throw new IllegalStateException("Cannot join arena in state " + this.status);

        for (ArenaPlayerImpl arenaPlayer : this.players) {
            if (arenaPlayer.getUUID().equals(player.getUniqueId())) {
                return false;
            }
        }

        Message message = ConfigLang.ARENA_JOIN_MESSAGE.asMessage();
        message.addPlaceholders(this.getJoinPlaceholders(player));

        PlayerJoinArenaLobbyEvent event = new PlayerJoinArenaLobbyEvent(player, this, this.getDataHolder().getWaitingLocation().toBukkit(this.world), message);

        event.call();
        if (event.isCancelled()) return false;

        if (this.players.size() >= this.getDataHolder().getMaxPlayers()) {
            return false;
        }

        ArenaPlayerImpl arenaPlayer = new ArenaPlayerImpl(this.plugin, player, this);
        this.players.add(arenaPlayer);
        arenaPlayer.teleport(event.getLocation());

        this.sendMessage(event.getJoinMessage());

        if (this.getStatus() == ArenaStatus.IDLING)
            this.setStatus(ArenaStatus.WAITING);

        if (event.getArena().getPlayers().size() >= event.getArena().getDataHolder().getMinPlayersToStart()
                && event.getArena().getStatus() != ArenaStatus.STARTING) {
            this.plugin.getGameManager().startArenaSequence(event.getArena());
        }

        return true;
    }

    @Override
    public boolean joinGame(Player player, Color team) {
        if (this.joinGame(player)) {
            this.getAsArenaPlayer(player).get().setTeam(team);
            return true;
        }
        return false;
    }

    @Override
    public boolean rejoinGame(Player player) {
        if (!this.checkState(ArenaStatus.RUNNING))
            throw new IllegalStateException("Cannot join arena in state " + this.status);

        for (ArenaPlayerImpl arenaPlayer : this.players) {
            if (arenaPlayer.getUUID().equals(player.getUniqueId())) {
                return false;
            }
        }

        ArenaPlayerImpl p = null;
        for (ArenaPlayerImpl arenaPlayer : this.removed.keySet()) {
            if (arenaPlayer.getUUID().equals(player.getUniqueId())) {
                p = arenaPlayer;
                break;
            }
        }
        if (p == null)
            return false;

        this.removed.remove(p);

        Message message = ConfigLang.REJOIN_BROADCAST_MESSAGE.asMessage();
        message.addPlaceholders(this.getJoinPlaceholders(player));

        PlayerRejoinArenaEvent event = new PlayerRejoinArenaEvent(player, this, this.getDataHolder().getSpectatorLocation().toBukkit(this.world), message);

        event.call();
        if (event.isCancelled()) return false;

        p.teleport(event.getLocation());
        p.startRespawn();

        this.sendMessage(event.getJoinMessage());

        return true;
    }

    @Override
    public boolean spectateGame(Player player) {
        if (!this.checkState(ArenaStatus.RUNNING))
            throw new IllegalStateException("Cannot join arena in state " + this.status);

        for (ArenaPlayerImpl arenaPlayer : this.players) {
            if (arenaPlayer.getUUID().equals(player.getUniqueId())) {
                return false;
            }
        }

        for (ArenaSpectatorImpl spectator : this.spectators) {
            if (spectator.getUUID().equals(player.getUniqueId())) {
                return false;
            }
        }

        PlayerSpectateArenaEvent event = new PlayerSpectateArenaEvent(player, this, this.getDataHolder().getSpectatorLocation().toBukkit(this.world));

        event.call();
        if (event.isCancelled()) return false;

        ArenaSpectatorImpl arenaSpectator = new ArenaSpectatorImpl(player, this, true);
        this.spectators.add(arenaSpectator);

        return true;
    }

    @Override
    public boolean kickPlayer(Player player, KickReason reason) {
        if (!this.isArenaPlayer(player)) return false;
        ArenaPlayerImpl arenaPlayer = this.getAsArenaPlayer(player).get();
        this.players.remove(arenaPlayer);
        this.removed.put(arenaPlayer, reason);

        // TODO: 27-03-2022 event and msg
        return true;
    }

    @Override
    public void kickAllPlayers(KickReason reason) {
        for (ArenaPlayerImpl player : this.players) {
            this.players.remove(player);
            this.removed.put(player, reason);
        }
    }

    @Override
    public void removeSpectator(Player spectator) {

    }

    @Override
    public Block setBlock(LocationXYZ location, XMaterial material) {
        return this.setBlock(this.world.getBlockAt(location.toBukkit(this.world)), material);
    }

    @Override
    public Block setBlock(Block block, XMaterial material) {
        if (!material.isSupported()) return block;
        XBlock.setType(block, material);
        block.setMetadata("placed", new FixedMetadataValue(this.plugin, this.getName()));
        return block;
    }

    @Override
    public Instant getGameStartTime() {
        return this.startTime;
    }

    @Override
    public Duration getRunningTime() {
        return Duration.ofMilliseconds(Instant.now().toEpochMilli() - this.startTime.toEpochMilli());
    }

    @Override
    public Set<TeamImpl> getRemainingTeams() {
        Set<TeamImpl> remainingTeams = new HashSet<>(this.teams);
        remainingTeams.removeIf(TeamImpl::isEliminated);
        return remainingTeams;
    }

    @Override
    public Set<ArenaPlayerImpl> getPlayers() {
        return Collections.unmodifiableSet(this.players);
    }

    @Override
    public Optional<ArenaPlayerImpl> getAsArenaPlayer(Player player) {
        for (ArenaPlayerImpl arenaPlayer : this.players) {
            if (arenaPlayer.getUUID().equals(player.getUniqueId())) return Optional.of(arenaPlayer);
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
        if (!Util.isBed(bed)) return;

        final PlaceholderEntry[] bedBrokenPlaceholders = new PlaceholderEntry[]{
                PlaceholderEntry.symbol("defend_team_color", Util.getConfigCode(affected.getColor())),
                PlaceholderEntry.symbol("attack_team_color", Util.getConfigCode(player.getTeam().getColor())),
                PlaceholderEntry.symbol("defend_team_name", affected.getName()),
                PlaceholderEntry.symbol("attack_team_name", player.getTeam().getName()),
                PlaceholderEntry.symbol("attack_name", player.getName())
        };
        Message bedBrokenOthers = ConfigLang.BED_BROKEN_OTHERS.asMessage();
        bedBrokenOthers.addPlaceholders(bedBrokenPlaceholders);
        Message bedBrokenSelf = ConfigLang.BED_BROKEN_SELF.asMessage();
        bedBrokenSelf.addPlaceholders(bedBrokenPlaceholders);
        BedDestroyEvent event = new BedDestroyEvent(player, affected, bed, this, bedBrokenOthers, bedBrokenSelf);
        event.call();

        if (event.isCancelled()) return;

        bed.getDrops().clear();
        bed.breakNaturally();
        event.getAffectedTeam().setBedBroken(true);
        event.getDestroyer().getPoints().getCount(ArenaPlayer.PlayerPoints.BEDS).increment();
        this.bedBrokenStatistics.add(event.getDestroyer(), event.getAffectedTeam());
        // TODO: Add more effect
        this.sendMessage(event.getBedBrokenMessages(), member -> ((ArenaPlayer) member).getTeam().equals(event.getAffectedTeam()));
        event.getAffectedTeam().sendMessage(event.getBedBrokenMessages());
    }

    @Override
    public DeathStatistics getDeathStatistics() {
        return this.deathStatistics;
    }

    @Override
    public BedBrokenStatistics getBedBrokenStatistics() {
        return this.bedBrokenStatistics;
    }

    @Override
    public boolean stop() {
        return false;
    }

    @Override
    protected Collection<MessagingMember> getMembers() {
        Collection<MessagingMember> members = new ArrayList<>();
        members.addAll(this.players);
        members.addAll(this.spectators);
        return members;
    }

    boolean checkState(ArenaStatus... whitelisted) {
        for (ArenaStatus arenaStatus : whitelisted) {
            if (this.status == arenaStatus)
                return true;
        }
        return false;
    }

    Placeholder[] getJoinPlaceholders(Player player) {
        return new PlaceholderEntry[]{
                PlaceholderEntry.symbol("player_name", player.getName()),
                PlaceholderEntry.symbol("current_players", String.valueOf(this.getPlayers().size())),
                PlaceholderEntry.symbol("max_players", String.valueOf(this.getDataHolder().getMaxPlayersPerTeam()))
        };
    }

}
