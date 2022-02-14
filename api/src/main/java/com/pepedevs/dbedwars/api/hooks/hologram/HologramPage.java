package com.pepedevs.dbedwars.api.hooks.hologram;

import com.pepedevs.dbedwars.api.util.ClickAction;

import java.util.List;
import java.util.Set;

public interface HologramPage {
    
    Hologram getParent();

    List<HologramLine<?>> getLines();

    <C> HologramLine<C> addNewLine(C content, int height);

    <C> HologramLine<C> changeLine(int index, C content, int height);

    void removeLine(int index);

    <C> HologramLine<C> insertNewLine(int index, C content, int height);

    Set<ClickAction> getActions();

    boolean addAction(ClickAction clickAction);

    boolean removeAction(ClickAction clickAction);
    
}
