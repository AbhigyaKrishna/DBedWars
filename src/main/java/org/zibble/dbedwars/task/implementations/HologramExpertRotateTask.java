package org.zibble.dbedwars.task.implementations;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.objects.hologram.HologramRotateTask;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.task.CancellableWorkload;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HologramExpertRotateTask extends HologramRotateTask {

    private final DBedwars plugin;
    private final ConcurrentLinkedQueue<Map.Entry<LocationXYZYP, Integer>> frames;
    private final short delayMillis;

    public HologramExpertRotateTask(
            DBedwars plugin,
            Hologram hologram,
            TaskEndAction endAction,
            LinkedHashMap<LocationXYZYP, Integer> frames,
            short delayMillis) {
        super(hologram, endAction);
        this.plugin = plugin;
        this.frames = new ConcurrentLinkedQueue<>(frames.entrySet());
        this.delayMillis = delayMillis;
    }

    @Override
    public void start() {
        if (this.frames.size() == 0) throw new IllegalStateException("No animations in hologram!");

        this.task = new HologramRotate(this.delayMillis);
        this.plugin.getThreadHandler().submitAsync(this.task);
    }

    private class HologramRotate extends CancellableWorkload {

        private final HologramExpertRotateTask task;
        private final short delayMillis;
        private Map.Entry<LocationXYZYP, Integer> frame;
        private int currentFrame;
        private int frameParts;
        private int partMove;

        private long lastExec;
        private long frameStart;

        protected HologramRotate(short delayMillis) {
            this.task = HologramExpertRotateTask.this;
            this.delayMillis = delayMillis;
            this.frame = new AbstractMap.SimpleEntry<>(null, 0);
            this.currentFrame = 0;
            this.frameParts = 1;
        }

        @Override
        public void compute() {
            if (this.isCancelled()) return;

            this.lastExec = System.currentTimeMillis();

            if (this.lastExec - this.frameStart >= this.frame.getValue() * 50) {
                if (this.frame.getKey() != null) this.task.frames.add(this.frame);

                if (this.currentFrame == this.task.frames.size()) {
                    if (this.task.endAction == TaskEndAction.REVERSE) {
                        List<Map.Entry<LocationXYZYP, Integer>> l = new ArrayList<>(this.task.frames);
                        Collections.reverse(l);
                        this.task.frames.clear();
                        this.task.frames.addAll(l);

                        this.currentFrame = 0;
                    }
                }

                this.frame = this.task.frames.poll();
                this.currentFrame++;
                this.frameParts = (int) Math.ceil(this.frame.getValue() * 50 / this.delayMillis);
                this.partMove = 1;
                this.frameStart = System.currentTimeMillis();
            }

            this.task.rotateAndMoveHologram(
                    this.frame.getKey().getX() / this.frameParts * this.partMove,
                    this.frame.getKey().getY() / this.frameParts * this.partMove,
                    this.frame.getKey().getZ() / this.frameParts * this.partMove,
                    this.frame.getKey().getYaw() / this.frameParts * this.partMove,
                    this.frame.getKey().getPitch() / this.frameParts * this.partMove);

            this.partMove++;
        }

        @Override
        public boolean shouldExecute() {
            return !this.isCancelled() && System.currentTimeMillis() - lastExec > this.delayMillis;
        }

    }

}
