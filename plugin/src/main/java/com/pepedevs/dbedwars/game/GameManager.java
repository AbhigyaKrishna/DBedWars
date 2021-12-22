package com.pepedevs.dbedwars.game;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaStatus;
import com.pepedevs.dbedwars.api.game.spawner.DropType;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableArena;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableItemSpawner;
import com.pepedevs.dbedwars.task.CountdownTask;

import java.util.*;

public class GameManager implements com.pepedevs.dbedwars.api.handler.GameManager {

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
        Arena a = new com.pepedevs.dbedwars.game.arena.Arena(this.plugin);
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
        this.plugin
                .getThreadHandler()
                .submitAsync(
                        new CountdownTask(
                                arena,
                                (short)
                                        this.plugin
                                                .getConfigHandler()
                                                .getMainConfiguration()
                                                .getArenaSection()
                                                .getStartTimer()));
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
