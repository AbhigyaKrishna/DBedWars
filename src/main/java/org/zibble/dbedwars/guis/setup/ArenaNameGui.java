package org.zibble.dbedwars.guis.setup;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.guis.component.GuiComponent;
import org.zibble.inventoryframework.menu.inventory.AnvilMenu;
import org.zibble.inventoryframework.protocol.Materials;
import org.zibble.inventoryframework.protocol.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ArenaNameGui extends GuiComponent<AnvilMenu, ArenaNameGui> {

    public static ArenaNameGui openDefault(Player player) {
        return ArenaNameGui.creator().open(player);
    }

    public static ArenaNameGui creator() {
        return new ArenaNameGui();
    }

    private ArenaNameGui() {
        super(new AnvilMenu());
        ItemStack item = new ItemStack(Materials.PAPER);
        item.displayName(Component.text("ARENA DISPLAY NAME"));
        this.mask("AB")
                .item('A', item);
    }

    public ArenaNameGui item(Supplier<ItemStack> item) {
        this.item('A', item);
        return this;
    }

    public ArenaNameGui outputClick(BiConsumer<ArenaNameGui, String> outputClick) {
        this.menu.setOutputClick(s -> outputClick.accept(this, s));
        return this;
    }

}
