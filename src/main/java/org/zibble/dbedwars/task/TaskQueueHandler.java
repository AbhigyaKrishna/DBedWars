package org.zibble.dbedwars.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.task.Task;
import org.zibble.dbedwars.api.task.Workload;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.function.Predicate;

public class TaskQueueHandler {

    public static final int MAX_QUEUING_POOL_CONSTRAIN = 30 * 1000000;
    public static final int DEFAULT_QUEUING_POOL_CONSTRAIN = 30 * 1000000;

    private final ThreadFactory threadFactory;
    protected final Map<Integer, Task> queueingPool;

    private int queueID;

    public TaskQueueHandler() {
        this("Thread %d");
    }

    public TaskQueueHandler(String threadNameFormat) {
        this.threadFactory = new ThreadFactoryBuilder().setNameFormat(threadNameFormat).build();
        this.queueingPool = new ConcurrentHashMap<>();
        this.queueID = 0;
    }

    public Task newPool(int poolCapacity) {
        return this.newPool(poolCapacity, DEFAULT_QUEUING_POOL_CONSTRAIN);
    }

    public Task newPool(int poolCapacity, long constrainNanos) {
        if (constrainNanos >= MAX_QUEUING_POOL_CONSTRAIN)
            throw new IllegalArgumentException("Pool constrain cannot be larger than 50ms.");

        WorkloadDistributor distributor = new WorkloadDistributor(this, ++this.queueID);
        for (int i = 0; i < poolCapacity; i++) {
            distributor.createThread(constrainNanos);
        }
        this.queueingPool.put(queueID, distributor);
        distributor.start(this.threadFactory);
        return distributor;
    }

    public Task getTask(int poolId) {
        return this.queueingPool.get(poolId);
    }

    public void submit(int poolId, Workload workload) {
        this.queueingPool.get(poolId).submit(workload);
    }

    public CancellableWorkload runTaskLater(int poolId, Runnable runnable, long delay) {
        CancellableWorkload workload =
                new CancellableWorkload() {
                    final long start = System.currentTimeMillis();

                    @Override
                    public void compute() {
                        this.setCancelled(true);
                        runnable.run();
                    }

                    @Override
                    public boolean shouldExecute() {
                        return !this.isCancelled()
                                && System.currentTimeMillis() - this.start >= delay;
                    }
                };
        this.submit(poolId, workload);
        return workload;
    }

    public CancellableWorkload runTaskTimer(int poolId, Runnable runnable, long time) {
        CancellableWorkload workload =
                new CancellableWorkload() {
                    long lastExec = System.currentTimeMillis();

                    @Override
                    public void compute() {
                        lastExec = System.currentTimeMillis();
                        runnable.run();
                    }

                    @Override
                    public boolean shouldExecute() {
                        return !this.isCancelled()
                                && System.currentTimeMillis() - this.lastExec >= time;
                    }
                };
        this.submit(poolId, workload);
        return workload;
    }

    public CancellableWorkload runTaskTimer(
            int poolId, Runnable runnable, long time, Predicate<Runnable> interruption) {
        CancellableWorkload workload =
                new CancellableWorkload() {
                    long lastExec = System.currentTimeMillis();

                    @Override
                    public void compute() {
                        lastExec = System.currentTimeMillis();
                        runnable.run();
                        if (interruption.test(runnable)) {
                            this.setCancelled(true);
                        }
                    }

                    @Override
                    public boolean shouldExecute() {
                        return !this.isCancelled()
                                && System.currentTimeMillis() - this.lastExec >= time;
                    }
                };
        this.submit(poolId, workload);
        return workload;
    }

    public void cancelTask(int poolID) {
        Task task = this.queueingPool.remove(poolID);
        task.cancel();
    }
}
