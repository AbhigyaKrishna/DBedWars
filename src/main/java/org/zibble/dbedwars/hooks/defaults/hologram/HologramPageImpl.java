package org.zibble.dbedwars.hooks.defaults.hologram;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.hooks.hologram.HologramEntityType;
import org.zibble.dbedwars.api.hooks.hologram.HologramLine;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;
import org.zibble.dbedwars.api.util.ClickAction;

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
    public HologramLine.Text addNewTextLine(Component text) {
        HologramLineImpl.Text line = new HologramLineImpl.Text(this, text);
        this.lines.add(line);
        for (UUID uuid : this.parent.getViewerPages().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            HologramManager.getInstance().respawnHologram((HologramImpl) this.getParent(), player);
        }
        return line;
    }

    @Override
    public HologramLine.Head addNewHeadLine(ItemStack itemStack) {
        HologramLineImpl.Head line = new HologramLineImpl.Head(this, itemStack);
        this.lines.add(line);
        for (UUID uuid : this.parent.getViewerPages().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            HologramManager.getInstance().respawnHologram((HologramImpl) this.getParent(), player);
        }
        return line;
    }

    @Override
    public HologramLine.SmallHead addNewSmallHeadLine(ItemStack itemStack) {
        HologramLineImpl.SmallHead line = new HologramLineImpl.SmallHead(this, itemStack);
        this.lines.add(line);
        for (UUID uuid : this.parent.getViewerPages().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            HologramManager.getInstance().respawnHologram((HologramImpl) this.getParent(), player);
        }
        return line;
    }

    @Override
    public HologramLine.Icon addNewIconLine(ItemStack itemStack) {
        HologramLineImpl.Icon line = new HologramLineImpl.Icon(this, itemStack);
        this.lines.add(line);
        for (UUID uuid : this.parent.getViewerPages().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            HologramManager.getInstance().respawnHologram((HologramImpl) this.getParent(), player);
        }
        return line;
    }

    @Override
    public HologramLine.Entity addNewEntityLine(HologramEntityType entityType) {
        HologramLineImpl.Entity line = new HologramLineImpl.Entity(this, entityType);
        this.lines.add(line);
        for (UUID uuid : this.parent.getViewerPages().keySet()) {
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
    public HologramLine.Text insertNewTextLine(int index, Component text) {
        HologramLineImpl.Text line = new HologramLineImpl.Text(this, text);
        this.lines.add(index, line);
        for (UUID uuid : this.parent.getViewerPages().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            HologramManager.getInstance().respawnHologram((HologramImpl) this.getParent(), player);
        }
        return line;
    }

    @Override
    public HologramLine.Head insertNewHeadLine(int index, ItemStack itemStack) {
        HologramLineImpl.Head line = new HologramLineImpl.Head(this, itemStack);
        this.lines.add(index, line);
        for (UUID uuid : this.parent.getViewerPages().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            HologramManager.getInstance().respawnHologram((HologramImpl) this.getParent(), player);
        }
        return line;
    }

    @Override
    public HologramLine.SmallHead insertNewSmallHeadLine(int index, ItemStack itemStack) {
        HologramLineImpl.SmallHead line = new HologramLineImpl.SmallHead(this, itemStack);
        this.lines.add(index, line);
        for (UUID uuid : this.parent.getViewerPages().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            HologramManager.getInstance().respawnHologram((HologramImpl) this.getParent(), player);
        }
        return line;
    }

    @Override
    public HologramLine.Icon insertNewIconLine(int index, ItemStack itemStack) {
        HologramLineImpl.Icon line = new HologramLineImpl.Icon(this, itemStack);
        this.lines.add(index, line);
        for (UUID uuid : this.parent.getViewerPages().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            HologramManager.getInstance().respawnHologram((HologramImpl) this.getParent(), player);
        }
        return line;
    }

    @Override
    public HologramLine.Entity insertNewEntityLine(int index, HologramEntityType entityType) {
        HologramLineImpl.Entity line = new HologramLineImpl.Entity(this, entityType);
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
