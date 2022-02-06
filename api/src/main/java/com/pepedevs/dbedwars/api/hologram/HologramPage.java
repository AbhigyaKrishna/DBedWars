package com.pepedevs.dbedwars.api.hologram;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public class HologramPage {

    private final Hologram parent;
    private int index;
    private final List<Integer> clickableEntityIds;
    private final List<HologramLine> lines;
    private final Set<HologramClickAction> actions;

    public HologramPage(Hologram parent, int index) {
        this.parent = parent;
        this.index = index;
        this.clickableEntityIds = new CopyOnWriteArrayList<>();
        this.lines = new LinkedList<>();
        this.actions = Collections.synchronizedSet(new HashSet<>());
    }

    public Hologram getParent() {
        return this.parent;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void addLine(HologramLine line) {
        this.lines.add(line);
        line.setParent(this);
    }

    public void removeLine(int index) {
        HologramLine line = this.lines.remove(index);
        if (line.getParent().equals(this)) line.setParent(null);
    }

    public List<HologramLine> getLines() {
        return Collections.unmodifiableList(this.lines);
    }

    public void addAction(HologramClickAction action) {
        this.actions.add(action);
    }

    public Set<HologramClickAction> getActions() {
        return Collections.unmodifiableSet(this.actions);
    }

    public void removeAction(Predicate<HologramClickAction> predicate) {
        this.actions.removeIf(predicate);
    }

    public void clearActions() {
        this.actions.clear();
    }
}

