package com.pepedevs.dbedwars.action.objects;

import com.pepedevs.dbedwars.api.action.Action;

import java.time.Duration;

public class ProcessedActionHolder<K> {

    private final Action<K> action;
    private final boolean shouldExecute;
    private final Duration delay;

    public ProcessedActionHolder(Action<K> action, boolean shouldExecute, Duration delay) {
        this.action = action;
        this.shouldExecute = shouldExecute;
        this.delay = delay;
    }

    public Action<?> getAction() {
        return action;
    }

    public boolean shouldExecute() {
        return shouldExecute;
    }

    public Duration getDelay() {
        return delay;
    }

}
