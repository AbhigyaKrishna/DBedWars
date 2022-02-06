package com.pepedevs.dbedwars.api.task;

import com.pepedevs.radium.task.Workload;

public abstract class DelayedTask implements Workload {

    private boolean run;
    private final long firstTry;
    private final long delay;

    public abstract void run();

    public DelayedTask(long delay) {
        this.run = false;
        this.firstTry = System.currentTimeMillis();
        this.delay = delay;
    }

    @Override
    public final void compute() {
        this.run = true;
        this.run();
    }

    @Override
    public final boolean shouldExecute() {
        return System.currentTimeMillis() - firstTry >= this.delay && !run;
    }

    @Override
    public final boolean reSchedule() {
        return !run;
    }

}
