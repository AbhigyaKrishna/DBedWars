package org.zibble.dbedwars.game;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.ArenaStatus;
import org.zibble.dbedwars.api.game.spawner.DropInfo;
import org.zibble.dbedwars.api.handler.GameManager;
import org.zibble.dbedwars.configuration.configurable.ConfigurableArena;
import org.zibble.dbedwars.configuration.configurable.ConfigurableItemSpawner;
import org.zibble.dbedwars.configuration.configurable.ConfigurableShop;
import org.zibble.dbedwars.game.arena.ArenaImpl;
import org.zibble.dbedwars.game.arena.settings.ArenaSettingsImpl;
import org.zibble.dbedwars.game.arena.spawner.DropInfoImpl;
import org.zibble.dbedwars.game.arena.view.ShopInfoImpl;
import org.zibble.dbedwars.handler.ConfigHandler;
import org.zibble.dbedwars.task.implementations.ArenaStartTask;

import java.util.*;

public class GameManagerImpl implements GameManager {

    private static final String GAME_ID_FORMAT = "bw_arena_%s_%d";

    private final DBedwars plugin;

    private final Location lobbySpawn;
    private final Set<ArenaDataHolderImpl> arenaDataHolders;
    private final Set<DropInfo> dropTypes;
    private final Set<ShopInfoImpl> shops;
    private final Multimap<String, Arena> arenas;
    private ArenaSettingsImpl defaultSettings;

    public GameManagerImpl(DBedwars plugin, Location lobbySpawn) {
        this.plugin = plugin;
        this.lobbySpawn = lobbySpawn;
        this.arenaDataHolders = new HashSet<>();
        this.dropTypes = new HashSet<>();
        this.arenas = ArrayListMultimap.create();
        this.shops = new HashSet<>();
    }

    public void load() {
        ConfigHandler configHandler = this.plugin.getConfigHandler();
        for (ConfigurableArena cfg : configHandler.getArenas()) {
            this.arenaDataHolders.add(ArenaDataHolderImpl.fromConfig(cfg));
        }

        for (ConfigurableItemSpawner cfg : configHandler.getDropTypes()) {
            this.dropTypes.add(DropInfoImpl.fromConfig(cfg));
        }

        for (Map.Entry<String, ConfigurableShop> entry : configHandler.getShops().entrySet()) {
            this.shops.add(ShopInfoImpl.fromConfig(entry.getValue()));
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

    public void startArenaSequence(Arena arena) {
        if (arena.getPlayers().size() < arena.getSettings().getMinPlayers()) return;

        arena.setStatus(ArenaStatus.STARTING);
        this.plugin.getThreadHandler().submitAsync(
                new ArenaStartTask(arena, (short) this.plugin.getConfigHandler().getMainConfiguration().getArenaSection().getStartTimer()));
    }

    public Set<DropInfo> getDropTypes() {
        return Collections.unmodifiableSet(this.dropTypes);
    }

    public Set<ArenaDataHolderImpl> getArenaDataHolders() {
        return arenaDataHolders;
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

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public Optional<? extends ArenaPlayer> getArenaPlayer(Player player) {
        for (Arena value : this.arenas.values()) {
            Optional<? extends ArenaPlayer> arenaPlayer = value.getAsArenaPlayer(player);
            if (arenaPlayer.isPresent()) return arenaPlayer;
        }
        return Optional.empty();
    }

}
