package org.zibble.dbedwars.game;

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
import org.zibble.dbedwars.game.arena.view.ShopInfoImpl;
import org.zibble.dbedwars.task.implementations.ArenaStartTask;

import java.util.*;

public class GameManagerImpl implements GameManager {

    private final DBedwars plugin;

    private final Location lobbySpawn;
    private final Set<DropInfo> dropTypes;
    private final Map<String, Arena> arenas;
    private final Set<ShopInfoImpl> shops;

    public GameManagerImpl(DBedwars plugin, Location lobbySpawn) {
        this.plugin = plugin;
        this.lobbySpawn = lobbySpawn;
        this.dropTypes = new HashSet<>();
        this.arenas = new HashMap<>();
        this.shops = new HashSet<>();
    }

    public void load() {
        for (ConfigurableItemSpawner cis : this.plugin.getConfigHandler().getDropTypes()) {
            if (cis.isValid()) this.dropTypes.add(cis.toDropType());
        }
        for (ConfigurableArena ca : this.plugin.getConfigHandler().getArenas()) {
            if (ca.isValid()) {
                Arena arena = ca.toArena();
                this.arenas.put(ca.getIdentifier(), arena);
            }
        }

        for (Map.Entry<String, ConfigurableShop> entry : this.plugin.getConfigHandler().getShops().entrySet()) {
            if (entry.getValue().isValid()) {
                this.shops.add(ShopInfoImpl.fromConfig(entry.getValue()));
            }
        }
    }

    @Override
    public Arena createArena(String name) {
        Arena a = new ArenaImpl(this.plugin);
        this.arenas.put(name, a);
        a.getSettings().setName(name);
        return a;
    }

    @Override
    public boolean containsArena(String name) {
        return this.arenas.containsKey(name);
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

    @Override
    public Map<String, Arena> getArenas() {
        return Collections.unmodifiableMap(this.arenas);
    }

    @Override
    public Arena getArena(String name) {
        return arenas.get(name);
    }

    public Set<ShopInfoImpl> getShops() {
        return shops;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public Optional<ArenaPlayer> getArenaPlayer(Player player) {
        for (Arena value : this.arenas.values()) {
            Optional<ArenaPlayer> arenaPlayer = value.getAsArenaPlayer(player);
            if (arenaPlayer.isPresent()) return arenaPlayer;
        }
        return Optional.empty();
    }

}
