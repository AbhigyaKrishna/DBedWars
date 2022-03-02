package org.zibble.dbedwars.api.hooks.hologram;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.util.ClickAction;

import java.util.List;
import java.util.Set;

public interface HologramPage {
    
    Hologram getParent();

    List<HologramLine<?>> getLines();

    HologramLine.Text addNewTextLine(Component text);

    HologramLine.Head addNewHeadLine(ItemStack itemStack);

    HologramLine.SmallHead addNewSmallHeadLine(ItemStack itemStack);

    HologramLine.Icon addNewIconLine(ItemStack itemStack);

    HologramLine.Entity addNewEntityLine(HologramEntityType entityType);

    void removeLine(int index);

    HologramLine.Text insertNewTextLine(int index, Component text);

    HologramLine.Head insertNewHeadLine(int index, ItemStack itemStack);

    HologramLine.SmallHead insertNewSmallHeadLine(int index, ItemStack itemStack);

    HologramLine.Icon insertNewIconLine(int index, ItemStack itemStack);

    HologramLine.Entity insertNewEntityLine(int index, HologramEntityType entityType);

    Set<ClickAction> getActions();

    boolean addAction(ClickAction clickAction);

    boolean removeAction(ClickAction clickAction);
    
}
