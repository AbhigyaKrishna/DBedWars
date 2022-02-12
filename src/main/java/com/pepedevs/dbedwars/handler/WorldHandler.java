package com.pepedevs.dbedwars.handler;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.hooks.world.WorldAdaptor;
import com.pepedevs.dbedwars.hooks.SlimeWorldManagerHook;
import com.pepedevs.dbedwars.task.implementations.WorldAdaptorImpl;

public class WorldHandler {

    private final DBedwars plugin;

    private final WorldAdaptor worldAdaptor;

    public WorldHandler(DBedwars plugin) {
        this.plugin = plugin;
        if (plugin.getDependences()[1].isEnabled()) {
            this.worldAdaptor = new SlimeWorldManagerHook(this.plugin);
            ((SlimeWorldManagerHook) this.worldAdaptor).setup();
        } else this.worldAdaptor = new WorldAdaptorImpl(plugin);
    }

    public WorldAdaptor getWorldAdaptor() {
        return this.worldAdaptor;
    }
}
