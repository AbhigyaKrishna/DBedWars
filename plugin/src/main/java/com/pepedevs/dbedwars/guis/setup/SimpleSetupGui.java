package com.pepedevs.dbedwars.guis.setup;

import com.pepedevs.corelib.menu.inventory.ItemMenu;
import com.pepedevs.corelib.menu.inventory.action.ItemClickAction;
import com.pepedevs.corelib.menu.inventory.item.action.ActionItem;
import com.pepedevs.corelib.menu.inventory.item.action.ItemAction;
import com.pepedevs.corelib.menu.inventory.item.action.ItemActionPriority;
import com.pepedevs.corelib.menu.inventory.size.ItemMenuSize;
import com.pepedevs.corelib.utils.StringUtils;
import com.pepedevs.corelib.utils.itemstack.stainedglass.StainedGlassItemStack;
import com.pepedevs.corelib.utils.itemstack.wool.WoolItemStack;
import com.pepedevs.corelib.utils.xseries.XMaterial;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.util.Color;
import com.pepedevs.dbedwars.api.util.LocationXYZYP;
import com.pepedevs.dbedwars.api.util.gui.IMenu;
import com.pepedevs.dbedwars.item.InnerCustomItem;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class SimpleSetupGui extends IMenu<ItemMenu> {

    private final DBedwars plugin;

    public SimpleSetupGui(DBedwars plugin) {
        super("SIMPLE_SETUP", new ItemMenu("Basic Setup", ItemMenuSize.SIX_LINE, null));
        this.plugin = plugin;
    }

    @Override
    protected void setUpMenu(
            Player player, @Nullable ItemClickAction action, @Nullable Map<String, Object> info) {
        this.menu.fillToAll(VOID_ITEM);
        Arena arena = (Arena) info.get("arena");
        this.menu.setTitle(
                StringUtils.translateAlternateColorCodes(
                        "&eBasic Setup &7(" + arena.getSettings().getName() + ")"));
        if (action != null) {
            this.menu.setParent(action.getMenu());
        }
        if (info.get("page").equals(1)) {
            this.addPageChangeButtons((byte) 1, player, arena);
            this.renderPageOne(arena);
            this.addPageInfo((byte) 1);
        } else if (info.get("page").equals(2)) {
            this.addPageChangeButtons((byte) 2, player, arena);
            this.renderPageTwo(arena);
            this.addPageInfo((byte) 2);
        }

        ActionItem save =
                new ActionItem(
                        StringUtils.translateAlternateColorCodes("&bSave settings"),
                        XMaterial.WRITABLE_BOOK.parseItem(),
                        StringUtils.translateAlternateColorCodes("&eSave current configurations!"));
        save.addAction(
                new ItemAction() {
                    @Override
                    public ItemActionPriority getPriority() {
                        return ItemActionPriority.NORMAL;
                    }

                    @Override
                    public void onClick(ItemClickAction itemClickAction) {
                        arena.saveData(true);

                        itemClickAction.setClose(true);
                        itemClickAction
                                .getPlayer()
                                .getInventory()
                                .remove(InnerCustomItem.SIMPLE_SETUP_ITEM.getItem().toItemStack());
                        itemClickAction
                                .getPlayer()
                                .sendMessage(
                                        StringUtils.translateAlternateColorCodes("&aArena Saved!"));
                    }
                });
        this.menu.setItem(8, save);

        this.menu.update(player);
    }

    private void renderPageOne(Arena arena) {
        ActionItem lobby =
                new ActionItem(
                        StringUtils.translateAlternateColorCodes("&aSet Lobby"),
                        XMaterial.DARK_OAK_DOOR.parseItem(),
                        StringUtils.translateAlternateColorCodes(
                                Arrays.asList(
                                        "&eClick to set lobby location at your current location.",
                                        "&eRight click to teleport to lobby location.")));
        this.menu.setItem(20, lobby);

        lobby.addAction(
                new ItemAction() {
                    @Override
                    public ItemActionPriority getPriority() {
                        return ItemActionPriority.NORMAL;
                    }

                    @Override
                    public void onClick(ItemClickAction itemClickAction) {
                        if (itemClickAction.getClickType().equals(ClickType.LEFT)) {
                            arena.getSettings()
                                    .setLobby(
                                            LocationXYZYP.valueOf(
                                                    itemClickAction.getPlayer().getLocation()));
                            arena.getSettings()
                                    .setSpectatorLocation(
                                            LocationXYZYP.valueOf(
                                                    itemClickAction.getPlayer().getLocation()));
                            itemClickAction
                                    .getPlayer()
                                    .sendMessage(
                                            StringUtils.translateAlternateColorCodes(
                                                    "&aLobby location set!"));
                        } else if (itemClickAction.getClickType().equals(ClickType.RIGHT)) {
                            if (arena.getSettings().hasLobby()) {
                                if (SimpleSetupGui.this
                                                .plugin
                                                .getServer()
                                                .getWorld(arena.getSettings().getName())
                                        == null) {
                                    if (SimpleSetupGui.this
                                            .plugin
                                            .getGeneratorHandler()
                                            .getWorldAdaptor()
                                            .saveExist(arena.getSettings().getName())) {
                                        SimpleSetupGui.this
                                                .plugin
                                                .getThreadHandler()
                                                .getLeastWorkAsyncWorker()
                                                .add(
                                                        () -> {
                                                            World world = arena.loadWorld();
                                                            arena.setWorld(world);
                                                            SimpleSetupGui.this
                                                                    .plugin
                                                                    .getThreadHandler()
                                                                    .submitSync(
                                                                            () ->
                                                                                    itemClickAction
                                                                                            .getPlayer()
                                                                                            .teleport(
                                                                                                    arena.getSettings()
                                                                                                            .getLobby()
                                                                                                            .toBukkit(
                                                                                                                    world)));
                                                        });
                                    } else {
                                        itemClickAction
                                                .getPlayer()
                                                .sendMessage(
                                                        StringUtils.translateAlternateColorCodes(
                                                                "&cArena world not set. Teleporting"
                                                                        + " to location in current"
                                                                        + " world!"));
                                        itemClickAction
                                                .getPlayer()
                                                .teleport(
                                                        arena.getSettings()
                                                                .getLobby()
                                                                .toBukkit(
                                                                        itemClickAction
                                                                                .getPlayer()
                                                                                .getWorld()));
                                    }
                                }
                            } else {
                                itemClickAction
                                        .getPlayer()
                                        .sendMessage(
                                                StringUtils.translateAlternateColorCodes(
                                                        "&cArena lobby location not set!"));
                            }
                        }
                    }
                });
        ActionItem spawner =
                new ActionItem(
                        StringUtils.translateAlternateColorCodes("&aSet Item Spawners"),
                        XMaterial.GOLD_INGOT.parseItem(),
                        StringUtils.translateAlternateColorCodes(
                                "&eClick to view options for configuring spawners."));
        spawner.addAction(
                new ItemAction() {
                    @Override
                    public ItemActionPriority getPriority() {
                        return ItemActionPriority.NORMAL;
                    }

                    @Override
                    public void onClick(ItemClickAction itemClickAction) {
                        Map<String, Object> info = new HashMap<>();
                        info.put("arena", arena);
                        info.put("type", "simple");
                        info.put("team-spawner", false);
                        SimpleSetupGui.this
                                .plugin
                                .getGuiHandler()
                                .getGui("SPAWNER_SETUP")
                                .open(itemClickAction, info, itemClickAction.getPlayer());
                    }
                });

        this.menu.setItem(22, spawner);

        ActionItem maxInTeam =
                new ActionItem(
                        StringUtils.translateAlternateColorCodes("&aTeam Size"),
                        XMaterial.PAPER.parseItem(),
                        StringUtils.translateAlternateColorCodes(
                                "&eNumber of players in the team."));
        maxInTeam.addAction(
                new ItemAction() {
                    @Override
                    public ItemActionPriority getPriority() {
                        return ItemActionPriority.NORMAL;
                    }

                    @Override
                    public void onClick(ItemClickAction itemClickAction) {
                        SimpleSetupGui.this
                                .plugin
                                .getGuiHandler()
                                .getGui("TEAM_SIZE_SETUP")
                                .open(
                                        itemClickAction,
                                        Collections.singletonMap("arena", arena),
                                        itemClickAction.getPlayer());
                    }
                });
        this.menu.setItem(24, maxInTeam);

        ActionItem world =
                new ActionItem(
                        StringUtils.translateAlternateColorCodes("&aSet world"),
                        XMaterial.GRASS_BLOCK.parseItem(),
                        StringUtils.translateAlternateColorCodes(
                                new String[] {
                                    "&eClick to set arena world to current world.", "&eAnd save it"
                                }));
        world.addAction(
                new ItemAction() {
                    @Override
                    public ItemActionPriority getPriority() {
                        return ItemActionPriority.NORMAL;
                    }

                    @Override
                    public void onClick(ItemClickAction itemClickAction) {
                        String world = itemClickAction.getPlayer().getWorld().getName();
                        if (world.equals(SimpleSetupGui.this.plugin.getMainWorld())
                                || world.equals(
                                        SimpleSetupGui.this.plugin.getMainWorld() + "_nether")
                                || world.equals(
                                        SimpleSetupGui.this.plugin.getMainWorld() + "_the_end")) {
                            itemClickAction
                                    .getPlayer()
                                    .sendMessage(
                                            StringUtils.translateAlternateColorCodes(
                                                    "&cCannot set main world to arena!"));
                            return;
                        }

                        CompletableFuture<Boolean> future = new CompletableFuture<>();
                        SimpleSetupGui.this
                                .plugin
                                .getThreadHandler()
                                .getLeastWorkSyncWorker()
                                .add(
                                        () -> {
                                            itemClickAction
                                                    .getPlayer()
                                                    .getWorld()
                                                    .getPlayers()
                                                    .forEach(
                                                            p ->
                                                                    p.teleport(
                                                                            SimpleSetupGui.this
                                                                                    .plugin
                                                                                    .getServer()
                                                                                    .getWorld(
                                                                                            SimpleSetupGui
                                                                                                    .this
                                                                                                    .plugin
                                                                                                    .getMainWorld())
                                                                                    .getSpawnLocation()));
                                            SimpleSetupGui.this
                                                    .plugin
                                                    .getGeneratorHandler()
                                                    .getWorldAdaptor()
                                                    .unloadWorld(world, true);
                                            future.complete(true);
                                        });
                        SimpleSetupGui.this
                                .plugin
                                .getThreadHandler()
                                .getLeastWorkAsyncWorker()
                                .add(
                                        () -> {
                                            try {
                                                future.get();
                                            } catch (InterruptedException
                                                    | ExecutionException ignored) {
                                            }
                                            if (arena.getWorld() != null)
                                                SimpleSetupGui.this
                                                        .plugin
                                                        .getGeneratorHandler()
                                                        .getWorldAdaptor()
                                                        .unloadWorld(
                                                                arena.getWorld().getName(), false);
                                            arena.saveWorld(world, true);
                                        });
                    }
                });

        this.menu.setItem(30, world);

        ActionItem generate =
                new ActionItem(
                        StringUtils.translateAlternateColorCodes("&aGenerate World"),
                        XMaterial.ANVIL.parseItem(),
                        StringUtils.translateAlternateColorCodes(
                                "&eGenerate world from directory."));
        generate.addAction(
                new ItemAction() {
                    @Override
                    public ItemActionPriority getPriority() {
                        return ItemActionPriority.NORMAL;
                    }

                    @Override
                    public void onClick(ItemClickAction itemClickAction) {
                        Map<String, Object> info = new HashMap<>();
                        info.put("arena", arena);
                        info.put("type", "final");
                        SimpleSetupGui.this
                                .plugin
                                .getGuiHandler()
                                .getGui("MAP_SETUP")
                                .open(itemClickAction, info, itemClickAction.getPlayer());
                    }
                });
        this.menu.setItem(32, generate);
    }

    private void renderPageTwo(Arena arena) {
        byte[] index = new byte[] {10, 12, 14, 16, 19, 21, 23, 25, 28, 30, 32, 34, 37, 39, 41, 43};
        Color[] colors = Color.values();
        Set<Team> teams = arena.getSettings().getAvailableTeams();
        List<Color> enabledColor = teams.stream().map(Team::getColor).collect(Collectors.toList());

        for (byte i = 0; i < index.length; i++) {
            ActionItem color;
            byte finalI = i;
            if (enabledColor.contains(colors[i])) {
                color =
                        new ActionItem(
                                colors[i].getChatColor() + "Team " + colors[i].name(),
                                new WoolItemStack(colors[i].getWoolColor()),
                                StringUtils.translateAlternateColorCodes(
                                        new String[] {
                                            "&eLeft click to open team settings.",
                                            "&cRight click to disable team."
                                        }));
                byte finalI1 = i;
                color.addAction(
                        new ItemAction() {
                            @Override
                            public ItemActionPriority getPriority() {
                                return ItemActionPriority.NORMAL;
                            }

                            @Override
                            public void onClick(ItemClickAction itemClickAction) {
                                if (itemClickAction.getClickType().equals(ClickType.LEFT)) {
                                    Map<String, Object> info = new HashMap<>();
                                    info.put("arena", arena);
                                    info.put("type", "simple");
                                    info.put(
                                            "team",
                                            arena.getSettings().getAvailableTeams().stream()
                                                    .filter(
                                                            t ->
                                                                    t.getColor()
                                                                            .equals(
                                                                                    colors[
                                                                                            finalI1]))
                                                    .findFirst()
                                                    .get());
                                    SimpleSetupGui.this
                                            .plugin
                                            .getGuiHandler()
                                            .getGui("TEAM_SETUP")
                                            .open(
                                                    itemClickAction,
                                                    info,
                                                    itemClickAction.getPlayer());
                                } else if (itemClickAction.getClickType().equals(ClickType.RIGHT)) {
                                    arena.getSettings().disableTeam(colors[finalI]);
                                }
                            }
                        });
            } else {
                color =
                        new ActionItem(
                                colors[i].getChatColor() + "Team " + colors[i].name(),
                                new StainedGlassItemStack(colors[i].getGlassColor()),
                                StringUtils.translateAlternateColorCodes("&aClick to enable!"));
                color.addAction(
                        new ItemAction() {
                            @Override
                            public ItemActionPriority getPriority() {
                                return ItemActionPriority.NORMAL;
                            }

                            @Override
                            public void onClick(ItemClickAction itemClickAction) {
                                arena.getSettings().enableTeam(colors[finalI]);
                            }
                        });
            }
            color.addAction(
                    new ItemAction() {
                        @Override
                        public ItemActionPriority getPriority() {
                            return ItemActionPriority.LOW;
                        }

                        @Override
                        public void onClick(ItemClickAction itemClickAction) {
                            SimpleSetupGui.this.renderPageTwo(arena);
                            itemClickAction.setUpdate(true);
                            SimpleSetupGui.this.menu.update(itemClickAction.getPlayer());
                        }
                    });
            menu.setItem(index[i], color);
        }
    }

    private void addPageInfo(byte active) {
        ActionItem basicSetup =
                new ActionItem(
                        StringUtils.translateAlternateColorCodes("&aBasic Setup"),
                        XMaterial.WHITE_STAINED_GLASS_PANE.parseItem());
        ActionItem teamSetup =
                new ActionItem(
                        StringUtils.translateAlternateColorCodes("&fTeam Setup"),
                        XMaterial.WHITE_STAINED_GLASS_PANE.parseItem());

        switch (active) {
            case 1:
                basicSetup.setIcon(XMaterial.LIME_STAINED_GLASS_PANE.parseItem());
                break;
            case 2:
                teamSetup.setIcon(XMaterial.LIME_STAINED_GLASS_PANE.parseItem());
                break;
        }

        this.menu.setItem(48, basicSetup);
        this.menu.setItem(50, teamSetup);
    }

    private void addPageChangeButtons(byte page, Player player, Arena arena) {
        ActionItem back = new ActionItem(XMaterial.ARROW.parseItem());
        ActionItem next = new ActionItem(XMaterial.ARROW.parseItem());
        if (page == 1) {
            back = BACK;
            next.setName(StringUtils.translateAlternateColorCodes("&fGo to &aTeam Setup"));
            next.setLore(
                    Collections.singletonList(
                            StringUtils.translateAlternateColorCodes(
                                    "&fAdd team assets for each team.")));
            next.addAction(
                    new ItemAction() {
                        @Override
                        public ItemActionPriority getPriority() {
                            return ItemActionPriority.NORMAL;
                        }

                        @Override
                        public void onClick(ItemClickAction itemClickAction) {
                            Map<String, Object> info = new HashMap<>();
                            info.put("page", 2);
                            info.put("arena", arena);
                            SimpleSetupGui.this.setUpMenu(player, null, info);
                        }
                    });
        } else if (page == 2) {
            back.setName(StringUtils.translateAlternateColorCodes("&fGo to &aBasic Setup"));
            back.setLore(
                    Collections.singletonList(
                            StringUtils.translateAlternateColorCodes(
                                    "&fConfigure basic settings.")));
            back.addAction(
                    new ItemAction() {
                        @Override
                        public ItemActionPriority getPriority() {
                            return ItemActionPriority.NORMAL;
                        }

                        @Override
                        public void onClick(ItemClickAction itemClickAction) {
                            Map<String, Object> info = new HashMap<>();
                            info.put("page", 1);
                            info.put("arena", arena);
                            SimpleSetupGui.this.setUpMenu(player, null, info);
                        }
                    });
            next =
                    new ActionItem(
                            StringUtils.translateAlternateColorCodes("&aFinish"),
                            XMaterial.EMERALD_BLOCK.parseItem(),
                            Collections.singletonList(
                                    StringUtils.translateAlternateColorCodes(
                                            "&fand enable arena.")));
        }

        back.addAction(
                new ItemAction() {
                    @Override
                    public ItemActionPriority getPriority() {
                        return ItemActionPriority.NORMAL;
                    }

                    @Override
                    public void onClick(ItemClickAction itemClickAction) {
                        itemClickAction.setUpdate(true);
                    }
                });
        next.addAction(
                new ItemAction() {
                    @Override
                    public ItemActionPriority getPriority() {
                        return ItemActionPriority.NORMAL;
                    }

                    @Override
                    public void onClick(ItemClickAction itemClickAction) {
                        itemClickAction.setUpdate(true);
                    }
                });

        this.menu.setItem(53, next);
        this.menu.setItem(45, back);
    }
}
