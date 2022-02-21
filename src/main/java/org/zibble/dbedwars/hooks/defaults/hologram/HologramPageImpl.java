package org.zibble.dbedwars.hooks.defaults.hologram;

import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;
import org.zibble.dbedwars.api.hooks.hologram.HologramLine;
import org.zibble.dbedwars.api.util.ClickAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class HologramPageImpl implements HologramPage {

    private final HologramImpl parent;
    private final List<HologramLineImpl<?>> lines;
    private final Set<ClickAction> actions;

    private double lineGap = 0.1;

    public HologramPageImpl(HologramImpl parent) {
        this.parent = parent;
        this.lines = Collections.synchronizedList(new ArrayList<>());
        this.actions = Collections.synchronizedSet(new java.util.HashSet<>());
    }

    @Override
    public Hologram getParent() {
        return parent;
    }

    @Override
    public List<HologramLine<?>> getLines() {
        return Collections.unmodifiableList(this.lines);
    }

    @Override
    public <C> HologramLine<C> addNewLine(C content, float height) {
        HologramLineImpl<C> line = new HologramLineImpl<>(this, content, height);
        this.lines.add(line);
        for (UUID uuid : this.parent.getViewerPages().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            HologramManager.getInstance().respawnHologram((HologramImpl) this.getParent(), player);
        }
        return line;
    }

    @Override
    public <C> HologramLine<C> changeLine(int index, C content, float height) {
        HologramLineImpl<C> line = new HologramLineImpl<>(this, content, height);
        this.lines.set(index, line);
        for (UUID uuid : this.getParent().getViewerPages().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            HologramManager.getInstance().respawnHologram((HologramImpl) this.getParent(), player);
        }
        return line;
    }

    @Override
    public void removeLine(int index) {
        this.lines.remove(index);
        for (UUID uuid : this.getParent().getViewerPages().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            HologramManager.getInstance().respawnHologram((HologramImpl) this.getParent(), player);
        }
    }

    @Override
    public <C> HologramLine<C> insertNewLine(int index, C content, float height) {
        HologramLineImpl<C> line = new HologramLineImpl<>(this, content, height);
        this.lines.add(index, line);
        for (UUID uuid : this.parent.getViewerPages().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            HologramManager.getInstance().respawnHologram((HologramImpl) this.getParent(), player);
        }
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
