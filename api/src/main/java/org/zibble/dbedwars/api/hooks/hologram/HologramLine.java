package org.zibble.dbedwars.api.hooks.hologram;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

public interface HologramLine<C> {
    
    HologramPage getParent();

    C getContent();

    void setContent(C content);

    float getHeight();

    interface Text extends HologramLine<Component> {}

    interface Head extends HologramLine<ItemStack> {}

    interface SmallHead extends HologramLine<ItemStack> {}

    interface Icon extends HologramLine<ItemStack> {}

    interface Entity extends HologramLine<HologramEntityType> {}

}
