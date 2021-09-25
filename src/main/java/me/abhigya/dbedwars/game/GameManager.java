package me.abhigya.dbedwars.game;

import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.ArenaStatus;
import me.abhigya.dbedwars.api.game.spawner.DropType;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableArena;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableItemSpawner;
import me.abhigya.dbedwars.task.CountdownTask;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameManager {

    private final DBedwars plugin;

    private final Set<DropType> dropTypes;
    private final Map<String, Arena> arenas;
    /*private List<PopupTowerBuilder> popupTowerBuilders = new ArrayList<>();*/

    public GameManager(DBedwars plugin) {
        this.plugin = plugin;
        this.dropTypes = new HashSet<>();
        this.arenas = new HashMap<>();
    }

    public void load() {
        for (ConfigurableItemSpawner cis : this.plugin.getConfigHandler().getDropTypes()) {
            if (cis.isValid())
                this.dropTypes.add(cis.toDropType());
        }
        for (ConfigurableArena ca : this.plugin.getConfigHandler().getArenas()) {
            if (ca.isValid()) {
                Arena arena = ca.toArena();
                this.arenas.put(ca.getIdentifier(), arena);
            }
        }
    }

    public Arena createArena(String name) {
        Arena a = new me.abhigya.dbedwars.game.arena.Arena(this.plugin);
        this.arenas.put(name, a);
        a.getSettings().setName(name);
        return a;
    }

    public boolean containsArena(String name) {
        return this.arenas.containsKey(name);
    }

    public void startArenaSequence(Arena arena) {
        if (arena.getPlayers().size() < arena.getSettings().getMinPlayers())
            return;

        arena.setStatus(ArenaStatus.STARTING);
        this.plugin.getThreadHandler().addAsyncWork(new CountdownTask(arena, (short) this.plugin.getMainConfiguration().getArenaSection().getStartTimer()));
    }

    public Set<DropType> getDropTypes() {
        return Collections.unmodifiableSet(this.dropTypes);
    }

    public Map<String, Arena> getArenas() {
        return Collections.unmodifiableMap(this.arenas);
    }

    public Arena getArena(String name) {
        try {
            return arenas.get(name);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /*public List<PopupTowerBuilder> getPopupTowerBuilders() {
        return popupTowerBuilders;
    }*/
}
