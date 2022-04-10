package org.zibble.dbedwars.api.task;

import org.zibble.dbedwars.api.util.mixin.Cancellable;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class CancellableWorkload implements Workload, Cancellable {

    protected final AtomicBoolean cancelled;

    public CancellableWorkload() {
        this(false);
    }

    public CancellableWorkload(boolean cancelled) {
        this.cancelled = new AtomicBoolean(cancelled);
    }

    @Override
    public boolean reSchedule() {
        return !this.isCancelled();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled.get();
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled.set(cancelled);
    }

}
