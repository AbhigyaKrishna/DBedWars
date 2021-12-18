package com.pepedevs.dbedwars.guis;

import com.pepedevs.corelib.gui.inventory.ItemMenu;
import com.pepedevs.corelib.gui.inventory.action.ItemClickAction;
import com.pepedevs.corelib.gui.inventory.size.ItemMenuSize;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.view.ShopPage;
import com.pepedevs.dbedwars.api.util.gui.IMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

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
        ShopPage page = (ShopPage) info.getOrDefault("page", null);
        ArenaPlayer ap = (ArenaPlayer) info.get("player");
        if (page == null)
            page =
                    (ap.getShopView().getDefaultPage() == null
                            ? ap.getShopView().getShopPages().values().stream().findFirst().get()
                            : ap.getShopView().getDefaultPage());
        this.menu.setTitle(page.getTitle());
        for (byte i = 0; i < page.getPattern().length; i++) {
            for (byte j = 0; j < 9; j++) {
                this.menu.setItem(i * 9 + j, page.getPattern()[i][j]);
            }
        }
    }

}
