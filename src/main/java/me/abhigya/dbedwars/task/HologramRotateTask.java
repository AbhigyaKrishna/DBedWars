package me.abhigya.dbedwars.task;

import me.Abhigya.core.util.hologram.Hologram;
import me.Abhigya.core.util.tasks.Workload;
import me.abhigya.dbedwars.DBedwars;
import org.bukkit.Location;


public class HologramRotateTask {

  // TODO ADD EXPERT ROTATION
  // TODO HOOK INTO BEDWARS GAME

  private final Hologram hologram;
  private final DBedwars plugin;

  public HologramRotateTask(DBedwars plugin, Hologram initialisedHologram) {
    this.plugin = plugin;
    this.hologram = initialisedHologram;
  }

  public void startRotation(
      float degreeRotatedPerCycle,
      int ticksPerAnimationCycle,
      String onAnimationEnd,
      boolean slowDownAtEnd,
      float verticalMovement) {
    if (slowDownAtEnd) {
      float horizontalAcceleration =
          (4 * degreeRotatedPerCycle) / (ticksPerAnimationCycle * ticksPerAnimationCycle);
      float verticalAcceleration =
          (4 * verticalMovement) / (ticksPerAnimationCycle * ticksPerAnimationCycle);
      plugin
          .getThreadHandler()
          .addAsyncWork(
              new Workload() {

                int tick = 0;
                int revert = 1;
                long timestamp = System.currentTimeMillis();

                @Override
                public void compute() {
                  timestamp = System.currentTimeMillis();
                  if (tick >= ticksPerAnimationCycle) {
                    tick = 0;
                    if (onAnimationEnd.equalsIgnoreCase("reverse")) {
                      if (revert == 1) {
                        revert = -1;
                      } else if (revert == -1) {
                        revert = 1;
                      }
                    }
                  }
                  if (tick >= ticksPerAnimationCycle / 2) {
                    tick++;
                    rotateHologram(
                        revert
                            * (horizontalAcceleration / 2)
                            * ((2 * (ticksPerAnimationCycle - tick)) + 1),
                        (revert
                            * (verticalAcceleration / 2)
                            * ((2 * (ticksPerAnimationCycle - tick)) + 1)));
                  } else {
                    tick++;
                    rotateHologram(
                        revert * (horizontalAcceleration / 2) * ((2 * tick) + 1),
                        (revert * (verticalAcceleration / 2) * ((2 * tick) + 1)));
                  }
                }

                @Override
                public boolean reSchedule() {
                  return true;
                }

                @Override
                public boolean shouldExecute() {
                  return System.currentTimeMillis() - timestamp >= 50;
                }
              });
    } else {
      plugin
          .getThreadHandler()
          .addAsyncWork(
              new Workload() {

                long timestamp = System.currentTimeMillis();
                int revert = 1;
                int tick = 0;

                @Override
                public void compute() {
                  tick++;
                  timestamp = System.currentTimeMillis();
                  rotateAndMoveHologram(
                      0,
                      revert * verticalMovement / ticksPerAnimationCycle,
                      0,
                      revert * degreeRotatedPerCycle / ticksPerAnimationCycle,
                      0);
                  if (tick >= ticksPerAnimationCycle) {
                    tick = 0;
                    if (onAnimationEnd.equalsIgnoreCase("reverse")) {
                      if (revert == 1) {
                        revert = -1;
                      } else if (revert == -1) {
                        revert = 1;
                      }
                    }
                  }
                }

                @Override
                public boolean reSchedule() {
                  return true;
                }

                @Override
                public boolean shouldExecute() {
                  return System.currentTimeMillis() - timestamp >= 50;
                }
              });
    }
  }

  public void startRotation(float x, float y, float z, float yaw, float pitch, int animationTicks) {
    plugin
        .getThreadHandler()
        .addAsyncWork(
            new Workload() {

              long timestamp = System.currentTimeMillis();
              int tick = 0;

              @Override
              public void compute() {
                timestamp = System.currentTimeMillis();
                tick++;
                rotateAndMoveHologram(
                    x / animationTicks,
                    y / animationTicks,
                    z / animationTicks,
                    yaw / animationTicks,
                    pitch / animationTicks);
              }

              @Override
              public boolean reSchedule() {
                return tick <= animationTicks;
              }

              @Override
              public boolean shouldExecute() {
                return System.currentTimeMillis() - timestamp >= 50;
              }
            });
  }

  private void rotateAndMoveHologram(double x, double y, double z, float yaw, float pitch) {
    Location location = hologram.getLocation().clone();
    location.setX(location.getX() + x);
    location.setY(location.getY() + y);
    location.setZ(location.getZ() + z);
    location.setYaw(location.getYaw() + yaw);
    location.setPitch(location.getPitch() + pitch);
    hologram.teleport(location);
  }

  private void rotateHologram(float degreeRotate, float verticalAdd) {
    Location location = hologram.getLocation().clone();
    location.setYaw(location.getYaw() + degreeRotate);
    location.setY(location.getY() + verticalAdd);
    hologram.teleport(location);
  }

  /*public void startExpertRotation(LinkedHashMap<Location, Integer> map){
      AtomicInteger totalDelay = new AtomicInteger(0);
      map.forEach((key, value) -> {
          idkBruh(key, totalDelay.get());
          totalDelay.addAndGet(value);
          System.out.println(totalDelay);
      });
  }

  private void idkBruh(Location location, int ticks){
      plugin.getThreadHandler().addAsyncWork(new Workload() {
          int tick = 1;
          long timestamp = System.currentTimeMillis();
          @Override
          public void compute() {
              System.out.println("HMM" + System.currentTimeMillis());
              startRotation((float) location.getX(),(float) location.getY(),(float) location.getZ(),location.getYaw(),location.getPitch(),ticks);
          }

          @Override
          public boolean reSchedule() {
              return tick<=ticks;
          }

          @Override
          public boolean shouldExecute() {
              if (System.currentTimeMillis()-timestamp<=50){
                  return false;
              }
              if (ticks == 0){
                  return true;
              }
              timestamp = System.currentTimeMillis();
              tick++;
              return tick>=ticks;
          }
      });
  }*/

}
