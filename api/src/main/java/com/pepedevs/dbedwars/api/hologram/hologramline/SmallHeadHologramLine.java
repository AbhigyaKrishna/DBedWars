package com.pepedevs.dbedwars.api.hologram.hologramline;

import com.pepedevs.dbedwars.api.hologram.HologramLine;
import com.pepedevs.radium.holograms.utils.PacketUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SmallHeadHologramLine extends HologramLine<ItemStack> {

    public SmallHeadHologramLine(Location location, ItemStack item) {
        super(location, Type.SMALL_HEAD, item);
    }

    @Override
    public void show(Player... players) {
        for (Player player : players) {
            if (this.isVisible(player)) continue;

            PacketUtils.showFakeEntityArmorStand(
                    player, this.getLocation(), this.entityIds[0], true, true, true);
            PacketUtils.helmetFakeEntity(player, this.getContent(), this.entityIds[0]);
            this.viewers.add(player.getUniqueId());
        }
    }

    @Override
    public void update(Player... players) {}

    @Override
    public void destroy() {}
}
