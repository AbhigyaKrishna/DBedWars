package me.abhigya.dbedwars.addon;

import io.github.abhiram555.RemoteAddonContainer;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.configuration.PluginFiles;

import java.io.File;

public class AddonManager {

    private final DBedwars plugin;
    private final RemoteAddonContainer< BedWarsAddon > addonContainer;

    public AddonManager( DBedwars plugin ) {
        this.plugin = plugin;
        this.addonContainer = new RemoteAddonContainer<>( this.plugin );
        this.addonContainer.getAddonSettings( ).setMainFest( "addon.yml" );
    }

    public void loadAddon( ) {
        for ( File jars : PluginFiles.ADDON.getFile( ).listFiles( ) ) {
            try {
                this.addonContainer.loadAddon( jars );
            } catch ( Exception ignored ) {
            }
        }
        for ( BedWarsAddon addon : this.addonContainer.getAddonClasses( ) ) {
            addon.onLoad( );
        }
    }

    public void enableAddon( ) {
        for ( BedWarsAddon addon : this.addonContainer.getAddonClasses( ) ) {
            addon.onEnable( );
        }
    }

    public void disableAddon( ) {
        for ( BedWarsAddon addon : this.addonContainer.getAddonClasses( ) ) {
            addon.onDisable( );
        }
    }

}
