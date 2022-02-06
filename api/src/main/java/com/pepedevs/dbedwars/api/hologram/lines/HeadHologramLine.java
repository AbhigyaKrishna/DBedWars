package com.pepedevs.dbedwars.api.hologram.lines;

import com.pepedevs.dbedwars.api.hologram.HologramLine;
import com.pepedevs.radium.holograms.utils.PacketUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HeadHologramLine extends HologramLine {

    private ItemStack content;

    public HeadHologramLine(Location location, ItemStack item) {
        super(location, HologramLineType.HEAD);
        this.content = item;
    }

    public ItemStack getContent() {
        return content;
    }

    public void setContent(ItemStack content) {
        this.content = content;
    }

    @Override
    public void show(Player... players) {
        for (Player player : players) {
            if (this.isVisible(player)) continue;

            PacketUtils.showFakeEntityArmorStand(player, this.getLocation(), this.entityIds[0], true, false, true);
            PacketUtils.helmetFakeEntity(player, this.content, this.entityIds[0]);
            this.viewers.add(player.getUniqueId());
        }
    }

    @Override
    public void update(Player... players) {}

    @Override
    public void destroy() {}
}
