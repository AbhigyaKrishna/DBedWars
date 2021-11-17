package com.pepedevs.dbedwars.task;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.task.CancellableTask;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.titles.TitleUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class RespawnTask extends CancellableTask implements Listener {

    private final DBedwars plugin;
    private final ArenaPlayer player;
    private int time;
    private long lastExecuted;

    public RespawnTask(DBedwars plugin, ArenaPlayer player) {
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
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    // TODO: Revamp this
    @Override
    public void compute() {
        this.lastExecuted = System.currentTimeMillis();
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
            this.setCancelled(true);
            HandlerList.unregisterAll(this);
        }
    }

    @Override
    public boolean shouldExecute() {
        return System.currentTimeMillis() - this.lastExecuted >= 1000;
    }

    @EventHandler
    private void handleDeath(PlayerDeathEvent event) {
        if (event.getEntity() != this.player.getPlayer())
            return;

        this.setCancelled(true);
        HandlerList.unregisterAll(this);
    }

}
