package com.pepedevs.dbedwars.handler;

import com.pepedevs.corelib.utils.scheduler.SchedulerUtils;
import com.pepedevs.corelib.task.Workload;
import com.pepedevs.corelib.task.WorkloadDistributor;
import com.pepedevs.corelib.task.WorkloadThread;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.task.UpdateTask;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ThreadHandler implements com.pepedevs.dbedwars.api.handler.ThreadHandler {

    private final DBedwars plugin;
    private final WorkloadDistributor distributor;
    private final UpdateTask updater;
    private final WorkloadThread[] syncThreads;
    private final WorkloadThread[] asyncThreads;
    private final WorkloadThread updaterThread;
    private BukkitTask updateThreadConsumer;

    private Map<Long, BukkitTask> syncTask;
    private Map<Long, BukkitTask> asyncTask;

    public ThreadHandler(DBedwars plugin, int syncThreadCount, int asyncThreadCount) {
        this.plugin = plugin;
        this.syncTask = new ConcurrentHashMap<>();
        this.asyncTask = new ConcurrentHashMap<>();
        this.distributor = new WorkloadDistributor();
        this.updater = new UpdateTask(plugin);

        this.syncThreads = new WorkloadThread[syncThreadCount];
        this.asyncThreads = new WorkloadThread[asyncThreadCount];
        long syncNanos = Math.round(20 * 1000000 / syncThreadCount);
        long asyncNanos = Math.round(20 * 1000000 / asyncThreadCount);
        for (int i = 0; i < syncThreadCount; i++) {
            syncThreads[i] = distributor.createThread(syncNanos);
        }
        for (int i = 0; i < asyncThreadCount; i++) {
            asyncThreads[i] = distributor.createThread(asyncNanos);
        }

        this.updaterThread = this.distributor.createThread(10 * 1000000);
    }

    public void runThreads() {
        BukkitTask task;
        for (WorkloadThread wt : this.syncThreads) {
            task = SchedulerUtils.runTaskTimer(wt, 5L, 1L, this.plugin);
            syncTask.put(wt.getWorkThreadId(), task);
        }
        for (WorkloadThread wt : asyncThreads) {
            task = SchedulerUtils.runTaskTimerAsynchronously(wt, 5L, 1L, this.plugin);
            asyncTask.put(wt.getWorkThreadId(), task);
        }
        this.updater.start();
        this.updateThreadConsumer =
                SchedulerUtils.runTaskTimerAsynchronously(this.updaterThread, 5L, 1L, this.plugin);
    }

    public List<Future<Boolean>> destroyAllThreads() {
        ArrayList<Callable<Boolean>> callables = new ArrayList<>();
        Callable<Boolean> callback;
        for (WorkloadThread t : syncThreads) {
            callback =
                    () -> {
                        CompletableFuture<Boolean> end = new CompletableFuture<>();
                        t.add(() -> end.complete(true));
                        if (end.get()) {
                            syncTask.get(t.getWorkThreadId()).cancel();
                            return true;
                        }
                        return false;
                    };
            callables.add(callback);
        }
        for (WorkloadThread t : asyncThreads) {
            callback =
                    () -> {
                        CompletableFuture<Boolean> end = new CompletableFuture<>();
                        t.add(() -> end.complete(true));
                        if (end.get()) {
                            syncTask.get(t.getWorkThreadId()).cancel();
                            return true;
                        }
                        return false;
                    };
            callables.add(callback);
        }

        ExecutorService es =
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Boolean>> results = new ArrayList<>();
        try {
            results = es.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        es.shutdown();
        this.updater.stop();

        return results;
    }

    @Override
    public WorkloadThread[] getSyncThreads() {
        return syncThreads;
    }

    @Override
    public WorkloadThread getLeastWorkSyncWorker() {
        WorkloadThread thread = null;
        for (WorkloadThread t : syncThreads) {
            if (thread == null) {
                thread = t;
                continue;
            }

            if (t.getDeque().size() < thread.getDeque().size()) thread = t;
        }

        return thread;
    }

    @Override
    public void submitSync(Workload load) {
        this.getLeastWorkSyncWorker().add(load);
    }

    @Override
    public void submitSync(Collection<Workload> load) {
        WorkloadThread thread = this.getLeastWorkSyncWorker();
        for (Workload w : load) {
            thread.add(w);
        }
    }

    @Override
    public WorkloadThread[] getAsyncThreads() {
        return asyncThreads;
    }

    @Override
    public WorkloadThread getLeastWorkAsyncWorker() {
        WorkloadThread thread = null;
        for (WorkloadThread t : asyncThreads) {
            if (thread == null) {
                thread = t;
                continue;
            }

            if (t.getDeque().size() < thread.getDeque().size()) thread = t;
        }

        return thread;
    }

    @Override
    public void submitAsync(Workload load) {
        this.getLeastWorkAsyncWorker().add(load);
    }

    @Override
    public void submitAsync(Collection<Workload> load) {
        WorkloadThread thread = this.getLeastWorkAsyncWorker();
        for (Workload w : load) {
            thread.add(w);
        }
    }

    public UpdateTask getUpdater() {
        return updater;
    }

    public WorkloadThread getUpdaterThread() {
        return updaterThread;
    }

    public Map<Long, BukkitTask> getSyncTask() {
        return syncTask;
    }

    public Map<Long, BukkitTask> getAsyncTask() {
        return asyncTask;
    }
}
