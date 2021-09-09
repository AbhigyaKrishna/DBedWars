package me.abhigya.dbedwars.handler;

import me.Abhigya.core.util.scheduler.SchedulerUtils;
import me.Abhigya.core.util.tasks.Workload;
import me.Abhigya.core.util.tasks.WorkloadDistributor;
import me.Abhigya.core.util.tasks.WorkloadThread;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.task.UpdateTask;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ThreadHandler {

    private final DBedwars plugin;
    private final WorkloadDistributor distributor;

    private WorkloadThread[] syncThreads;
    private WorkloadThread[] asyncThreads;

    private final UpdateTask updater;
    private WorkloadThread updaterThread;
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
        this.updateThreadConsumer = SchedulerUtils.runTaskTimerAsynchronously(this.updaterThread, 5L, 1L, this.plugin);
    }

    public List<Future<Boolean>> destroyAllThreads() {
        ArrayList<Callable<Boolean>> callables = new ArrayList<>();
        Callable<Boolean> callback;
        for (WorkloadThread t : syncThreads) {
            callback = () -> {
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
            callback = () -> {
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

        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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

    public WorkloadThread[] getSyncThreads() {
        return syncThreads;
    }

    public WorkloadThread getLeastWorkSyncWorker() {
        WorkloadThread thread = null;
        for (WorkloadThread t : syncThreads) {
            if (thread == null) {
                thread = t;
                continue;
            }

            if (t.getDeque().size() < thread.getDeque().size())
                thread = t;
        }

        return thread;
    }

    public void addSyncWork(Workload work) {
        this.getLeastWorkSyncWorker().add(work);
    }

    public void addSyncWork(Collection<Workload> work) {
        WorkloadThread thread = this.getLeastWorkSyncWorker();
        for (Workload w : work) {
            thread.add(w);
        }
    }

    public WorkloadThread[] getAsyncThreads() {
        return asyncThreads;
    }

    public WorkloadThread getLeastWorkAsyncWorker() {
        WorkloadThread thread = null;
        for (WorkloadThread t : asyncThreads) {
            if (thread == null) {
                thread = t;
                continue;
            }

            if (t.getDeque().size() < thread.getDeque().size())
                thread = t;
        }

        return thread;
    }

    public void addAsyncWork(Workload work) {
        this.getLeastWorkAsyncWorker().add(work);
    }

    public void addAsyncWork(Collection<Workload> work) {
        WorkloadThread thread = this.getLeastWorkAsyncWorker();
        for (Workload w : work) {
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
