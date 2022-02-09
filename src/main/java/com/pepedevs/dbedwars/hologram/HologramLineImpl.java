package com.pepedevs.dbedwars.hologram;

import com.pepedevs.dbedwars.api.hologram.HologramClickAction;
import com.pepedevs.dbedwars.api.hologram.HologramLine;
import com.pepedevs.dbedwars.api.hologram.HologramPage;
import com.pepedevs.dbedwars.api.util.ClickType;
import com.pepedevs.dbedwars.hologram.utils.PacketUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

public class HologramLineImpl<C> extends HologramLine <C> {

    protected HologramPage parent;
    protected final Type type;
    protected BiFunction<Component, Player, Component> messageParse = (component, player) -> component;
    protected final int[] entityIds;
    protected C content;

    protected HologramLineImpl(Location location, Type type, C content) {
        super(location);
        this.type = type;
        this.entityIds = new int[2];
        this.entityIds[0] = PacketUtils.getFreeEntityId();
        this.content = content;
    }

    @Override
    public void show(Player... players) {
        for (Player player : players) {
            PacketUtils.hideFakeEntities(player, this.entityIds[0]);
        }
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

    @Override
    public void destroy() {
        this.hideAll();
        this.parent = null;
    }

    @Override
    public HologramPage getParent() {
        return parent;
    }

    @Override
    public void setParent(HologramPage page) {
        this.parent = page;
        if (page != null) {
            this.messageParse = this.parent.getParent().getSettings().getParser();
        } else {
            this.messageParse = (component, player) -> component;
        }
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
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

    @Override
    public C getContent() {
        return this.content;
    }

    @Override
    public void setContent(C content) {
        this.content = content;
    }

}

