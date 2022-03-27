package org.zibble.dbedwars.api.hooks.hologram;

import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.ClickAction;

import java.util.List;
import java.util.Set;

public interface HologramPage {
    
    Hologram getParent();

    List<HologramLine<?>> getLines();

    HologramLine.Text addNewTextLine(Message text);

    HologramLine.Head addNewHeadLine(ItemStack itemStack);

    HologramLine.SmallHead addNewSmallHeadLine(ItemStack itemStack);

    HologramLine.Icon addNewIconLine(ItemStack itemStack);

    HologramLine.Entity addNewEntityLine(HologramEntityType entityType);

    void removeLine(int index);

    HologramLine.Text insertNewTextLine(int index, Message text);

    HologramLine.Head insertNewHeadLine(int index, ItemStack itemStack);

    HologramLine.SmallHead insertNewSmallHeadLine(int index, ItemStack itemStack);

    HologramLine.Icon insertNewIconLine(int index, ItemStack itemStack);

    HologramLine.Entity insertNewEntityLine(int index, HologramEntityType entityType);

    Set<ClickAction> getActions();

    boolean addAction(ClickAction clickAction);

    boolean removeAction(ClickAction clickAction);
    
}
