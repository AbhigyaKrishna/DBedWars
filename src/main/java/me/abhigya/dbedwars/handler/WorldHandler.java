package me.abhigya.dbedwars.handler;

import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.handler.WorldAdaptor;
import me.abhigya.dbedwars.hooks.SlimeWorldManagerHook;
import me.abhigya.dbedwars.task.DefaultWorldAdaptor;

public class WorldHandler {

    private final DBedwars plugin;

    private final WorldAdaptor worldAdaptor;

    public WorldHandler(DBedwars plugin) {
        this.plugin = plugin;
        if (plugin.getDependences()[1].isEnabled()) {
            this.worldAdaptor = new SlimeWorldManagerHook(this.plugin);
            ((SlimeWorldManagerHook) this.worldAdaptor).setup();
        }
        else
            this.worldAdaptor = new DefaultWorldAdaptor(plugin);
    }

    public WorldAdaptor getWorldAdaptor() {
        return worldAdaptor;
    }
}
