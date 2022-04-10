package org.zibble.dbedwars.api.nms;

import org.bukkit.entity.Silverfish;

public interface IBedBug {

    IBedBug clearDefaultPathfinding();

    IBedBug addCustomDefaults();

    IBedBug initTargets(double reachModifier);

    IBedBug setChaseRadius(float radius);

    Silverfish getSilverFish();

}
