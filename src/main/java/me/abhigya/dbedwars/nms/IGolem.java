package me.abhigya.dbedwars.nms;

public interface IGolem {

    IGolem clearDefaultPathfinding();

    IGolem addCustomDefaults();

    IGolem initTargets();

    IGolem setChaseRadius(float radius);


}
