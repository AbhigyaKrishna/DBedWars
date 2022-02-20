package org.zibble.dbedwars.game.arena;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.events.PlayerFinalKillEvent;
import org.zibble.dbedwars.api.events.PlayerKillEvent;
import org.zibble.dbedwars.api.events.TeamEliminateEvent;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.feature.custom.DeathAnimationFeature;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.DeathCause;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.game.view.ShopView;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.objects.points.IntegerCount;
import org.zibble.dbedwars.api.objects.points.Points;
import org.zibble.dbedwars.api.util.Acceptor;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.Pair;
import org.zibble.dbedwars.api.util.SchedulerUtils;
import org.zibble.dbedwars.cache.InventoryBackup;
import org.zibble.dbedwars.configuration.Lang;
import org.zibble.dbedwars.messaging.member.PlayerMember;
import org.zibble.dbedwars.task.implementations.RespawnTask;
import org.zibble.dbedwars.utils.Utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArenaPlayer extends PlayerMember implements org.zibble.dbedwars.api.game.ArenaPlayer {

    private final DBedwars plugin;
    private final Key<UUID> key;
    private final String name;
    private final Arena arena;

    private Team team;
    private final Points points;
    private boolean finalKilled;
    private boolean respawning;
    private Pair<org.zibble.dbedwars.api.game.ArenaPlayer, Instant> lastHit;
    private InventoryBackup inventoryBackup;

    public ArenaPlayer(DBedwars plugin, Player player, Arena arena) {
        super(player);
        this.plugin = plugin;
        this.key = Key.of(player.getUniqueId());
        this.name = player.getName();
        this.arena = arena;
        this.points = new Points();
        this.points.registerCount(PlayerPoints.KILLS, new IntegerCount());
        this.points.registerCount(PlayerPoints.DEATH, new IntegerCount());
        this.points.registerCount(PlayerPoints.BEDS, new IntegerCount());
        this.points.registerCount(PlayerPoints.FINAL_KILLS, new IntegerCount());
    }

    @Override
    public Key<UUID> getKey() {
        return this.key;
    }

    @Override
    public Arena getArena() {
        return this.arena;
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
    public Points getPoints() {
        return this.points;
    }

    @Override
    public void kill(DeathCause reason) {
        PlayerKillEvent event;
        if (this.team.isBedBroken()) {
            // Get message from config
            event = new PlayerFinalKillEvent(this, this.getLastHitTagged(), this.arena, reason, Lang.FINAL_KILL_MESSAGE.asMessage());
            event.call();
            if (event.isCancelled()) return;

            event.getVictim().getPoints().getCount(PlayerPoints.DEATH).increment();
            if (event.getAttacker() != null) event.getAttacker().getPoints().getCount(PlayerPoints.FINAL_KILLS).increment();

            this.plugin.getFeatureManager().runFeature(BedWarsFeatures.DEATH_ANIMATION_FEATURE, DeathAnimationFeature.class, new Acceptor<DeathAnimationFeature>() {
                @Override
                public boolean accept(DeathAnimationFeature feature) {
                    List<Player> players = new ArrayList<>();
                    for (org.zibble.dbedwars.api.game.ArenaPlayer arenaPlayer : ArenaPlayer.this.arena.getPlayers()) {
                        if (arenaPlayer.getUUID().equals(event.getVictim().getUUID())) continue;
                        players.add(arenaPlayer.getPlayer());
                    }
                    feature.play(event.getVictim().getPlayer(), players);
                    return true;
                }
            });

            event.getVictim().getPlayer().getInventory().clear();
            event.getVictim().setFinalKilled(true);
            event.getVictim().getArena().sendMessage(event.getKillMessage());

            // TODO: Make spectator

            boolean bool = true;
            for (org.zibble.dbedwars.api.game.ArenaPlayer arenaPlayer : event.getVictim().getTeam().getPlayers()) {
                bool = bool && arenaPlayer.isFinalKilled();
            }
            if (bool) {
                TeamEliminateEvent e = new TeamEliminateEvent(this.arena, event.getVictim().getTeam());
                e.call();
                e.getTeam().setEliminated(true);
                if (this.arena.getRemainingTeams().size() <= 1) {
                    this.arena.end();
                }
            }
        } else {
            event = new PlayerKillEvent(this, this.getLastHitTagged(), this.arena, reason, AdventureMessage.from(this.getTeam().getColor().getMiniCode() + this.getPlayer().getName() + " <gray>died."));
            event.call();

            if (event.isCancelled()) return;

            event.getVictim().getPoints().getCount(PlayerPoints.DEATH).increment();
            if (event.getAttacker() != null) event.getAttacker().getPoints().getCount(PlayerPoints.KILLS).increment();
            this.inventoryBackup = InventoryBackup.createBackup(this.getPlayer());
            // TODO: Make spectator
            event.getVictim().getPlayer().getInventory().clear();
            this.respawning = true;
            this.plugin.getThreadHandler().submitAsync(new RespawnTask(this.plugin, event.getVictim()));
        }
    }

    @Override
    public boolean isHitTagged() {
        return this.lastHit.getValue().until(Instant.now(), ChronoUnit.SECONDS) <= this.plugin.getConfigHandler().getMainConfiguration().getArenaSection().getPlayerHitTagLength();
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
    public UUID getUUID() {
        return this.key.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(this.getUUID());
    }

    @Override
    public void spawn(Location location) {
        SchedulerUtils.runTask(new Runnable() {
            @Override
            public void run() {
                ArenaPlayer.this.getPlayer().setGameMode(GameMode.SURVIVAL);
                Utils.setSpawnInventory(ArenaPlayer.this.getPlayer(), ArenaPlayer.this.team);
                if (ArenaPlayer.this.inventoryBackup != null) {
                    ArenaPlayer.this.inventoryBackup.applyPermanents(ArenaPlayer.this.getPlayer());
                }
                ArenaPlayer.this.getPlayer().teleport(location);
                ArenaPlayer.this.getPlayer().setHealth(20);
            }
        });
    }

    @Override
    public org.zibble.dbedwars.api.game.ArenaPlayer getLastHitTagged() {
        return this.isHitTagged() ? this.lastHit.getKey() : null;
    }

    @Override
    public void setLastHitTag(org.zibble.dbedwars.api.game.ArenaPlayer player, Instant instant) {
        this.lastHit = new Pair<>(player, instant);
    }

    @Override
    public boolean isRespawning() {
        return this.respawning;
    }

    @Override
    public void setRespawning(boolean flag) {
        this.respawning = flag;
    }

    @Override
    public ShopView getShopView() {
        return null;
    }

}
