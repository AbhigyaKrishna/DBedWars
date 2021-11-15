package com.pepedevs.dbedwars.task;

import me.Abhigya.core.util.scheduler.SchedulerUtils;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaStatus;
import com.pepedevs.dbedwars.api.game.spawner.Spawner;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;

public final class UpdateTask implements Runnable {

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
        this.task = SchedulerUtils.runTaskTimerAsynchronously(this, 0L, 1L, this.plugin);
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
            this.plugin
                    .getThreadHandler()
                    .addAsyncWork(
                            () -> {
                                for (Arena a : this.plugin.getGameManager().getArenas().values()) {
                                    if (a.isEnabled() && a.getStatus() == ArenaStatus.RUNNING) {
                                        a.getSpawners().forEach(Spawner::tick);
                                    }
                                }
                            });
            /*List<PopupTowerBuilder> builders = plugin.getGameManager().getPopupTowerBuilders();
            List<PopupTowerBuilder> buildersClone = new ArrayList<>(builders);
            for (int i = 0; i < builders.size(); i++) {
                Workload workload = buildersClone.get(i).getBlockQueue().poll();
                if(workload == null){
                    builders.remove(buildersClone.get(i));
                    continue;
                }
                plugin.getThreadHandler().addSyncWork(workload);
                i++;
                workload = buildersClone.get(i).getBlockQueue().poll();
                if(workload == null){
                    builders.remove(buildersClone.get(i));
                    continue;
                }
                plugin.getThreadHandler().addSyncWork(workload);
            }*/

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
