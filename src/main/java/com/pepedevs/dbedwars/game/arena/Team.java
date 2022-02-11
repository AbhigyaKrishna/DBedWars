package com.pepedevs.dbedwars.game.arena;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.pepedevs.radium.utils.math.collision.BoundingBox;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.events.PlayerJoinTeamEvent;
import com.pepedevs.dbedwars.api.events.PlayerLeaveTeamEvent;
import com.pepedevs.dbedwars.api.events.TrapTriggerEvent;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.trap.Trap;
import com.pepedevs.dbedwars.api.game.spawner.DropType;
import com.pepedevs.dbedwars.api.messaging.member.MessagingMember;
import com.pepedevs.dbedwars.api.util.Color;
import com.pepedevs.dbedwars.api.util.LocationXYZ;
import com.pepedevs.dbedwars.api.util.LocationXYZYP;
import com.pepedevs.dbedwars.game.arena.traps.TrapEnum;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableTeam;
import com.pepedevs.dbedwars.messaging.AbstractMessaging;

import java.util.*;

public class Team extends AbstractMessaging implements com.pepedevs.dbedwars.api.game.Team {

    private final DBedwars plugin;
    private final Color color;
    private ConfigurableTeam cfgTeam;
    private LocationXYZ bedLocation;
    private LocationXYZYP spawn;
    private LocationXYZYP shopNpcLocation;
    private LocationXYZYP upgradesNpcLocation;
    private Multimap<DropType, LocationXYZ> spawners;

    private Arena arena;
    private boolean bedBroken;
    private boolean eliminated;
    private Set<ArenaPlayer> players;
    private BoundingBox islandArea;
    //    private NPC shopNpc;
    //    private NPC upgradesNpc;

    private List<Trap> trapQueue;

    public Team(DBedwars plugin, Color color) {
        this.plugin = plugin;
        this.color = color;
        this.spawners = ArrayListMultimap.create();
        if (this.plugin.getConfigHandler().getMainConfiguration().getTrapSection().isTrapQueueEnabled())
            this.trapQueue = new ArrayList<>(this.plugin.getConfigHandler().getMainConfiguration().getTrapSection().getTrapQueueLimit());
        else this.trapQueue = new ArrayList<>();
    }

