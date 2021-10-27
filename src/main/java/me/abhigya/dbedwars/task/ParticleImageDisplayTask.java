package me.abhigya.dbedwars.task;

import me.Abhigya.core.particle.particlelib.ParticleBuilder;
import me.Abhigya.core.util.tasks.Workload;
import org.bukkit.Location;

import java.awt.*;

public class ParticleImageDisplayTask implements Workload {

  private final int ticksToDisplayFor;
  private final Location topLeft;
  private final String axis;
  private final Color[][] imagePixels;
  private final double mainAxisMultiplier;
  private final double yAxisMultiplier;
  private final ParticleBuilder particle;

  public ParticleImageDisplayTask(
      int ticksToDisplayFor,
      Dimension dimension,
      Location topLeft,
      String mainAxisXorZ,
      Color[][] imagePixels,
      ParticleBuilder nonLocationParticleBuilder) {
    this.ticksToDisplayFor = ticksToDisplayFor;
    this.topLeft = topLeft.clone();
    if (!(mainAxisXorZ.equalsIgnoreCase("x") || mainAxisXorZ.equalsIgnoreCase("z")))
      throw new IllegalArgumentException("Not accepted value for axis");
    this.axis = mainAxisXorZ;
    if (imagePixels.length == 0) throw new IllegalArgumentException("Image has no pixels");
    this.imagePixels = imagePixels;
    this.mainAxisMultiplier = dimension.width / ((double) imagePixels.length);
    this.yAxisMultiplier = dimension.height / ((double) imagePixels[0].length);
    this.particle = nonLocationParticleBuilder;
  }

  private long timestamp = System.currentTimeMillis();
  private int tick = -1;

  @Override
  public void compute() {
    if (axis.equals("x"))
      for (int x = 0; x < imagePixels.length; x++) {
        for (int y = 0; y < imagePixels[0].length; y++) {
          if (imagePixels[x][y] != null) {
            particle
                .setLocation(topLeft.add(mainAxisMultiplier * x, -yAxisMultiplier * y, 0))
                .setColor(imagePixels[x][y])
                .display();
            topLeft.subtract(mainAxisMultiplier * x, -yAxisMultiplier * y, 0);
          }
        }
      }
  }

  @Override
  public boolean reSchedule() {
    return tick < ticksToDisplayFor;
  }

  @Override
  public boolean shouldExecute() {
    if (System.currentTimeMillis() - timestamp < 50) return false;
    tick++;
    timestamp = System.currentTimeMillis();
    return tick < ticksToDisplayFor;
  }
}
