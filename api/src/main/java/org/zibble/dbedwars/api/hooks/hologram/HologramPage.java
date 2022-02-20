package org.zibble.dbedwars.api.hooks.hologram;

import org.zibble.dbedwars.api.util.ClickAction;

import java.util.List;
import java.util.Set;

public interface HologramPage {
    
    Hologram getParent();

    List<HologramLine<?>> getLines();

    <C> HologramLine<C> addNewLine(C content, float height);

    <C> HologramLine<C> changeLine(int index, C content, float height);

    void removeLine(int index);

    <C> HologramLine<C> insertNewLine(int index, C content, float height);

    Set<ClickAction> getActions();

    boolean addAction(ClickAction clickAction);

    boolean removeAction(ClickAction clickAction);
    
}
