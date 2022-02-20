package org.zibble.dbedwars.action.objects;

import org.zibble.dbedwars.api.action.Action;

import java.time.Duration;

public class ProcessedActionHolder {

    private final Action<?> action;
    private final boolean shouldExecute;
    private final Duration delay;
    private final int repeats;

    public ProcessedActionHolder(Action<?> action, boolean shouldExecute, Duration delay, int repeats) {
        this.action = action;
        this.shouldExecute = shouldExecute;
        this.delay = delay;
        this.repeats = repeats;
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

    public int getRepeats() {
        return repeats;
    }
}
