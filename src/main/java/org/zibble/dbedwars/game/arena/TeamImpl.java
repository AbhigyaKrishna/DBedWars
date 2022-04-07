package org.zibble.dbedwars.game.arena;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.events.PlayerJoinTeamEvent;
import org.zibble.dbedwars.api.events.PlayerLeaveTeamEvent;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.game.spawner.Spawner;
import org.zibble.dbedwars.api.game.trap.Trap;
import org.zibble.dbedwars.api.hooks.npc.BedwarsNPC;
import org.zibble.dbedwars.api.messaging.member.MessagingMember;
import org.zibble.dbedwars.api.objects.math.BoundingBox;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.configuration.configurable.ConfigurableNpc;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.game.ArenaDataHolderImpl;
import org.zibble.dbedwars.game.arena.traps.TrapImpl;
import org.zibble.dbedwars.game.arena.traps.TrapQueue;
import org.zibble.dbedwars.game.arena.traps.TriggerType;
import org.zibble.dbedwars.messaging.AbstractMessaging;
import org.zibble.dbedwars.utils.ConfigurationUtil;

import java.time.Instant;
import java.util.*;

public class TeamImpl extends AbstractMessaging implements Team {

    private final DBedwars plugin;
    private final Color color;
    private final Arena arena;
    private LocationXYZ bedLocation;
    private LocationXYZYP spawn;

    private boolean bedBroken;
    private boolean eliminated;
    private Set<ArenaPlayer> players;
    private BoundingBox islandArea;

    private Set<BedwarsNPC> npcs;
    private TrapQueue trapQueue;
    private Instant lastTrapTriggered;

    public TeamImpl(DBedwars plugin, Color color, Arena arena) {
        this.plugin = plugin;
        this.color = color;
        this.arena = arena;
        this.players = new HashSet<>();
        this.npcs = new HashSet<>();
        this.trapQueue = new TrapQueue(this);
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

    public void init(ArenaDataHolderImpl.TeamDataHolderImpl teamData) {
        this.bedBroken = false;
        this.eliminated = false;
        this.setSpawn(teamData.getSpawnLocation());
        this.setBedLocation(teamData.getBed());

        this.islandArea = new BoundingBox(
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
        PlayerJoinTeamEvent event = new PlayerJoinTeamEvent(player.getPlayer(), player, this.arena, this);
        event.call();

        if (event.isCancelled()) return;

        player.setTeam(this.getColor());
        this.players.add(player);
    }

    @Override
    public void removePlayer(ArenaPlayer player) {
        PlayerLeaveTeamEvent event = new PlayerLeaveTeamEvent(player.getPlayer(), player, this.arena, this);
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
    public Set<Spawner> getSpawners() {
        Set<Spawner> spawners = new HashSet<>();
        for (Spawner spawner : this.arena.getSpawners()) {
            spawner.getTeam().ifPresent(team -> {
                if (team.getColor() == this.getColor()) {
                    spawners.add(spawner);
                }
            });
        }
        return spawners;
    }

    @Override
    public List<Trap> getTraps() {
        return this.trapQueue;
    }

    @Override
    public Instant getLastTrapTriggered() {
        return lastTrapTriggered;
    }

    @Override
    public void setLastTrapTriggered(Instant lastTrapTriggered) {
        this.lastTrapTriggered = lastTrapTriggered;
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

    public void complete(ArenaDataHolderImpl.TeamDataHolderImpl teamData) {
        this.sendTeamPacket();
        for (ArenaDataHolderImpl.ShopDataHolderImpl shop : teamData.getShops()) {
            this.initShop(shop);
        }
        for (ArenaPlayer player : this.players) {
            ((ArenaPlayerImpl) player).complete();
        }
    }

    private void sendTeamPacket() {
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

    private void initShop(ArenaDataHolderImpl.ShopDataHolderImpl shopData) {
        for (ArenaPlayer player : this.players) {
            ((ArenaPlayerImpl) player).addShop(shopData.getShopType());
        }

        ConfigurableNpc cfg = null;
        for (ConfigurableNpc npc : this.plugin.getConfigHandler().getNpc()) {
            if (shopData.getShopType().getKey().get().equals(npc.getShop())) {
                cfg = npc;
                break;
            }
        }
        
        if (cfg == null) return;

        BedwarsNPC npc = ConfigurationUtil.createNpc(shopData.getLocation().toBukkit(this.arena.getWorld()), cfg);
        npc.spawn();
        this.npcs.add(npc);
    }

    public void triggerTrap(TriggerType type, ArenaPlayer trigger) {
        Collection<Trap> toRemove = new ArrayList<>();
        for (Trap trap : this.trapQueue) {
            if (trap instanceof TrapImpl) {
                TrapImpl trapImpl = (TrapImpl) trap;
                if (trapImpl.getTriggerType() == type) {
                    if (trapImpl.trigger(trigger)) {
                        toRemove.add(trap);
                        this.lastTrapTriggered = Instant.now();
                        if (this.arena.getSettings().isTrapQueueEnabled()) break;
                    }
                }
            }
        }
        this.trapQueue.removeAll(toRemove);
    }

    public void clearCache() {
        this.trapQueue.clear();
    }

    @Override
    protected Collection<MessagingMember> getMembers() {
        return new ArrayList<>(this.players);
    }

}
