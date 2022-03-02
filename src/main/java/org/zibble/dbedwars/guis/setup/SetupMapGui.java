package org.zibble.dbedwars.guis.setup;

import com.cryptomorin.xseries.XMaterial;
import com.pepedevs.radium.gui.inventory.Item;
import com.pepedevs.radium.gui.inventory.action.ItemClickAction;
import com.pepedevs.radium.gui.inventory.custom.book.BookItemMenu;
import com.pepedevs.radium.gui.inventory.item.action.ActionItem;
import com.pepedevs.radium.gui.inventory.item.action.ItemAction;
import com.pepedevs.radium.gui.inventory.item.action.ItemActionPriority;
import com.pepedevs.radium.gui.inventory.item.voidaction.VoidActionItem;
import com.pepedevs.radium.gui.inventory.size.ItemMenuSize;
import com.pepedevs.radium.utils.StringUtils;
import com.pepedevs.radium.utils.world.WorldUtils;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.util.SchedulerUtils;
import org.zibble.dbedwars.api.util.gui.IMenu;
import org.zibble.dbedwars.configuration.PluginFiles;

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
            Arrays.fill(items, IMenu.VOID_ITEM);
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
                                                .submitAsync(
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
                                                                    .submitSync(
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
                    this.menu.setBarButton(b, IMenu.BACK);
                    break;
                case 6:
                    this.menu.setBarButton(b, IMenu.PREVIOUS_PAGE);
                    break;
                case 7:
                    this.menu.setBarButton(b, IMenu.NEXT_PAGE);
                    break;
                default:
                    this.menu.setBarButton(b, bar);
            }
        }

        File[] configured = PluginFiles.ARENA_DATA_ARENACACHE.listFiles();
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
                                        .submitAsync(
                                                () -> {
                                                    CompletableFuture<Boolean> future =
                                                            new CompletableFuture<>();
                                                    SetupMapGui.this
                                                            .plugin
                                                            .getThreadHandler()
                                                            .submitAsync(
                                                                    () -> {
                                                                        World world =
                                                                                SetupMapGui.this
                                                                                        .plugin
                                                                                        .getServer()
                                                                                        .getWorld(
                                                                                                file
                                                                                                        .getName());
                                                                        SchedulerUtils.runTask(
                                                                                new Runnable() {
                                                                                    @Override
                                                                                    public void
                                                                                            run() {
                                                                                        if (world
                                                                                                != null) {
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
                                                                                            SetupMapGui
                                                                                                    .this
                                                                                                    .plugin
                                                                                                    .getGeneratorHandler()
                                                                                                    .getWorldAdaptor()
                                                                                                    .unloadWorld(
                                                                                                            file
                                                                                                                    .getName(),
                                                                                                            true);
                                                                                            future
                                                                                                    .complete(
                                                                                                            true);
                                                                                        }
                                                                                    }
                                                                                });
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
                                        .submitSync(
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
