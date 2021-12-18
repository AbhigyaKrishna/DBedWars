package com.pepedevs.dbedwars.api.nms;

public interface IGolem {

    IGolem clearDefaultPathfinding();

    IGolem addCustomDefaults();

    IGolem initTargets(double reachModifier);

    IGolem setChaseRadius(float radius);

}
