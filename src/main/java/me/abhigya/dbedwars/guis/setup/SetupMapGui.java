package me.abhigya.dbedwars.guis.setup;

import me.Abhigya.core.menu.inventory.Item;
import me.Abhigya.core.menu.inventory.action.ItemClickAction;
import me.Abhigya.core.menu.inventory.custom.book.BookItemMenu;
import me.Abhigya.core.menu.inventory.item.action.ActionItem;
import me.Abhigya.core.menu.inventory.item.action.ItemAction;
import me.Abhigya.core.menu.inventory.item.action.ItemActionPriority;
import me.Abhigya.core.menu.inventory.item.voidaction.VoidActionItem;
import me.Abhigya.core.menu.inventory.size.ItemMenuSize;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.world.WorldUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.util.gui.IMenu;
import me.abhigya.dbedwars.configuration.PluginFiles;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("unchecked")
public class SetupMapGui extends IMenu<BookItemMenu> {

    private final DBedwars plugin;

    private final VoidActionItem noMapItem =
            new VoidActionItem(
                    StringUtils.translateAlternateColorCodes("&cNo map found :("),
                    XMaterial.PAPER.parseItem(),
                    StringUtils.translateAlternateColorCodes(
                            new String[] {"&cAdd some arena world to", "&cthe root directory!"}));

    public SetupMapGui(DBedwars plugin) {
        super(
                "MAP_SETUP",
                new BookItemMenu(
                        StringUtils.translateAlternateColorCodes("&3Select map to setup!"),
                        ItemMenuSize.SIX_LINE,
                        ItemMenuSize.ONE_LINE,
                        null));
        this.plugin = plugin;
    }

