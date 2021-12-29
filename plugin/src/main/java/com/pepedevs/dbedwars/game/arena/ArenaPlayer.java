package com.pepedevs.dbedwars.game.arena;

import com.pepedevs.corelib.utils.entity.UUIDPlayer;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.events.PlayerFinalKillEvent;
import com.pepedevs.dbedwars.api.events.PlayerKillEvent;
import com.pepedevs.dbedwars.api.events.TeamEliminateEvent;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.DeathCause;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.game.view.ShopView;
import com.pepedevs.dbedwars.api.messaging.message.AdventureMessage;
import com.pepedevs.dbedwars.cache.InventoryBackup;
import com.pepedevs.dbedwars.messaging.member.PlayerMember;
import com.pepedevs.dbedwars.task.RespawnTask;
import com.pepedevs.dbedwars.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.Instant;

public class ArenaPlayer extends PlayerMember implements com.pepedevs.dbedwars.api.game.ArenaPlayer {

    private final UUIDPlayer player;
    private final String name;
    private Arena arena;
    private Team team;

    private boolean spectator;
    private short bedDestroy;
    private int kill;
    private int finalKill;
    private int death;
    private boolean respawning;
    private boolean finalKilled;
    private com.pepedevs.dbedwars.api.game.ArenaPlayer lastHitTag;
    private Instant lastHitTime;
    private ShopView shopView;
    private InventoryBackup lastBackup;

    public ArenaPlayer(Player player, Arena arena) {
        super(player);
        this.player = new UUIDPlayer(player);
        this.name = player.getName();
        this.arena = arena;
        this.shopView = new com.pepedevs.dbedwars.game.arena.view.shoptest.ShopView(this);
    }

    @Override
    public Arena getArena() {
        return this.arena;
    }

    @Override
    public void setArena(Arena arena) {
        this.arena = arena;
    }

    @Override
    public Team getTeam() {
        return this.team;
    }

    @Override
    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean isSpectator() {
        return this.spectator;
    }

    @Override
    public void setSpectator(boolean spectator) {
        if (spectator) {
            this.spectator = true;
            this.getPlayer().setGameMode(GameMode.SPECTATOR);
        } else {
            this.spectator = false;
            this.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }

    @Override
    public void addKill() {
        this.kill++;
    }

    @Override
    public void setKill(int count) {
        this.kill = count;
    }

    @Override
    public int getKills() {
        return this.kill;
    }

    @Override
    public void addFinalKills() {
        this.finalKill++;
    }

    @Override
    public int getFinalKills() {
        return this.finalKill;
    }

    @Override
    public void setFinalKills(int count) {
        this.finalKill = count;
    }

    @Override
    public void addDeath() {
        this.death++;
    }

    @Override
    public int getDeath() {
        return this.death;
    }

    @Override
    public void setDeath(int count) {
        this.death = count;
    }

    @Override
    public void addBedDestroy() {
        this.bedDestroy++;
    }

    @Override
    public short getBedDestroy() {
        return this.bedDestroy;
    }

    @Override
    public void setBedDestroy(short bedDestroy) {
        this.bedDestroy = bedDestroy;
    }

    @Override
    public void kill(DeathCause reason) {
        // TODO: revamp this
        PlayerKillEvent event;
        if (this.team.isBedBroken()) {
            event = new PlayerFinalKillEvent(
                            this,
                            this.getLastHitTagged(),
                            this.arena,
                            reason,
                            AdventureMessage.from(this.getTeam().getColor().getMiniCode()
                                    + this.getPlayer().getName()
                                    + " <gray>died. <aqua>FINAL KILL"));
            event.call();

            if (event.isCancelled()) return;

            event.getVictim().addDeath();
            if (event.getAttacker() != null) event.getAttacker().addFinalKills();
            event.getVictim().setSpectator(true);
            event.getVictim().getPlayer().getInventory().clear();
            if (reason == DeathCause.VOID)
                event.getVictim()
                        .getPlayer()
                        .teleport(
                                this.arena
                                        .getSettings()
                                        .getSpectatorLocation()
                                        .toBukkit(this.arena.getWorld()));
            event.getVictim().setFinalKilled(true);
            event.getVictim().getArena().sendMessage(event.getKillMessage());

            if (event.getVictim().getTeam().getPlayers().stream()
                    .allMatch(com.pepedevs.dbedwars.api.game.ArenaPlayer::isFinalKilled)) {
                TeamEliminateEvent e =
                        new TeamEliminateEvent(this.arena, event.getVictim().getTeam());
                e.call();

                e.getTeam().setEliminated(true);

                if (this.arena.getRemainingTeams().size() <= 1) {
                    this.arena.end();
                }
            }
        } else {
            event = new PlayerKillEvent(
                            this,
                            this.getLastHitTagged(),
                            this.arena,
                            reason,
                            AdventureMessage.from(this.getTeam().getColor().getMiniCode()
                                    + this.getPlayer().getName()
                                    + " <gray>died."));
            event.call();

            if (event.isCancelled()) return;

            this.addDeath();
            if (event.getAttacker() != null) event.getAttacker().addKill();
            this.setSpectator(true);
            this.lastBackup = InventoryBackup.createBackup(this.getPlayer());
            event.getVictim().getPlayer().getInventory().clear();
            this.getPlayer()
                    .teleport(
                            this.arena
                                    .getSettings()
                                    .getSpectatorLocation()
                                    .toBukkit(this.arena.getWorld()));
            this.arena.sendMessage(event.getKillMessage());
            this.setRespawning(true);
            DBedwars.getInstance()
                    .getThreadHandler()
                    .submitAsync(new RespawnTask(DBedwars.getInstance(), event.getVictim()));
        }
    }

    @Override
    public boolean isFinalKilled() {
        return this.finalKilled;
    }

    @Override
    public void setFinalKilled(boolean flag) {
        this.finalKilled = flag;
    }

    @Override
    public UUIDPlayer getUUIDPlayer() {
        return this.player;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Player getPlayer() {
        return this.player.get();
    }

    @Override
    public void spawn(Location location) {
        Utils.setSpawnInventory(this.getPlayer(), this.team);
        if (this.lastBackup != null) {
            this.lastBackup.applyPermanents(this.getPlayer());
        }
        this.getPlayer().teleport(location);
        this.getPlayer().setHealth(20);
    }

    @Override
    public com.pepedevs.dbedwars.api.game.ArenaPlayer getLastHitTagged() {
        if (this.lastHitTime == null) return null;

        return (System.currentTimeMillis() - this.lastHitTime.toEpochMilli()) / 1000
                        > DBedwars.getInstance()
                                .getConfigHandler()
                                .getMainConfiguration()
                                .getArenaSection()
                                .getPlayerHitTagLength()
                ? null
                : this.lastHitTag;
    }

    @Override
    public void setLastHitTag(com.pepedevs.dbedwars.api.game.ArenaPlayer player) {
        this.lastHitTag = player;
        this.setLastHitTime(Instant.now());
    }

    @Override
    public Instant getLastHitTime() {
        return this.lastHitTime;
    }

    @Override
    public void setLastHitTime(Instant instant) {
        this.lastHitTime = instant;
    }

    @Override
    public boolean isRespawning() {
        return this.respawning;
    }

    public void setRespawning(boolean flag) {
        this.respawning = flag;
    }

    @Override
    public ShopView getShopView() {
        return this.shopView;
    }

    @Override
    public void queueRespawn() {
        this.respawning = true;
        DBedwars.getInstance()
                .getThreadHandler()
                .submitAsync(new RespawnTask(DBedwars.getInstance(), this));
    }
}
