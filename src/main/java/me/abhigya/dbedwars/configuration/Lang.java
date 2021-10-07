package me.abhigya.dbedwars.configuration;

import me.Abhigya.core.util.StringUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public enum Lang {

    PREFIX( "prefix", "&6[ &9Bedwars &6]" ),

    HOOK_FOUND( "hook-found", "&a{hook} found! Hooking into it..." ),

    /* Error */
    ERROR_WRITE_FILES( "error.write-files", "&aPlugin resource directories cannot be created. Please check if the system has required permissions to write files!" ),

    /* General */
    ARENA( "general.arena", "arena" ),
    COLOR_YELLOW( "general.color-yellow", "yellow" ),
    COLOR_ORANGE( "general.color-orange", "orange" ),
    COLOR_RED( "general.color-red", "red" ),
    COLOR_BLUE( "general.color-blue", "blue" ),
    COLOR_LIGHT_BLUE( "general.color-lightBlue", "light blue" ),
    COLOR_CYAN( "general.color-cyan", "cyan" ),
    COLOR_LIME( "general.color-lime", "lime" ),
    COLOR_GREEN( "general.color-green", "green" ),
    COLOR_PURPLE( "general.color-purple", "purple" ),
    COLOR_PINK( "general.color-pink", "pink" ),
    COLOR_WHITE( "general.color-white", "white" ),
    COLOR_LIGHT_GRAY( "general.color-lightGray", "light gray" ),
    COLOR_GRAY( "general.color-gray", "gray" ),
    COLOR_BROWN( "general.color-brown", "brown" ),
    COLOR_BLACK( "general.color-black", "black" ),

    ;

    private static YamlConfiguration lang;

    private String key;
    private String def;

    Lang( String key, String def ) {
        this.key = key;
        this.def = def;
    }

    public static void setLangFile( File langFile ) {
        lang = YamlConfiguration.loadConfiguration( langFile );
    }

    public static YamlConfiguration getLang( ) {
        return lang;
    }

    public String getKey( ) {
        return key;
    }

    public String getDef( ) {
        return def;
    }

    public String toString( ) {
        String value = lang.getString( this.key, this.def );
        return StringUtils.translateAlternateColorCodes( value );
    }
}
