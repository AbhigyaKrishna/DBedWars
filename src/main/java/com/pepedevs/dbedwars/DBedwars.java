package com.pepedevs.dbedwars;

import co.aikar.commands.PaperCommandManager;
import com.github.retrooper.packetevents.PacketEvents;
import com.pepedevs.dbedwars.action.TranslationRegistryImpl;
import com.pepedevs.dbedwars.api.action.ActionTranslationRegistry;
import com.pepedevs.dbedwars.api.plugin.Plugin;
import com.pepedevs.dbedwars.api.plugin.PluginAdapter;
import com.pepedevs.dbedwars.api.plugin.PluginDependence;
import com.pepedevs.radium.database.DatabaseType;
import com.pepedevs.radium.holograms.HologramManager;
import com.pepedevs.radium.placeholders.PlaceholderUtil;
import com.pepedevs.radium.utils.ServerPropertiesUtils;
import com.pepedevs.dbedwars.api.version.Version;
import com.pepedevs.dbedwars.api.DBedWarsAPI;
import com.pepedevs.dbedwars.api.nms.NMSAdaptor;
import com.pepedevs.dbedwars.commands.BedwarsCommand;
import com.pepedevs.dbedwars.configuration.PluginFiles;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableDatabase;
import com.pepedevs.dbedwars.database.DatabaseBridge;
import com.pepedevs.dbedwars.database.MongoDB;
import com.pepedevs.dbedwars.database.MySQL;
import com.pepedevs.dbedwars.database.SQLite;
import com.pepedevs.dbedwars.game.GameManager;
import com.pepedevs.dbedwars.handler.*;
import com.pepedevs.dbedwars.item.*;
import com.pepedevs.dbedwars.messaging.Messaging;
import com.pepedevs.dbedwars.nms.v1_8_R3.NMSUtils;
import com.pepedevs.dbedwars.utils.ConfigurationUtils;
import com.pepedevs.dbedwars.utils.Debugger;
import com.pepedevs.dbedwars.utils.PluginFileUtils;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Random;
import java.util.logging.Level;

public final class DBedwars extends PluginAdapter {

    private Version serverVersion;
    private GameManager gameManager;
    private Listener[] listeners;

    private String mainWorld;

    private FeatureManager featureManager;
    private ConfigHandler configHandler;
    private WorldHandler worldHandler;
    private GuiHandler guiHandler;
    private CustomItemHandler customItemHandler;
    private ThreadHandler threadHandler;
    private HologramManager hologramManager;

    private Messaging messaging;
    private ActionTranslationRegistry actionRegistry;
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
        this.nmsAdaptor = this.registerNMSAdaptor();
        this.featureManager = new FeatureManager(this);
        this.featureManager.registerDefaults();
        this.actionRegistry = new TranslationRegistryImpl();

        PacketEvents.getAPI().getSettings().bStats(true).debug(false).checkForUpdates(false);
        PacketEvents.getAPI().init();

        this.getServer()
                .getServicesManager()
                .register(DBedWarsAPI.class, new APIImpl(this), this, ServicePriority.Normal);

        this.mainWorld = ServerPropertiesUtils.getStringProperty("level-name", "world");

        boolean spawnNpc = ServerPropertiesUtils.getBooleanProperty("spawn-npcs", false);
        int spawnProt = ServerPropertiesUtils.getIntProperty("spawn-protection", -1);

