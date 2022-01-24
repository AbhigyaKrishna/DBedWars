package com.pepedevs.dbedwars.guis.setup;

import com.pepedevs.radium.gui.inventory.ItemMenu;
import com.pepedevs.radium.gui.inventory.action.ItemClickAction;
import com.pepedevs.radium.gui.inventory.item.action.ActionItem;
import com.pepedevs.radium.gui.inventory.item.action.ItemAction;
import com.pepedevs.radium.gui.inventory.item.action.ItemActionPriority;
import com.pepedevs.radium.gui.inventory.size.ItemMenuSize;
import com.pepedevs.radium.utils.StringUtils;
import com.pepedevs.radium.utils.xseries.XMaterial;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.util.NBTUtils;
import com.pepedevs.dbedwars.api.util.gui.IMenu;
import com.pepedevs.dbedwars.item.InnerCustomItem;
import com.pepedevs.dbedwars.utils.Utils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@SuppressWarnings("unchecked")
public class SetupTypeGui extends IMenu<ItemMenu> {

    private final DBedwars plugin;

    public SetupTypeGui(DBedwars plugin) {
        super(
                "TYPE_SETUP",
                new ItemMenu(
                        StringUtils.translateAlternateColorCodes("&3Choose type of setup"),
                        ItemMenuSize.THREE_LINE,
                        null));
        this.plugin = plugin;

        this.menu.fillToAll(VOID_ITEM);
    }

    @Override
    public void setUpMenu(
            Player player, @Nullable ItemClickAction action, @Nullable Map<String, Object> info) {
        ActionItem simple =
                new ActionItem(
                        StringUtils.translateAlternateColorCodes("&e&lSimple Setup"),
                        XMaterial.REPEATER.parseItem(),
                        StringUtils.translateAlternateColorCodes(
                                new String[] {
                                    "",
                                    "&7Easy and quick arena setup!",
                                    "",
                                    "&dOnly essential options for setup."
                                }));

        simple.addAction(
                new ItemAction() {
                    @Override
                    public ItemActionPriority getPriority() {
                        return ItemActionPriority.NORMAL;
                    }

                    @Override
                    public void onClick(ItemClickAction itemClickAction) {
                        itemClickAction
                                .getPlayer()
                                .getInventory()
                                .addItem(
                                        NBTUtils.addNbtData(
                                                Utils.setMaxStackSize(
                                                        InnerCustomItem.SIMPLE_SETUP_ITEM
                                                                .getItem()
                                                                .toItemStack(), 1),
                                                "arena",
                                                info.get("arena")));
                        itemClickAction.setClose(true);
                    }
                });

        ActionItem advanced =
                new ActionItem(
                        StringUtils.translateAlternateColorCodes("&c&lAdvanced Setup"),
                        XMaterial.COMPARATOR.parseItem(),
                        StringUtils.translateAlternateColorCodes(
                                new String[] {
                                    "",
                                    "&7Advanced detailed setup",
                                    "&7for customizing whole arena!",
                                    "",
                                    "&dAll customization options for setup."
                                }));

        advanced.addAction(
                new ItemAction() {
                    @Override
                    public ItemActionPriority getPriority() {
                        return ItemActionPriority.NORMAL;
                    }

                    @Override
                    public void onClick(ItemClickAction itemClickAction) {
                        itemClickAction
                                .getPlayer()
                                .getInventory()
                                .addItem(
                                        NBTUtils.addNbtData(
                                                Utils.setMaxStackSize(
                                                        InnerCustomItem.ADVANCED_SETUP_ITEM
                                                                .getItem()
                                                                .toItemStack(), 1),
                                                "arena",
                                                info.get("arena")));
                        itemClickAction.setClose(true);
                    }
                });

        this.menu.setItem(11, simple);
        this.menu.setItem(15, advanced);
    }
}
