package me.abhigya.dbedwars;

import me.Abhigya.core.util.hologram.HologramFactory;
import me.Abhigya.core.util.server.Version;
import me.abhigya.dbedwars.api.DBedWarsAPI;
import me.abhigya.dbedwars.game.GameManager;
import me.abhigya.dbedwars.handler.CustomItemHandler;
import me.abhigya.dbedwars.handler.GuiHandler;
import me.abhigya.dbedwars.handler.ThreadHandler;
import me.abhigya.dbedwars.handler.WorldHandler;

public final class APIImpl extends DBedWarsAPI {

    private final DBedwars plugin;

    public APIImpl( DBedwars plugin ) {
        this.plugin = plugin;
    }

    @Override
    public Version getServerVersion( ) {
        return this.plugin.getServerVersion( );
    }

    @Override
    public String getPluginVersion( ) {
        return this.plugin.getVersion( );
    }

    @Override
    public GameManager getGameManager( ) {
        return this.plugin.getGameManager( );
    }

    @Override
    public WorldHandler getGeneratorHandler( ) {
        return this.plugin.getGeneratorHandler( );
    }

    @Override
    public CustomItemHandler getCustomItemHandler( ) {
        return this.plugin.getCustomItemHandler( );
    }

    @Override
    public GuiHandler getGuiHandler( ) {
        return this.plugin.getGuiHandler( );
    }

    @Override
    public ThreadHandler getThreadHandler( ) {
        return this.plugin.getThreadHandler( );
    }

    @Override
    public HologramFactory getHologramFactory( ) {
        return this.plugin.getHologramFactory( );
    }

}
