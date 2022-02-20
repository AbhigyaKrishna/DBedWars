package org.zibble.dbedwars.handler;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.hooks.world.WorldAdaptor;
import org.zibble.dbedwars.hooks.SlimeWorldManagerHook;
import org.zibble.dbedwars.hooks.defaults.world.WorldAdaptorImpl;

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
