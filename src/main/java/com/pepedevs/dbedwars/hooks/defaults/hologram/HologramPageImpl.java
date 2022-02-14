package com.pepedevs.dbedwars.hooks.defaults.hologram;

import com.pepedevs.dbedwars.api.hooks.hologram.Hologram;
import com.pepedevs.dbedwars.api.hooks.hologram.HologramPage;
import com.pepedevs.dbedwars.api.hooks.hologram.HologramLine;
import com.pepedevs.dbedwars.api.util.ClickAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class HologramPageImpl implements HologramPage {

    private final HologramImpl parent;
    private int index;
    private final List<HologramLineImpl<?>> lines;
    private final Set<ClickAction> actions;

    private double lineGap = 0.1;

    public HologramPageImpl(HologramImpl parent, int index) {
        this.parent = parent;
        this.index = index;
        this.lines = Collections.synchronizedList(new ArrayList<>());
        this.actions = Collections.synchronizedSet(new java.util.HashSet<>());
    }

    @Override
    public Hologram getParent() {
        return parent;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public List<HologramLine<?>> getLines() {
        return Collections.unmodifiableList(this.lines);
    }

    @Override
    public <C> HologramLine<C> addNewLine(C content, int height) {
        HologramLineImpl<C> line = new HologramLineImpl<>(this, content, height, lines.size());
        this.lines.add(line);
        return line;
    }

    @Override
    public <C> HologramLine<C> changeLine(int index, C content, int height) {
        HologramLineImpl<C> line = new HologramLineImpl<>(this, content, height, index);
        this.lines.set(index, line);
        return line;
    }

    @Override
    public Set<ClickAction> getActions() {
        return Collections.unmodifiableSet(this.actions);
    }

    @Override
    public boolean addAction(ClickAction clickAction) {
        return this.actions.add(clickAction);
    }

    @Override
    public boolean removeAction(ClickAction clickAction) {
        return this.actions.remove(clickAction);
    }

    public double getLineGap() {
        return lineGap;
    }

    public void setLineGap(double lineGap) {
        this.lineGap = lineGap;
    }
}
