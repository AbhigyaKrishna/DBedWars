package com.pepedevs.dbedwars.task.implementations;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.api.messaging.message.AdventureMessage;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.api.task.CancellableWorkload;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class RespawnTask extends CancellableWorkload implements Listener {

    private final DBedwars plugin;
    private final ArenaPlayer player;
    private final AtomicInteger time;
    Message message =
            AdventureMessage.from("<red>Respawning in <gold><time>s",
                    PlaceholderEntry.symbol("<time>",
                            new Supplier<String>() {
                                @Override
                                public String get() {
                                    return String.valueOf(RespawnTask.this.time.get());
                                }
                            }));
    private long lastExecuted;

    public RespawnTask(DBedwars plugin, ArenaPlayer player) {
        this.plugin = plugin;
        this.time =
                new AtomicInteger(
                        this.plugin
                                .getConfigHandler()
                                .getMainConfiguration()
                                .getArenaSection()
                                .getRespawnTime());
        this.player = player;
        this.player.sendTitle(message);
        this.lastExecuted = System.currentTimeMillis();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    // TODO: Revamp this
    @Override
    public void compute() {
        this.lastExecuted = System.currentTimeMillis();
        this.time.decrementAndGet();
        this.player.sendTitle(message);
        if (time.get() == 0) {
            ((com.pepedevs.dbedwars.game.arena.ArenaPlayer) this.player).setRespawning(false);
            this.plugin.getThreadHandler().submitSync(() -> this.player.setSpectator(false));
            this.plugin
                    .getThreadHandler()
                    .submitSync(
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
        if (event.getEntity() != this.player.getPlayer()) return;

        this.setCancelled(true);
        HandlerList.unregisterAll(this);
    }
}