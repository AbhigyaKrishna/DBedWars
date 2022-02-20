package org.zibble.dbedwars.game.arena.traps;

public class TrapEnum {

    public enum TargetType {
        TRAP_BUYER,
        TEAM,
        ENEMY_TEAM,
        ENEMY_AT_BASE,
        TEAM_AT_BASE,
        RANDOM_TEAM_PLAYER,
        RANDOM_ENEMY_PLAYER,
        ALL_ENEMY,
        ALL_PLAYER,
        ;

        public static final TargetType[] VALUES = values();

    }

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
}
