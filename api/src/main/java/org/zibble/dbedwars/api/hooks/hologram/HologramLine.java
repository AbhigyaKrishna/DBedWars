package org.zibble.dbedwars.api.hooks.hologram;

import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.messaging.message.Message;

public interface HologramLine<C> {
    
    HologramPage getParent();

    C getContent();

    void setContent(C content);

    float getHeight();

    interface Text extends HologramLine<Message> {}

    interface Head extends HologramLine<ItemStack> {}

    interface SmallHead extends HologramLine<ItemStack> {}

    interface Icon extends HologramLine<ItemStack> {}

    interface Entity extends HologramLine<HologramEntityType> {}

}
