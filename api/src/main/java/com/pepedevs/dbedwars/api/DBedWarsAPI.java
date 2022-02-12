package com.pepedevs.dbedwars.api;

import com.pepedevs.dbedwars.api.hooks.world.WorldAdaptor;
import com.pepedevs.dbedwars.api.plugin.Plugin;
import com.pepedevs.radium.holograms.HologramManager;
import com.pepedevs.dbedwars.api.version.Version;
import com.pepedevs.dbedwars.api.handler.*;

public abstract class DBedWarsAPI {

    private static DBedWarsAPI INSTANCE = null;

    public DBedWarsAPI() {
        INSTANCE = this;
    }

    public static DBedWarsAPI getApi() {
        return INSTANCE;
    }

    public abstract Version getServerVersion();

    public abstract String getPluginVersion();

    public abstract GameManager getGameManager();

    public abstract WorldAdaptor getGeneratorHandler();

    public abstract CustomItemHandler getCustomItemHandler();

    public abstract GuiHandler getGuiHandler();

    public abstract ThreadHandler getThreadHandler();

    public abstract HologramManager getHologramFactory();

    public abstract Version getVersion();

    public abstract Plugin getPlugin();
}
