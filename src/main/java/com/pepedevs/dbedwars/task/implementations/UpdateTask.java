package com.pepedevs.dbedwars.task.implementations;

import com.pepedevs.dbedwars.api.util.Tickable;
import com.pepedevs.dbedwars.api.util.SchedulerUtils;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaStatus;
import com.pepedevs.dbedwars.api.game.spawner.Spawner;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public final class UpdateTask implements Runnable {

    private static final List<Tickable> TICKABLES = Collections.synchronizedList(new LinkedList<>());
    private final DBedwars plugin;

    private BukkitTask task;
    private Instant started;
    private long startedMillis;

    private Instant current;

    public UpdateTask(DBedwars plugin) {
        this.plugin = plugin;
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
            // TODO have to remove
            this.plugin
                    .getThreadHandler()
                    .submitAsync(
                            () -> {
                                for (Arena a : this.plugin.getGameManager().getArenas().values()) {
                                    if (a.isEnabled() && a.getStatus() == ArenaStatus.RUNNING) {
                                        a.getSpawners().forEach(Spawner::tick);
                                    }
                                }
                            });
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

}
