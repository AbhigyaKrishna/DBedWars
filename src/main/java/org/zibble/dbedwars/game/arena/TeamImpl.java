package org.zibble.dbedwars.game.arena;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.events.PlayerJoinTeamEvent;
import org.zibble.dbedwars.api.events.PlayerLeaveTeamEvent;
import org.zibble.dbedwars.api.events.TrapTriggerEvent;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.game.spawner.DropInfo;
import org.zibble.dbedwars.api.game.trap.Trap;
import org.zibble.dbedwars.api.messaging.member.MessagingMember;
import org.zibble.dbedwars.api.objects.math.BoundingBox;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.game.arena.traps.TrapEnum;
import org.zibble.dbedwars.messaging.AbstractMessaging;

import java.util.*;

public class TeamImpl extends AbstractMessaging implements Team {

    private final DBedwars plugin;
    private final Color color;
    private LocationXYZ bedLocation;
    private LocationXYZYP spawn;
    private LocationXYZYP shopNpcLocation;
    private LocationXYZYP upgradesNpcLocation;
    private Multimap<DropInfo, LocationXYZ> spawners;

    private Arena arena;
    private boolean bedBroken;
    private boolean eliminated;
    private Set<ArenaPlayer> players;
    private BoundingBox islandArea;

    private List<Trap> trapQueue;

    public TeamImpl(DBedwars plugin, Color color) {
        this.plugin = plugin;
        this.color = color;
        this.spawners = ArrayListMultimap.create();
        if (this.plugin.getConfigHandler().getMainConfiguration().getTrapSection().isTrapQueueEnabled())
            this.trapQueue = new ArrayList<>(this.plugin.getConfigHandler().getMainConfiguration().getTrapSection().getTrapQueueLimit());
        else this.trapQueue = new ArrayList<>();
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
    public void addSpawner(DropInfo dropType, LocationXYZ location) {
        this.spawners.put(dropType, location);
    }

    @Override
    public Multimap<DropInfo, LocationXYZ> getSpawners() {
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

    public void init(Arena arena) {
        this.arena = arena;
        this.players = new HashSet<>();
        this.bedBroken = false;
        this.eliminated = false;
        this.islandArea = new BoundingBox(
                        this.spawn.getX() - this.arena.getSettings().getIslandRadius(),
                        this.spawn.getY() - 50,
                        this.spawn.getZ() - this.arena.getSettings().getIslandRadius(),
                        this.spawn.getX() + this.arena.getSettings().getIslandRadius(),
                        this.spawn.getY() + 50,
                        this.spawn.getZ() + this.arena.getSettings().getIslandRadius());

        List<String> names = new ArrayList<>();
        for (ArenaPlayer player : this.getPlayers()) {
            names.add(player.getName());
        }
        WrapperPlayServerTeams.ScoreBoardTeamInfo info = new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                Component.text(this.getColor().getName(), this.getColor().getColorComponent()),
                null,
                null,
                WrapperPlayServerTeams.NameTagVisibility.ALWAYS,
                WrapperPlayServerTeams.CollisionRule.ALWAYS,
                this.getColor().getColorComponent(),
                WrapperPlayServerTeams.OptionData.NONE
        );
        WrapperPlayServerTeams teams = new WrapperPlayServerTeams(this.getName(), WrapperPlayServerTeams.TeamMode.CREATE,
                Optional.of(info), names);
        for (ArenaPlayer player : this.arena.getPlayers()) {
            TextComponent nameComponent = Component.text("[", this.getColor().getColorComponent())
                    .append(ConfigLang.valueOf("COLOR_" + this.getColor().getName()).asMessage().asComponentWithPAPI(player.getPlayer())[0]
                            .color(this.getColor().getColorComponent()))
                    .append(Component.text("] ", this.getColor().getColorComponent()));
            info.setPrefix(nameComponent);
            PacketEvents.getAPI().getPlayerManager().sendPacket(player.getPlayer(), teams);
        }
    }

    @Override
    public Arena getArena() {
        return this.arena;
    }

    @Override
    public void addPlayer(ArenaPlayer player) {
        PlayerJoinTeamEvent event = new PlayerJoinTeamEvent(player.getPlayer(), player, this.arena, this);
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

    public void initNpc() {

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
    protected Collection<MessagingMember> getMembers() {
        return new ArrayList<>(this.players);
    }

    @Override
    public String toString() {
        return "Team{" +
                "plugin=" + plugin +
                ", color=" + color +
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
