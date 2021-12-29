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

    /* Bedwars commands */
    NO_ARENA_FOUND_W_NAME("arena.no-arena-found", "<red>No arena found with this name!"),
    NOT_IN_AN_ARENA("arena.not-in-arena", "<red>You are not in a arena!")

    /* Arena */



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
                SERVER_LOADED_LANG.put(value, AdventureMessage.from(value.getDef()));
            }
        }

        YamlConfiguration lang = YamlConfiguration.loadConfiguration(file);
        for (Lang l : Lang.values()) {
            String value = lang.getString(l.getKey());
            if (value != null) {
                SERVER_LOADED_LANG.put(l, ConfigMessage.from(value));
            } else {
                SERVER_LOADED_LANG.put(l, AdventureMessage.from(l.getDef()));
            }
        }
    }

    public static ConfigTranslator getTranslator() {
        return TRANSLATOR;
    }

    private final String key;
    private final String def;

    Lang(String key, String def) {
        this.key = key;
        this.def = def;
    }

    public String getKey() {
        return key;
    }

    public String getDef() {
        return def;
    }

    public String toString() {
        return this.asMessage().getMessage();
    }

    public Message asMessage() {
        return SERVER_LOADED_LANG.get(this);
    }

}
