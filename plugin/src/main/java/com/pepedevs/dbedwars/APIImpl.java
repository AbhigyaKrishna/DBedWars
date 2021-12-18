package com.pepedevs.dbedwars;

import com.pepedevs.corelib.holograms.HologramManager;
import com.pepedevs.corelib.utils.version.Version;
import com.pepedevs.dbedwars.api.DBedWarsAPI;
import com.pepedevs.dbedwars.api.handler.*;

public final class APIImpl extends DBedWarsAPI {

    private final DBedwars plugin;

    public APIImpl(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public Version getServerVersion() {
        return this.plugin.getServerVersion();
    }

    @Override
    public String getPluginVersion() {
        return this.plugin.getVersion();
    }

    @Override
    public GameManager getGameManager() {
        return this.plugin.getGameManager();
    }

    @Override
    public WorldAdaptor getGeneratorHandler() {
        return this.plugin.getGeneratorHandler().getWorldAdaptor();
    }

    @Override
    public CustomItemHandler getCustomItemHandler() {
        return this.plugin.getCustomItemHandler();
    }

    @Override
    public GuiHandler getGuiHandler() {
        return this.plugin.getGuiHandler();
    }

    @Override
    public ThreadHandler getThreadHandler() {
        return this.plugin.getThreadHandler();
    }

    @Override
    public HologramManager getHologramFactory() {
        return this.plugin.getHologramManager();
    }

    @Override
    public Version getVersion() {
        return this.plugin.getServerVersion();
    }
}
