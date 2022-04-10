package org.zibble.dbedwars.script.action;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.script.action.Action;
import org.zibble.dbedwars.api.util.Duration;

public class ActionProcessor {

    private final Action<?> action;
    private final boolean shouldExecute;
    private final Duration delay;
    private final int repeats;

    public ActionProcessor(Action<?> action, boolean shouldExecute, Duration delay, int repeats) {
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

    public void execute() {
        if (!this.shouldExecute()) return;
        DBedwars.getInstance().getThreadHandler().runTaskLater(() -> {
            int runTimes = this.getRepeats() + 1;
            int i = 0;
            while (i++ < runTimes) {
                this.getAction().execute();
            }
        }, this.getDelay());
    }

}
