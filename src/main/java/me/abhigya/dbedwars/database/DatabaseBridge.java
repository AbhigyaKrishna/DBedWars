package me.abhigya.dbedwars.database;

import me.abhigya.dbedwars.DBedwars;

public abstract class DatabaseBridge {

    private final DBedwars plugin;

    public DatabaseBridge(DBedwars plugin){
        this.plugin = plugin;
    }

    public abstract void init();
    public abstract void disconnect();


    public DBedwars getPlugin() {
        return plugin;
    }
}
