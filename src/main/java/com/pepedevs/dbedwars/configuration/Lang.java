package com.pepedevs.dbedwars.configuration;

import com.pepedevs.dbedwars.api.messaging.message.AdventureMessage;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.configuration.translator.ConfigTranslator;
import com.pepedevs.dbedwars.configuration.translator.LegacyTranslator;
import com.pepedevs.dbedwars.configuration.translator.MiniMessageTranslator;
import com.pepedevs.dbedwars.messaging.MiniMessageWrapper;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.EnumMap;

public enum Lang {

    PREFIX("prefix", "<gold>[ <blue>Bedwars <gold>]"),

    /* Some default plugin messages */
    UNKNOWN_COMMAND("plugin-default.unknown-command","<red>This is an unknown command. use <gold>/bedwars help <red>for more info."),
    NO_PERMISSION("plugin-default.no-permission","<red>You don't have permission for this!"),

    /* General */
    ARENA("general.arena", "arena"),
    COLOR_YELLOW("general.color-yellow", "yellow"),
    COLOR_ORANGE("general.color-orange", "orange"),
    COLOR_RED("general.color-red", "red"),
    COLOR_BLUE("general.color-blue", "blue"),
    COLOR_LIGHT_BLUE("general.color-lightBlue", "light blue"),
    COLOR_CYAN("general.color-cyan", "cyan"),
    COLOR_LIME("general.color-lime", "lime"),
    COLOR_GREEN("general.color-green", "green"),
    COLOR_PURPLE("general.color-purple", "purple"),
    COLOR_PINK("general.color-pink", "pink"),
    COLOR_WHITE("general.color-white", "white"),
    COLOR_LIGHT_GRAY("general.color-lightGray", "light gray"),
    COLOR_GRAY("general.color-gray", "gray"),
    COLOR_BROWN("general.color-brown", "brown"),
    COLOR_BLACK("general.color-black", "black"),

    /*

     */
    REJOIN_PLAYER_MESSAGE("gameplay.rejoin.player-rejoin-message","<green>Rejoining to match..."),
    REJOIN_BROADCAST_MESSAGE("gameplay.rejoin.player-rejoin-broadcast","<gray><player_name> has reconnected to the match"),
    REJOIN_FAILED_GAME_ENDED("gameplay.rejoin.player-rejoin-game-ended","<red>You cannot reconnect to the match! Either the match ended or your bed is broken"),

    /* Bedwars commands */
    NO_ARENA_FOUND_W_NAME("command-response.no-arena-found", "<red>No arena found with this name!"),
    NOT_IN_AN_ARENA("command-response.not-in-arena", "<red>You are not in a arena!"),
    SPECIFIED_ARENA_IS_FULL("command-response.arena-is-full","<red>This arena is currently full! Try again later..."),
    BLOCKED_COMMAND("command-response.blocked-command","<red>You cannot use this command during match!"),

    /* Game - Pregame*/
    ARENA_JOIN_MESSAGE("gameplay.pregame.player-arena-join", "<green><player_name> <yellow>has joined (<aqua><current_players></aqua>/<aqua><max_players></aqua>)!"),
    ARENA_LEAVE_MESSAGE("gameplay.pregame.player-arena-leave","<green><player_name> <yellow>has quit!"),

    /* Game - Ingame*/
    BED_BROKEN_OTHERS("gameplay.ingame.bed-broken-broadcast-others", "<defend_team_color><defend_team_name> 's Bed <gray>was destroyed by <attack_team_color><attack_team_name>"),
    BED_BROKEN_SELF("gameplay.ingame.bed-broken-broadcast-self", "<gray>Your bed was destroyed by <defend_team_color><defend_team_name>"),
    ARENA_START_MESSAGE("gameplay.ingame.game-start", "<green><bold>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬", "<white>                                 <bold>Bed Wars", "<yellow><bold>     Protect your bed and destroy the enemy beds.", "<yellow><bold>      Upgrade yourself and your team by collecting,<yellow><bold>    Iron, Gold, Emerald and Diamond from generators", "<yellow><bold>                  to access powerful upgrades.", "", "<green><bold>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),

    /* Game - Postgame*/
    FINAL_KILL_MESSAGE("gameplay.postgame.final-kill", "<victim_team_color><player_name> <gray>died. <aqua><bold>FINAL KILL!"),
    GAME_END_MESSAGE("gameplay.postgame.", ""),

    ;

    private static final EnumMap<Lang, Message> SERVER_LOADED_LANG = new EnumMap<>(Lang.class);

    private static ConfigTranslator TRANSLATOR;

    public static void init(MainConfiguration.LangSection cfg) {
        if (cfg.getModernSettings() != null) {
            MiniMessageWrapper.importConfig(cfg.getModernSettings());
            TRANSLATOR = new MiniMessageTranslator(MiniMessageWrapper.getConfigInstance());
        } else
            TRANSLATOR = new LegacyTranslator(cfg.getLegacySettings().getTranslationChar());
    }

    public static void load(File file) {
        SERVER_LOADED_LANG.clear();
        if (!file.exists()) {
            for (Lang value : Lang.values()) {
                Message message = AdventureMessage.from(new String[0]);
                for (String s : value.getDefault()) {
                    message.addLine(s);
                }
                SERVER_LOADED_LANG.put(value, message);
            }
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (Lang lang : Lang.values()) {
            if (config.isList(lang.getKey())) {
                String[] strings = config.getStringList(lang.getKey()).toArray(new String[0]);
                Message message = ConfigMessage.from(new String[0]);
                for (String string : strings) {
                    message.addLine(string);
                }
                SERVER_LOADED_LANG.put(lang, message);
                continue;
            }
            String value = config.getString(lang.getKey());
            if (value != null) {
                SERVER_LOADED_LANG.put(lang, ConfigMessage.from(value));
            } else {
                Message message = AdventureMessage.from(new String[0]);
                for (String s : lang.getDefault()) {
                    message.addLine(s);
                }
                SERVER_LOADED_LANG.put(lang, message);
            }
        }
    }

    public static ConfigTranslator getTranslator() {
        return TRANSLATOR;
    }

    private final String key;
    private final String[] def;

    Lang(String key, String... def) {
        this.key = key;
        this.def = def;
    }

    private String getKey() {
        return key;
    }

    private String[] getDefault() {
        return def;
    }

    public String toString() {
        return this.asMessage().getMessage();
    }

    public Message asMessage() {
        return SERVER_LOADED_LANG.get(this);
    }

}
