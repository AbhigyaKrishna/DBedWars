package org.zibble.dbedwars.guis.component;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.menus.player.MenuPlayer;
import org.zibble.inventoryframework.ClickAction;
import org.zibble.inventoryframework.MenuItem;
import org.zibble.inventoryframework.menu.Menu;
import org.zibble.inventoryframework.menu.nameable.NameableMenu;
import org.zibble.inventoryframework.protocol.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unchecked"})
public class GuiComponent<T extends Menu, R extends GuiComponent> {

    protected final T menu;
    protected Player player;

    public static <T extends Menu, R extends GuiComponent> GuiComponent<T, R> creator(T menu) {
        return new GuiComponent<>(menu);
    }

    protected GuiComponent(T menu) {
        this.menu = menu;
    }

    public R title(Component title) {
        if (this.menu instanceof NameableMenu) {
            ((NameableMenu) this.menu).title(title);
        }
        return (R) this;
    }

    public R mask(String... masks) {
        menu.mask(masks);
        return (R) this;
    }

    public R mask(Supplier<String[]> masks) {
        menu.mask(masks.get());
        return (R) this;
    }

    public R item(char c, ItemStack item) {
        return this.item(c, MenuItem.of(item));
    }

    public R item(char c, ItemStack item, ClickAction clickAction) {
        MenuItem<ItemStack> menuItem = MenuItem.of(item);
        menuItem.clickAction(clickAction);
        return this.item(c, menuItem);
    }

    public R item(char c, MenuItem<ItemStack> item) {
        menu.item(c, item);
        return (R) this;
    }

    public R item(char c, Supplier<ItemStack> item) {
        return this.item(c, item.get());
    }

    public R item(char c, Supplier<ItemStack> item, ClickAction clickAction) {
        return this.item(c, item.get(), clickAction);
    }

    public R onOpen(BiConsumer<R, Player> onOpen) {
        menu.onOpen(p -> onOpen.accept((R) this, (Player) p.handle()));
        return (R) this;
    }

    public R onClose(BiConsumer<R, Player> onClose) {
        menu.onClose(p -> onClose.accept((R) this, (Player) p.handle()));
        return (R) this;
    }

    public R open(Player player) {
        this.close();
        menu.open(MenuPlayer.of(player));
        this.player = player;
        return (R) this;
    }

    public R close() {
        if (this.player != null) {
            menu.close(MenuPlayer.of(this.player));
            this.player = null;
        }
        return (R) this;
    }

    public R update() {
        if (this.player != null) {
            this.menu.update(MenuPlayer.of(this.player));
        }
        return (R) this;
    }

    public R updateSlot(int slot) {
        if (this.player != null) {
            this.menu.updateSlot(slot, MenuPlayer.of(this.player));
        }
        return (R) this;
    }

    public T handle() {
        return this.menu;
    }

    public Player getViewer() {
        return this.player;
    }

}
