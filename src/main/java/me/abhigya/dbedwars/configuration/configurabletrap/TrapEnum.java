package me.abhigya.dbedwars.configuration.configurabletrap;

public class TrapEnum {

    public enum ActionType {

        TITLE,
        ACTION_BAR,
        BOSS_BAR,
        COMMAND,
        DELAY,
        SOUND,
        MUSIC,
        EFFECT,
        ENCHANT,
        ;

    }

    public enum TargetType {

        ENEMY_AT_BASE,
        ;

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
            return simpleName;
        }

        public static TriggerType matchTrigger(String trigger) {
            for (TriggerType t : values()) {
                if (t.name().equalsIgnoreCase(trigger) || t.name().replace("_", " ").equalsIgnoreCase(trigger)
                        || t.getSimpleName().equalsIgnoreCase(trigger))
                    return t;
            }

            return null;
        }
    }

}
