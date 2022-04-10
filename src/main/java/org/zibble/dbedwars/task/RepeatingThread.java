package org.zibble.dbedwars.task;

import org.zibble.dbedwars.api.task.Task;
import org.zibble.dbedwars.api.task.Workload;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class RepeatingThread implements Runnable, Task {

    protected final int id;
    protected final ExecutorService executor;
    protected final Runnable runnable;
    protected Future<Void> task;
    protected long interval;
    protected boolean cancelled = false;

    public RepeatingThread(int id, long interval, ThreadFactory threadFactory, Runnable runnable) {
        this.id = id;
        this.interval = interval;
        this.executor = Executors.newSingleThreadExecutor(threadFactory);
        this.runnable = runnable;
    }

    public long getInterval() {
        return this.interval;
    }

    public synchronized void setInterval(long interval) {
        this.interval = interval;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Deprecated
    @Override
    public void submit(Workload workload) {
    }

    @Override
    public void cancel() {
        if (this.task == null) throw new IllegalStateException("Task never started!");
        this.task.cancel(true);
        this.cancelled = true;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public boolean isRunning() {
        return this.task != null && !this.task.isCancelled();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        this.task = (Future<Void>) this.executor.submit(
                () -> {
                    long nextLoop = System.currentTimeMillis() - this.interval;
                    while (true) {
                        try {
                            nextLoop += this.interval;
                            long sleep = nextLoop - System.currentTimeMillis();
                            if (sleep > interval) sleep = interval;
                            if (sleep > 0) Thread.sleep(sleep);
                            runnable.run();
                        } catch (InterruptedException pluginDisabled) {
                            Thread.currentThread().interrupt();
                            break;
                        } catch (Exception | NoClassDefFoundError e) {
                            e.printStackTrace();
                        }
                    }
                    this.executor.shutdown();
                });
    }

}
