package org.zibble.dbedwars.configuration;

import org.bukkit.configuration.file.YamlConfiguration;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.configuration.translator.ConfigTranslator;
import org.zibble.dbedwars.configuration.translator.LegacyTranslator;
import org.zibble.dbedwars.configuration.translator.MiniMessageTranslator;
import org.zibble.dbedwars.messaging.MiniMessageWrapper;

import java.io.File;
import java.util.EnumMap;

public enum Lang {

    PREFIX("prefix", "<gold>[ <blue>Bedwars <gold>]"),

    /* Some default plugin messages */
    UNKNOWN_COMMAND("plugin-default.unknown-command","<red>This is an unknown command. use <gold>/bedwars help <red>for more info."),
    NO_PERMISSION("plugin-default.no-permission","<red>You don't have permission for this!"),

    /* General */
    ARENA("general.arena", "Arena"),
    COLOR_YELLOW("general.color-yellow", "Yellow"),
    COLOR_ORANGE("general.color-orange", "Orange"),
    COLOR_RED("general.color-red", "Red"),
    COLOR_BLUE("general.color-blue", "Blue"),
    COLOR_LIGHT_BLUE("general.color-lightBlue", "Light blue"),
    COLOR_CYAN("general.color-cyan", "Cyan"),
    COLOR_LIME("general.color-lime", "Lime"),
    COLOR_GREEN("general.color-green", "Green"),
    COLOR_PURPLE("general.color-purple", "Purple"),
    COLOR_PINK("general.color-pink", "Pink"),
    COLOR_WHITE("general.color-white", "White"),
    COLOR_LIGHT_GRAY("general.color-lightGray", "Light gray"),
    COLOR_GRAY("general.color-gray", "Gray"),
    COLOR_BROWN("general.color-brown", "Brown"),
    COLOR_BLACK("general.color-black", "Black"),

    /*Rejoin related messages*/
    REJOIN_PLAYER_MESSAGE("gameplay.rejoin.player-rejoin-message","<green>Rejoining to match..."),
    REJOIN_BROADCAST_MESSAGE("gameplay.rejoin.player-rejoin-broadcast","<gray><player_name> has reconnected to the match"),
    REJOIN_FAILED_GAME_ENDED("gameplay.rejoin.rejoin-failed.game-ended","<red>You cannot reconnect to the match! Either the match ended or your bed is broken"),
    REJOIN_FAILED_NO_PREVIOUS_GAME("gameplay.rejoin.rejoin-failed.no-previous-game","<red>You were not in an arena to reconnect!"),

    /* Bedwars commands */
    NO_ARENA_FOUND_W_NAME("command-response.no-arena-found", "<red>No arena found with this name!"),
    NOT_IN_AN_ARENA("command-response.not-in-arena", "<red>You are not in a arena!"),
    SPECIFIED_ARENA_IS_FULL("command-response.arena-is-full","<red>This arena is currently full! Try again later..."),
    BLOCKED_COMMAND("command-response.blocked-command","<red>You cannot use this command during match!"),

    /* Game - Pregame*/
    ARENA_JOIN_MESSAGE("gameplay.pregame.player-arena-join", "<green><player_name> <yellow>has joined (<aqua><current_players></aqua>/<aqua><max_players></aqua>)!"),
    ARENA_LEAVE_MESSAGE("gameplay.pregame.player-arena-leave","<green><player_name> <yellow>has quit!"),
    ARENA_COUNTDOWN_INSUFFICIENT("gameplay.pregame.countdown-insufficent-players","<red>There aren't enough players for the arena! Countdown has been stopped!"),

