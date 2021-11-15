package com.pepedevs.dbedwars.task;

import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.titles.TitleUtils;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.task.FixedRateScheduleTask;

public class RespawnTask extends FixedRateScheduleTask {

    private final DBedwars plugin;
    private final ArenaPlayer player;
    private int time;
    private long lastExecuted;

    public RespawnTask(DBedwars plugin, ArenaPlayer player) {
        super(
                20,
                plugin.getConfigHandler()
                        .getMainConfiguration()
                        .getArenaSection()
                        .getRespawnTime());
        this.plugin = plugin;
        this.time =
                this.plugin
                        .getConfigHandler()
                        .getMainConfiguration()
                        .getArenaSection()
                        .getRespawnTime();
        this.player = player;
        TitleUtils.send(
                player.getPlayer(),
                StringUtils.translateAlternateColorCodes("&cRespawning in &6" + time + "s"),
                "");
        this.lastExecuted = System.currentTimeMillis();
    }

    // TODO: Revamp this
    @Override
    public void compute() {
        lastExecuted = System.currentTimeMillis();
        this.time--;
        TitleUtils.send(
                player.getPlayer(),
                StringUtils.translateAlternateColorCodes("&cRespawning in &6" + time + "s"),
                "");
        if (time == 0) {
            ((com.pepedevs.dbedwars.game.arena.ArenaPlayer) this.player).setRespawning(false);
            this.plugin.getThreadHandler().addSyncWork(() -> this.player.setSpectator(false));
            this.plugin
                    .getThreadHandler()
                    .addSyncWork(
                            () ->
                                    this.player.spawn(
                                            this.player
                                                    .getTeam()
                                                    .getSpawn()
                                                    .toBukkit(this.player.getArena().getWorld())));
        }
    }

    @Override
    public boolean shouldExecute() {
        return System.currentTimeMillis() - this.lastExecuted >= 1000;
    }

    @Override
    public boolean reSchedule() {
        return time != 0;
    }
}
