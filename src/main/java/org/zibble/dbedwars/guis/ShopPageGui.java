package org.zibble.dbedwars.guis;

import org.zibble.dbedwars.game.arena.view.ShopItem;
import org.zibble.dbedwars.game.arena.view.ShopView;
import org.zibble.dbedwars.guis.component.GuiComponent;
import org.zibble.inventoryframework.ClickAction;
import org.zibble.inventoryframework.MenuItem;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;
import org.zibble.inventoryframework.protocol.Item;

import java.util.function.Supplier;

public class ShopPageGui extends GuiComponent<ChestMenu, ShopPageGui> {

    public ShopPageGui(int row) {
        super(new ChestMenu(row, null));
    }

    @Override
    public final ShopPageGui item(char c, Item item) {
        return super.item(c, item);
    }

    @Override
    public final ShopPageGui item(char c, Item item, ClickAction clickAction) {
        return super.item(c, item, clickAction);
    }

    @Override
    public final ShopPageGui item(char c, MenuItem<? extends Item> item) {
        return super.item(c, item);
    }

    @Override
    public final ShopPageGui item(char c, Supplier<? extends Item> item) {
        return super.item(c, item);
    }

    @Override
    public final ShopPageGui item(char c, Supplier<? extends Item> item, ClickAction clickAction) {
        return super.item(c, item, clickAction);
    }

    public ShopPageGui shopItem(char c, ShopItem item, ShopView shopView) {
        return super.item(c, item.asMenuItem(shopView));
    }

    public ShopPageGui shopItem(char c, Supplier<ShopItem> item, ShopView shopView) {
        return this.shopItem(c, item.get(), shopView);
    }

    @Override
    public final ShopPageGui overrideSlot(int slot, Item item, ClickAction clickAction) {
        return super.overrideSlot(slot, item, clickAction);
    }

    @Override
    public final ShopPageGui overrideSlot(int slot, Item item) {
        return super.overrideSlot(slot, item);
    }

    @Override
    public final ShopPageGui overrideSlot(int slot, MenuItem<? extends Item> item) {
        return super.overrideSlot(slot, item);
    }

    public final ShopPageGui overrideSlot(int slot, ShopItem item, ShopView shopView) {
        return super.overrideSlot(slot, item.asMenuItem(shopView));
    }


}
