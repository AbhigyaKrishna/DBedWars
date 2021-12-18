package com.pepedevs.dbedwars.api.nms;

public interface IBedBug {

    IBedBug clearDefaultPathfinding();

    IBedBug addCustomDefaults();

    IBedBug initTargets(double reachModifier);

    IBedBug setChaseRadius(float radius);
}
