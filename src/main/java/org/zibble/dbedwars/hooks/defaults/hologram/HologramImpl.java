package org.zibble.dbedwars.hooks.defaults.hologram;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;
import org.zibble.dbedwars.api.util.ClickAction;
import org.zibble.dbedwars.api.util.Duration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HologramImpl implements Hologram {

    private final List<HologramPageImpl> hologramPages;
    private final Map<UUID, Integer> viewerPages;
    private final Set<UUID> viewers;
    private Location location;

    private final Set<ClickAction> clickActions;

    private int displayRange = 48;
    private int updateRange = 48;
    private Duration updateInterval = Duration.ofSeconds(20);
    private boolean inverted  = false;

    public HologramImpl(Location location) {
        this.hologramPages = Collections.synchronizedList(new ArrayList<>(1));
        this.viewerPages = new ConcurrentHashMap<>();
        this.clickActions = Collections.synchronizedSet(new HashSet<>());
        this.viewers = Collections.synchronizedSet(new HashSet<>());
        this.location = location;
    }

    @Override
    public HologramPage addPage() {
        HologramPageImpl hologramPage = new HologramPageImpl(this);
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
    public void changeViewerPage(UUID viewer, int page) {
        this.viewerPages.put(viewer, page);
        Player player = Bukkit.getPlayer(viewer);
        if (player != null)
            this.show(player);
    }

    @Override
    public int getCurrentPage(UUID viewer) {
        return this.viewerPages.get(viewer);
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
    public boolean isVisible(Player player) {
        return this.viewers.contains(player.getUniqueId());
    }

    public void show(Player... players) {
        for (Player player : players) {
            HologramManager.getInstance().respawnHologram(this, player);
        }
    }

    public void updateContent(Player... players) {
        for (Player player : players) {
            HologramManager.getInstance().updateContent(this, player);
        }
    }

    public void updateLocation(Player... players) {
        for (Player player : players) {
            HologramManager.getInstance().updateLocation(this, player);
        }
    }

    public void hide(Player... players) {
        for (Player player : players) {
            this.viewerPages.remove(player.getUniqueId());
            this.viewers.remove(player.getUniqueId());
            HologramManager.getInstance().despawnHologram(this, player);
        }
    }

    public void hideAll() {
        List<Player> players = new ArrayList<>();
        for (UUID viewer : this.viewers) {
            Player player = Bukkit.getPlayer(viewer);
            if (player != null) players.add(player);
            else  {
                this.viewers.remove(viewer);
                this.viewerPages.remove(viewer);
            }
        }
        this.hide(players.toArray(new Player[0]));
    }

    public void destroy() {
        this.hideAll();
    }

    @Override
    public Location getLocation() {
        return location.clone();
    }

    @Override
    public void teleport(Location location) {
        this.location = location;
        for (UUID viewer : this.viewers) {
            Player player = Bukkit.getPlayer(viewer);
            if (player == null) continue;
            HologramManager.getInstance().updateLocation(this, player);
        }
    }
}
