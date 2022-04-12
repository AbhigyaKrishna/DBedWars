package org.zibble.dbedwars.task.implementations;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.objects.math.BoundingBox;
import org.zibble.dbedwars.api.objects.serializable.ParticleEffectASC;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.api.task.Workload;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.Random;
import java.util.Set;

public class SpongeAnimationTask implements Workload {

    // TODO ADD PARTICLE OPTION IN CONFIG AS WELL AS HERE
    private final DBedwars plugin;
    private final Location centre;
    private final int radius;
    private final boolean shouldRemoveOnEnd;
    private final SoundVP soundOnBoxIncrease;
    private final SoundVP soundOnAnimationEnd;
    private int tick = 0;
    private long timestamp = System.currentTimeMillis();
    private boolean done = false;

    public SpongeAnimationTask(DBedwars plugin, Block block, int radius, boolean shouldRemoveOnEnd, String soundOnBoxIncrease, String soundOnAnimationEnd) {
        this.plugin = plugin;
        this.centre = block.getLocation().clone().add(0.5, 0.5, 0.5);
        this.radius = radius;
        this.shouldRemoveOnEnd = shouldRemoveOnEnd;
        this.soundOnBoxIncrease = SoundVP.valueOf(soundOnBoxIncrease);
        this.soundOnAnimationEnd = SoundVP.valueOf(soundOnAnimationEnd);
    }

    @Override
    public void compute() {
        Random random = new Random();
        int k = tick / 8;
        BoundingBox largerBox = new BoundingBox(centre.clone().add(k, k, k).toVector(), centre.clone().subtract(k, k, k).toVector());
        BoundingBox smallBox = new BoundingBox(centre.clone().add(k - 1, k - 1, k - 1).toVector(), centre.clone().subtract(k + 1, k + 1, k + 1).toVector());
        Set<Block> largerBoxBlocks = largerBox.getBlocks(centre.getWorld());
        Set<Block> smallBoxBlocks = smallBox.getBlocks(centre.getWorld());
        largerBoxBlocks.removeIf(smallBoxBlocks::contains);
        smallBoxBlocks.forEach(block -> {
            ParticleEffectASC.of(ParticleEffect.CLOUD, 1, 0.01F)
                    .build()
                    .setLocation(block.getLocation().add(0.5 + random.nextGaussian() / 5, 0.5 + random.nextGaussian() / 5, 0.5 + random.nextGaussian() / 5))
                    .display();
            ParticleEffectASC.of(ParticleEffect.CLOUD, 1, 0.01F)
                    .build()
                    .setLocation(block.getLocation().add(0.5 + random.nextGaussian() / 5, 0.5 + random.nextGaussian() / 5, 0.5 + random.nextGaussian() / 5))
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
