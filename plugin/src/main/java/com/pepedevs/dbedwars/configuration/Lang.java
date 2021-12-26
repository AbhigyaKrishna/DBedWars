package com.pepedevs.dbedwars.configuration;

import com.pepedevs.dbedwars.configuration.translator.ConfigTranslator;
import com.pepedevs.dbedwars.configuration.translator.LegacyTranslator;
import com.pepedevs.dbedwars.configuration.translator.MiniMessageTranslator;
import com.pepedevs.dbedwars.messaging.MiniMessageWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.EnumMap;

public enum Lang {

    PREFIX("prefix", "&6[ &9Bedwars &6]"),

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
    ;

    private static final EnumMap<Lang, Component> SERVER_LOADED_LANG = new EnumMap<>(Lang.class);

    private static ConfigTranslator TRANSLATOR;
    private static ConfigTranslator DEFAULT_TRANSLATOR;

    public static void init(MainConfiguration.LangSection cfg) {
        if (cfg.getModernSettings() != null) {
            MiniMessageWrapper.importConfig(cfg.getModernSettings());
            TRANSLATOR = new MiniMessageTranslator(MiniMessageWrapper.getConfigInstance());
        } else
            TRANSLATOR = new LegacyTranslator(cfg.getLegacySettings().getTranslationChar());

        DEFAULT_TRANSLATOR = new MiniMessageTranslator(MiniMessageWrapper.getFullInstance());
    }

    public static void load(File file) {
        SERVER_LOADED_LANG.clear();
        if (!file.exists()) {
            for (Lang value : Lang.values()) {
                SERVER_LOADED_LANG.put(value, DEFAULT_TRANSLATOR.translate(value.getDef()));
            }
        }

        YamlConfiguration lang = YamlConfiguration.loadConfiguration(file);
        for (Lang l : Lang.values()) {
            String value = lang.getString(l.getKey());
            if (value != null) {
                SERVER_LOADED_LANG.put(l, TRANSLATOR.translate(value));
            } else {
                SERVER_LOADED_LANG.put(l, DEFAULT_TRANSLATOR.translate(l.getDef()));
            }
        }
    }

    public static ConfigTranslator getTranslator() {
        return TRANSLATOR;
    }

    public static ConfigTranslator getDefaultTranslator() {
        return DEFAULT_TRANSLATOR;
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
        return TRANSLATOR.untranslate(this.asComponent());
    }

    public Component asComponent() {
        return SERVER_LOADED_LANG.get(this);
    }

}