    @Override
    public void setUpMenu(
            Player player, @Nullable ItemClickAction action, @Nullable Map<String, Object> info) {
        ArrayList<File> worldDirs = new ArrayList<>();
        Arena arena = (Arena) info.get("arena");
        String mainWorld = SetupMapGui.this.plugin.getMainWorld();
        this.menu.setTitle(
                StringUtils.translateAlternateColorCodes(
                        "&eSelect map to setup! &7(" + arena.getSettings().getName() + ")"));
        if (action != null) this.menu.setParent(action.getMenu());

        for (File f : new File("./").listFiles()) {
            if (f.isDirectory() && WorldUtils.worldFolderCheck(f)) {
                if (f.getName().equals(mainWorld)
                        || f.getName().equals(mainWorld + "_nether")
                        || f.getName().equals(mainWorld + "_the_end")) continue;
                worldDirs.add(f);
            }
        }

        menu.clear();
        Item[] items = new Item[45];
        VoidActionItem bar;
        if (worldDirs.isEmpty()) {
            Arrays.fill(items, VOID_ITEM);
            items[22] = noMapItem;
            bar =
                    new VoidActionItem(
                            StringUtils.translateAlternateColorCodes("&7"),
                            XMaterial.RED_STAINED_GLASS_PANE.parseItem());
            for (byte b = 0; b < 9; b++) {
                this.menu.setBarButton(b, bar);
            }
            this.menu.addItems(items);
            return;
        }

        bar =
                new VoidActionItem(
                        StringUtils.translateAlternateColorCodes("&7"),
                        XMaterial.WHITE_STAINED_GLASS_PANE.parseItem());
        for (byte b = 0; b < 9; b++) {
            switch (b) {
                case 2:
                    if (info.get("type").equals("final")
                            && SetupMapGui.this
                                    .plugin
                                    .getGeneratorHandler()
                                    .getWorldAdaptor()
                                    .saveExist(arena.getSettings().getName())) {
                        ActionItem world =
                                new ActionItem(
                                        StringUtils.translateAlternateColorCodes(
                                                "&eGenerate arena world!"),
                                        XMaterial.OAK_TRAPDOOR.parseItem());
                        world.addAction(
                                new ItemAction() {
                                    @Override
                                    public ItemActionPriority getPriority() {
                                        return ItemActionPriority.NORMAL;
                                    }

                                    @Override
                                    public void onClick(ItemClickAction itemClickAction) {
                                        SetupMapGui.this
                                                .plugin
                                                .getThreadHandler()
                                                .addAsyncWork(
                                                        () -> {
                                                            if (arena.isEnabled()) {
                                                                itemClickAction
                                                                        .getPlayer()
                                                                        .sendMessage(
                                                                                StringUtils
                                                                                        .translateAlternateColorCodes(
                                                                                                "&cPlease"
                                                                                                    + " disable"
                                                                                                    + " the arena"
                                                                                                    + " to make"
                                                                                                    + " changes"
                                                                                                    + " in the"
                                                                                                    + " world"
                                                                                                    + " or load"
                                                                                                    + " world!"));
                                                                return;
                                                            }
                                                            if (arena.getWorld() == null) {
                                                                World world = arena.loadWorld();
                                                                arena.setWorld(world);
                                                            }
                                                            SetupMapGui.this
                                                                    .plugin
                                                                    .getThreadHandler()
                                                                    .addSyncWork(
                                                                            () -> {
                                                                                if (arena.getSettings()
                                                                                        .hasLobby()) {
                                                                                    action.getPlayer()
                                                                                            .teleport(
                                                                                                    arena.getSettings()
                                                                                                            .getLobby()
                                                                                                            .toBukkit(
                                                                                                                    arena
                                                                                                                            .getWorld()));
                                                                                } else {
                                                                                    action.getPlayer()
                                                                                            .teleport(
                                                                                                    arena.getWorld()
                                                                                                            .getSpawnLocation());
                                                                                }
                                                                            });
                                                        });
                                    }
                                });
                        this.menu.setBarButton(b, world);
                    }
                    break;
                case 4:
                    this.menu.setBarButton(b, BACK);
                    break;
                case 6:
                    this.menu.setBarButton(b, PREVIOUS_PAGE);
                    break;
                case 7:
                    this.menu.setBarButton(b, NEXT_PAGE);
                    break;
                default:
                    this.menu.setBarButton(b, bar);
            }
        }

        File[] configured = PluginFiles.ARENA_DATA_ARENACACHE.getFile().listFiles();
        for (File file : worldDirs) {
            ActionItem item;
            if (Arrays.stream(configured).anyMatch(c -> c.getName().contains(file.getName()))) {
                item =
                        new ActionItem(
                                StringUtils.translateAlternateColorCodes("&c" + file.getName()),
                                XMaterial.RED_WOOL.parseItem());
            } else {
                item =
                        new ActionItem(
                                StringUtils.translateAlternateColorCodes("&a" + file.getName()),
                                XMaterial.GREEN_WOOL.parseItem());
            }

            item.addAction(
                    new ItemAction() {
                        @Override
                        public ItemActionPriority getPriority() {
                            return ItemActionPriority.NORMAL;
                        }

                        @Override
                        public void onClick(ItemClickAction action) {
                            if (info.get("type").equals("initial")) {

                                SetupMapGui.this
                                        .plugin
                                        .getThreadHandler()
                                        .addAsyncWork(
                                                () -> {
                                                    CompletableFuture<Boolean> future =
                                                            new CompletableFuture<>();
                                                    SetupMapGui.this
                                                            .plugin
                                                            .getThreadHandler()
                                                            .getLeastWorkSyncWorker()
                                                            .add(
                                                                    () -> {
                                                                        World world =
                                                                                SetupMapGui.this
                                                                                        .plugin
                                                                                        .getServer()
                                                                                        .getWorld(
                                                                                                file
                                                                                                        .getName());
                                                                        if (world != null) {
                                                                            world.getPlayers()
                                                                                    .forEach(
                                                                                            p ->
                                                                                                    p
                                                                                                            .teleport(
                                                                                                                    SetupMapGui
                                                                                                                            .this
                                                                                                                            .plugin
                                                                                                                            .getServer()
                                                                                                                            .getWorld(
                                                                                                                                    mainWorld)
                                                                                                                            .getSpawnLocation()));
                                                                            SetupMapGui.this
                                                                                    .plugin
                                                                                    .getGeneratorHandler()
                                                                                    .getWorldAdaptor()
                                                                                    .unloadWorld(
                                                                                            file
                                                                                                    .getName(),
                                                                                            true);
                                                                            future.complete(true);
                                                                        }
                                                                    });

                                                    try {
                                                        future.get();
                                                    } catch (InterruptedException
                                                            | ExecutionException ignored) {
                                                    }
                                                    arena.saveWorld(file.getName(), true);
                                                    action.getPlayer()
                                                            .sendMessage(
                                                                    StringUtils
                                                                            .translateAlternateColorCodes(
                                                                                    "&aWorld set"
                                                                                        + " successfully!"));
                                                });

                                Map<String, Object> info = new HashMap<>();
                                info.put("arena", arena.getSettings().getName());
                                SetupMapGui.this
                                        .plugin
                                        .getGuiHandler()
                                        .getGui("TYPE_SETUP")
                                        .open(null, info, action.getPlayer());
                            } else {
                                SetupMapGui.this
                                        .plugin
                                        .getThreadHandler()
                                        .getLeastWorkSyncWorker()
                                        .add(
                                                () -> {
                                                    World world =
                                                            SetupMapGui.this
                                                                    .plugin
                                                                    .getGeneratorHandler()
                                                                    .getWorldAdaptor()
                                                                    .loadWorldFromFolder(
                                                                            file.getName());
                                                    action.getPlayer()
                                                            .teleport(world.getSpawnLocation());
                                                });
                            }
                        }
                    });

            this.menu.addItem(item);
        }
    }
}
