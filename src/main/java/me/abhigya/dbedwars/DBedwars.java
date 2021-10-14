package me.abhigya.dbedwars;

import me.Abhigya.core.commands.CommandHandler;
import me.Abhigya.core.database.DatabaseType;
import me.Abhigya.core.placeholder.PlaceholderUtil;
import me.Abhigya.core.plugin.Plugin;
import me.Abhigya.core.plugin.PluginAdapter;
import me.Abhigya.core.plugin.PluginDependence;
import me.Abhigya.core.util.ServerPropertiesUtils;
import me.Abhigya.core.util.console.ConsoleUtils;
import me.Abhigya.core.util.hologram.HologramFactory;
import me.Abhigya.core.util.npc.NPCPool;
import me.Abhigya.core.util.server.Version;
import me.abhigya.dbedwars.api.DBedWarsAPI;
import me.abhigya.dbedwars.commands.*;
import me.abhigya.dbedwars.configuration.Lang;
import me.abhigya.dbedwars.configuration.PluginFiles;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableDatabase;
import me.abhigya.dbedwars.database.DatabaseBridge;
import me.abhigya.dbedwars.database.MongoDB;
import me.abhigya.dbedwars.database.MySQL;
import me.abhigya.dbedwars.database.SQLite;
import me.abhigya.dbedwars.game.GameManager;
import me.abhigya.dbedwars.handler.*;
import me.abhigya.dbedwars.item.*;
import me.abhigya.dbedwars.listeners.BlastProofGlassListener;
import me.abhigya.dbedwars.nms.NMSAdaptor;
import me.abhigya.dbedwars.nms.v1_8_R3.NMSUtils;
import me.abhigya.dbedwars.utils.ConfigurationUtils;
import me.abhigya.dbedwars.utils.PluginFileUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public final class DBedwars extends PluginAdapter {

    private String alias;
    private Version serverVersion;
    private GameManager gameManager;
    private List< Listener > listeners;

    private String mainWorld;

    private WorldHandler worldHandler;
    private GuiHandler guiHandler;
    private CustomItemHandler customItemHandler;
    private ThreadHandler threadHandler;
    private ConfigHandler configHandler;
    private NPCPool npcHandler;
    private ImageHandler imageHandler;
    private HologramFactory hologramFactory;

    private NMSAdaptor nmsAdaptor;
    private DatabaseBridge database;

    @Override
    public void onLoad( ) {

        if ( !PluginFiles.LANGUAGES.getFile( ).isDirectory( ) )
            PluginFiles.LANGUAGES.getFile( ).mkdirs( );

        for ( PluginFiles files : PluginFiles.getLanguageFiles( ) ) {
            if ( !files.getFile( ).exists( ) ) {
                this.saveResource( "languages/" + files.getFile( ).getName( ), PluginFiles.LANGUAGES.getFile( ), false );
            }
        }

        Lang.setLangFile( PluginFiles.ENGLISH.getFile( ) );

        this.alias = Lang.PREFIX.toString( );
    }

    @Override
    protected boolean setUp( ) {
        this.serverVersion = Version.getServerVersion( );
        this.nmsAdaptor = this.registerNMSAdaptor( );
        this.hologramFactory = new HologramFactory( this );

        this.getServer( ).getServicesManager( ).register( DBedWarsAPI.class, new APIImpl( this ), this, ServicePriority.Normal );

        this.mainWorld = ServerPropertiesUtils.getStringProperty( "level-name", "world" );

        boolean spawnNpc = ServerPropertiesUtils.getBooleanProperty( "spawn-npcs", false );
        int spawnProt = ServerPropertiesUtils.getIntProperty( "spawn-protection", -1 );
        return true;
    }

    @Override
    public void onDisable( ) {
//        this.threadHandler.destroyAllThreads();
    }

    /**
     * <ul>
     *     <li>PluginDependence[0] = MultiVerseCore</li>
     *     <li>PluginDependence[1] = SlimeWorldManager</li>
     * </ul>
     */
    @Override
    public PluginDependence[] getDependences( ) {
        if ( !PluginFiles.HOOKS.getFile( ).isDirectory( ) ) {
            if ( !PluginFiles.HOOKS.getFile( ).mkdirs( ) ) {
                ConsoleUtils.sendPluginMessage( ChatColor.RED + Lang.ERROR_WRITE_FILES.toString( ), this.getAlias( ) );
            }
        }
        PlaceholderUtil.tryHook( this );
        return new PluginDependence[]{
                new PluginDependence( "MultiVerse-Core" ) {

                    private final File file = PluginFiles.MULTIVERSE_CORE_HOOK.getFile( );

                    @Override
                    public Boolean apply( org.bukkit.plugin.Plugin plugin ) {
                        if ( plugin != null ) {
                            ConsoleUtils.sendPluginMessage( Lang.HOOK_FOUND.toString( ).replace( "{hook}", this.getName( ) ), DBedwars.this.getAlias( ) );
                            DBedwars.this.saveResource( "hooks/" + this.file.getName( ), this.file.getParentFile( ), false );
                            PluginFileUtils.set( this.file, "enabled", true );
                        } else {
                            if ( this.file.exists( ) ) {
                                PluginFileUtils.set( this.file, "enabled", false );
                            }
                        }
                        return true;
                    }
                },
                new PluginDependence( "SlimeWorldManager" ) {

                    private final File file = PluginFiles.SLIME_WORLD_MANAGER_HOOK.getFile( );

                    @Override
                    public Boolean apply( org.bukkit.plugin.Plugin plugin ) {
                        if ( plugin != null && DBedwars.this.checkSWM( plugin ) ) {
                            ConsoleUtils.sendPluginMessage( Lang.HOOK_FOUND.toString( ).replace( "{hook}", this.getName( ) ), DBedwars.this.getAlias( ) );
                            DBedwars.this.saveResource( "hooks/" + this.file.getName( ), this.file.getParentFile( ), false );
                            PluginFileUtils.set( this.file, "enabled", true );
                        } else {
                            if ( this.file.exists( ) ) {
                                PluginFileUtils.set( this.file, "enabled", false );
                            }
                        }
                        return true;
                    }
                }
        };
    }

    @Override
    protected boolean setUpConfig( ) {
        for ( PluginFiles folder : PluginFiles.getDirectories( ) ) {
            if ( !folder.getFile( ).isDirectory( ) )
                if ( !folder.getFile( ).mkdirs( ) ) {
                    ConsoleUtils.sendPluginMessage( Lang.ERROR_WRITE_FILES.toString( ), this.getAlias( ) );
                    return false;
                }
        }

        for ( PluginFiles file : PluginFiles.getFiles( ) ) {
            String path = "";
            File parent = file.getFile( ).getParentFile( );
            while ( !parent.getName( ).equals( PluginFiles.PLUGIN_DATA_FOLDER.getFile( ).getName( ) ) ) {
                path = parent.getName( ) + "/" + path;
                parent = parent.getParentFile( );
            }
            this.saveResource( path + file.getFile( ).getName( ), file.getFile( ).getParentFile( ), false );
        }

        return true;
    }

    @Override
    protected boolean setUpHandlers( ) {
        this.worldHandler = new WorldHandler( this );
        this.gameManager = new GameManager( this );
        this.configHandler = new ConfigHandler( this );
        this.guiHandler = new GuiHandler( this );
        this.customItemHandler = new CustomItemHandler( this );
        this.imageHandler = new ImageHandler(this);
        this.npcHandler = NPCPool.createDefault( this );


        this.threadHandler = new ThreadHandler( this, 4, 3 );
        this.threadHandler.runThreads( );

        CompletableFuture<Boolean> configLoaded = new CompletableFuture<>();

        this.threadHandler.getUpdaterThread( ).add( ( ) -> {
            this.configHandler.loadConfigurations( );

            this.guiHandler.loadMenus( );
            this.guiHandler.loadAnvilMenus( );

            this.registerCustomItems( );
            this.initDatabase( );

            configLoaded.complete(true);
        } );

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                configLoaded.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            imageHandler.clearCache();
            imageHandler.formatAllImagesToPNG();
            imageHandler.loadAllFormattedImages();
        });

        return true;
    }

    @Override
    protected boolean setUpCommands( ) {
        new CommandHandler( this, "bedwars", new Setup( this ), new Start( this ), new End( this ), new HoloTestCommand( ), new ImageParticleTestCommand( ) );

        return true;
    }

    @Override
    protected boolean setUpListeners( ) {
        this.listeners = new ArrayList<>( );
        this.listeners.add( new BlastProofGlassListener( ) );
        this.listeners.forEach( l -> this.getServer( ).getPluginManager( ).registerEvents( l, this ) );
        return true;
    }

    public static DBedwars getInstance( ) {
        return Plugin.getPlugin( DBedwars.class );
    }

    public Version getServerVersion( ) {
        return this.serverVersion;
    }

    public String getAlias( ) {
        return this.alias;
    }

    public String getVersion( ) {
        return this.getDescription( ).getVersion( );
    }

    public GameManager getGameManager( ) {
        return gameManager;
    }

    public String getMainWorld( ) {
        return this.mainWorld;
    }

    public WorldHandler getGeneratorHandler( ) {
        return this.worldHandler;
    }

    public CustomItemHandler getCustomItemHandler( ) {
        return this.customItemHandler;
    }

    public GuiHandler getGuiHandler( ) {
        return this.guiHandler;
    }

    public ThreadHandler getThreadHandler( ) {
        return this.threadHandler;
    }

    public ConfigHandler getConfigHandler( ) {
        return this.configHandler;
    }

    public NMSAdaptor getNMSAdaptor( ) {
        return this.nmsAdaptor;
    }

    public DatabaseBridge getDatabaseBridge( ) {
        return this.database;
    }

    public NPCPool getNpcHandler( ) {
        return this.npcHandler;
    }

    public ImageHandler getImageHandler() {
        return imageHandler;
    }

    public HologramFactory getHologramFactory( ) {
        return this.hologramFactory;
    }

    private void initDatabase( ) {
        DatabaseType type;
        if ( this.getConfigHandler( ).getDatabase( ).isValid( ) )
            type = ConfigurationUtils.matchEnum( this.getConfigHandler( ).getDatabase( ).getDatabase( ), DatabaseType.values( ) );
        else
            type = DatabaseType.SQLite;

        if ( type == DatabaseType.MYSQL ) {
            ConfigurableDatabase.ConfigurableMySQL cfg = this.getConfigHandler( ).getDatabase( ).getMySQL( );
            this.database = new MySQL( this, cfg.getHost( ), cfg.getPort( ), cfg.getDatabaseName( ), cfg.getUsername( ), cfg.getPassword( ), cfg.isReconnect( ), cfg.isSsl( ) );
        } else if ( type == DatabaseType.MongoDB ) {
            ConfigurableDatabase.ConfigurableMongoDB cfg = this.getConfigHandler( ).getDatabase( ).getMongoDB( );
            this.database = new MongoDB( this, cfg.getHost( ), cfg.getPort( ), cfg.getDatabaseName( ) );
        } else {
            this.database = new SQLite( this );
        }

        this.database.init( );
    }

    private NMSAdaptor registerNMSAdaptor( ) {
        switch ( this.serverVersion ) {
            case v1_8_R3:
                return new NMSUtils( );
            default:
                return null;
        }
    }

    private boolean checkSWM( org.bukkit.plugin.Plugin plugin ) {
        switch ( plugin.getDescription( ).getVersion( ) ) {
            case "2.2.0":
            case "2.1.3":
            case "2.1.2":
            case "2.1.1":
            case "2.1.0":
            case "2.0.5":
            case "2.0.4":
            case "2.0.3":
            case "2.0.2":
            case "2.0.1":
            case "2.0.0":
            case "1.1.4":
            case "1.1.3":
            case "1.1.2":
            case "1.1.1":
            case "1.1.0":
            case "1.0.2":
            case "1.0.1":
            case "1.0.0-BETA":
                ConsoleUtils.sendPluginMessage( ChatColor.DARK_RED + "Could not hook into SlimeWorldManager support! You are running an unsupported version", this.getAlias( ) );
                return false;
        }
        return true;
    }

    private void registerCustomItems( ) {
        this.customItemHandler.registerItem( "FIREBALL", new FireballItem( this ) );
        this.customItemHandler.registerItem( "TNT", new TNTItem( this ) );
        this.customItemHandler.registerItem( "BRIDGE_EGG", new BridgeEgg( this ) );
        this.customItemHandler.registerItem( "WATER_BUCKET", new WaterBucket( this ) );
        this.customItemHandler.registerItem( "SPONGE", new Sponge( this ) );
        this.customItemHandler.registerItem( "DREAM_DEFENDER", new DreamDefenderSpawnEgg( this ) );
        this.customItemHandler.registerItem( "BED_BUG", new BedBugSnowball( this ) );
        this.customItemHandler.registerItem("POPUP_TOWER", new PopupTowerChestItem(this));
    }

    @Override
    public @NotNull
    ChunkGenerator getDefaultWorldGenerator( @NotNull String worldName, @Nullable String id ) {
        return new ChunkGenerator( ) {
            @NotNull
            @Override
            public ChunkData generateChunkData( @NotNull World world, @NotNull Random random, int x, int z, @NotNull ChunkGenerator.BiomeGrid biome ) {
                return this.createChunkData( world );
            }
        };
    }

}
