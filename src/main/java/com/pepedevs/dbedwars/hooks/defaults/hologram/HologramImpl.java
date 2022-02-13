package com.pepedevs.dbedwars.hooks.defaults.hologram;

import com.pepedevs.dbedwars.api.hooks.hologram.Hologram;
import com.pepedevs.dbedwars.api.hooks.hologram.HologramPage;
import com.pepedevs.dbedwars.api.hooks.hologram.HologramLine;
import com.pepedevs.dbedwars.api.util.ClickAction;
import com.pepedevs.dbedwars.api.util.Duration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HologramImpl implements Hologram {

    private final List<HologramPageImpl> hologramPages;
    private final Map<UUID, Integer> viewerPages;
    private final List<UUID> viewers;
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
        this.viewers = Collections.synchronizedList(new ArrayList<>());
        this.location = location;
    }

    @Override
    public HologramPage addPage() {
        HologramPageImpl hologramPage = new HologramPageImpl(this, this.hologramPages.size());
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
    }

    @Override
    public Set<ClickAction> getClickActions() {
        return Collections.unmodifiableSet(this.clickActions);
    }

    @Override
    public boolean isVisible(Player player) {
        return this.viewers.contains(player.getUniqueId());
    }

    public void show(int page, Player... players) {
        HologramPageImpl holoPage = this.hologramPages.get(page);
        for (Player player : players) {

            if (this.viewerPages.containsKey(player.getUniqueId())) {
                HologramPageImpl shown = this.hologramPages.get(this.viewerPages.get(player.getUniqueId()));
                for (HologramLine<?> line : shown.getLines()) {
                    ((HologramLineImpl<?>) line).hide(player);
                }
            }
            this.viewerPages.put(player.getUniqueId(), page);
            this.viewers.add(player.getUniqueId());
            for (HologramLine<?> line : holoPage.getLines()) {
                ((HologramLineImpl<?>) line).show(player);
            }
        }
    }

    public void update(Player... players) {
        for (Player player : players) {
            if (!this.isVisible(player)) continue;
            int page = this.viewerPages.get(player.getUniqueId());
            HologramPageImpl pg = this.hologramPages.get(page);
            for (HologramLine<?> line : pg.getLines()) {
                ((HologramLineImpl<?>) line).update(player);
            }
        }
    }

    public void hide(Player... players) {
        for (Player player : players) {
            int index = this.viewerPages.getOrDefault(player.getUniqueId(), -1);
            if (index == -1) continue;
            HologramPageImpl page = this.hologramPages.get(index);
            page.getLines().forEach(line -> ((HologramLineImpl<?>) line).hide(player));
            this.viewers.remove(player.getUniqueId());
            this.viewerPages.remove(player.getUniqueId());
        }
    }

    public void hideAll() {
        List<Player> players = new ArrayList<>();
        for (UUID viewer : this.viewers) {
            Player player = Bukkit.getPlayer(viewer);
            if (player != null) players.add(player);
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

    }
}
