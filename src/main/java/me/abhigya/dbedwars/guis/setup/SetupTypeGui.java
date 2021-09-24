package me.abhigya.dbedwars.guis.setup;

import me.Abhigya.core.menu.inventory.ItemMenu;
import me.Abhigya.core.menu.inventory.action.ItemClickAction;
import me.Abhigya.core.menu.inventory.item.action.ActionItem;
import me.Abhigya.core.menu.inventory.item.action.ItemAction;
import me.Abhigya.core.menu.inventory.item.action.ItemActionPriority;
import me.Abhigya.core.menu.inventory.size.ItemMenuSize;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.util.gui.IMenu;
import me.abhigya.dbedwars.item.CustomItems;
import me.abhigya.dbedwars.utils.Utils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SetupTypeGui extends IMenu<ItemMenu> {

    public SetupTypeGui(DBedwars plugin) {
        super(plugin, "TYPE_SETUP", new ItemMenu(StringUtils.translateAlternateColorCodes("&3Choose type of setup"),
                ItemMenuSize.THREE_LINE, null));

        this.menu.fillToAll(VOID_ITEM);
    }

    @Override
    public void setUpMenu(Player player, @Nullable ItemClickAction action, @Nullable Map<String, Object> info) {
        ActionItem simple = new ActionItem(StringUtils.translateAlternateColorCodes("&e&lSimple Setup"),
                XMaterial.REPEATER.parseItem(), StringUtils.translateAlternateColorCodes(new String[]{"",
                "&7Easy and quick arena setup!", "", "&dOnly essential options for setup."}));

        simple.addAction(new ItemAction() {
            @Override
            public ItemActionPriority getPriority() {
                return ItemActionPriority.NORMAL;
            }

            @Override
            public void onClick(ItemClickAction itemClickAction) {
                itemClickAction.getPlayer().getInventory().addItem(Utils.addNbtData(Utils.setUnStackable(CustomItems.SIMPLE_SETUP_ITEM.getItem().toItemStack()),
                        "arena", info.get("arena")));
                itemClickAction.setClose(true);
            }
        });

        ActionItem advanced = new ActionItem(StringUtils.translateAlternateColorCodes("&c&lAdvanced Setup"),
                XMaterial.COMPARATOR.parseItem(), StringUtils.translateAlternateColorCodes(new String[]{"",
                "&7Advanced detailed setup", "&7for customizing whole arena!", "", "&dAll customization options for setup."}));

        advanced.addAction(new ItemAction() {
            @Override
            public ItemActionPriority getPriority() {
                return ItemActionPriority.NORMAL;
            }

            @Override
            public void onClick(ItemClickAction itemClickAction) {
                itemClickAction.getPlayer().getInventory().addItem(Utils.addNbtData(Utils.setUnStackable(CustomItems.ADVANCED_SETUP_ITEM.getItem().toItemStack()),
                        "arena", info.get("arena")));
                itemClickAction.setClose(true);
            }
        });

        this.menu.setItem(11, simple);
        this.menu.setItem(15, advanced);
    }
}
