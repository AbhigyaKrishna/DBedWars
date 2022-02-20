package org.zibble.dbedwars.task;

import org.zibble.dbedwars.api.task.Task;
import org.zibble.dbedwars.api.task.Workload;

import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;

/** A class that stores and computes {@link Workload} instances. */
public class WorkloadThread implements Runnable, Task {

    private final Queue<Workload> deque = new ConcurrentLinkedQueue<>();

    private final TaskQueueHandler handler;
    private final long maxNanosPerTick;
    private final int workThreadId;
    private RepeatingThread thread;

    WorkloadThread(final TaskQueueHandler handler, final int workThreadId, final long maxNanosPerTick) {
        this.handler = handler;
        this.workThreadId = workThreadId;
        this.maxNanosPerTick = maxNanosPerTick;
    }

    @Override
    public void submit(Workload workload) {
        deque.add(workload);
    }

    public Collection<Workload> getDeque() {
        return Collections.unmodifiableCollection(deque);
    }

    public long getMaxNanosPerTick() {
        return this.maxNanosPerTick;
    }

    @Override
    public void run() {
        final long stopTime = System.nanoTime() + this.maxNanosPerTick;
        final Workload first = this.deque.poll();
        if (first == null) {
            return;
        }
        this.computeWorkload(first);
        Workload workload;
        while (System.nanoTime() <= stopTime && (workload = this.deque.poll()) != null) {
            this.computeWorkload(workload);
            if (!first.reSchedule() && first.equals(workload)) {
                break;
            }
        }
    }

    private void computeWorkload(final Workload workload) {
        if (workload.shouldExecute()) {
            workload.compute();
        }
        if (workload.reSchedule()) {
            this.deque.add(workload);
        }
    }

    protected void init(ThreadFactory threadFactory) {
        this.thread = new RepeatingThread(this.workThreadId, 50, threadFactory, this);
        this.thread.run();
    }

    @Override
    public int getID() {
        return this.workThreadId;
    }

    public TaskQueueHandler getHandler() {
        return this.handler;
    }

    @Override
    public void cancel() {
        if (this.thread == null || this.thread.isCancelled())
            throw new IllegalStateException("Task already stopped!");

        this.thread.cancel();
    }

    @Override
    public boolean isCancelled() {
        return this.thread.isCancelled();
    }

    @Override
    public boolean isRunning() {
        return this.thread != null && !this.isCancelled();
    }
}
