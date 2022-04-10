package org.zibble.dbedwars.api.task;

public abstract class DelayedTask implements Workload {

    private final long firstTry;
    private final long delay;
    private boolean run;

    public DelayedTask(long delay) {
        this.run = false;
        this.firstTry = System.currentTimeMillis();
        this.delay = delay;
    }

    public abstract void run();

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
