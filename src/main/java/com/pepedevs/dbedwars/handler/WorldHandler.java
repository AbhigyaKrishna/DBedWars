package com.pepedevs.dbedwars.handler;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.handler.WorldAdaptor;
import com.pepedevs.dbedwars.hooks.SlimeWorldManagerHook;
import com.pepedevs.dbedwars.task.implementations.DefaultWorldAdaptor;

public class WorldHandler {

    private final DBedwars plugin;

    private final WorldAdaptor worldAdaptor;

    public WorldHandler(DBedwars plugin) {
        this.plugin = plugin;
        if (plugin.getDependences()[1].isEnabled()) {
            this.worldAdaptor = new SlimeWorldManagerHook(this.plugin);
            ((SlimeWorldManagerHook) this.worldAdaptor).setup();
        } else this.worldAdaptor = new DefaultWorldAdaptor(plugin);
    }

    public WorldAdaptor getWorldAdaptor() {
        return this.worldAdaptor;
    }
}