    /* Game - Ingame*/
    BED_BROKEN_OTHERS("gameplay.ingame.bed-broken-broadcast-others", "<defend_team_color><defend_team_name> 's Bed <gray>was destroyed by <attack_team_color><attack_team_name>"),
    BED_BROKEN_SELF("gameplay.ingame.bed-broken-broadcast-self", "<gray>Your bed was destroyed by <defend_team_color><defend_team_name>"),
    ARENA_START_MESSAGE("gameplay.ingame.game-start",
            "<green><bold>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
            "<white>                                 <bold>Bed Wars", "<yellow><bold>     Protect your bed and destroy the enemy beds.",
            "<yellow><bold>      Upgrade yourself and your team by collecting,<yellow><bold>    Iron, Gold, Emerald and Diamond from generators",
            "<yellow><bold>                  to access powerful upgrades.",
            "",
            "<green><bold>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
    CANNOT_BREAK_OWN_BED("gameplay.ingame.cannot-break-own-bed","<red>You cannot destroy your own bed!"),
    CANNOT_BREAK_MAP_BLOCKS("gameplay.ingame.cannot-break-map-blocks","<red>You cannot break blocks which are not placed by players!"),
    /* Game - Postgame*/
    FINAL_KILL_MESSAGE("gameplay.postgame.final-kill", "<victim_team_color><player_name> <gray>died. <aqua><bold>FINAL KILL!"),
    GAME_END_MESSAGE("gameplay.postgame.game-end-message","<team_color><player_name> <green>has won the game"),
    GAME_END_BROADCAST("gameplay.postgame.game-end-top-stats-broadcast",
            "<green><bold>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"
                    ,"<white>                                 <bold>Bed Wars"
                    ,"<reset>"
                    ,"<yellow>                          <winner>"
                    ,"<reset>"
                    ,"<yellow>                          <bold>1st Killer<gray> - <first_killer_name> - <first_killer_kill>"
                    ,"<yellow>                          <bold>2nd Killer<gray> - <second_killer_name> - <second_killer_kill>"
                    ,"<yellow>                          <bold>3rd Killer<gray> - <third_killer_name> - <third_killer_kill>"
                    ,"<green><bold>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"
    ),
    GAME_START_COUNTDOWN("game-start-count-down",
            "[TITLE][DELAY = 0s]<yellow>❺",
            "[MESSAGE][DELAY = 0s]<yellow>The game starts in <gold><countdown> <yellow>seconds!",
            "[SOUND][DELAY = 0s]block.note_block.bell",
            "[TITLE][DELAY = 1s]<yellow>❹",
            "[MESSAGE][DELAY = 1s]<yellow>The game starts in <gold><countdown> <yellow>seconds!",
            "[SOUND][DELAY = 1s]block.note_block.bell",
            "[TITLE][DELAY = 2s]<red>❸",
            "[MESSAGE][DELAY = 2s]<red>The game starts in <gold><countdown> <red>seconds!",
            "[SOUND][DELAY = 2s]block.note_block.bell",
            "[TITLE][DELAY = 3s]<red>❷",
            "[MESSAGE][DELAY = 3s]<red>The game starts in <gold><countdown> <red>seconds!",
            "[SOUND][DELAY = 3s]block.note_block.bell",
            "[TITLE][DELAY = 4s]<red>❶",
            "[MESSAGE][DELAY = 4s]<red>The game starts in <gold><countdown> <red>seconds!",
            "[SOUND][DELAY = 4s]block.note_block.bell"
    ),

    /*
    Death messages
     */
    DEATH_MESSAGE_PLAYER_ATTACK("death-messages.player-attack","<victim_color><victim_player> <white>was terrible in pvp compared to <attacker_color><attacker_player><white>."),
    DEATH_MESSAGE_UNKNOWN_REASON("death-messages.unknown-reason","<victim_color><victim_player> <white>died somehow but i guess we will never know how."),
    //Fall-death
    DEATH_MESSAGE_FALL_BY_PLAYER("death-messages.fall-death.by-player","<victim_color><victim_player> <white>was thrown into the sky by<attacker_color><attacker_player><white>and died."),
    DEATH_MESSAGE_FALL_NO_PLAYER("death-messages.fall-death.no-player","<victim_color><victim_player> <white>tried to fly like a bird but died of fall damage finally<white>."),
    //Velocity-death
    DEATH_MESSAGE_VELOCITY_BY_PLAYER("death-messages.velocity-death.by-player","<victim_color><victim_player> <white>tried to become flash but could not handle the speed and smashed into a wall"),
    DEATH_MESSAGE_VELOCITY_NO_PLAYER("death-messages.velocity-death.no-player","<victim_color><victim_player> <white>was given enormous power of speed by<attacker_color><attacker_player><white>but couldn't handle it and died."),
    //Explosion Death
    DEATH_MESSAGE_EXPLOSION_BY_PLAYER("death-messages.explosion-death.by-player","<victim_color><victim_player> <white> exploded into million pieces by <attacker_color><attacker_player><white>but couldn't handle it and died."),
    DEATH_MESSAGE_EXPLOSION_NO_PLAYER("death-messages.explosion-death.no-player","<victim_color><victim_player> <white> exploded into million pieces."),
    //Drown Death
    DEATH_MESSAGE_DROWN_BY_PLAYER("death-messages.drown-death.by-player","<victim_color><victim_player> <white>died because of <attacker_color><attacker_player><white> using up all the oxygen"),
    DEATH_MESSAGE_DROWN_NO_PLAYER("death-messages.drown-death.no-player","<victim_color><victim_player> <white>died by drowning himself cause he thought he can breath underwater like a fish"),
    //Burning Death
    DEATH_MESSAGE_BURNING_BY_PLAYER("death-messages.death-by-burning.by-player","<victim_color><victim_player> <white>was deep fried by <attacker_color><attacker_player><white>."),
    DEATH_MESSAGE_BURNING_NO_PLAYER("death-messages.death-by-burning.no-player","<victim_color><victim_player> <white>was trying to get a burning sensation but killed himself in the process."),
    //Magic Death
    DEATH_MESSAGE_MAGIC_BY_PLAYER("death-messages.death-by-magic.by-player","<victim_color><victim_player> <white>died because <attacker_color><attacker_player><white>overdosed him with potions."),
    DEATH_MESSAGE_MAGIC_NO_PLAYER("death-messages.death-by-magic.no-player","<victim_color><victim_player> <white>died due to overdose from potions."),
    //Void Death
    DEATH_MESSAGE_VOID_BY_PLAYER("death-messages.death-by-void.by-player","<victim_color><victim_player> <white>was pushed into void by <attacker_color><attacker_player><white>like your parents push you to study."),
    DEATH_MESSAGE_VOID_NO_PLAYER("death-messages.death-by-void.no-player","<victim_color><victim_player> <white>was pressurised by the society so he killed himself."),
    //Cramming Death
    DEATH_MESSAGE_CRAMMING_BY_PLAYER("death-messages.death-by-cramming.by-player","<victim_color><victim_player> <white>was squished by <attacker_color><attacker_player><white>."),
    DEATH_MESSAGE_CRAMMING_NO_PLAYER("death-messages.death-by-cramming.no-player","<victim_color><victim_player> <white>had no place to exist."),
    //Crushed Death
    DEATH_MESSAGE_CRUSHED_BY_PLAYER("death-messages.crushed.by-player","<victim_color><victim_player> <white>was crushed by <attacker_color><attacker_player><white>."),
    DEATH_MESSAGE_CRUSHED_NO_PLAYER("death-messages.crushed.no-player","<victim_color><victim_player> <white>got pressed like a bottle of sauce."),
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
