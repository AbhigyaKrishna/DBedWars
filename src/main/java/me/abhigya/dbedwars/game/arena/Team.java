package me.abhigya.dbedwars.game.arena;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.npc.NPC;
import me.Abhigya.core.util.npc.Profile;
import me.Abhigya.core.util.packet.packetevents.utils.player.Skin;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.events.game.PlayerJoinTeamEvent;
import me.abhigya.dbedwars.api.events.game.PlayerLeaveTeamEvent;
import me.abhigya.dbedwars.api.events.game.PlayerOpenShopEvent;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.game.spawner.DropType;
import me.abhigya.dbedwars.api.util.Color;
import me.abhigya.dbedwars.api.util.LocationXYZ;
import me.abhigya.dbedwars.api.util.LocationXYZYP;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableTeam;
import me.abhigya.dbedwars.utils.ConfigurationUtils;

import java.util.*;

public class Team implements me.abhigya.dbedwars.api.game.Team {

    private ConfigurableTeam cfgTeam;

    private final Color color;
    private LocationXYZ bedLocation;
    private LocationXYZYP spawn;
    private LocationXYZYP shopNpcLocation;
    private LocationXYZYP upgradesNpcLocation;
    private Multimap<DropType, LocationXYZ> spawners;

    private Arena arena;
    private boolean bedBroken;
    private boolean eliminated;
    private Set<ArenaPlayer> players;
    private NPC shopNpc;
    private NPC upgradesNpc;

    public Team(Color color) {
        this.color = color;
        this.spawners = ArrayListMultimap.create();
    }

    public Team(ConfigurableTeam cfgTeam) {
        this(cfgTeam.getColor());
        this.cfgTeam = cfgTeam;
        this.reloadData();
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public String getName() {
        return this.color.getName();
    }

    @Override
    public void setBedLocation(LocationXYZ location) {
        this.bedLocation = location;
    }

    @Override
    public LocationXYZ getBedLocation() {
        return this.bedLocation;
    }

    @Override
    public void setSpawn(LocationXYZYP location) {
        this.spawn = location;
    }

    @Override
    public LocationXYZYP getSpawn() {
        return this.spawn;
    }

    @Override
    public void addSpawner(DropType dropType, LocationXYZ location) {
        this.spawners.put(dropType, location);
    }

    @Override
    public Multimap<DropType, LocationXYZ> getSpawners() {
        return this.spawners;
    }

    @Override
    public void setShopNpc(LocationXYZYP location) {
        this.shopNpcLocation = location;
    }

    @Override
    public LocationXYZYP getShopNpc() {
        return this.shopNpcLocation;
    }

    @Override
    public void setUpgradesNpc(LocationXYZYP location) {
        this.upgradesNpcLocation = location;
    }

    @Override
    public LocationXYZYP getUpgradesNpc() {
        return this.upgradesNpcLocation;
    }

    @Override
    public void reloadData() {
        if (this.cfgTeam == null)
            return;

        this.bedLocation = this.cfgTeam.getBedLocation();
        this.spawn = this.cfgTeam.getSpawn();
        this.shopNpcLocation = this.cfgTeam.getShopNpc();
        this.upgradesNpcLocation = this.cfgTeam.getUpgrades();
        this.spawners = this.cfgTeam.getSpawners();
    }

    @Override
    public boolean isConfigured() {
        return this.bedLocation != null && this.spawn != null && this.shopNpcLocation != null && this.upgradesNpcLocation != null;
    }

    @Override
    public void init(Arena arena) {
        this.arena = arena;
        this.players = new HashSet<>();
        this.bedBroken = false;
        this.eliminated = false;
    }

    @Override
    public Arena getArena() {
        return this.arena;
    }

    @Override
    public void addPlayer(ArenaPlayer player) {
        PlayerJoinTeamEvent event = new PlayerJoinTeamEvent(player.getPlayer(), player, this.arena, this);
        event.call();

        if (event.isCancelled())
            return;

        player.setTeam(this);
        this.players.add(player);
    }

    @Override
    public void removePlayer(ArenaPlayer player) {
        PlayerLeaveTeamEvent event = new PlayerLeaveTeamEvent(player.getPlayer(), player, this.arena, this);
        event.call();

        if (event.isCancelled())
            return;

        player.setTeam(null);
        this.players.remove(player);
    }

    @Override
    public Set<ArenaPlayer> getPlayers() {
        return Collections.unmodifiableSet(this.players);
    }

    @Override
    public void sendMessage(String message) {
        this.players.forEach(p -> p.sendMessage(ConfigurationUtils.parseMessage(ConfigurationUtils.parsePlaceholder(message, p.getPlayer()))));
    }

    @Override
    public boolean isBedBroken() {
        return this.bedBroken;
    }

    @Override
    public void setBedBroken(boolean flag) {
        this.bedBroken = flag;
    }

    @Override
    public boolean isEliminated() {
        return this.eliminated;
    }

    @Override
    public void setEliminated(boolean flag) {
        if (flag) {
            if (!this.players.stream().allMatch(ArenaPlayer::isFinalKilled))
                return;

            this.eliminated = true;
        } else {
            if (this.players.stream().allMatch(ArenaPlayer::isFinalKilled))
                return;

            this.eliminated = false;
        }
    }

    @Override
    public void spawnShopNpc(LocationXYZYP location) {
        this.shopNpc = NPC.builder().location(this.shopNpcLocation.toBukkit(this.arena.getWorld()))
                .profile(new Profile(UUID.randomUUID(), StringUtils.translateAlternateColorCodes("&eShop"), new Skin("", "")))
                .lookAtPlayer(true)
                .imitatePlayer(false)
                .build(DBedwars.getInstance().getNpcHandler());
        this.shopNpc.addInteractAction((npc, player, clickType) -> {
            Optional<ArenaPlayer> ap = this.arena.getAsArenaPlayer(player);
            ap.ifPresent(arenaPlayer -> {
                PlayerOpenShopEvent event = new PlayerOpenShopEvent(arenaPlayer, this.arena, arenaPlayer.getShopView());
                event.call();

                if (event.isCancelled())
                    return;

                DBedwars.getInstance().getGuiHandler().getGuis().get("SHOP").open(null, Collections.singletonMap("player", arenaPlayer), player);
            });
        });
    }

    @Override
    public void spawnUpgradesNpc(LocationXYZYP location) {
        this.upgradesNpc = NPC.builder().location(this.upgradesNpcLocation.toBukkit(this.arena.getWorld()))
                .profile(new Profile(UUID.randomUUID(), StringUtils.translateAlternateColorCodes("&eUpgrades"), new Skin("", "")))
                .lookAtPlayer(true)
                .imitatePlayer(false)
                .build(DBedwars.getInstance().getNpcHandler());
    }
}
