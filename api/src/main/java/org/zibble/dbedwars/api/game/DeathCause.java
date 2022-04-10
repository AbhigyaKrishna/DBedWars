package org.zibble.dbedwars.api.game;

import org.bukkit.event.entity.EntityDamageEvent;

public enum DeathCause {

    ATTACK,
    FALL,
    VELOCITY,
    EXPLOSION,
    PROJECTILE,
    SUFFOCATION,
    DROWN,
    BURNING,
    MAGIC,
    VOID,
    LIGHTENING,
    SUICIDE,
    HUNGER,
    ENTITY_CRAMMED,
    CRUSHED,
    PRICKED,
    UNKNOWN,
    ;

    public static final DeathCause[] VALUES = values();

    public static DeathCause getCause(EntityDamageEvent.DamageCause damageCause) {
        switch (damageCause.name()) {
            case "CONTACT":
            case "ENTITY_ATTACK":
            case "ENTITY_SWEEP_ATTACK":
                return ATTACK;
            case "PROJECTILE":
                return PROJECTILE;
            case "SUFFOCATION":
                return SUFFOCATION;
            case "FALL":
                return FALL;
            case "FIRE":
            case "FIRE_TICK":
            case "MELTING":
            case "LAVA":
            case "HOT_FLOOR":
                return BURNING;
            case "DROWNING":
                return DROWN;
            case "BLOCK_EXPLOSION":
            case "ENTITY_EXPLOSION":
                return EXPLOSION;
            case "VOID":
                return VOID;
            case "LIGHTENING":
                return LIGHTENING;
            case "SUICIDE":
                return SUICIDE;
            case "STARVATION":
            case "DRYOUT":
                return HUNGER;
            case "POISON":
            case "MAGIC":
            case "WITHER":
            case "DRAGON_BREATH":
                return MAGIC;
            case "FALLING_BLOCK":
                return CRUSHED;
            case "THORNS":
                return PRICKED;
            case "FLY_INTO_WALL":
                return VELOCITY;
            case "CRAMMING":
                return ENTITY_CRAMMED;
            case "CUSTOM":
            default:
                return UNKNOWN;
        }
    }

}
