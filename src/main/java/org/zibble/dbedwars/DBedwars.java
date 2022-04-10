package org.zibble.dbedwars;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.nms.NMSAdaptor;
import org.zibble.dbedwars.api.plugin.Plugin;
import org.zibble.dbedwars.api.plugin.PluginAdapter;
import org.zibble.dbedwars.api.plugin.PluginDependence;
import org.zibble.dbedwars.api.version.Version;
import org.zibble.dbedwars.commands.framework.CommandRegistryImpl;
import org.zibble.dbedwars.database.DatabaseType;
import org.zibble.dbedwars.database.bridge.*;
import org.zibble.dbedwars.game.GameManagerImpl;
import org.zibble.dbedwars.game.setup.SetupSessionManager;
import org.zibble.dbedwars.handler.*;
import org.zibble.dbedwars.hooks.defaults.hologram.HologramManager;
import org.zibble.dbedwars.io.ExternalLibrary;
import org.zibble.dbedwars.item.*;
import org.zibble.dbedwars.listeners.VanishListener;
import org.zibble.dbedwars.messaging.Messaging;
import org.zibble.dbedwars.nms.v1_8_R3.NMSUtils;
import org.zibble.dbedwars.script.ScriptRegistryImpl;
import org.zibble.dbedwars.utils.Debugger;
import org.zibble.dbedwars.utils.gamerule.GameRuleHandler;
import org.zibble.inventoryframework.InventoryFramework;

import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;

public final class DBedwars extends PluginAdapter {

    private Version serverVersion;
    private GameManagerImpl gameManager;
    private Listener[] listeners;

    private String mainWorld;

    private FeatureManager featureManager;
    private ConfigHandler configHandler;
    private CustomItemHandler customItemHandler;
    private ThreadHandler threadHandler;
    private HookManagerImpl hookManager;
    private MenuHandler menuHandler;
    private SetupSessionManager setupSessionManager;

    private Messaging messaging;
    private ScriptRegistryImpl scriptRegistry;
    private CommandRegistryImpl commandRegistry;
    private NMSAdaptor nmsAdaptor;
    private DatabaseBridge database;

    public static DBedwars getInstance() {
        return Plugin.getPlugin(DBedwars.class);
    }

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    protected boolean setUp() {
        Debugger.setEnabled(true); // TODO remove this
        this.serverVersion = Version.getServerVersion();
        this.threadHandler = new ThreadHandler();
        this.threadHandler.runThreads(4);
        this.nmsAdaptor = this.registerNMSAdaptor();
        this.hookManager = new HookManagerImpl(this);
        this.hookManager.load();
        this.featureManager = new FeatureManager(this);
        this.featureManager.registerDefaults();
        this.scriptRegistry = new ScriptRegistryImpl();
        this.scriptRegistry.registerDefaults();
        this.commandRegistry = new CommandRegistryImpl(this);
        this.messaging = new Messaging(this);
        this.messaging.init(this.getServer().getConsoleSender());

        PacketEvents.getAPI().getSettings().bStats(true).debug(false).checkForUpdates(false);
        PacketEvents.getAPI().init();
        InventoryFramework.init(r -> this.threadHandler.submitAsync(r::run));

        this.getServer().getServicesManager().register(DBedWarsAPI.class, new APIImpl(this), this, ServicePriority.Highest);

        Properties properties = new Properties();

        this.mainWorld = properties.getProperty("level-name", "world");

        boolean spawnNpc = Boolean.parseBoolean(properties.getProperty("spawn-npcs", "false"));
        int spawnProt = NumberUtils.toInt(properties.getProperty("spawn-protection", "-1"), -1);
        return true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.threadHandler.destroy();
        PacketEvents.getAPI().terminate();
        Arrays.stream(this.listeners).forEach(HandlerList::unregisterAll);
    }

    @Override
    public PluginDependence[] getDependences() {
        return this.hookManager.getDependencies();
    }

    @Override
    protected boolean setUpConfig() {
        this.configHandler = new ConfigHandler(this);
        this.configHandler.initFiles();
        this.configHandler.initMainConfig();
        this.configHandler.initLanguage();

        return true;
    }

    @Override
    protected boolean setUpHandlers() {
        this.gameManager = new GameManagerImpl(this, this.configHandler.getLobbyLocation());
        this.customItemHandler = new CustomItemHandler(this);
        this.setupSessionManager = new SetupSessionManager();
        this.menuHandler = new MenuHandler(this);

        this.threadHandler.submitAsync(() -> {
            this.configHandler.loadConfigurations();
            this.configHandler.loadItems();

            this.registerCustomItems();
            this.initDatabase();
        });

        return true;
    }

