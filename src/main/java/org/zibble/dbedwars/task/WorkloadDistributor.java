package org.zibble.dbedwars.task;

import org.zibble.dbedwars.api.task.Task;
import org.zibble.dbedwars.api.task.Workload;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;

/**
 * A class that stores and distributes {@link WorkloadThread} instances.
 */
public class WorkloadDistributor implements Task {

    private final Map<Integer, WorkloadThread> tasks = new ConcurrentHashMap<>();

    private final int id;
    private final TaskQueueHandler handler;
    private ThreadFactory threadFactory;
    private boolean running;

    private int nextId = 0;

    WorkloadDistributor(final TaskQueueHandler handler, final int id) {
        this.handler = handler;
        this.id = id;
        this.running = false;
    }

    protected WorkloadThread createThread(final long nanoPerTick) {
        final WorkloadThread thread = new WorkloadThread(this.handler, ++this.nextId, nanoPerTick);
        this.tasks.put(this.nextId, thread);
        if (this.running) thread.init(this.threadFactory);
        return thread;
    }

    protected void start(ThreadFactory threadFactory) {
        for (WorkloadThread value : this.tasks.values()) {
            value.init(threadFactory);
        }
        this.threadFactory = threadFactory;
        this.running = true;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void cancel() {
        for (WorkloadThread value : this.tasks.values()) {
            if (!value.isCancelled()) value.cancel();
        }
        this.running = false;
        this.handler.queueingPool.remove(this.getID());
    }

    @Override
    public boolean isCancelled() {
        return this.tasks.values().stream().allMatch(WorkloadThread::isCancelled);
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    public WorkloadThread getLeastWorker() {
        WorkloadThread thread = null;
        for (WorkloadThread value : this.tasks.values()) {
            if (thread == null || thread.getDeque().size() > value.getDeque().size()) {
                thread = value;
            }
        }

        return thread;
    }

    @Override
    public void submit(Workload workload) {
        this.getLeastWorker().submit(workload);
    }

}
