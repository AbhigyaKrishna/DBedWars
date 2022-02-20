package org.zibble.dbedwars.api;

import com.pepedevs.radium.holograms.HologramManager;
import org.zibble.dbedwars.api.handler.CustomItemHandler;
import org.zibble.dbedwars.api.handler.GameManager;
import org.zibble.dbedwars.api.handler.GuiHandler;
import org.zibble.dbedwars.api.handler.ThreadHandler;
import org.zibble.dbedwars.api.hooks.world.WorldAdaptor;
import org.zibble.dbedwars.api.plugin.Plugin;
import org.zibble.dbedwars.api.version.Version;

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