    public Team(DBedwars plugin, ConfigurableTeam cfgTeam) {
        this(plugin, cfgTeam.getColor());
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
    public LocationXYZ getBedLocation() {
        return this.bedLocation;
    }

    @Override
    public void setBedLocation(LocationXYZ location) {
        this.bedLocation = location;
    }

    @Override
    public LocationXYZYP getSpawn() {
        return this.spawn;
    }

    @Override
    public void setSpawn(LocationXYZYP location) {
        this.spawn = location;
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
    public LocationXYZYP getShopNpc() {
        return this.shopNpcLocation;
    }

    @Override
    public void setShopNpc(LocationXYZYP location) {
        this.shopNpcLocation = location;
    }

    @Override
    public LocationXYZYP getUpgradesNpc() {
        return this.upgradesNpcLocation;
    }

    @Override
    public void setUpgradesNpc(LocationXYZYP location) {
        this.upgradesNpcLocation = location;
    }

    @Override
    public void reloadData() {
        if (this.cfgTeam == null) return;

        this.bedLocation = this.cfgTeam.getBedLocation();
        this.spawn = this.cfgTeam.getSpawn();
        this.shopNpcLocation = this.cfgTeam.getShopNpc();
        this.upgradesNpcLocation = this.cfgTeam.getUpgrades();
        this.spawners = this.cfgTeam.getSpawners();
    }

    @Override
    public boolean isConfigured() {
        return this.bedLocation != null
                && this.spawn != null
                && this.shopNpcLocation != null
                && this.upgradesNpcLocation != null;
    }

    @Override
    public void init(Arena arena) {
        this.arena = arena;
        this.players = new HashSet<>();
        this.bedBroken = false;
        this.eliminated = false;
        this.islandArea =
                new BoundingBox(
                        this.spawn.getX() - this.arena.getSettings().getIslandRadius(),
                        this.spawn.getY() - 50,
                        this.spawn.getZ() - this.arena.getSettings().getIslandRadius(),
                        this.spawn.getX() + this.arena.getSettings().getIslandRadius(),
                        this.spawn.getY() + 50,
                        this.spawn.getZ() + this.arena.getSettings().getIslandRadius());
    }

    @Override
    public Arena getArena() {
        return this.arena;
    }

    @Override
    public void addPlayer(ArenaPlayer player) {
        PlayerJoinTeamEvent event =
                new PlayerJoinTeamEvent(player.getPlayer(), player, this.arena, this);
        event.call();

        if (event.isCancelled()) return;

        player.setTeam(this);
        this.players.add(player);
    }

    @Override
    public void removePlayer(ArenaPlayer player) {
        PlayerLeaveTeamEvent event =
                new PlayerLeaveTeamEvent(player.getPlayer(), player, this.arena, this);
        event.call();

        if (event.isCancelled()) return;

        player.setTeam(null);
        this.players.remove(player);
    }

    @Override
    public Set<ArenaPlayer> getPlayers() {
        return Collections.unmodifiableSet(this.players);
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
            if (!this.players.stream().allMatch(ArenaPlayer::isFinalKilled)) return;

            this.eliminated = true;
        } else {
            if (this.players.stream().allMatch(ArenaPlayer::isFinalKilled)) return;

            this.eliminated = false;
        }
    }

    @Override
    public BoundingBox getIslandArea() {
        return this.islandArea;
    }

    @Override
    public List<Trap> getTrapQueue() {
        return this.trapQueue;
    }

    public void triggerTrap(TrapEnum.TriggerType trigger, ArenaPlayer target) {
        if (this.plugin.getConfigHandler().getMainConfiguration().getTrapSection().isTrapQueueEnabled()) {
            for (Trap trap : new LinkedList<>(this.trapQueue)) {
                if (trap.getTriggerType() == trigger) {
                    TrapTriggerEvent event = new TrapTriggerEvent(trap, target, this);
                    event.call();

                    if (event.isCancelled()) return;

                    event.getTrap().trigger(event.getTarget(), event.getTeam());
                    this.trapQueue.remove(event.getTrap());
                    break;
                }
            }
        } else {
            for (Trap trap : new LinkedList<>(this.trapQueue)) {
                if (trap.getTriggerType() == trigger) {
                    TrapTriggerEvent event = new TrapTriggerEvent(trap, target, this);
                    event.call();

                    if (event.isCancelled()) return;

                    event.getTrap().trigger(event.getTarget(), event.getTeam());
                    this.trapQueue.remove(event.getTrap());
                }
            }
        }
    }

    @Override
    public void spawnShopNpc(LocationXYZYP location) {
        //        this.shopNpc =
        //                NPC.builder()
        //                        .location(this.shopNpcLocation.toBukkit(this.arena.getWorld()))
        //                        .profile(
        //                                new Profile(
        //                                        UUID.randomUUID(),
        //
        // StringUtils.translateAlternateColorCodes("&eShop"),
        //                                        new Skin("", "")))
        //                        .lookAtPlayer(true)
        //                        .imitatePlayer(false)
        //                        .build(DBedwars.getInstance().getNpcHandler());
        //        this.shopNpc.addInteractAction(
        //                (npc, player, clickType) -> {
        //                    Optional<ArenaPlayer> ap = this.arena.getAsArenaPlayer(player);
        //                    ap.ifPresent(
        //                            arenaPlayer -> {
        //                                PlayerOpenShopEvent event =
        //                                        new PlayerOpenShopEvent(
        //                                                arenaPlayer, this.arena,
        // arenaPlayer.getShopView());
        //                                event.call();
        //
        //                                if (event.isCancelled()) return;
        //
        //                                DBedwars.getInstance()
        //                                        .getGuiHandler()
        //                                        .getGuis()
        //                                        .get("SHOP")
        //                                        .open(
        //                                                null,
        //                                                Collections.singletonMap("player",
        // arenaPlayer),
        //                                                player);
        //                            });
        //                });
    }

    @Override
    public void spawnUpgradesNpc(LocationXYZYP location) {
        //        this.upgradesNpc =
        //                NPC.builder()
        //
        // .location(this.upgradesNpcLocation.toBukkit(this.arena.getWorld()))
        //                        .profile(
        //                                new Profile(
        //                                        UUID.randomUUID(),
        //
        // StringUtils.translateAlternateColorCodes("&eUpgrades"),
        //                                        new Skin("", "")))
        //                        .lookAtPlayer(true)
        //                        .imitatePlayer(false)
        //                        .build(DBedwars.getInstance().getNpcHandler());
    }

    public void clearCache() {
        this.spawners.clear();
        this.arena = null;
        this.players = null;
        this.islandArea = null;
        //        this.plugin.getNpcHandler().removeNPC(this.shopNpc.getEntityId());
        //        this.shopNpc = null;
        //        this.plugin.getNpcHandler().removeNPC(this.upgradesNpc.getEntityId());
        //        this.upgradesNpc = null;
        this.trapQueue.clear();
    }

    @Override
    public Collection<MessagingMember> getMembers() {
        return new ArrayList<>(this.players);
    }

    @Override
    public String toString() {
        return "Team{" +
                "plugin=" + plugin +
                ", color=" + color +
                ", cfgTeam=" + cfgTeam +
                ", bedLocation=" + bedLocation +
                ", spawn=" + spawn +
                ", shopNpcLocation=" + shopNpcLocation +
                ", upgradesNpcLocation=" + upgradesNpcLocation +
                ", spawners=" + spawners +
                ", arena=" + arena +
                ", bedBroken=" + bedBroken +
                ", eliminated=" + eliminated +
                ", players=" + players +
                ", islandArea=" + islandArea +
                ", trapQueue=" + trapQueue +
                '}';
    }
}