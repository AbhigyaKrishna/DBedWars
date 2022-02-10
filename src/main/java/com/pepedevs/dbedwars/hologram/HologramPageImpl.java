package com.pepedevs.dbedwars.hologram;

import com.pepedevs.dbedwars.api.hologram.Hologram;
import com.pepedevs.dbedwars.api.util.ClickAction;
import com.pepedevs.dbedwars.api.hologram.HologramLine;
import com.pepedevs.dbedwars.api.hologram.HologramPage;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public class HologramPageImpl extends HologramPage {

    private final Hologram parent;
    private int index;
    private final List<Integer> clickableEntityIds;
    private final List<HologramLine<?>> lines;
    private final Set<ClickAction> actions;

    public HologramPageImpl(HologramImpl parent, int index) {
        this.parent = parent;
        this.index = index;
        this.clickableEntityIds = new CopyOnWriteArrayList<>();
        this.lines = new LinkedList<>();
        this.actions = Collections.synchronizedSet(new HashSet<>());
    }

    @Override
    public Hologram getParent() {
        return this.parent;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void addLine(HologramLine<?> line) {
        this.lines.add(line);
        line.setParent(this);
    }

    @Override
    public void removeLine(int index) {
        HologramLine<?> line = this.lines.remove(index);
        if (line.getParent().equals(this)) line.setParent(null);
    }

    @Override
    public List<HologramLine<?>> getLines() {
        return Collections.unmodifiableList(this.lines);
    }

    @Override
    public void addAction(ClickAction action) {
        this.actions.add(action);
    }

    @Override
    public Set<ClickAction> getActions() {
        return Collections.unmodifiableSet(this.actions);
    }

    @Override
    public void removeAction(Predicate<ClickAction> predicate) {
        this.actions.removeIf(predicate);
    }

    @Override
    public void clearActions() {
        this.actions.clear();
    }
}

