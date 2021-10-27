package me.abhigya.dbedwars.api;

import me.Abhigya.core.util.hologram.HologramFactory;
import me.Abhigya.core.util.server.Version;
import me.abhigya.dbedwars.api.handler.*;

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

    public abstract HologramFactory getHologramFactory();
}