    @Override
    protected boolean setUpCommands() {
        this.commandRegistry = new CommandRegistryImpl(this);
        this.commandRegistry.registerPackage("org.zibble.dbedwars.commands");
        return true;
    }

    @Override
    protected boolean setUpListeners() {
        this.listeners = new Listener[]{new VanishListener(), new GameRuleHandler(this)};
        for (Listener listener : this.listeners) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
        return true;
    }

    public Version getServerVersion() {
        return this.serverVersion;
    }

    public String getVersion() {
        return this.getDescription().getVersion();
    }

    public GameManagerImpl getGameManager() {
        return gameManager;
    }

    public String getMainWorld() {
        return this.mainWorld;
    }

    public CustomItemHandler getCustomItemHandler() {
        return this.customItemHandler;
    }

    public ThreadHandler getThreadHandler() {
        return this.threadHandler;
    }

    public ConfigHandler getConfigHandler() {
        return this.configHandler;
    }

    public NMSAdaptor getNMSAdaptor() {
        return this.nmsAdaptor;
    }

    public DatabaseBridge getDatabaseBridge() {
        return this.database;
    }

    public FeatureManager getFeatureManager() {
        return featureManager;
    }

    public HookManagerImpl getHookManager() {
        return hookManager;
    }

    public MenuHandler getMenuHandler() {
        return menuHandler;
    }

    private void initDatabase() {
        DatabaseType type;
        if (this.getConfigHandler().getDatabase().getDatabase() != null)
            type = this.getConfigHandler().getDatabase().getDatabase();
        else type = DatabaseType.SQLite;


        if (type == DatabaseType.MYSQL) {
            this.database = new MySQLBridge(this.getConfigHandler().getDatabase().getMySQL());
        } else if (type == DatabaseType.MongoDB) {
            this.loadLibrary(ExternalLibrary.MONGO_DATABASE);
            this.database = new MongoDBBridge(this.getConfigHandler().getDatabase().getMongoDB());
        } else if (type == DatabaseType.H2) {
            this.loadLibrary(ExternalLibrary.H2_DATABASE);
            this.database = new H2DatabaseBridge();
        } else if (type == DatabaseType.PostGreSQL) {
            this.loadLibrary(ExternalLibrary.POSTGRESQL_DATABASE);
            this.database = new PostGreSqlBridge(this.getConfigHandler().getDatabase().getMySQL());
        } else {
            this.loadLibrary(ExternalLibrary.SQLITE_DATABASE);
            this.database = new SQLiteBridge();
        }

        this.database.init();
    }

    private void loadLibrary(ExternalLibrary library) {
        if (!library.exists()) {
            library.download();
        }
        library.load();
    }

    private NMSAdaptor registerNMSAdaptor() {
        switch (this.serverVersion) {
            case v1_8_R3:
                return new NMSUtils();
            default:
                return null;
        }
    }

    public SetupSessionManager getSetupSessionManager() {
        return setupSessionManager;
    }

    public ScriptRegistryImpl scriptRegistry() {
        return scriptRegistry;
    }

    private boolean checkSWM(org.bukkit.plugin.Plugin plugin) {
        switch (plugin.getDescription().getVersion()) {
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
                Debugger.debug("Could not hook into SlimeWorldManager support! You are running" + " an unsupported version", Level.SEVERE);
                return false;
        }
        return true;
    }

    private void registerCustomItems() {
        this.customItemHandler.registerItem(new FireballItem(this));
        this.customItemHandler.registerItem(new TNTItem(this));
        this.customItemHandler.registerItem(new BridgeEgg(this));
        this.customItemHandler.registerItem(new WaterBucket(this));
        this.customItemHandler.registerItem(new Sponge(this));
        this.customItemHandler.registerItem(new DreamDefenderSpawnEgg(this));
        this.customItemHandler.registerItem(new BedBugSnowball(this));
        this.customItemHandler.registerItem(new PopupTowerChestItem(this));
        this.customItemHandler.registerItem(new BlastProofGlass(this));
    }

    @Override
    public @NotNull ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return new ChunkGenerator() {
            @NotNull
            @Override
            public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull ChunkGenerator.BiomeGrid biome) {
                return this.createChunkData(world);
            }
        };
    }

}
