package org.zibble.dbedwars.game;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.spawner.DropInfo;
import org.zibble.dbedwars.api.handler.GameManager;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardData;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.configuration.configurable.*;
import org.zibble.dbedwars.game.arena.ArenaCategoryImpl;
import org.zibble.dbedwars.game.arena.ArenaImpl;
import org.zibble.dbedwars.game.arena.GameEvent;
import org.zibble.dbedwars.game.arena.settings.ArenaSettingsImpl;
import org.zibble.dbedwars.game.arena.spawner.DropInfoImpl;
import org.zibble.dbedwars.game.arena.traps.TargetRegistryImpl;
import org.zibble.dbedwars.game.arena.view.ShopInfoImpl;
import org.zibble.dbedwars.handler.ConfigHandler;
import org.zibble.dbedwars.utils.ConfigurationUtil;

import java.util.*;

public class GameManagerImpl implements GameManager {

    private static final String GAME_ID_FORMAT = "bw_arena_%s_%d";

    private final DBedwars plugin;
    private final Set<ArenaDataHolderImpl> arenaDataHolders;
    private final Set<DropInfo> dropTypes;
    private final Set<ShopInfoImpl> shops;
    private final Multimap<String, Arena> arenas;
    private final Map<String, ScoreboardData> scoreboardData;
    private final Set<ArenaCategoryImpl> categories;
    private final List<GameEvent> events;
    private Location lobbySpawn;
    private ArenaSettingsImpl defaultSettings;
    private TargetRegistryImpl targetRegistry;

    public GameManagerImpl(DBedwars plugin, Location lobbySpawn) {
        this.plugin = plugin;
        this.lobbySpawn = lobbySpawn;
        this.arenaDataHolders = new HashSet<>();
        this.dropTypes = new HashSet<>();
        this.categories = new HashSet<>();
        this.arenas = ArrayListMultimap.create();
        this.shops = new HashSet<>();
        this.scoreboardData = new HashMap<>();
        this.targetRegistry = new TargetRegistryImpl();
        this.events = new ArrayList<>();
    }

    public void load() {
        ConfigHandler configHandler = this.plugin.getConfigHandler();
        for (ConfigurableScoreboard scoreboard : configHandler.getScoreboards()) {
            this.scoreboardData.put(scoreboard.getKey(), ConfigurationUtil.createScoreboard(scoreboard));
        }

        for (ConfigurableItemSpawner cfg : configHandler.getDropTypes()) {
            this.dropTypes.add(DropInfoImpl.fromConfig(cfg));
        }

        for (Map.Entry<String, ConfigurableShop> entry : configHandler.getShops().entrySet()) {
            this.shops.add(ShopInfoImpl.fromConfig(entry.getValue()));
        }

        for (ConfigurableArenaCategory category : configHandler.getCategories()) {
            this.categories.add(ArenaCategoryImpl.fromConfig(this, category));
        }

        for (ConfigurableArena cfg : configHandler.getArenas()) {
            this.arenaDataHolders.add(ArenaDataHolderImpl.fromConfig(this, cfg));
        }

        for (String s : configHandler.getEvent().getOrder()) {
            ConfigurableEvents.Event config = configHandler.getEvent().getEvents().get(s);
            if (config == null) continue;
            this.events.add(GameEvent.fromConfig(config));
        }

        this.defaultSettings = new ArenaSettingsImpl();
        this.defaultSettings.setStartTimer(configHandler.getMainConfiguration().getArenaSection().getStartTimer());
        this.defaultSettings.setRespawnTime(configHandler.getMainConfiguration().getArenaSection().getRespawnTime());
        this.defaultSettings.setIslandRadius(configHandler.getMainConfiguration().getArenaSection().getIslandRadius());
        this.defaultSettings.setMinYAxis(configHandler.getMainConfiguration().getArenaSection().getMinYAxis());
        this.defaultSettings.setPlayerHitTagLength(configHandler.getMainConfiguration().getArenaSection().getPlayerHitTagLength());
        this.defaultSettings.setGameEndDelay(configHandler.getMainConfiguration().getArenaSection().getGameEndDelay());
        this.defaultSettings.setDisableHunger(configHandler.getMainConfiguration().getArenaSection().isDisableHunger());
        this.defaultSettings.setBedDestroyPoint(configHandler.getMainConfiguration().getArenaSection().getBedDestroyPoint());
        this.defaultSettings.setKillPoint(configHandler.getMainConfiguration().getArenaSection().getKillPoint());
        this.defaultSettings.setFinalKillPoint(configHandler.getMainConfiguration().getArenaSection().getFinalKillPoint());
        this.defaultSettings.setDeathPoint(configHandler.getMainConfiguration().getArenaSection().getDeathPoint());
        this.defaultSettings.setTrapQueueEnabled(configHandler.getMainConfiguration().getTrapSection().isTrapQueueEnabled());
        this.defaultSettings.setTrapQueueSize(configHandler.getMainConfiguration().getTrapSection().getTrapQueueLimit());
        this.defaultSettings.setTrapTriggerDelay(Duration.ofSeconds(configHandler.getMainConfiguration().getTrapSection().getTrapTriggerMeantime()));

        this.targetRegistry.registerDefaults();
    }

    public Arena createArena(String name) {
        ArenaDataHolderImpl holder = this.getDataHolder(name);
        if (holder == null) return null;
        // TODO: 01-04-2022 override
        String gameId = String.format(GAME_ID_FORMAT, name, this.arenas.containsKey(name) ? this.arenas.get(name).size() : 0);
        ArenaImpl arena = new ArenaImpl(this.plugin, gameId, holder, this.defaultSettings.clone());
        this.arenas.put(name, arena);
        return arena;
    }

    public Arena getArena(World world) {
        for (Arena arena : this.arenas.values()) {
            if (arena.getWorld().equals(world)) return arena;
        }
        return null;
    }

    public Set<DropInfo> getDropTypes() {
        return Collections.unmodifiableSet(this.dropTypes);
    }

    public Set<ArenaDataHolderImpl> getArenaDataHolders() {
        return arenaDataHolders;
    }

    public Set<ArenaCategoryImpl> getCategories() {
        return categories;
    }

    public ArenaCategoryImpl getCategory(String name) {
        for (ArenaCategoryImpl category : categories) {
            if (category.getName().equalsIgnoreCase(name)) return category;
        }
        return null;
    }

    public ArenaDataHolderImpl getDataHolder(String id) {
        for (ArenaDataHolderImpl holder : this.arenaDataHolders) {
            if (holder.getId().equals(id)) return holder;
        }
        return null;
    }

    public Set<ShopInfoImpl> getShops() {
        return shops;
    }

    public List<GameEvent> getEvents() {
        return events;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    public Optional<? extends ArenaPlayer> getArenaPlayer(Player player) {
        for (Arena value : this.arenas.values()) {
            Optional<? extends ArenaPlayer> arenaPlayer = value.getAsArenaPlayer(player);
            if (arenaPlayer.isPresent()) return arenaPlayer;
        }
        return Optional.empty();
    }

    public Map<String, ScoreboardData> getScoreboardData() {
        return scoreboardData;
    }

    public TargetRegistryImpl getTargetRegistry() {
        return targetRegistry;
    }

}
