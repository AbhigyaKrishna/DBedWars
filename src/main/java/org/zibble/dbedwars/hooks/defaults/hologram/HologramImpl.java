package org.zibble.dbedwars.hooks.defaults.hologram;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.mixin.ClickAction;
import org.zibble.dbedwars.api.objects.serializable.Duration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HologramImpl implements Hologram {

    private final HologramManager manager;
    private final Key key;

    private final List<HologramPageImpl> hologramPages;
    private final Map<UUID, Integer> viewerPages;
    private final Set<UUID> viewers;
    private final Set<ClickAction> clickActions;
    private Location location;
    private int displayRange = 48;
    private int updateRange = 48;
    private Duration updateInterval = Duration.ofSeconds(20);

    private boolean inverted = false;
    private boolean hasChangedContentType = false;

    private boolean clickRegistered;
    private boolean updateRegistered;
    private long lastUpdate;

    public HologramImpl(HologramManager manager, Key key, Location location) {
        this.manager = manager;
        this.key = key;
        this.hologramPages = Collections.synchronizedList(new ArrayList<>(1));
        this.viewerPages = new ConcurrentHashMap<>();
        this.clickActions = Collections.synchronizedSet(new HashSet<>());
        this.viewers = Collections.synchronizedSet(new HashSet<>());
        this.location = location;
        this.clickRegistered = true;
        this.updateRegistered = true;
        this.lastUpdate = System.currentTimeMillis();
    }

    @Override
    public HologramPage addPage() {
        HologramPageImpl hologramPage = new HologramPageImpl(this.manager, this.hologramPages.size(), this);
        this.hologramPages.add(hologramPage);
        return hologramPage;
    }

    @Override
    public List<HologramPage> getHologramPages() {
        return Collections.unmodifiableList(this.hologramPages);
    }

    @Override
    public Map<UUID, Integer> getViewerPages() {
        return Collections.unmodifiableMap(this.viewerPages);
    }

    @Override
    public void changeViewerPage(Player viewer, int page) {
        if (!this.isVisible(viewer)) return;
        HologramPageImpl currentPage = this.getCurrentPage(viewer);
        this.viewerPages.put(viewer.getUniqueId(), page);
        if (currentPage != null) {
            this.manager.respawnHologram(this, viewer);
        } else {
            this.manager.spawnHologram(this, viewer);
        }
    }

    @Override
    public HologramPageImpl getCurrentPage(Player viewer) {
        int i = this.viewerPages.getOrDefault(viewer.getUniqueId(), -1);
        return i != -1 ? this.hologramPages.get(i) : null;
    }

    @Override
    public int getDisplayRange() {
        return displayRange;
    }

    @Override
    public void setDisplayRange(int displayRange) {
        this.displayRange = displayRange;
    }

    @Override
    public int getUpdateRange() {
        return updateRange;
    }

    @Override
    public void setUpdateRange(int updateRange) {
        this.updateRange = updateRange;
    }

    @Override
    public Duration getUpdateInterval() {
        return updateInterval;
    }

    @Override
    public void setUpdateInterval(Duration updateInterval) {
        this.updateInterval = updateInterval;
    }

    @Override
    public boolean isInverted() {
        return inverted;
    }

    @Override
    public void setInverted(boolean inverted) {
        this.inverted = inverted;
        this.teleport(this.getLocation());
    }

    @Override
    public Set<ClickAction> getClickActions() {
        return Collections.unmodifiableSet(this.clickActions);
    }

    @Override
    public void addClickAction(ClickAction action) {
        this.clickActions.add(action);
    }

    @Override
    public boolean isVisible(Player player) {
        return this.viewers.contains(player.getUniqueId());
    }

    @Override
    public boolean isClickRegistered() {
        return clickRegistered;
    }

    @Override
    public void setClickRegistered(boolean clickRegistered) {
        this.clickRegistered = clickRegistered;
    }

    @Override
    public boolean isUpdateRegistered() {
        return updateRegistered;
    }

    @Override
    public void setUpdateRegistered(boolean updateRegistered) {
        this.updateRegistered = updateRegistered;
    }

    @Override
    public void show(Player... players) {
        for (Player player : players) {
            if (this.isVisible(player)) continue;
            this.viewers.add(player.getUniqueId());
            this.manager.spawnHologram(this, player);
        }
    }

    @Override
    public void updateContent(Player... players) {
        for (Player player : players) {
            this.manager.updateContent(this, player);
        }
    }

    @Override
    public void updateLocation(Player... players) {
        for (Player player : players) {
            this.manager.updateLocation(this, player);
        }
    }

    @Override
    public void hide(Player... players) {
        for (Player player : players) {
            this.manager.despawnHologram(this, player);
            this.viewerPages.remove(player.getUniqueId());
            this.viewers.remove(player.getUniqueId());
        }
    }

    @Override
    public void hideAll() {
        List<Player> players = new ArrayList<>();
        for (UUID viewer : this.viewers) {
            Player player = Bukkit.getPlayer(viewer);
            if (player != null) players.add(player);
            else {
                this.viewers.remove(viewer);
                this.viewerPages.remove(viewer);
            }
        }
        this.hide(players.toArray(new Player[0]));
    }

    @Override
    public void destroy() {
        this.hideAll();
        this.manager.getHolograms().remove(this.key);
    }

    @Override
    public Location getLocation() {
        return location.clone();
    }

    @Override
    public void teleport(Location location) {
        this.location = location;
        this.manager.updateLocation(this);
    }

    @Override
    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean hasChangedContentType() {
        return hasChangedContentType;
    }

    public void setHasChangedContentType(boolean hasChangedContentType) {
        this.hasChangedContentType = hasChangedContentType;
    }

    @Override
    public Key getKey() {
        return this.key;
    }

}
