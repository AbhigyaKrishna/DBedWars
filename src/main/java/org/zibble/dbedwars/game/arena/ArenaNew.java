package org.zibble.dbedwars.game.arena;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.events.ArenaStartEvent;
import org.zibble.dbedwars.api.events.BedDestroyEvent;
import org.zibble.dbedwars.api.events.PlayerJoinArenaLobbyEvent;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.*;
import org.zibble.dbedwars.api.game.settings.ArenaSettings;
import org.zibble.dbedwars.api.game.spawner.Spawner;
import org.zibble.dbedwars.api.hooks.scoreboard.UpdatingScoreboard;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.messaging.member.MessagingMember;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.math.BoundingBox;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.Duration;
import org.zibble.dbedwars.api.util.KickReason;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.game.NewArenaDataHolder;
import org.zibble.dbedwars.game.TeamAssigner;
import org.zibble.dbedwars.game.arena.settings.ArenaSettingsImpl;
import org.zibble.dbedwars.listeners.ArenaListener;
import org.zibble.dbedwars.listeners.GameListener;
import org.zibble.dbedwars.messaging.AbstractMessaging;
import org.zibble.dbedwars.utils.Utils;
import org.zibble.dbedwars.utils.gamerule.GameRuleDisableDaylightCycle;
import org.zibble.dbedwars.utils.gamerule.GameRuleType;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class ArenaNew extends AbstractMessaging implements Arena {

    private static final String WORlD_NAME_FORMAT = "bw_arena_%s";

    private final DBedwars plugin;

    private final String name;
    private final NewArenaDataHolder dataHolder;
    private String worldFileName;
    private final ArenaSettingsImpl settings;
    private ArenaStatus status;

    private World world;
    private Instant startTime;
    private UpdatingScoreboard scoreboard;
    private ArenaListener arenaHandler;
    private GameListener gameHandler;
    private List<Spawner> spawners;
    private Set<Team> teams;
    private Set<ArenaPlayer> players;
    private List<ArenaSpectator> spectators;
    private Map<ArenaPlayer, KickReason> removed;

    public ArenaNew(DBedwars plugin, String name, NewArenaDataHolder dataHolder) {
        this(plugin, name, dataHolder, new ArenaSettingsImpl());
    }

    public ArenaNew(DBedwars plugin, String name, NewArenaDataHolder dataHolder, ArenaSettingsImpl settings) {
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
    }

    public String getName() {
        return name;
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
                this.getSettings().getWorldEnv());
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
    public Team getTeam(Color color) {
        for (Team team : teams) {
            if (team.getColor().equals(color)) {
                return team;
            }
        }
        return null;
    }

    @Override
    public Set<Team> getTeams() {
        return Collections.unmodifiableSet(this.teams);
    }

    @Override
    public List<ArenaSpectator> getSpectators() {
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
    public boolean start() {
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


        return false;
    }

    @Override
    public boolean end() {
        return false;
    }

    @Override
    public void joinGame(Player player) {
        ArenaPlayer aPlayer = new ArenaPlayerImpl(this.plugin, player, this);
        this.joinGame(aPlayer);
    }

    @Override
    public void joinGame(ArenaPlayer player) {
        PlaceholderEntry[] joinLeavePlaceholders = new PlaceholderEntry[]{
                PlaceholderEntry.symbol("player_name", player.getName()),
                PlaceholderEntry.symbol("current_players", String.valueOf(this.getPlayers().size())),
                PlaceholderEntry.symbol("max_players", String.valueOf(this.getSettings().getMaxPlayer()))
        };

        Message message = ConfigLang.ARENA_JOIN_MESSAGE.asMessage();
        message.addPlaceholders(joinLeavePlaceholders);

        PlayerJoinArenaLobbyEvent event = new PlayerJoinArenaLobbyEvent(
                player.getPlayer(),
                this,
                this.settings.getLobby().toBukkit(this.world),
                message);

        event.call();
        if (event.isCancelled()) return;

        this.players.add(player);
        player.teleport(event.getLocation());

        if (event.getArena().getStatus() == ArenaStatus.IDLING)
            event.getArena().setStatus(ArenaStatus.WAITING);

        event.getArena().sendMessage(event.getJoinMessage());

        if (event.getArena().getPlayers().size() >= event.getArena().getSettings().getMinPlayers()
                && event.getArena().getStatus() != ArenaStatus.STARTING) {
            this.plugin.getGameManager().startArenaSequence(event.getArena());
        }
    }

    @Override
    public void joinGame(Player player, Team team) {

    }

    @Override
    public void joinGame(ArenaPlayer player, Team team) {

    }

    @Override
    public boolean kickPlayer(ArenaPlayer player) {
        return false;
    }

    @Override
    public boolean kickPlayer(ArenaPlayer player, KickReason reason) {
        return false;
    }

    @Override
    public void kickAllPlayers() {

    }

    @Override
    public void kickAllPlayers(KickReason reason) {

    }

    @Override
    public Block setBlock(LocationXYZ location, Material material) {
        return null;
    }

    @Override
    public Block setBlock(Block block, Material material) {
        return null;
    }

    @Override
    public Instant getGameStartTime() {
        return this.startTime;
    }

    @Override
    public Duration getRunningTime() {
        return Duration.of(TimeUnit.MILLISECONDS, Instant.now().toEpochMilli() - this.startTime.toEpochMilli());
    }

    @Override
    public boolean isSleeping() {
        return this.status == ArenaStatus.SLEEPING;
    }

    @Override
    public Set<Team> getRemainingTeams() {
        Set<Team> remainingTeams = new HashSet<>(this.teams);
        remainingTeams.removeIf(Team::isEliminated);
        return remainingTeams;
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
    public boolean stop() {
        return false;
    }

    @Override
    public Collection<MessagingMember> getMembers() {
        Collection<MessagingMember> members = new ArrayList<>();
        members.addAll(this.players);
        members.addAll(this.spectators);
        return members;
    }

}
