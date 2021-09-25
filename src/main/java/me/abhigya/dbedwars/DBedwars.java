package me.abhigya.dbedwars;

import me.Abhigya.core.commands.CommandHandler;
import me.Abhigya.core.placeholder.PlaceholderUtil;
import me.Abhigya.core.plugin.Plugin;
import me.Abhigya.core.plugin.PluginAdapter;
import me.Abhigya.core.plugin.PluginDependence;
import me.Abhigya.core.util.ServerPropertiesUtils;
import me.Abhigya.core.util.console.ConsoleUtils;
import me.Abhigya.core.util.hologram.HologramFactory;
import me.Abhigya.core.util.npc.NPCPool;
import me.Abhigya.core.util.server.Version;
import me.Abhigya.core.version.CoreVersion;
import me.abhigya.dbedwars.addon.AddonManager;
import me.abhigya.dbedwars.commands.Setup;
import me.abhigya.dbedwars.commands.Start;
import me.abhigya.dbedwars.configuration.Lang;
import me.abhigya.dbedwars.configuration.MainConfiguration;
import me.abhigya.dbedwars.configuration.PluginFiles;
import me.abhigya.dbedwars.game.GameManager;
import me.abhigya.dbedwars.handler.ConfigHandler;
import me.abhigya.dbedwars.handler.GuiHandler;
import me.abhigya.dbedwars.handler.ThreadHandler;
import me.abhigya.dbedwars.handler.WorldHandler;
import me.abhigya.dbedwars.item.ChestListener;
import me.abhigya.dbedwars.nms.NMSAdaptor;
import me.abhigya.dbedwars.nms.v1_8_R3.NMSUtils;
import me.abhigya.dbedwars.utils.PluginFileUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class DBedwars extends PluginAdapter {

    private String alias;
    private Version serverVersion;
    private AddonManager addonManager;
    private GameManager gameManager;
    private List<Listener> listeners;
    private MainConfiguration mainConfiguration;

    private String mainWorld;

    private WorldHandler worldHandler;
    private GuiHandler guiHandler;
    private ThreadHandler threadHandler;
    private ConfigHandler configHandler;
    private NPCPool npcHandler;
    private HologramFactory hologramFactory;
    
    private NMSAdaptor nmsAdaptor;

    @Override
    public void onLoad() {
        if (!PluginFiles.ADDON.getFile().isDirectory())
            PluginFiles.ADDON.getFile().mkdirs();

        if (!PluginFiles.LANGUAGES.getFile().isDirectory())
            PluginFiles.LANGUAGES.getFile().mkdirs();

        for (PluginFiles files : PluginFiles.getLanguageFiles()) {
            if (!files.getFile().exists()) {
                this.saveResource("languages/" + files.getFile().getName(), PluginFiles.LANGUAGES.getFile(), false);
            }
        }

        Lang.setLangFile(PluginFiles.ENGLISH.getFile());

        this.alias = Lang.PREFIX.toString();
        this.addonManager = new AddonManager(this);
        this.addonManager.loadAddon();
    }

    @Override
    protected boolean setUp() {
        this.serverVersion = Version.getServerVersion();
        this.nmsAdaptor = this.registerNMSAdaptor();
        this.addonManager.enableAddon();
        this.hologramFactory = new HologramFactory(this);

        this.mainWorld = ServerPropertiesUtils.getStringProperty("level-name", "world");

        boolean spawnNpc = ServerPropertiesUtils.getBooleanProperty("spawn-npcs", false);
        int spawnProt = ServerPropertiesUtils.getIntProperty("spawn-protection", -1);
        return true;
    }

    @Override
    public void onDisable() {
//        this.addonManager.disableAddon();
//        this.threadHandler.destroyAllThreads();
    }

    @Override
    public CoreVersion getRequiredCoreVersion() {
        return CoreVersion.v1_2_1;
    }

    /**
     * <ul>
     *     <li>PluginDependence[0] = MultiVerseCore</li>
     *     <li>PluginDependence[1] = SlimeWorldManager</li>
     * </ul>
     */
    @Override
    public PluginDependence[] getDependences() {
        if (!PluginFiles.HOOKS.getFile().isDirectory()) {
            if (!PluginFiles.HOOKS.getFile().mkdirs()) {
                ConsoleUtils.sendPluginMessage(ChatColor.RED + Lang.ERROR_WRITE_FILES.toString(), this.getAlias());
            }
        }
        PlaceholderUtil.tryHook(this);
        return new PluginDependence[]{
                new PluginDependence("MultiVerse-Core") {

                    private final File file = PluginFiles.MULTIVERSE_CORE_HOOK.getFile();

                    @Override
                    public Boolean apply(org.bukkit.plugin.Plugin plugin) {
                        if (plugin != null) {
                            ConsoleUtils.sendPluginMessage(Lang.HOOK_FOUND.toString().replace("{hook}", this.getName()), DBedwars.this.getAlias());
                            DBedwars.this.saveResource("hooks/" + this.file.getName(), this.file.getParentFile(), false);
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

                    private final File file = PluginFiles.SLIME_WORLD_MANAGER_HOOK.getFile();

                    @Override
                    public Boolean apply(org.bukkit.plugin.Plugin plugin) {
                        if (plugin != null && DBedwars.this.checkSWM(plugin)) {
                            ConsoleUtils.sendPluginMessage(Lang.HOOK_FOUND.toString().replace("{hook}", this.getName()), DBedwars.this.getAlias());
                            DBedwars.this.saveResource("hooks/" + this.file.getName(), this.file.getParentFile(), false);
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
        for (PluginFiles folder : PluginFiles.getDirectories()) {
            if (!folder.getFile().isDirectory())
                if (!folder.getFile().mkdirs()) {
                    ConsoleUtils.sendPluginMessage(Lang.ERROR_WRITE_FILES.toString(), this.getAlias());
                    return false;
                }
        }

        for (PluginFiles file : PluginFiles.getFiles()) {
            String path = "";
            File parent = file.getFile().getParentFile();
            while (!parent.getName().equals(PluginFiles.PLUGIN_DATA_FOLDER.getFile().getName())) {
                path = parent.getName() + "/" + path;
                parent = parent.getParentFile();
            }
            this.saveResource(path + file.getFile().getName(), file.getFile().getParentFile(), false);
        }

        this.mainConfiguration = new MainConfiguration(this);
        this.mainConfiguration.load(YamlConfiguration.loadConfiguration(PluginFiles.CONFIG.getFile()));

        return true;
    }

    @Override
    protected boolean setUpHandlers() {
        this.worldHandler = new WorldHandler(this);

        this.threadHandler = new ThreadHandler(this, 4, 3);
        this.threadHandler.runThreads();

        this.threadHandler.addAsyncWork(() -> {
            this.gameManager = new GameManager(this);

            this.configHandler = new ConfigHandler(this);
            this.configHandler.loadConfigurations();

            this.guiHandler = new GuiHandler(this);
            this.guiHandler.loadMenus();
            this.guiHandler.loadAnvilMenus();
            this.guiHandler.loadItems();
            this.npcHandler = NPCPool.createDefault(this);
        });

        return true;
    }

    @Override
    protected boolean setUpCommands() {
        new CommandHandler(this, "bedwars", new Setup(this), new Start(this));

        return true;
    }

    @Override
    protected boolean setUpListeners() {
        this.listeners = new ArrayList<>();
        this.listeners.add(new ChestListener());
        this.listeners.forEach(l -> this.getServer().getPluginManager().registerEvents(l, this));
        return true;
    }

    public static DBedwars getInstance() {
        return Plugin.getPlugin(DBedwars.class);
    }

    public Version getServerVersion() {
        return this.serverVersion;
    }

    public String getAlias() {
        return this.alias;
    }

    public String getVersion() {
        return this.getDescription().getVersion();
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public MainConfiguration getMainConfiguration() {
        return mainConfiguration;
    }

    public String getMainWorld() {
        return this.mainWorld;
    }

    public WorldHandler getGeneratorHandler() {
        return this.worldHandler;
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
        return nmsAdaptor;
    }

    public NPCPool getNpcHandler() {
        return npcHandler;
    }

    public HologramFactory getHologramFactory() {
        return hologramFactory;
    }

    private NMSAdaptor registerNMSAdaptor() {
        switch (this.serverVersion) {
            case v1_8_R3:
                return new NMSUtils();
            default:
                return null;
        }
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
                ConsoleUtils.sendPluginMessage(ChatColor.DARK_RED + "Could not hook into SlimeWorldManager support! You are running an unsupported version", this.getAlias());
                return false;
        }
        return true;
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
