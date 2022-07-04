package org.zibble.dbedwars.guis.setup;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.guis.component.GuiComponent;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.inventoryframework.menu.inventory.AnvilMenu;
import org.zibble.inventoryframework.protocol.Item;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ArenaNameGui extends GuiComponent<AnvilMenu, ArenaNameGui> {

    private ArenaNameGui() {
        super(new AnvilMenu());

        this.mask("AB")
                .item(() -> BwItemStack.builder()
                        .displayName(AdventureMessage.from("ARENA DISPLAY NAME"))
                        .build());
    }

    public static ArenaNameGui openDefault(Player player) {
        return ArenaNameGui.creator().open(player);
    }

    public static ArenaNameGui creator() {
        return new ArenaNameGui();
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