        PlaceholderUtil.tryHook(this);
        return true;
    }

    @Override
    public void onDisable() {
        this.threadHandler.destroy();
    }

    /**
     * <ul>
     *   <li>PluginDependence[0] = MultiVerseCore
     *   <li>PluginDependence[1] = SlimeWorldManager
     * </ul>
     */
    @Override
    public PluginDependence[] getDependences() {
        return new PluginDependence[] {
            new PluginDependence("MultiVerse-Core") {

                private final File file = PluginFiles.MULTIVERSE_CORE_HOOK;

                @Override
                public Boolean apply(org.bukkit.plugin.Plugin plugin) {
                    if (plugin != null) {
                        DBedwars.this
                                .getLogger()
                                .info("MultiVerse-Core found, enabling MultiVerse-Core support.");
                        DBedwars.this.saveResource(
                                "hooks/" + this.file.getName(), this.file.getParentFile(), false);
                        PluginFileUtils.set(this.file, "enabled", true);
                    } else {
                        if (this.file.exists()) {
                            PluginFileUtils.set(this.file, "enabled", false);
                        }
                    }
                    return true;
                }
            },
            new PluginDependence("SlimeWorldManager") {

                private final File file = PluginFiles.SLIME_WORLD_MANAGER_HOOK;

                @Override
                public Boolean apply(org.bukkit.plugin.Plugin plugin) {
                    if (plugin != null && DBedwars.this.checkSWM(plugin)) {
                        DBedwars.this
                                .getLogger()
                                .info("SlimeWorldManager found, enabling SlimeWorldManager support.");
                        DBedwars.this.saveResource(
                                "hooks/" + this.file.getName(), this.file.getParentFile(), false);
                        PluginFileUtils.set(this.file, "enabled", true);
                    } else {
                        if (this.file.exists()) {
                            PluginFileUtils.set(this.file, "enabled", false);
                        }
                    }
                    return true;
                }
            }
        };
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
        this.messaging = new Messaging(this);
        this.messaging.init(this.getServer().getConsoleSender());
        this.threadHandler = new ThreadHandler(this);
        this.threadHandler.runThreads(4);
        this.worldHandler = new WorldHandler(this);
        this.gameManager = new GameManager(this);
        this.guiHandler = new GuiHandler(this);
        this.customItemHandler = new CustomItemHandler(this);
        this.hologramManager = new HologramManager(this, this.threadHandler.getUpdaterTask());

        this.threadHandler.submitAsync(
                () -> {
                    this.configHandler.loadConfigurations();

                    this.guiHandler.loadMenus();
                    this.guiHandler.loadAnvilMenus();

                    this.registerCustomItems();
                    this.initDatabase();
                });

        return true;
    }

    @Override
    protected boolean setUpCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.enableUnstableAPI("help");
        manager.registerCommand(new BedwarsCommand(this));

        return true;
    }

    @Override
    protected boolean setUpListeners() {
        this.listeners = new Listener[0];
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

    public GameManager getGameManager() {
        return gameManager;
    }

    public String getMainWorld() {
        return this.mainWorld;
    }

    public WorldHandler getGeneratorHandler() {
        return this.worldHandler;
    }

    public CustomItemHandler getCustomItemHandler() {
        return this.customItemHandler;
    }

    public GuiHandler getGuiHandler() {
        return this.guiHandler;
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

    public HologramManager getHologramManager() {
        return this.hologramManager;
    }

    public FeatureManager getFeatureManager() {
        return featureManager;
    }

    private void initDatabase() {
        DatabaseType type;
        if (this.getConfigHandler().getDatabase().isValid())
            type =
                    ConfigurationUtils.matchEnum(
                            this.getConfigHandler().getDatabase().getDatabase(),
                            DatabaseType.values());
        else type = DatabaseType.SQLite;

        if (type == DatabaseType.MYSQL) {
            ConfigurableDatabase.ConfigurableMySQL cfg =
                    this.getConfigHandler().getDatabase().getMySQL();
            this.database =
                    new MySQL(
                            this,
                            cfg.getHost(),
                            cfg.getPort(),
                            cfg.getDatabaseName(),
                            cfg.getUsername(),
                            cfg.getPassword(),
                            cfg.isReconnect(),
                            cfg.isSsl());
        } else if (type == DatabaseType.MongoDB) {
            ConfigurableDatabase.ConfigurableMongoDB cfg =
                    this.getConfigHandler().getDatabase().getMongoDB();
            this.database = new MongoDB(this, cfg.getHost(), cfg.getPort(), cfg.getDatabaseName());
        } else {
            this.database = new SQLite(this);
        }

        this.database.init();
    }

    private NMSAdaptor registerNMSAdaptor() {
        switch (this.serverVersion) {
            case v1_8_R3:
                return new NMSUtils();
            default:
                return null;
        }
    }

    public ActionTranslationRegistry actionRegistry() {
        return actionRegistry;
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
