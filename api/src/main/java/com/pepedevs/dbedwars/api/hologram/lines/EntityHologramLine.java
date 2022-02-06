package com.pepedevs.dbedwars.api.hologram.lines;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.pepedevs.dbedwars.api.hologram.HologramLine;
import com.pepedevs.radium.holograms.utils.HologramEntities;
import com.pepedevs.radium.holograms.utils.PacketUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EntityHologramLine extends HologramLine {

    private EntityType content;

    public EntityHologramLine(Location location, EntityType entity) {
        super(location, HologramLineType.ENTITY);
        Validate.isTrue(
                HologramEntities.isAllowed(entity),
                "EntityType `" + entity.name() + "` cannot be used in the hologram!");
        this.content = entity;
        this.entityIds[1] = PacketUtils.getFreeEntityId();
    }

    public EntityType getContent() {
        return this.content;
    }

    public void setContent(EntityType entity) {
        this.content = entity;
    }

    @Override
    public void show(Player... players) {
        for (Player player : players) {
            if (this.isVisible(player)) continue;
            PacketUtils.showFakeEntityArmorStand(player, this.getLocation(), this.entityIds[0], true, true, true);

            if (this.content.isAlive())
                PacketUtils.showFakeEntityLiving(player, this.getLocation(), EntityTypes.getByName(this.content.name()), this.entityIds[1]);
            else
                PacketUtils.showFakeEntity(player, this.getLocation(), EntityTypes.getByName(this.content.name()), this.entityIds[1]);

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
