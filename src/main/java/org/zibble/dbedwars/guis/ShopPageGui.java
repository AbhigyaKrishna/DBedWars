package org.zibble.dbedwars.guis;

import org.zibble.dbedwars.game.arena.view.shop.ShopItem;
import org.zibble.dbedwars.guis.component.GuiComponent;
import org.zibble.inventoryframework.ClickAction;
import org.zibble.inventoryframework.MenuItem;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;
import org.zibble.inventoryframework.spigot.SpigotItem;

import java.util.function.Supplier;

public class ShopPageGui extends GuiComponent<ChestMenu, ShopPageGui> {

    public ShopPageGui(int row) {
        super(new ChestMenu(row, null));
    }

    @Override
    public final ShopPageGui item(char c, SpigotItem item) {
        return super.item(c, item);
    }

    @Override
    public final ShopPageGui item(char c, SpigotItem item, ClickAction clickAction) {
        return super.item(c, item, clickAction);
    }

    @Override
    public final ShopPageGui item(char c, MenuItem<SpigotItem> item) {
        return super.item(c, item);
    }

    @Override
    public final ShopPageGui item(char c, Supplier<SpigotItem> item) {
        return super.item(c, item);
    }

    @Override
    public final ShopPageGui item(char c, Supplier<SpigotItem> item, ClickAction clickAction) {
        return super.item(c, item, clickAction);
    }

    public ShopPageGui shopItem(char c, ShopItem item) {
        return super.item(c, item.asMenuItem());
    }

    public ShopPageGui shopItem(char c, Supplier<ShopItem> item) {
        return this.shopItem(c, item.get());
    }

    @Override
    public final ShopPageGui overrideSlot(int slot, SpigotItem item, ClickAction clickAction) {
        return super.overrideSlot(slot, item, clickAction);
    }

    @Override
    public final ShopPageGui overrideSlot(int slot, SpigotItem item) {
        return super.overrideSlot(slot, item);
    }

    @Override
    public final ShopPageGui overrideSlot(int slot, MenuItem<SpigotItem> item) {
        return super.overrideSlot(slot, item);
    }

    public final ShopPageGui overrideSlot(int slot, ShopItem item) {
        return super.overrideSlot(slot, item.asMenuItem());
    }


}
