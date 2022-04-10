package org.zibble.dbedwars.task.implementations;

import org.bukkit.scheduler.BukkitTask;
import org.zibble.dbedwars.api.util.SchedulerUtils;
import org.zibble.dbedwars.api.util.mixin.Tickable;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public final class UpdateTask implements Runnable {

    private static final List<Tickable> TICKABLES = Collections.synchronizedList(new LinkedList<>());

    private BukkitTask task;
    private Instant started;
    private long startedMillis;

    private Instant current;

    public static void addTickable(Tickable tickable) {
        TICKABLES.add(tickable);
    }

    public static void removeTickable(Tickable tickable) {
        TICKABLES.remove(tickable);
    }

    public static void removeTickableIf(Predicate<Tickable> predicate) {
        TICKABLES.removeIf(predicate);
    }

    public static List<Tickable> getTickables() {
        return TICKABLES;
    }

    public boolean start() {
        if (this.task != null) return false;

        this.started = Instant.now();
        this.startedMillis = this.started.toEpochMilli();
        this.task = SchedulerUtils.runTaskTimerAsynchronously(this, 0L, 1L);
        return true;
    }

    public boolean stop() {
        if (this.task == null) return false;

        this.task.cancel();
        return true;
    }

    @Override
    public void run() {
        this.current = Instant.now();
        try {
            for (Tickable tickable : TICKABLES) {
                try {
                    tickable.tick();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ignored) {
        }
    }

    public int getTaskId() {
        return task.getTaskId();
    }

    public Instant getStarted() {
        return started;
    }

    public long getStartedMillis() {
        return startedMillis;
    }

    public Instant getCurrent() {
        return current;
    }

}
