package me.abhigya.dbedwars.configuration;

import me.abhigya.dbedwars.DBedwars;

import java.io.File;

public enum PluginFiles {

    /* Folder */
    PLUGIN_DATA_FOLDER(DBedwars.getInstance().getDataFolder()),
    ADDON(new File(PLUGIN_DATA_FOLDER.getFile(), "addons")),
    ARENA(new File(PLUGIN_DATA_FOLDER.getFile(), "arena")),
    ARENA_DATA(new File(ARENA.getFile(), "data")),
    ARENA_DATA_ARENACACHE(new File(ARENA_DATA.getFile(), "arenacache")),
    ARENA_DATA_SETTINGS(new File(ARENA_DATA.getFile(), "settings")),
    HOOKS(new File(PLUGIN_DATA_FOLDER.getFile(), "hooks")),
    LANGUAGES(new File(PLUGIN_DATA_FOLDER.getFile(), "languages")),

    /* Hook Files */
    MULTIVERSE_CORE_HOOK(new File(HOOKS.getFile(), "Multiverse-Core-Hook.yml")),
    SLIME_WORLD_MANAGER_HOOK(new File(HOOKS.getFile(), "SlimeWorldManager-Hook.yml")),

    /* Language Files */
    ENGLISH(new File(LANGUAGES.getFile(), "English.yml")),

    /* Files */
    CONFIG(new File(PLUGIN_DATA_FOLDER.getFile(), "config.yml")),
    ITEM_SPAWNERS(new File(PLUGIN_DATA_FOLDER.getFile(), "itemspawner.yml")),
    TRAPS(new File(PLUGIN_DATA_FOLDER.getFile(), "traps.yml")),
    CUSTOM_ITEMS(new File(PLUGIN_DATA_FOLDER.getFile(),"custom-items.yml")),
    SHOP(new File(PLUGIN_DATA_FOLDER.getFile(), "shop.yml"))
    ;

    private File file;

    PluginFiles(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public static PluginFiles[] getDirectories() {
        return new PluginFiles[]{PLUGIN_DATA_FOLDER, ADDON, ARENA, ARENA_DATA, ARENA_DATA_ARENACACHE, ARENA_DATA_SETTINGS,
                HOOKS};
    }

    public static PluginFiles[] getHooksSettingFiles() {
        return new PluginFiles[]{SLIME_WORLD_MANAGER_HOOK};
    }

    public static PluginFiles[] getLanguageFiles() {
        return new PluginFiles[]{ENGLISH};
    }

    public static PluginFiles[] getFiles() {
        return new PluginFiles[]{CONFIG, ITEM_SPAWNERS, TRAPS, SHOP, CUSTOM_ITEMS};
    }
}
