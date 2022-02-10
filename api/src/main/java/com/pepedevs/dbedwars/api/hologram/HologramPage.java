package com.pepedevs.dbedwars.api.hologram;

import com.pepedevs.dbedwars.api.util.ClickAction;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public abstract class HologramPage {

    public abstract Hologram getParent();

    public abstract int getIndex();

    public abstract void setIndex(int index);

    public abstract void addLine(HologramLine<?> line);

    public abstract void removeLine(int index);

    public abstract List<HologramLine<?>> getLines();

    public abstract void addAction(ClickAction action);

    public abstract Set<ClickAction> getActions();

    public abstract void removeAction(Predicate<ClickAction> predicate);

    public abstract void clearActions();
}
