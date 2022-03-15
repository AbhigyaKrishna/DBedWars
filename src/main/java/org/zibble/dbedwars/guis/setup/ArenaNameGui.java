package org.zibble.dbedwars.guis.setup;

import com.cryptomorin.xseries.XMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.util.item.ItemMetaBuilder;
import org.zibble.dbedwars.guis.component.GuiComponent;
import org.zibble.inventoryframework.menu.inventory.AnvilMenu;
import org.zibble.inventoryframework.protocol.Item;
import org.zibble.inventoryframework.spigot.SpigotItem;

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

        this.mask("AB")
                .item(() -> new SpigotItem(ItemMetaBuilder.of(XMaterial.PAPER)
                        .displayName(Component.text("ARENA DISPLAY NAME"))
                        .toItemStack()));
    }

    public ArenaNameGui item(Supplier<? extends Item> item) {
        this.item('A', item);
        return this;
    }

    public ArenaNameGui outputClick(BiConsumer<ArenaNameGui, String> outputClick) {
        this.menu.setOutputClick(s -> outputClick.accept(this, s));
        return this;
    }

}
