package com.pepedevs.dbedwars.api.hologram.hologramline;

import com.pepedevs.dbedwars.api.hologram.HologramLine;
import com.pepedevs.radium.holograms.utils.PacketUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class IconHologramLine extends HologramLine<ItemStack> {


    public IconHologramLine(Location location, ItemStack item) {
        super(location, Type.ICON, item);
        this.entityIds[1] = PacketUtils.getFreeEntityId();
    }

    @Override
    public void show(Player... players) {
        for (Player player : players) {
            if (this.isVisible(player)) continue;

            PacketUtils.showFakeEntityArmorStand(player, this.getLocation(), this.entityIds[0], true, true, true);
            PacketUtils.showFakeEntityItem(player, this.getLocation(), this.getContent(), this.entityIds[1]);
            PacketUtils.attachFakeEntity(player, this.entityIds[0], this.entityIds[1]);
            this.viewers.add(player.getUniqueId());
        }
    }

    @Override
    public void update(Player... players) {}

    @Override
    public void hide(Player... players) {
        for (Player player : players) {
            if (!this.isVisible(player)) continue;

            PacketUtils.hideFakeEntities(player, this.entityIds[0], this.entityIds[1]);
            this.viewers.remove(player.getUniqueId());
        }
    }

    @Override
    public void hideAll() {
        for (UUID uuid : this.getViewers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;

            PacketUtils.hideFakeEntities(player, this.entityIds[0], this.entityIds[1]);
        }
        this.viewers.clear();
    }

    @Override
    public void destroy() {}
}
