package org.zibble.dbedwars.guis.setup;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.menus.player.MenuPlayer;
import org.zibble.inventoryframework.MenuItem;
import org.zibble.inventoryframework.menu.inventory.AnvilMenu;
import org.zibble.inventoryframework.protocol.Materials;
import org.zibble.inventoryframework.protocol.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ArenaNameGui {

    public static ActionFuture<AnvilMenu> openDefault(Player player) {
        return ArenaNameGui.creator().open(player);
    }

    public static ArenaNameGui creator() {
        return new ArenaNameGui();
    }

    private ItemStack item;
    private BiConsumer<AnvilMenu, String> outputClick;
    private BiConsumer<AnvilMenu, Player> openAction;
    private BiConsumer<AnvilMenu, Player> closeAction;

    private ArenaNameGui() {
        this.item = new ItemStack(Materials.PAPER);
        item.displayName(Component.text("ARENA DISPLAY NAME"));
    }

    public ArenaNameGui item(Supplier<ItemStack> item) {
        this.item = item.get();
        return this;
    }

    public ArenaNameGui outputClick(BiConsumer<AnvilMenu, String> outputClick) {
        this.outputClick = outputClick;
        return this;
    }

    public ArenaNameGui openAction(BiConsumer<AnvilMenu, Player> openAction) {
        this.openAction = openAction;
        return this;
    }

    public ArenaNameGui closeAction(BiConsumer<AnvilMenu, Player> closeAction) {
        this.closeAction = closeAction;
        return this;
    }

    public ActionFuture<AnvilMenu> open(Player player) {
        return ActionFuture.supplyAsync(() -> {
            AnvilMenu menu = new AnvilMenu();

            menu.mask("AB");
            menu.item('A', MenuItem.of(item));

            menu.setOutputClick(s -> outputClick.accept(menu, s));
            menu.onOpen(p -> openAction.accept(menu, (Player) p.handle()));
            menu.onClose(p -> closeAction.accept(menu, (Player) p.handle()));

            menu.open(MenuPlayer.of(player));
            return menu;
        });
    }

}
