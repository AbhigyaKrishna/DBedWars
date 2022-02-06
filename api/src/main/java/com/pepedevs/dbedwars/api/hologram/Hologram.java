package com.pepedevs.dbedwars.api.hologram;

import com.pepedevs.dbedwars.api.util.ClickType;
import com.pepedevs.radium.holograms.HologramManager;
import com.pepedevs.radium.holograms.object.HologramSettings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Hologram extends AbstractHologram {

    protected final String id;
    protected HologramSettings settings;
    protected final List<HologramPage> pages;
    protected final Map<UUID, Integer> viewerPages;

    protected Hologram(String id, Location location) {
        super(location);
        this.id = id;
        this.settings = new HologramSettings();
        this.pages = new LinkedList<>();
        this.viewerPages = new ConcurrentHashMap<>();
        this.addPage();
    }

    public String getId() {
        return this.id;
    }

    @Override
    public void show(Player... players) {
        this.show(0, players);
    }

    public void show(int page, Player... players) {
        HologramPage holoPage = this.pages.get(page);
        for (Player player : players) {
            if (this.viewerPages.containsKey(player.getUniqueId())) {
                HologramPage shown = this.pages.get(this.viewerPages.get(player.getUniqueId()));
                for (HologramLine line : shown.getLines()) {
                    line.hide(player);
                }
            }
            this.viewerPages.put(player.getUniqueId(), page);
            this.viewers.add(player.getUniqueId());
            for (HologramLine line : holoPage.getLines()) {
                line.show(player);
            }
        }
    }

    public void showAll() {
        this.show(Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }

    @Override
    public void update(Player... players) {
        for (Player player : players) {
            if (!this.isVisible(player)) continue;
            int page = this.viewerPages.get(player.getUniqueId());
            HologramPage pg = this.pages.get(page);
            for (HologramLine line : pg.getLines()) {
                line.update(player);
            }
        }
    }

    @Override
    public void hide(Player... players) {
        for (Player player : players) {
            int index = this.viewerPages.getOrDefault(player.getUniqueId(), -1);
            if (index == -1) continue;
            HologramPage page = this.pages.get(index);
            page.getLines().forEach(line -> line.hide(player));
            this.viewers.remove(player.getUniqueId());
            this.viewerPages.remove(player.getUniqueId());
        }
    }

    @Override
    public void hideAll() {
        for (UUID uuid : this.getViewers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;

            int index = this.viewerPages.getOrDefault(uuid, -1);
            if (index == -1) continue;

            HologramPage page = this.pages.get(index);
            page.getLines().forEach(line -> line.hide(player));
        }
        this.viewers.clear();
        this.viewerPages.clear();
    }

    @Override
    public void destroy() {
        this.hideAll();
        HologramManager.get().deleteHologram(this.id);
    }

    public HologramPage addPage() {
        HologramPage page = new HologramPage(this, pages.size());
        pages.add(page);
        return page;
    }

    public void changePage(int page, Player player) {
        this.show(page, player);
    }

    public HologramSettings getSettings() {
        return this.settings;
    }

    public void setSettings(HologramSettings settings) {
        this.settings = settings;
    }

    public List<HologramPage> getPages() {
        return Collections.unmodifiableList(this.pages);
    }

    @Override
    public boolean handleClick(Player player, int entityId, ClickType clickType) {
        if (!this.isVisible(player)) return false;

        HologramPage page = this.pages.get(this.viewerPages.get(player.getUniqueId()));
        for (HologramLine line : page.getLines()) {
            if (line.handleClick(player, entityId, clickType)) {
                for (HologramClickAction action : page.getActions()) {
                    action.onClick(player, clickType);
                }
                for (HologramClickAction action : this.actions) {
                    action.onClick(player, clickType);
                }
                return true;
            }
        }
        return false;
    }
}

