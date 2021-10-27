package me.abhigya.dbedwars.nms;

public interface IGolem {

  IGolem clearDefaultPathfinding();

  IGolem addCustomDefaults();

  IGolem initTargets(double reachModifier);

  IGolem setChaseRadius(float radius);
}
