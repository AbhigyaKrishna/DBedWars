package com.pepedevs.dbedwars.api.hologram;

import com.pepedevs.dbedwars.api.util.ClickType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class HologramObject {

    protected final Set<UUID> viewers;
    protected Location location;
    protected final Set<HologramClickAction> actions;

    protected HologramObject(Location location) {
        this.location = location;
        this.location.setPitch(0.0f);
        this.viewers = Collections.synchronizedSet(new HashSet<>());
        this.actions = Collections.synchronizedSet(new HashSet<>());
    }

    public abstract void show(Player... players);

    public abstract void update(Player... players);

    public abstract void hide(Player... players);

    public abstract void hideAll();

    public abstract void destroy();

    public abstract boolean handleClick(Player player, int entityId, ClickType clickType);

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

    public Set<UUID> getViewers() {
        return Collections.unmodifiableSet(this.viewers);
    }

    public boolean isVisible(Player player) {
        return this.viewers.contains(player.getUniqueId());
    }

    public Location getLocation() {
        return this.location;
    }
}

