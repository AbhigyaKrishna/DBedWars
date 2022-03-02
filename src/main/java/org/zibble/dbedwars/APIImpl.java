package org.zibble.dbedwars;

import com.pepedevs.radium.holograms.HologramManager;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.handler.*;
import org.zibble.dbedwars.api.hooks.world.WorldAdaptor;
import org.zibble.dbedwars.api.version.Version;

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
    public HookManager getHookManager() {
        return this.plugin.getHookManager();
    }

    @Override
    public MenuHandler getMenuHandler() {
        return this.plugin.getMenuHandler();
    }

    @Override
    public HologramManager getHologramFactory() {
        return this.plugin.getHologramManager();
    }

    @Override
    public Version getVersion() {
        return this.plugin.getServerVersion();
    }

    @Override
    public DBedwars getPlugin() {
        return this.plugin;
    }

}
