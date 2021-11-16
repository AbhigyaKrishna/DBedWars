package com.pepedevs.dbedwars.api.task;

import me.Abhigya.core.util.tasks.Workload;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class CancellableTask implements Workload {

    private final AtomicBoolean cancel;

    public CancellableTask() {
        this.cancel = new AtomicBoolean(false);
    }

    public CancellableTask(boolean bool) {
        this.cancel = new AtomicBoolean(bool);
    }

    @Override
    public boolean reSchedule() {
        return !this.isCancelled();
    }

    public void setCancelled(boolean cancelled) {
        this.cancel.set(cancelled);
    }

    public boolean isCancelled() {
        return this.cancel.get();
    }

}
