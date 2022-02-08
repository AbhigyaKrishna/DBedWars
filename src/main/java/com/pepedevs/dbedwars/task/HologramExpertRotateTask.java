package com.pepedevs.dbedwars.task;

import com.pepedevs.radium.holograms.object.Hologram;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.task.CancellableTask;
import com.pepedevs.dbedwars.api.util.LocationXYZYP;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HologramExpertRotateTask extends HologramRotateTask {

    private final ConcurrentLinkedQueue<Map.Entry<LocationXYZYP, Integer>> frames;
    private final short delayMillis;

    public HologramExpertRotateTask(
            DBedwars plugin,
            Hologram hologram,
            LinkedHashMap<LocationXYZYP, Integer> frames,
            short delayMillis) {
        super(plugin, hologram);
        this.frames = new ConcurrentLinkedQueue<>(frames.entrySet());
        this.delayMillis = delayMillis;
    }

    @Override
    public void start() {
        if (this.frames.size() == 0) throw new IllegalStateException("No animations in hologram!");

        this.task = new HologramRotate(this.delayMillis);
        this.plugin.getThreadHandler().submitAsync(this.task);
    }

    private class HologramRotate extends CancellableTask {

        private final HologramExpertRotateTask task;
        private final short delayMillis;
        private Map.Entry<LocationXYZYP, Integer> frame;
        private int frameParts;
        private int partMove;

        private long lastExec;
        private long frameStart;

        protected HologramRotate(short delayMillis) {
            this.task = HologramExpertRotateTask.this;
            this.delayMillis = delayMillis;
            this.frame = new AbstractMap.SimpleEntry<>(null, 0);
            this.frameParts = 1;
        }

        @Override
        public void compute() {
            if (this.isCancelled()) return;

            this.lastExec = System.currentTimeMillis();

            if (this.lastExec - this.frameStart >= this.frame.getValue() * 50) {
                if (this.frame.getKey() != null) this.task.frames.add(this.frame);

                this.frame = this.task.frames.poll();
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
