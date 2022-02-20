package org.zibble.dbedwars.api.handler;

import org.zibble.dbedwars.api.game.Arena;

import java.util.Map;

public interface GameManager {

    Arena createArena(String name);

    boolean containsArena(String name);

    Map<String, Arena> getArenas();

    Arena getArena(String name);
}
