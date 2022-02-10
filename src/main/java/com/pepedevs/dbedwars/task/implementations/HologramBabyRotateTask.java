package com.pepedevs.dbedwars.task.implementations;

import com.pepedevs.radium.holograms.object.Hologram;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.task.CancellableWorkload;

public class HologramBabyRotateTask extends HologramRotateTask {

    private final float degreeRotatedPerCycle;
    private final int ticksPerAnimationCycle;
    private final String onAnimationEnd;
    private final boolean slowDownAtEnd;
    private final float verticalMovement;

    public HologramBabyRotateTask(
            DBedwars plugin,
            Hologram hologram,
            float degreeRotatedPerCycle,
            int ticksPerAnimationCycle,
            String onAnimationEnd,
            boolean slowDownAtEnd,
            float verticalMovement) {
        super(plugin, hologram);
        this.degreeRotatedPerCycle = degreeRotatedPerCycle;
        this.ticksPerAnimationCycle = ticksPerAnimationCycle;
        this.onAnimationEnd = onAnimationEnd;
        this.slowDownAtEnd = slowDownAtEnd;
        this.verticalMovement = verticalMovement;
    }

    @Override
    public void start() {
        if (this.slowDownAtEnd) {
            float horizontalAcceleration =
                    (4 * this.degreeRotatedPerCycle)
                            / (this.ticksPerAnimationCycle * this.ticksPerAnimationCycle);
            float verticalAcceleration =
                    (4 * this.verticalMovement)
                            / (this.ticksPerAnimationCycle * this.ticksPerAnimationCycle);
            this.task = new HologramRotateSlow(horizontalAcceleration, verticalAcceleration);
        } else {
            this.task = new HologramRotateNoSlow();
        }

        this.plugin.getThreadHandler().submitAsync(this.task);
    }

    private class HologramRotateSlow extends CancellableWorkload {

        private final HologramBabyRotateTask task;
        private final float horizontalAcceleration;
        private final float verticalAcceleration;

        private int tick;
        private int revert;
        private long timestamp;

        protected HologramRotateSlow(float horizontalAcceleration, float verticalAcceleration) {
            this.task = HologramBabyRotateTask.this;
            this.horizontalAcceleration = horizontalAcceleration;
            this.verticalAcceleration = verticalAcceleration;
            this.tick = 0;
            this.revert = 1;
        }

        @Override
        public void compute() {
            this.timestamp = System.currentTimeMillis();
            if (this.tick >= this.task.ticksPerAnimationCycle) {
                this.tick = 0;
                if (this.task.onAnimationEnd.equalsIgnoreCase("reverse")) {
                    if (this.revert == 1) {
                        this.revert = -1;
                    } else if (this.revert == -1) {
                        this.revert = 1;
                    }
                }
            }
            if (this.tick >= this.task.ticksPerAnimationCycle / 2) {
                this.tick++;
                this.task.rotateHologram(
                        this.revert
                                * (this.horizontalAcceleration / 2)
                                * ((2 * (this.task.ticksPerAnimationCycle - this.tick)) + 1),
                        (this.revert
                                * (this.verticalAcceleration / 2)
                                * ((2 * (this.task.ticksPerAnimationCycle - this.tick)) + 1)));
            } else {
                this.tick++;
                this.task.rotateHologram(
                        this.revert * (this.horizontalAcceleration / 2) * ((2 * this.tick) + 1),
                        (this.revert * (this.verticalAcceleration / 2) * ((2 * this.tick) + 1)));
            }
        }

        @Override
        public boolean shouldExecute() {
            return System.currentTimeMillis() - this.timestamp >= 50;
        }
    }

    private class HologramRotateNoSlow extends CancellableWorkload {

        private final HologramBabyRotateTask task;

        private int tick;
        private int revert;
        private long lastExec;

        public HologramRotateNoSlow() {
            this.task = HologramBabyRotateTask.this;
            this.tick = 0;
            this.revert = 1;
        }

        @Override
        public void compute() {
            this.tick++;
            this.lastExec = System.currentTimeMillis();
            this.task.rotateAndMoveHologram(
                    0,
                    this.revert * this.task.verticalMovement / this.task.ticksPerAnimationCycle,
                    0,
                    this.revert
                            * this.task.degreeRotatedPerCycle
                            / this.task.ticksPerAnimationCycle,
                    0);
            if (this.tick >= this.task.ticksPerAnimationCycle) {
                this.tick = 0;
                if (this.task.onAnimationEnd.equalsIgnoreCase("reverse")) {
                    if (this.revert == 1) {
                        this.revert = -1;
                    } else if (this.revert == -1) {
                        this.revert = 1;
                    }
                }
            }
        }

        @Override
        public boolean shouldExecute() {
            return System.currentTimeMillis() - this.lastExec >= 50;
        }
    }
}
