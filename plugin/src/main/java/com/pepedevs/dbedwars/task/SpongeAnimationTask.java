package com.pepedevs.dbedwars.task;

import com.pepedevs.corelib.particle.particlelib.ParticleBuilder;
import com.pepedevs.corelib.particle.particlelib.ParticleEffect;
import com.pepedevs.corelib.utils.math.LocationUtils;
import com.pepedevs.corelib.task.Workload;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.util.SoundVP;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpongeAnimationTask implements Workload {

    // TODO ADD PARTICLE OPTION IN CONFIG AS WELL AS HERE
    private final DBedwars plugin;
    private final Location centre;
    private final int radius;
    private final boolean shouldRemoveOnEnd;
    private final SoundVP soundOnBoxIncrease;
    private final SoundVP soundOnAnimationEnd;

    public SpongeAnimationTask(
            DBedwars plugin,
            Block block,
            int radius,
            boolean shouldRemoveOnEnd,
            String soundOnBoxIncrease,
            String soundOnAnimationEnd) {
        this.plugin = plugin;
        this.centre = block.getLocation().clone().add(0.5, 0.5, 0.5);
        this.radius = radius;
        this.shouldRemoveOnEnd = shouldRemoveOnEnd;
        this.soundOnBoxIncrease = SoundVP.valueOf(soundOnBoxIncrease);
        this.soundOnAnimationEnd = SoundVP.valueOf(soundOnAnimationEnd);
    }

    private int tick = 0;
    private long timestamp = System.currentTimeMillis();
    private boolean done = false;

    @Override
    public void compute() {
        Random random = new Random();
        int k = tick / 8;
        List<Block> largerBox =
                new ArrayList<>(
                        LocationUtils.getBlocksBetween(
                                centre.clone().add(k, k, k), centre.clone().subtract(k, k, k)));
        List<Block> smallBox =
                new ArrayList<>(
                        LocationUtils.getBlocksBetween(
                                centre.clone().add(k - 1, k - 1, k - 1),
                                centre.clone().subtract(k - 1, k - 1, k - 1)));
        largerBox.removeIf(smallBox::contains);
        largerBox.forEach(
                block -> {
                    new ParticleBuilder(
                                    ParticleEffect.CLOUD,
                                    block.getLocation()
                                            .add(
                                                    0.5 + random.nextGaussian() / 5,
                                                    0.5 + random.nextGaussian() / 5,
                                                    0.5 + random.nextGaussian() / 5))
                            .setSpeed(0.01F)
                            .setAmount(1)
                            .display();
                    new ParticleBuilder(
                                    ParticleEffect.CLOUD,
                                    block.getLocation()
                                            .add(
                                                    0.5 + random.nextGaussian() / 5,
                                                    0.5 + random.nextGaussian() / 5,
                                                    0.5 + random.nextGaussian() / 5))
                            .setSpeed(0.01F)
                            .setAmount(1)
                            .display();
                });
        soundOnBoxIncrease.play(centre);
    }

    @Override
    public boolean reSchedule() {
        if (tick > (radius * 8) + 6) {
            if (!this.done) {
                soundOnAnimationEnd.play(centre);
                if (shouldRemoveOnEnd)
                    plugin.getThreadHandler()
                            .submitSync(() -> centre.getBlock().setType(Material.AIR));
                this.done = true;
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldExecute() {
        if (System.currentTimeMillis() - timestamp < 50) return false;
        timestamp = System.currentTimeMillis();
        tick++;
        return tick % 8 == 0;
    }
}
