package com.pepedevs.dbedwars.action.objects;

import com.pepedevs.dbedwars.api.action.Action;

import java.time.Duration;

public class ProcessedActionHolder {

    private final Action<?> action;
    private final boolean shouldExecute;
    private final Duration delay;

    public ProcessedActionHolder(Action<?> action, boolean shouldExecute, Duration delay) {
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
