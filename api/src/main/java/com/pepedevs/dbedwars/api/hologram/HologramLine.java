package com.pepedevs.dbedwars.api.hologram;

import com.pepedevs.dbedwars.api.hologram.lines.HologramLineType;
import com.pepedevs.dbedwars.api.util.ClickType;
import com.pepedevs.radium.holograms.utils.PacketUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

public abstract class HologramLine extends AbstractHologram {

    protected HologramPage parent;
    protected final HologramLineType type;
    protected BiFunction<Component, Player, Component> messageParse = (component, player) -> component;
    protected final int[] entityIds;

    protected HologramLine(Location location, HologramLineType type) {
        super(location);
        this.type = type;
        this.entityIds = new int[2];
        this.entityIds[0] = PacketUtils.getFreeEntityId();
    }

    @Override
    public void update(Player... players) {
        for (Player player : players) {
            if (!this.isVisible(player)) continue;
            this.hide(player);
            this.show(player);
        }
    }

    @Override
    public void hide(Player... players) {
        for (Player player : players) {
            if (!this.isVisible(player)) continue;
            PacketUtils.hideFakeEntities(player, this.entityIds[0]);
            this.viewers.remove(player.getUniqueId());
        }
    }

    @Override
    public void hideAll() {
        for (UUID uuid : this.getViewers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;

            PacketUtils.hideFakeEntities(player, this.entityIds[0]);
        }
        this.viewers.clear();
    }

    public HologramPage getParent() {
        return parent;
    }

    protected void setParent(HologramPage page) {
        this.parent = page;
        if (page != null) {
            this.messageParse = this.parent.getParent().getSettings().getMessageParser();
        } else {
            this.messageParse = (component, player) -> component;
        }
    }

    public HologramLineType getType() {
        return this.type;
    }

    protected int[] getEntityIds() {
        return this.entityIds;
    }

    @Override
    public boolean handleClick(Player player, int entityId, ClickType clickType) {
        if (!this.isVisible(player)) return false;

        for (int id : this.entityIds) {
            if (id == entityId) {
                for (HologramClickAction action : this.actions) {
                    action.onClick(player, clickType);
                }
                return true;
            }
        }
        return false;
    }

    protected Component parse(Component text, Player player) {
        return this.messageParse.apply(text, player);
    }

    protected List<Component> parse(List<Component> text, Player player) {
        List<Component> list = new ArrayList<>();
        for (Component s : text) {
            list.add(this.messageParse.apply(s, player));
        }
        return list;
    }
}