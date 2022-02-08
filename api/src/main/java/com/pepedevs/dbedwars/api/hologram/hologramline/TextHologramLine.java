package com.pepedevs.dbedwars.api.hologram.hologramline;

import com.pepedevs.dbedwars.api.hologram.HologramLine;
import com.pepedevs.radium.holograms.utils.PacketUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TextHologramLine extends HologramLine<Component> {

    public TextHologramLine(Location location, Component content) {
        super(location, Type.TEXT, content);
        //        this.height = this.getParent().getParent().getSettings().getHeightText();
    }

    @Override
    public void show(Player... players) {
        for (Player player : players) {
            if (this.isVisible(player)) continue;

            PacketUtils.showFakeEntityArmorStand(
                    player, this.getLocation(), this.entityIds[0], true, true, true);
            PacketUtils.updateFakeEntityCustomName(
                    player, this.parse(this.getContent(), player), this.entityIds[0]);
            this.viewers.add(player.getUniqueId());
        }
    }

    @Override
    public void destroy() {}
}
