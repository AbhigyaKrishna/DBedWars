package org.zibble.dbedwars.api.nms;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface IVillager {

    void spawn();

    void setDisplayName(String name);

    void teleport(Location location);

    void setLookAtPlayer(boolean flag);

    LivingEntity asBukkitEntity();
}
