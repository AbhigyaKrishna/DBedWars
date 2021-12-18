package com.pepedevs.dbedwars.api.handler;

import com.pepedevs.dbedwars.api.game.Arena;

import java.util.Map;

public interface GameManager {

    Arena createArena(String name);

    boolean containsArena(String name);

    Map<String, Arena> getArenas();

    Arena getArena(String name);

}
