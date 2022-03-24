package org.zibble.dbedwars.api.game;

public enum DeathCause {

    ATTACK,
    FALL,
    VELOCITY,
    EXPLOSION,
    SUFFOCATION,
    DROWN,
    BURNING,
    MAGIC,
    VOID,
    SUICIDE,
    ENTITY_CRAMMED,
    CRUSHED,
    UNKNOWN,
    ;

    public static final DeathCause[] VALUES = values();

}
