package org.zibble.dbedwars.game.arena.traps;

public enum TriggerType {

    ENEMY_BASE_ENTER_EVENT("EnemyBaseEnterEvent"),
    ENEMY_BASE_EXIT_EVENT("EnemyBaseExitEvent"),
    TEAMMATE_BASE_ENTER_EVENT("TeammateBaseEnterEvent"),
    TEAMMATE_BASE_EXIT_EVENT("TeammateBaseExitEvent"),
    ;

    private final String simpleName;

    TriggerType(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getSimpleName() {
        return this.simpleName;
    }
}
