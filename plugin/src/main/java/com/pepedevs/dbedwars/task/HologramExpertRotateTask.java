package com.pepedevs.dbedwars.task;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.task.CancellableTask;
import com.pepedevs.dbedwars.api.util.LocationXYZYP;
import me.Abhigya.core.util.hologram.Hologram;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class HologramExpertRotateTask extends HologramRotateTask {

    private final LinkedList<Map.Entry<LocationXYZYP, Integer>> frames;
    private final short delayMillis;

    public HologramExpertRotateTask(
            DBedwars plugin,
            Hologram hologram,
            LinkedHashMap<LocationXYZYP, Integer> frames,
            short delayMillis) {
        super(plugin, hologram);
        this.frames = new LinkedList<>(frames.entrySet());
        this.delayMillis = delayMillis;
    }

    @Override
    public void start() {
        if (this.frames.size() == 0) throw new IllegalStateException("No animations in hologram!");

        this.task = new HologramRotate(this.delayMillis);
        this.plugin.getThreadHandler().addAsyncWork(this.task);
    }

    private class HologramRotate extends CancellableTask {

        private final HologramExpertRotateTask task;
        private final short delayMillis;
        private LocationXYZYP loc;
        private int time;
        private int frame;
        private int frameParts;
        private int partMove;

        private long lastExec;
        private long frameStart;

        protected HologramRotate(short delayMillis) {
            this.task = HologramExpertRotateTask.this;
            this.delayMillis = delayMillis;
            this.frame = -1;
            this.frameParts = 1;
        }

        @Override
        public void compute() {
            if (this.isCancelled()) return;

            this.lastExec = System.currentTimeMillis();

            if (this.lastExec - this.frameStart >= this.time) {
                if (++this.frame >= this.task.frames.size()) this.frame = 0;

                this.loc = this.task.frames.get(this.frame).getKey();
                this.time = this.task.frames.get(this.frame).getValue() * 50;
                this.frameParts = (int) Math.ceil(this.time / this.delayMillis);
                this.partMove = 1;
                this.frameStart = System.currentTimeMillis();
            }

            this.task.rotateAndMoveHologram(
                    this.loc.getX() / this.frameParts * this.partMove,
                    this.loc.getY() / this.frameParts * this.partMove,
                    this.loc.getZ() / this.frameParts * this.partMove,
                    this.loc.getYaw() / this.frameParts * this.partMove,
                    this.loc.getPitch() / this.frameParts * this.partMove);

            this.partMove++;
        }

        @Override
        public boolean shouldExecute() {
            return !this.isCancelled() && System.currentTimeMillis() - lastExec > this.delayMillis;
        }
    }
}
