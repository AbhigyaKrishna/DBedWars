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
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.DeathCause;
import org.zibble.dbedwars.api.game.statistics.ResourceStatistics;
import org.zibble.dbedwars.api.hooks.scoreboard.Scoreboard;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.objects.points.IntegerCount;
import org.zibble.dbedwars.api.objects.points.Points;
import org.zibble.dbedwars.api.util.*;
import org.zibble.dbedwars.cache.InventoryData;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.MainConfiguration;
import org.zibble.dbedwars.configuration.configurable.ConfigurableScoreboard;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.game.arena.view.ShopInfoImpl;
import org.zibble.dbedwars.game.arena.view.ShopView;
import org.zibble.dbedwars.task.implementations.RespawnTask;
import org.zibble.dbedwars.utils.Util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ArenaPlayerImpl extends ArenaSpectatorImpl implements ArenaPlayer {

    private final DBedwars plugin;

    private Color team;
    private Scoreboard scoreboard;
    private final Points points;
    private boolean finalKilled;
    private boolean respawning;
    private Pair<ArenaPlayer, Instant> lastHit;
    private InventoryData inventoryBackup;
    private Map<Key<String>, ShopView> shops;
    private final InventoryData respawnItems;
    private final ResourceStatistics resourceStatistics;

    public ArenaPlayerImpl(DBedwars plugin, Player player, Arena arena) {
        super(player, arena);
        this.plugin = plugin;
        this.points = new Points();
        this.points.registerCount(PlayerPoints.KILLS, new IntegerCount());
        this.points.registerCount(PlayerPoints.DEATH, new IntegerCount());
        this.points.registerCount(PlayerPoints.BEDS, new IntegerCount());
        this.points.registerCount(PlayerPoints.FINAL_KILLS, new IntegerCount());
        this.shops = new HashMap<>(2);
        this.respawnItems = new InventoryData();
        this.resourceStatistics = new ResourceStatistics(this);
    }

    @Override
    public TeamImpl getTeam() {
        return (TeamImpl) this.arena.getTeam(this.team);
    }

    @Override
    public void setTeam(Color team) {
        this.team = team;
    }

    @Override
    public Points getPoints() {
        return this.points;
    }

    @Override
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    @Override
    public void kill(DeathCause reason) {
        PlayerKillEvent event;
        ArenaPlayer killer = this.getLastHitTagged();
        Message deathMessage = Util.getDeathMessage(reason, killer == null? null : killer.getPlayer());
        if (this.getTeam().isBedBroken()) {
            deathMessage.concatLine(0, Util.convertMessage(ConfigLang.FINAL_KILL.asMessage(),
                    deathMessage instanceof ConfigMessage ? ConfigMessage.empty() : AdventureMessage.empty()).getMessage());
            event = new PlayerFinalKillEvent(this, killer, this.arena, reason, deathMessage);
            event.call();
            if (event.isCancelled()) return;

            if (event.getAttacker() != null)
                event.getAttacker().getPoints().getCount(PlayerPoints.FINAL_KILLS).increment();

            event.getVictim().setFinalKilled(true);
        } else {
            event = new PlayerKillEvent(this, killer, this.arena, reason, deathMessage);
            event.call();

            if (event.isCancelled()) return;

            if (event.getAttacker() != null) event.getAttacker().getPoints().getCount(PlayerPoints.KILLS).increment();
            this.inventoryBackup = InventoryData.create(this.getPlayer());
            event.getVictim().getPlayer().getInventory().clear();
        }

        event.getVictim().getPoints().getCount(PlayerPoints.DEATH).increment();
        this.plugin.getFeatureManager().runFeature(BedWarsFeatures.DEATH_ANIMATION_FEATURE, DeathAnimationFeature.class, feature -> {
            List<Player> players = new ArrayList<>();
            for (ArenaPlayer arenaPlayer : this.arena.getPlayers()) {
                if (arenaPlayer.getUUID().equals(event.getVictim().getUUID())) continue;
                players.add(arenaPlayer.getPlayer());
            }
            feature.play(event.getVictim(), players);
            return true;
        });
        this.hide();
        event.getVictim().getArena().sendMessage(event.getKillMessage());

        event.getVictim().getPlayer().getInventory().clear();
        this.arena.getDeathStatistics().put(this, killer, reason);

        if (this.isFinalKilled()) {
            for (ArenaPlayer arenaPlayer : event.getVictim().getTeam().getPlayers()) {
                if (!arenaPlayer.isFinalKilled()) {
                    return;
                }
            }
            TeamEliminateEvent e = new TeamEliminateEvent(this.arena, event.getVictim().getTeam());
            e.call();
            e.getTeam().setEliminated(true);
            if (this.arena.getRemainingTeams().size() <= 1) {
                this.arena.end();
            }
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
    public String getName() {
        return this.name;
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(this.getUUID());
    }

    public ShopView getShop(String key) {
        return this.shops.get(Key.of(key));
    }

    @Override
    public void spawn(Location location) {
        SchedulerUtils.runTask(() -> {
            this.getPlayer().setGameMode(GameMode.SURVIVAL);
            this.respawnItems.applyInventory(this.getPlayer());
            this.getPlayer().teleport(location);
            this.getPlayer().setHealth(20);
        });
    }

    @Override
    public ArenaPlayer getLastHitTagged() {
        return this.isHitTagged() ? this.lastHit.getKey() : null;
    }

    @Override
    public void setLastHitTag(ArenaPlayer player, Instant instant) {
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
    public ResourceStatistics getResourceStatistics() {
        return resourceStatistics;
    }

    protected void complete() {
        // TODO: 28-03-2022 More placeholders
        Placeholder[] placeholders = new Placeholder[]{
                PlaceholderEntry.symbol("<player_name>", this.getPlayer().getName()),
                PlaceholderEntry.symbol("<player_team_name>", this.getTeam().getName()),
                PlaceholderEntry.symbol("<player_team_color>", String.valueOf(this.getTeam().getColor().getColor().asRGB())),
        };
        MainConfiguration.RespawnItemsSection respawnItemsSection = DBedwars.getInstance().getConfigHandler().getMainConfiguration().getRespawnItemsSection();
        for (String s : respawnItemsSection.getInventory()) {
            this.respawnItems.addItem(BwItemStack.valueOf(s, placeholders));
        }

        if (respawnItemsSection.getHelmet() != null) {
            this.respawnItems.setHelmet(BwItemStack.valueOf(respawnItemsSection.getHelmet(), placeholders));
        }
        if (respawnItemsSection.getChestplate() != null) {
            this.respawnItems.setChestPlate(BwItemStack.valueOf(respawnItemsSection.getChestplate(), placeholders));
        }
        if (respawnItemsSection.getLeggings() != null) {
            this.respawnItems.setLeggings(BwItemStack.valueOf(respawnItemsSection.getLeggings(), placeholders));
        }
        if (respawnItemsSection.getBoots() != null) {
            this.respawnItems.setBoots(BwItemStack.valueOf(respawnItemsSection.getBoots(), placeholders));
        }

    }

    public InventoryData getRespawnItems() {
        return respawnItems;
    }

    public void initScoreboard(ConfigurableScoreboard cfg) {
        // TODO: add placeholders
        this.scoreboard = this.plugin.getHookManager().getScoreboardHook().createDynamicScoreboard(
                this.getPlayer(),
                ConfigMessage.from(cfg.getTitle()),
                Arrays.asList(ConfigMessage.from(cfg.getContent()).splitToLineMessage()),
                Duration.ofMilliseconds(cfg.getUpdateTick() * 50L));
    }

    public void addShop(ShopInfoImpl cfg) {
        this.shops.put(cfg.getKey(), new ShopView(this, cfg));
    }

    public void startRespawn() {
        this.respawning = true;
        this.hide();
        this.plugin.getThreadHandler().submitAsync(new RespawnTask(this.plugin, this));
    }

}
