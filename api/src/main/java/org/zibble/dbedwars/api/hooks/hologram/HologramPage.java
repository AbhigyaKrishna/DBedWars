package org.zibble.dbedwars.api.hooks.hologram;

import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.api.util.mixin.ClickAction;

import java.util.List;
import java.util.Set;

public interface HologramPage {

    Hologram getParent();

    int getPageNumber();

    List<HologramLine<?>> getLines();

    HologramLine.Text addNewTextLine(Message text);

    HologramLine.Head addNewHeadLine(BwItemStack itemStack);

    HologramLine.SmallHead addNewSmallHeadLine(BwItemStack itemStack);

    HologramLine.Icon addNewIconLine(BwItemStack itemStack);

    HologramLine.Entity addNewEntityLine(HologramEntityType entityType);

    void removeLine(int index);

    HologramLine.Text insertNewTextLine(int index, Message text);

    HologramLine.Head insertNewHeadLine(int index, BwItemStack itemStack);

    HologramLine.SmallHead insertNewSmallHeadLine(int index, BwItemStack itemStack);

    HologramLine.Icon insertNewIconLine(int index, BwItemStack itemStack);

    HologramLine.Entity insertNewEntityLine(int index, HologramEntityType entityType);

    Set<ClickAction> getActions();

    boolean addAction(ClickAction clickAction);

    boolean removeAction(ClickAction clickAction);

}
