package me.abhigya.dbedwars.task;

import me.Abhigya.core.util.scheduler.SchedulerUtils;
import me.Abhigya.core.util.server.Version;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.handler.WorldAdaptor;
import me.abhigya.dbedwars.configuration.PluginFiles;
import me.abhigya.dbedwars.utils.PluginFileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class DefaultWorldAdaptor implements WorldAdaptor {

    private final DBedwars plugin;

    public DefaultWorldAdaptor( DBedwars plugin ) {
        this.plugin = plugin;
    }

    @Override
    public World createWorld( String worldName, World.Environment environment ) {
        if ( Bukkit.getWorld( worldName ) != null )
            return Bukkit.getWorld( worldName );

        WorldCreator wc = new WorldCreator( worldName );
        wc.environment( environment );
        wc.type( WorldType.FLAT );
        if ( plugin.getServerVersion( ).isNewerEquals( Version.v1_13_R1 ) ) {
            wc.generator( plugin.getName( ) );
        } else {
            wc.generatorSettings( "2;0;1" );
        }
        return wc.createWorld( );
    }

    @Override
    public World loadWorldFromFolder( String worldName ) {
        if ( Bukkit.getWorld( worldName ) != null )
            return Bukkit.getWorld( worldName );

        WorldCreator wc = new WorldCreator( worldName );
        return wc.createWorld( );
    }

    @Override
    public World loadWorldFromSave( String fileName ) {
        World world = Bukkit.getWorld( fileName );
        if ( world == null ) {
            world = this.createWorld( fileName, World.Environment.NORMAL );
        }

        PluginFileUtils.copyWorldRegion( fileName, fileName );

        this.plugin.getNMSAdaptor( ).clearRegionFileCache( world );
        this.plugin.getNMSAdaptor( ).clearChunkCache( world );
        World finalWorld = world;
        SchedulerUtils.runTaskLater( ( ) -> {
            for ( Player player : finalWorld.getPlayers( ) ) {
                plugin.getNMSAdaptor( ).refreshPlayerChunk( player );
            }
        }, 20L, this.plugin );
        return world;
    }

    @Override
    public boolean saveWorld( String worldName, String fileName ) {
        this.unloadWorld( worldName, true );

        if ( !PluginFileUtils.saveWorldRegions( worldName, fileName ) )
            return false;

        return true;
    }

    @Override
    public boolean saveExist( String name ) {
        File file = new File( PluginFiles.ARENA_DATA_ARENACACHE.getFile( ), name + ".zip" );
        return file.exists( );
    }

    @Override
    public void unloadWorld( String worldName, boolean save ) {
        World world = Bukkit.getWorld( worldName );
        if ( world != null ) {
            List< Player > players = world.getPlayers( );
            Location spawn = Bukkit.getWorld( this.plugin.getMainWorld( ) ).getSpawnLocation( );
            players.forEach( p -> p.teleport( spawn ) );

            Bukkit.unloadWorld( world, save );
        }
    }

    @Override
    public void deleteWorld( String worldName ) {
        for ( File file : PluginFiles.ARENA_DATA_ARENACACHE.getFile( ).listFiles( ) ) {
            if ( file.getName( ).equals( worldName + ".yml" ) )
                file.delete( );
        }
    }

}
