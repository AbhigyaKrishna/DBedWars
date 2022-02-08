package com.pepedevs.dbedwars.configuration;

import com.pepedevs.dbedwars.DBedwars;

import java.io.File;

public class PluginFiles {

    /* Folder */
    public static final File PLUGIN_DATA_FOLDER = DBedwars.getInstance().getDataFolder();
    public static final File ARENA = new File(PLUGIN_DATA_FOLDER, "arena");
    public static final File ARENA_DATA = new File(ARENA, "data");
    public static final File ARENA_DATA_ARENACACHE = new File(ARENA_DATA, "arenacache");
    public static final File ARENA_DATA_SETTINGS = new File(ARENA_DATA, "settings");
    public static final File HOOKS = new File(PLUGIN_DATA_FOLDER, "hooks");
    public static final File LANGUAGES = new File(PLUGIN_DATA_FOLDER, "languages");

    /* Hook Files */
    public static final File MULTIVERSE_CORE_HOOK = new File(HOOKS, "Multiverse-Core-Hook.yml");
    public static final File SLIME_WORLD_MANAGER_HOOK = new File(HOOKS, "SlimeWorldManager-Hook.yml");

    /* Language Files */
    public static final File EN_US = new File(LANGUAGES, "en_US.yml");

    /* Files */
    public static final File CONFIG = new File(PLUGIN_DATA_FOLDER, "config.yml");
    public static final File ITEM_SPAWNERS = new File(PLUGIN_DATA_FOLDER, "itemspawner.yml");
    public static final File TRAPS = new File(PLUGIN_DATA_FOLDER, "traps.yml");
    public static final File CUSTOM_ITEMS = new File(PLUGIN_DATA_FOLDER, "custom-items.yml");
    public static final File SHOP = new File(PLUGIN_DATA_FOLDER, "shop.yml");
    public static final File UPGRADES = new File(PLUGIN_DATA_FOLDER, "upgrades.yml");
    public static final File HOLOGRAM = new File(PLUGIN_DATA_FOLDER, "hologram.yml");
    public static final File SCOREBOARD = new File(PLUGIN_DATA_FOLDER, "scoreboard.yml");

    /* Database */
    public static final File DATABASE = new File(PLUGIN_DATA_FOLDER, "database.yml");

    public static File[] getDirectories() {
        return new File[]{
                PLUGIN_DATA_FOLDER, ARENA, ARENA_DATA, ARENA_DATA_ARENACACHE, ARENA_DATA_SETTINGS, HOOKS, LANGUAGES
        };
    }

    public static File[] getHooksSettingFiles() {
        return new File[]{
                SLIME_WORLD_MANAGER_HOOK
        };
    }

    public static File[] getLanguageFiles() {
        return new File[] {
                EN_US
        };
    }

    public static File[] getFiles() {
        return new File[] {
            CONFIG,
            ITEM_SPAWNERS,
            TRAPS,
            SHOP,
            UPGRADES,
            CUSTOM_ITEMS,
            HOLOGRAM,
            SCOREBOARD,
            DATABASE
        };
    }

}
