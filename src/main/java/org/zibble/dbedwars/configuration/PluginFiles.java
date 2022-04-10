package org.zibble.dbedwars.configuration;

import org.zibble.dbedwars.DBedwars;

import java.io.File;

public class PluginFiles {

    /* Hook Files */
    public static final File MULTIVERSE_CORE_HOOK = new File(Folder.HOOKS, "Multiverse-Core-Hook.yml");
    public static final File SLIME_WORLD_MANAGER_HOOK = new File(Folder.HOOKS, "SlimeWorldManager-Hook.yml");
    /* Language Files */
    public static final File EN_US = new File(Folder.LANGUAGES, "en_US.yml");
    public static final File ACTION_MESSAGES_EN_US = new File(Folder.LANGUAGES, "action_messages_en_US.yml");
    /* Files */
    public static final File CONFIG = new File(Folder.PLUGIN_DATA_FOLDER, "config.yml");
    public static final File ITEM_SPAWNERS = new File(Folder.PLUGIN_DATA_FOLDER, "itemspawner.yml");
    public static final File TRAPS = new File(Folder.PLUGIN_DATA_FOLDER, "traps.yml");
    public static final File CUSTOM_ITEMS = new File(Folder.PLUGIN_DATA_FOLDER, "custom-items.yml");
    public static final File HOLOGRAM = new File(Folder.PLUGIN_DATA_FOLDER, "hologram.yml");
    public static final File SCOREBOARD = new File(Folder.PLUGIN_DATA_FOLDER, "scoreboard.yml");
    public static final File NPC = new File(Folder.PLUGIN_DATA_FOLDER, "npc.yml");
    public static final File EVENT = new File(Folder.PLUGIN_DATA_FOLDER, "event.yml");
    /* Database */
    public static final File DATABASE = new File(Folder.PLUGIN_DATA_FOLDER, "database.yml");

    public static File[] getDirectories() {
        return new File[]{
                Folder.PLUGIN_DATA_FOLDER,
                Folder.ARENA,
                Folder.DATA,
                Folder.ARENA_DATA_ARENACACHE,
                Folder.ARENA_DATA_SETTINGS,
                Folder.HOOKS,
                Folder.LANGUAGES,
                Folder.CACHE,
                Folder.LIBRARIES_CACHE,
                Folder.ITEMS,
                Folder.SHOPS,
                Folder.CATEGORY
        };
    }

    public static File[] getHooksSettingFiles() {
        return new File[]{
                SLIME_WORLD_MANAGER_HOOK
        };
    }

    public static File[] getLanguageFiles() {
        return new File[]{
                EN_US,
                ACTION_MESSAGES_EN_US
        };
    }

    public static File[] getFiles() {
        return new File[]{
                CONFIG,
                ITEM_SPAWNERS,
                TRAPS,
                CUSTOM_ITEMS,
                HOLOGRAM,
                SCOREBOARD,
                NPC,
                DATABASE
        };
    }

    public static class Folder {

        public static final File PLUGIN_DATA_FOLDER = DBedwars.getInstance().getDataFolder();
        public static final File ARENA = new File(PLUGIN_DATA_FOLDER, "arena");
        public static final File DATA = new File(ARENA, "data");
        public static final File ARENA_DATA_ARENACACHE = new File(DATA, "arenacache");
        public static final File ARENA_DATA_SETTINGS = new File(DATA, "settings");
        public static final File HOOKS = new File(PLUGIN_DATA_FOLDER, "hooks");
        public static final File LANGUAGES = new File(PLUGIN_DATA_FOLDER, "languages");
        public static final File CACHE = new File(PLUGIN_DATA_FOLDER, ".cache");
        public static final File LIBRARIES_CACHE = new File(CACHE, ".libraries");
        public static final File ITEMS = new File(PLUGIN_DATA_FOLDER, "items");
        public static final File SHOPS = new File(PLUGIN_DATA_FOLDER, "shops");
        public static final File CATEGORY = new File(PLUGIN_DATA_FOLDER, "category");

    }

    public static class Data {

        public static final File LOBBY_SPAWN = new File(Folder.DATA, "lobbyspawnlocation.yml");

    }

}
