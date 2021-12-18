package com.pepedevs.dbedwars.api.util;

import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

public class TrapEnum {

    public enum ActionType {
        TITLE {
            // [TITLE:{FADE_IN}:{STAY}:{FADE_OUT}] Title\nSubtitle
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

                if (split.length == 4
                        && NumberUtils.isDigits(split[1])
                        && NumberUtils.isDigits(split[2])
                        && NumberUtils.isDigits(split[3])) {
                    return new Consumer<ArenaPlayer>() {
                        @Override
                        public void accept(ArenaPlayer player) {
                            //                            TitleUtils.send(
                            //                                    player.getPlayer(),
                            //
                            // StringUtils.translateAlternateColorCodes(title),
                            //
                            // StringUtils.translateAlternateColorCodes(finalSubTitle),
                            //                                    Integer.parseInt(split[1]),
                            //                                    Integer.parseInt(split[2]),
                            //                                    Integer.parseInt(split[3]));
                        }
                    };
                } else {
                    return new Consumer<ArenaPlayer>() {
                        @Override
                        public void accept(ArenaPlayer player) {
                            //                            TitleUtils.send(
                            //                                    player.getPlayer(),
                            //
                            // StringUtils.translateAlternateColorCodes(title),
                            //
                            // StringUtils.translateAlternateColorCodes(finalSubTitle));
                        }
                    };
                }
            }
        },
        ACTION_BAR {
            // [ACTIONBAR] msg
            @Override
            public Consumer<ArenaPlayer> getAction(String statement) {
                String sub = statement.substring(statement.indexOf("["), statement.indexOf("]"));
                String main = statement.replace(sub, "").trim();
                return new Consumer<ArenaPlayer>() {
                    @Override
                    public void accept(ArenaPlayer player) {
                        //                        ActionBarUtils.send(
                        //                                player.getPlayer(),
                        // StringUtils.translateAlternateColorCodes(main));
                    }
                };
            }
        },
        @Deprecated
        BOSS_BAR {
            // [BOSSBAR:{PROGRESS}:{COLOR}:{STYLE}:{FLAG}] Text
            @Override
            public Consumer<ArenaPlayer> getAction(String statement) {
                //                String sub = statement.substring(statement.indexOf("["),
                // statement.indexOf("]"));
                //                String main = statement.replace(sub, "").trim();
                //                sub = sub.replace("[", "").replace("]", "").trim();
                //                String[] split = sub.split(":");
                //                double progress = 1.0D;
                //                BarColor barColor = BarColor.PINK;
                //                BarStyle barStyle = BarStyle.SOLID;
                //                List<BarFlag> flags = new ArrayList<>();
                //                if (split.length > 1) {
                //                    if (NumberUtils.isNumber(split[1])) {
                //                        double d = Double.parseDouble(split[1]);
                //                        if (d > 1) {
                //                            d = d / 100;
                //                        }
                //                        progress = d;
                //                    }
                //                }
                //                if (split.length > 2) {
                //                    try {
                //                        barColor = BarColor.valueOf(split[2]);
                //                    } catch (IllegalArgumentException ignored) {
                //                    }
                //                }
                //                if (split.length > 3) {
                //                    try {
                //                        barStyle = BarStyle.valueOf(split[3]);
                //                    } catch (IllegalArgumentException ignored) {
                //                    }
                //                }
                //                if (split.length > 4) {
                //                    String[] flagSplit = split[4].split(",");
                //                    for (String s : flagSplit) {
                //                        try {
                //                            BarFlag flag = BarFlag.valueOf(s.trim());
                //                            flags.add(flag);
                //                        } catch (IllegalArgumentException ignored) {
                //                        }
                //                    }
                //                }
                //                double finalProgress = progress;
                //                BarColor finalBarColor = barColor;
                //                BarStyle finalBarStyle = barStyle;
                //                return new Consumer<ArenaPlayer>() {
                //                    @Override
                //                    public void accept(ArenaPlayer player) {
                //                        BossBar bossBar =
                //                                BossBar.createBossBar(
                //                                        player.getPlayer(),
                //
                // StringUtils.translateAlternateColorCodes(main),
                //                                        finalProgress,
                //                                        finalBarColor,
                //                                        finalBarStyle,
                //                                        flags.toArray(new BarFlag[0]));
                //                        bossBar.show();
                //                    }
                //                };
                return super.getAction(statement);
            }
        },
        COMMAND {
            // [COMMAND:{CONSOLE/PLAYER}] cmd
            @Override
            public Consumer<ArenaPlayer> getAction(String statement) {
                String sub = statement.substring(statement.indexOf("["), statement.indexOf("]"));
                String main = statement.replace(sub, "").trim();
                sub = sub.replace("[", "").replace("]", "").trim();
                String[] split = sub.split(":");
                if (split.length > 1 && split[1].equalsIgnoreCase("CONSOLE")) {
                    return new Consumer<ArenaPlayer>() {
                        @Override
                        public void accept(ArenaPlayer player) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), main);
                        }
                    };
                } else {
                    return new Consumer<ArenaPlayer>() {
                        @Override
                        public void accept(ArenaPlayer player) {
                            player.getPlayer().performCommand(main);
                        }
                    };
                }
            }
        },
        SOUND {
            // [SOUND] soundName:{volume}:{pitch}
            @Override
            public Consumer<ArenaPlayer> getAction(String statement) {
                String str = statement.replace("[SOUND]", "").trim();
                SoundVP sound = SoundVP.valueOf(str);
                if (sound != null)
                    return new Consumer<ArenaPlayer>() {
                        @Override
                        public void accept(ArenaPlayer player) {
                            sound.play(player.getPlayer());
                        }
                    };
                else return super.getAction(statement);
            }
        },
        MUSIC,
        EFFECT {
            // [EFFECT] effectName:{amplifier}:{duration}
            @Override
            public Consumer<ArenaPlayer> getAction(String statement) {
                String str = statement.replace("[EFFECT]", "").trim();
                PotionEffectAT effect = PotionEffectAT.valueOf(str);
                if (effect != null)
                    return new Consumer<ArenaPlayer>() {
                        @Override
                        public void accept(ArenaPlayer player) {
                            effect.applyTo(player.getPlayer());
                        }
                    };
                else return super.getAction(statement);
            }
        },
        // TODO
        PARTICLE {
            // [PARTICLE]
            @Override
            public Consumer<ArenaPlayer> getAction(String statement) {
                return super.getAction(statement);
            }
        },
        ENCHANT,
        ;

        public static final ActionType[] VALUES = values();

        public Consumer<ArenaPlayer> getAction(String statement) {
            return new Consumer<ArenaPlayer>() {
                @Override
                public void accept(ArenaPlayer player) {}
            };
        }
    }

    public enum TargetType {
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

        public static TargetType match(String s) {
            for (TargetType value : VALUES) {
                if (value.name().equalsIgnoreCase(s)
                        || value.name().replace("_", "-").equalsIgnoreCase(s)
                        || value.name().replace("_", "").equalsIgnoreCase(s)) return value;
            }

            return null;
        }
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

        public static TriggerType matchTrigger(String trigger) {
            for (TriggerType t : values()) {
                if (t.name().equalsIgnoreCase(trigger)
                        || t.name().replace("_", " ").equalsIgnoreCase(trigger)
                        || t.getSimpleName().equalsIgnoreCase(trigger)) return t;
            }

            return null;
        }

        public String getSimpleName() {
            return this.simpleName;
        }
    }
}
