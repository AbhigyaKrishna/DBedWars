package org.zibble.dbedwars.game;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaStatus;
import org.zibble.dbedwars.api.game.spawner.DropType;
import org.zibble.dbedwars.configuration.configurable.ConfigurableArena;
import org.zibble.dbedwars.configuration.configurable.ConfigurableItemSpawner;
import org.zibble.dbedwars.game.arena.ArenaImpl;
import org.zibble.dbedwars.task.implementations.ArenaStartTask;

import java.util.*;

public class GameManager implements org.zibble.dbedwars.api.handler.GameManager {

    private final DBedwars plugin;

    private final Set<DropType> dropTypes;
    private final Map<String, Arena> arenas;

    public GameManager(DBedwars plugin) {
        this.plugin = plugin;
        this.dropTypes = new HashSet<>();
        this.arenas = new HashMap<>();
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

    public Set<DropType> getDropTypes() {
        return Collections.unmodifiableSet(this.dropTypes);
    }

    @Override
    public Map<String, Arena> getArenas() {
        return Collections.unmodifiableMap(this.arenas);
    }

    @Override
    public Arena getArena(String name) {
        return arenas.getOrDefault(name, null);
    }
}
