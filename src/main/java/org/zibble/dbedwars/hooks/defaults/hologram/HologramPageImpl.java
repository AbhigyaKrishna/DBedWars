package org.zibble.dbedwars.hooks.defaults.hologram;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.hooks.hologram.HologramEntityType;
import org.zibble.dbedwars.api.hooks.hologram.HologramLine;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.api.util.mixin.ClickAction;

import java.util.*;

public class HologramPageImpl implements HologramPage {

    protected final HologramImpl parent;
    private final int page;
    private final HologramManager manager;
    private final List<HologramLineImpl<?>> lines;
    private final Set<ClickAction> actions;

    private double lineGap = 0.1;

    public HologramPageImpl(HologramManager manager, int page, HologramImpl parent) {
        this.manager = manager;
        this.page = page;
        this.parent = parent;
        this.lines = Collections.synchronizedList(new ArrayList<>());
        this.actions = Collections.synchronizedSet(new java.util.HashSet<>());
    }

    @Override
    public Hologram getParent() {
        return parent;
    }

    @Override
    public int getPageNumber() {
        return this.page;
    }

    @Override
    public List<HologramLine<?>> getLines() {
        return Collections.unmodifiableList(this.lines);
    }

    @Override
    public void clearLines() {
        this.lines.clear();
    }

    @Override
    public HologramLine.Text addNewTextLine(Message text) {
        HologramLineImpl.Text line = new HologramLineImpl.Text(this.manager, this, text);
        this.lines.add(line);
        this.parent.setHasChangedContentType(true);
        this.manager.respawnHologram(this.parent);
        return line;
    }

    @Override
    public HologramLine.Head addNewHeadLine(BwItemStack itemStack) {
        HologramLineImpl.Head line = new HologramLineImpl.Head(this.manager, this, itemStack);
        this.lines.add(line);
        this.parent.setHasChangedContentType(true);
        this.manager.respawnHologram(this.parent);
        return line;
    }

    @Override
    public HologramLine.SmallHead addNewSmallHeadLine(BwItemStack itemStack) {
        HologramLineImpl.SmallHead line = new HologramLineImpl.SmallHead(this.manager, this, itemStack);
        this.lines.add(line);
        this.parent.setHasChangedContentType(true);
        this.manager.respawnHologram(this.parent);
        return line;
    }

    @Override
    public HologramLine.Icon addNewIconLine(BwItemStack itemStack) {
        HologramLineImpl.Icon line = new HologramLineImpl.Icon(this.manager, this, itemStack);
        this.lines.add(line);
        this.parent.setHasChangedContentType(true);
        this.manager.respawnHologram(this.parent);
        return line;
    }

    @Override
    public HologramLine.Entity addNewEntityLine(HologramEntityType entityType) {
        HologramLineImpl.Entity line = new HologramLineImpl.Entity(this.manager, this, entityType);
        this.lines.add(line);
        this.parent.setHasChangedContentType(true);
        this.manager.respawnHologram(this.parent);
        return line;
    }

    @Override
    public void removeLine(int index) {
        this.lines.remove(index);
        for (UUID uuid : this.getParent().getViewerPages().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            this.manager.respawnHologram((HologramImpl) this.getParent(), player);
        }
    }

    @Override
    public HologramLine.Text insertNewTextLine(int index, Message text) {
        HologramLineImpl.Text line = new HologramLineImpl.Text(this.manager, this, text);
        this.lines.add(index, line);
        this.parent.setHasChangedContentType(true);
        this.manager.respawnHologram(this.parent);
        return line;
    }

    @Override
    public HologramLine.Head insertNewHeadLine(int index, BwItemStack itemStack) {
        HologramLineImpl.Head line = new HologramLineImpl.Head(this.manager, this, itemStack);
        this.lines.add(index, line);
        this.parent.setHasChangedContentType(true);
        this.manager.respawnHologram(this.parent);
        return line;
    }

    @Override
    public HologramLine.SmallHead insertNewSmallHeadLine(int index, BwItemStack itemStack) {
        HologramLineImpl.SmallHead line = new HologramLineImpl.SmallHead(this.manager, this, itemStack);
        this.lines.add(index, line);
        this.parent.setHasChangedContentType(true);
        this.manager.respawnHologram(this.parent);
        return line;
    }

    @Override
    public HologramLine.Icon insertNewIconLine(int index, BwItemStack itemStack) {
        HologramLineImpl.Icon line = new HologramLineImpl.Icon(this.manager, this, itemStack);
        this.lines.add(index, line);
        this.parent.setHasChangedContentType(true);
        this.manager.respawnHologram(this.parent);
        return line;
    }

    @Override
    public HologramLine.Entity insertNewEntityLine(int index, HologramEntityType entityType) {
        HologramLineImpl.Entity line = new HologramLineImpl.Entity(this.manager, this, entityType);
        this.lines.add(index, line);
        this.parent.setHasChangedContentType(true);
        this.manager.respawnHologram(this.parent);
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
