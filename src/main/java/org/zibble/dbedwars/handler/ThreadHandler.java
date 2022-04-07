package org.zibble.dbedwars.handler;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.task.Task;
import org.zibble.dbedwars.api.task.Workload;
import org.zibble.dbedwars.api.util.Duration;
import org.zibble.dbedwars.api.util.SchedulerUtils;
import org.zibble.dbedwars.task.TaskQueueHandler;
import org.zibble.dbedwars.task.implementations.UpdateTask;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

public class ThreadHandler implements org.zibble.dbedwars.api.handler.ThreadHandler {

    private final TaskQueueHandler handler;
    private final UpdateTask updater;
    private Task task, updaterTask;

    public ThreadHandler(DBedwars plugin) {
        this.handler = new TaskQueueHandler("DBedWars Thread %d");
        this.updater = new UpdateTask(plugin);
    }

    public void runThreads(int threadCount) {
        this.task = this.handler.newPool(threadCount, 20 * 1000000);
        this.updaterTask = this.handler.newPool(1, 10 * 1000000);
        this.updater.start();
    }

    public void destroy() {
        this.handler.cancelTask(this.task.getID());
        this.handler.cancelTask(this.updaterTask.getID());
    }

    @Override
    public void submitSync(Runnable runnable) {
        this.task.submit(new Workload() {
            @Override
            public void compute() {
                SchedulerUtils.runTask(runnable);
            }
        });
    }

    @Override
    public void submitSync(Collection<Runnable> runnable) {
        for (Runnable r : runnable) {
            this.submitSync(r);
        }
    }

    @Override
    public void submitAsync(Workload load) {
        this.task.submit(load);
    }

    @Override
    public void submitAsync(Collection<Workload> load) {
        for (Workload w : load) {
            this.submitAsync(w);
        }
    }

    public CancellableWorkload runTaskLater(Runnable runnable, Duration delay) {
        return this.handler.runTaskLater(this.task.getID(), runnable, delay.toMillis());
    }

    public CancellableWorkload runTaskTimer(Runnable runnable, Duration interval) {
        return this.handler.runTaskTimer(this.task.getID(), runnable, interval.toMillis());
    }

    public TaskQueueHandler getTaskHandler() {
        return this.handler;
    }

    public Task getTask() {
        return this.task;
    }

    public Task getUpdaterTask() {
        return this.updaterTask;
    }

    public UpdateTask getUpdater() {
        return this.updater;
    }

}
