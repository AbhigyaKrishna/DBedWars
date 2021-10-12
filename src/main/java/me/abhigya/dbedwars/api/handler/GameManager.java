package me.abhigya.dbedwars.api.handler;

import me.abhigya.dbedwars.api.game.Arena;

import java.util.Map;

public interface GameManager {

    Arena createArena( String name );

    boolean containsArena( String name );

    Map< String, Arena > getArenas( );

    Arena getArena( String name );

}
