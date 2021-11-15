package com.pepedevs.dbedwars.guis;

import com.pepedevs.dbedwars.game.arena.view.shop.ShopView;
import me.Abhigya.core.menu.inventory.ItemMenu;
import me.Abhigya.core.menu.inventory.action.ItemClickAction;
import me.Abhigya.core.menu.inventory.size.ItemMenuSize;
import me.Abhigya.core.util.StringUtils;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.view.ViewItem;
import com.pepedevs.dbedwars.api.util.gui.IMenu;
import com.pepedevs.dbedwars.game.arena.view.shop.ShopPage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@SuppressWarnings("unchecked")
public class ShopGui extends IMenu<ItemMenu> {

    private final DBedwars plugin;

    public ShopGui(DBedwars plugin) {
        super("SHOP", new ItemMenu("Shop", ItemMenuSize.SIX_LINE, null));
        this.plugin = plugin;
    }

    @Override
    public void setUpMenu(
            Player player, @Nullable ItemClickAction action, @Nullable Map<String, Object> info) {
        if (info == null) return;

        this.menu.clear();
        String page = (String) info.getOrDefault("page", null);
        ArenaPlayer ap = (ArenaPlayer) info.get("player");
        if (page == null) page = ap.getShopView().getDefaultPage();
        ShopPage shopPage = ((ShopView) ap.getShopView()).getShopPages().get(page);
        this.menu.setTitle(StringUtils.translateAlternateColorCodes(shopPage.getTitle()));
        for (byte i = 0; i < shopPage.getPattern().length; i++) {
            for (byte j = 0; j < 9; j++) {
                ViewItem item = shopPage.getItems().getOrDefault(shopPage.getPattern()[i][j], null);
                if (item != null) {
                    this.menu.setItem(i * 9 + j, item.getActionItem(false));
                } else {
                    this.menu.setItem(i * 9 + j, null);
                }
            }
        }
    }
}
