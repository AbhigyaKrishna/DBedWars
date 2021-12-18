package com.pepedevs.dbedwars.guis.setup;

import com.pepedevs.corelib.gui.inventory.ItemMenu;
import com.pepedevs.corelib.gui.inventory.action.ItemClickAction;
import com.pepedevs.corelib.gui.inventory.item.action.ActionItem;
import com.pepedevs.corelib.gui.inventory.item.action.ItemAction;
import com.pepedevs.corelib.gui.inventory.item.action.ItemActionPriority;
import com.pepedevs.corelib.gui.inventory.item.voidaction.VoidActionItem;
import com.pepedevs.corelib.gui.inventory.size.ItemMenuSize;
import com.pepedevs.corelib.utils.StringUtils;
import com.pepedevs.corelib.utils.version.Version;
import com.pepedevs.corelib.utils.xseries.XMaterial;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.util.LocationXYZ;
import com.pepedevs.dbedwars.api.util.LocationXYZYP;
import com.pepedevs.dbedwars.api.util.gui.IMenu;
import com.pepedevs.dbedwars.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class TeamSetupGui extends IMenu<ItemMenu> {

    private final DBedwars plugin;

    protected TeamSetupGui(DBedwars plugin) {
        super(
                "TEAM_SETUP",
                new ItemMenu(
                        StringUtils.translateAlternateColorCodes("&eTeam Setup"),
                        ItemMenuSize.FOUR_LINE,
                        null));
        this.plugin = plugin;
    }

    @Override
    protected void setUpMenu(
            Player player, @Nullable ItemClickAction action, @Nullable Map<String, Object> info) {
        this.menu.setParent(action.getMenu());
        this.menu.fillToAll(VOID_ITEM);
        Arena arena = (Arena) info.get("arena");
        Team team = (Team) info.get("team");

        ItemStack icon = action.getClickedItem();
        this.menu.setItem(4, new VoidActionItem(icon.getItemMeta().getDisplayName(), icon));

        ActionItem bed =
                new ActionItem(
                        StringUtils.translateAlternateColorCodes("&eSet bed location"),
                        (this.plugin.getServerVersion().isOlderEquals(Version.v1_8_R3))
                                ? new ItemStack(Material.valueOf("BED"))
                                : XMaterial.RED_BED.parseItem());
        bed.addAction(
                new ItemAction() {
                    @Override
                    public ItemActionPriority getPriority() {
                        return ItemActionPriority.NORMAL;
                    }

                    @Override
                    public void onClick(ItemClickAction itemClickAction) {
                        Block block =
                                Utils.findBed(
                                        itemClickAction.getPlayer().getLocation(),
                                        (byte) 4,
                                        (byte) 4,
                                        (byte) 4);
                        if (block != null) {
                            team.setBedLocation(LocationXYZ.valueOf(block.getLocation()));
                            itemClickAction
                                    .getPlayer()
                                    .sendMessage(
                                            StringUtils.translateAlternateColorCodes(
                                                    "&aBed location set!"));
                        } else {
                            itemClickAction
                                    .getPlayer()
                                    .sendMessage(
                                            StringUtils.translateAlternateColorCodes(
                                                    "&cNo beds found at this location."));
                        }
                    }
                });

        ActionItem spawn =
                new ActionItem(
                        StringUtils.translateAlternateColorCodes("&eSet spawn location"),
                        XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.parseItem());
        spawn.addAction(
                new ItemAction() {
                    @Override
                    public ItemActionPriority getPriority() {
                        return ItemActionPriority.NORMAL;
                    }

                    @Override
                    public void onClick(ItemClickAction itemClickAction) {
                        team.setSpawn(
                                LocationXYZYP.valueOf(itemClickAction.getPlayer().getLocation()));
                        itemClickAction
                                .getPlayer()
                                .sendMessage(
                                        StringUtils.translateAlternateColorCodes(
                                                "&eSet spawn location"));
                    }
                });

        ActionItem spawner =
                new ActionItem(
                        StringUtils.translateAlternateColorCodes("&eAdd spawner"),
                        XMaterial.IRON_INGOT.parseItem());
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
                        info.put("type", info.get("type"));
                        info.put("team-spawner", true);
                        info.put("team", team);
                        TeamSetupGui.this
                                .plugin
                                .getGuiHandler()
                                .getGui("SPAWNER_SETUP")
                                .open(itemClickAction, info, itemClickAction.getPlayer());
                    }
                });

        ActionItem shop =
                new ActionItem(
                        StringUtils.translateAlternateColorCodes("&eAdd shop npc"),
                        XMaterial.STONE_BRICKS.parseItem());
        shop.addAction(
                new ItemAction() {
                    @Override
                    public ItemActionPriority getPriority() {
                        return ItemActionPriority.NORMAL;
                    }

                    @Override
                    public void onClick(ItemClickAction itemClickAction) {
                        team.setShopNpc(
                                LocationXYZYP.valueOf(itemClickAction.getPlayer().getLocation()));
                        itemClickAction
                                .getPlayer()
                                .sendMessage(
                                        StringUtils.translateAlternateColorCodes(
                                                "&aShop location set!"));
                    }
                });

        ActionItem upgrade =
                new ActionItem(
                        StringUtils.translateAlternateColorCodes("&eAdd upgrades npc"),
                        XMaterial.VILLAGER_SPAWN_EGG.parseItem());
        upgrade.addAction(
                new ItemAction() {
                    @Override
                    public ItemActionPriority getPriority() {
                        return ItemActionPriority.NORMAL;
                    }

                    @Override
                    public void onClick(ItemClickAction itemClickAction) {
                        team.setUpgradesNpc(
                                LocationXYZYP.valueOf(itemClickAction.getPlayer().getLocation()));
                        itemClickAction
                                .getPlayer()
                                .sendMessage(
                                        StringUtils.translateAlternateColorCodes(
                                                "&aUpgrade location set!"));
                    }
                });

        this.menu.setItem(11, bed);
        this.menu.setItem(15, spawn);
        this.menu.setItem(22, spawner);
        this.menu.setItem(29, shop);
        this.menu.setItem(33, upgrade);
        this.menu.setItem(8, BACK);
    }

}
