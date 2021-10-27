package me.abhigya.dbedwars.api.task;

import me.Abhigya.core.util.tasks.Workload;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class FixedRateScheduleTask implements Workload {

    private final int ticksPerExecution;
    private final AtomicLong numberOfExecutions;
    private final AtomicInteger checked;

    public FixedRateScheduleTask(int ticksPerExecution, long numberOfExecutions) {
        this.ticksPerExecution = ticksPerExecution;
        this.checked = new AtomicInteger(0);
        this.numberOfExecutions = new AtomicLong(numberOfExecutions);
    }

    @Override
    public boolean shouldExecute() {
        return this.checked.incrementAndGet() % this.ticksPerExecution == 0;
    }

    @Override
    public boolean reSchedule() {
        if (this.checked.get() % this.ticksPerExecution != 0) return true;

        return this.numberOfExecutions.decrementAndGet() > 0L;
    }
}
