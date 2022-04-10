package org.zibble.dbedwars.api.guis.component;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.guis.MenuPlayer;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.inventoryframework.ClickAction;
import org.zibble.inventoryframework.MenuItem;
import org.zibble.inventoryframework.menu.Menu;
import org.zibble.inventoryframework.menu.nameable.NameableMenu;
import org.zibble.inventoryframework.protocol.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unchecked"})
public class GuiComponent<T extends Menu, R extends GuiComponent> {

    protected final T menu;
    private final List<BiConsumer<R, Player>> openActions = new ArrayList<>(1);
    private final List<BiConsumer<R, Player>> closeActions = new ArrayList<>(1);
    protected Message title;
    protected Player player;

    protected GuiComponent(T menu) {
        this.menu = menu;
        this.menu.onOpen(p -> {
            for (BiConsumer<R, Player> action : openActions) {
                action.accept((R) this, (Player) p.handle());
            }
        });
        this.menu.onClose(p -> {
            for (BiConsumer<R, Player> action : this.closeActions) {
                action.accept((R) this, (Player) p.handle());
            }
        });
    }

    public static <T extends Menu, R extends GuiComponent> GuiComponent<T, R> creator(T menu) {
        return new GuiComponent<>(menu);
    }

    public R title(Component title) {
        if (this.menu instanceof NameableMenu) {
            ((NameableMenu) this.menu).title(title);
        }
        return (R) this;
    }

    public R title(Supplier<Component> title) {
        if (this.menu instanceof NameableMenu) {
            ((NameableMenu) this.menu).title(title.get());
        }
        return (R) this;
    }

    public R title(Message title) {
        this.title = title;
        return (R) this;
    }

    public R mask(String... masks) {
        menu.setMask(masks);
        return (R) this;
    }

    public R mask(Supplier<String[]> masks) {
        menu.setMask(masks.get());
        return (R) this;
    }

    public R item(char c, Item item) {
        return this.item(c, MenuItem.of(item));
    }

    public R item(char c, Item item, ClickAction clickAction) {
        MenuItem<? extends Item> menuItem = MenuItem.of(item);
        menuItem.setClickAction(clickAction);
        return this.item(c, menuItem);
    }

    public R item(char c, MenuItem<? extends Item> item) {
        menu.setItem(c, item);
        return (R) this;
    }

    public R item(char c, Supplier<? extends Item> item) {
        return this.item(c, item.get());
    }

    public R item(char c, Supplier<? extends Item> item, ClickAction clickAction) {
        return this.item(c, item.get(), clickAction);
    }

    public R addOpenAction(BiConsumer<R, Player> onOpen) {
        this.closeActions.add(onOpen);
        return (R) this;
    }

    public List<BiConsumer<R, Player>> getOpenActions() {
        return openActions;
    }

    public R addCloseAction(BiConsumer<R, Player> onClose) {
        this.closeActions.add(onClose);
        return (R) this;
    }

    public R overrideSlot(int slot, MenuItem<? extends Item> item) {
        this.menu.overrideSlot(slot, item);
        return (R) this;
    }

    public R overrideSlot(int slot, Item item, ClickAction clickAction) {
        MenuItem<? extends Item> menuItem = MenuItem.of(item);
        menuItem.setClickAction(clickAction);
        return this.overrideSlot(slot, menuItem);
    }

    public R overrideSlot(int slot, Item item) {
        return this.overrideSlot(slot, MenuItem.of(item));
    }

    public R removeOverride(int slot) {
        this.menu.removeOverride(slot);
        return (R) this;
    }

    public boolean isOverridden(int slot) {
        return this.menu.isOverridden(slot);
    }

    public List<BiConsumer<R, Player>> getCloseActions() {
        return closeActions;
    }

    public R open(Player player) {
        this.close();
        if (this.title != null)
            this.title(this.title.asComponentWithPAPI(player)[0]);
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
