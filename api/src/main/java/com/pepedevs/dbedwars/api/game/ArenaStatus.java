package com.pepedevs.dbedwars.api.game;

public enum ArenaStatus {
    IDLING,
    WAITING,
    STARTING,
    RUNNING,
    ENDING,
    REGENERATING,
    SLEEPING,
    STOPPED,
    ;

    public int getId() {
        return this.ordinal() + 1;
    }
}
