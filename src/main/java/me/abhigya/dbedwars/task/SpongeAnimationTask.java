package me.abhigya.dbedwars.task;

import me.Abhigya.core.particle.particlelib.ParticleBuilder;
import me.Abhigya.core.particle.particlelib.ParticleEffect;
import me.Abhigya.core.util.tasks.Workload;
import org.bukkit.block.Block;

public class SpongeAnimationTask implements Workload {

    private final Block block;
    private int animationTicks;
    private int ticksPerDistance;
    private final int actualAnimationTicks;

    public SpongeAnimationTask(Block block, int animationTicks, double animationRadius){
        this.block = block;
        this.animationTicks = animationTicks;
        this.ticksPerDistance = (int) (animationTicks/animationRadius);
        actualAnimationTicks = ((animationTicks/ticksPerDistance)*ticksPerDistance + ticksPerDistance);
    }

    private long timestamp = System.currentTimeMillis();
    private int tick = 0;
    private int cycleTick = 0;
    private int currentRadius = 1;

    @Override
    public void compute() {
        new ParticleBuilder(ParticleEffect.CLOUD,block.getLocation().add(0.5,0,0.5).add(currentRadius,0,0)).setAmount(1).setSpeed(0).display();
        System.out.println("RUN");
        currentRadius++;
    }

    @Override
    public boolean reSchedule() {
        return this.tick<actualAnimationTicks;
    }

    @Override
    public boolean shouldExecute() {
        if(System.currentTimeMillis()-timestamp<50)
            return false;
        timestamp = System.currentTimeMillis();
        tick++;
        cycleTick++;
        System.out.println(timestamp + "--" + tick + "--" + cycleTick);
        if (cycleTick<ticksPerDistance)
            return false;
        cycleTick = 0;
        return true;
    }
}
