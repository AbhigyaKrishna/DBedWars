package me.abhigya.dbedwars.configuration.configurabletrap;

import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.acionbar.ActionBarUtils;
import me.Abhigya.core.util.bossbar.BossBar;
import me.Abhigya.core.util.titles.TitleUtils;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import org.apache.commons.lang.math.NumberUtils;

import java.util.function.Consumer;

public class TrapEnum {

    public enum ActionType {

        TITLE {
            @Override
            public Consumer<ArenaPlayer> getAction(String statement) {
                String sub = statement.substring(statement.indexOf("["), statement.indexOf("]"));
                String main = statement.replace(sub, "").trim();
                sub = sub.replace("[", "").replace("]", "").trim();
                String[] split = sub.split(":");
                String title = main.split("\n")[0];
                String subTitle;
                try {
                    subTitle = main.split("\n")[1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    subTitle = "";
                }
                String finalSubTitle = subTitle;

                if (split.length == 4 && NumberUtils.isDigits(split[1]) && NumberUtils.isDigits(split[2]) && NumberUtils.isDigits(split[3])) {
                    return player -> TitleUtils.send(player.getPlayer(), StringUtils.translateAlternateColorCodes(title),
                            StringUtils.translateAlternateColorCodes(finalSubTitle), Integer.parseInt(split[1]), Integer.parseInt(split[2]),
                            Integer.parseInt(split[3]));
                } else {
                    return player -> TitleUtils.send(player.getPlayer(), StringUtils.translateAlternateColorCodes(title),
                            StringUtils.translateAlternateColorCodes(finalSubTitle));
                }
            }
        },
        ACTION_BAR {
            @Override
            public Consumer<ArenaPlayer> getAction(String statement) {
                String sub = statement.substring(statement.indexOf("["), statement.indexOf("]"));
                String main = statement.replace(sub, "").trim();
                return player -> ActionBarUtils.send(player.getPlayer(), StringUtils.translateAlternateColorCodes(main));
            }
        },
        BOSS_BAR {
            @Override
            public Consumer<ArenaPlayer> getAction(String statement) {
                return super.getAction(statement);
            }
        },
        COMMAND,
        DELAY,
        SOUND,
        MUSIC,
        EFFECT,
        ENCHANT,
        ;

        public Consumer<ArenaPlayer> getAction(String statement) {
            return player -> {};
        }

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
            return this.simpleName;
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
