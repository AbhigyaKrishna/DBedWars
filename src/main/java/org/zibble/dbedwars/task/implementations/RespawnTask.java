package org.zibble.dbedwars.task.implementations;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.task.CancellableWorkload;

import java.util.concurrent.atomic.AtomicInteger;

public class RespawnTask extends CancellableWorkload implements Listener {

    private final DBedwars plugin;
    private final ArenaPlayer player;
    private final AtomicInteger time;
    Message message;
    private long lastExecuted;

    public RespawnTask(DBedwars plugin, ArenaPlayer player) {
        this.plugin = plugin;
        this.time = new AtomicInteger(this.plugin.getConfigHandler().getMainConfiguration().getArenaSection().getRespawnTime());
        this.player = player;
        this.player.sendTitle(message);
        this.lastExecuted = System.currentTimeMillis();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
        this.message = AdventureMessage.from("<red>Respawning in <gold><time>s",
                PlaceholderEntry.symbol("<time>", () -> String.valueOf(this.time.get())));
    }

    // TODO: Revamp this
    @Override
    public void compute() {
        this.lastExecuted = System.currentTimeMillis();
        this.time.decrementAndGet();
        this.player.sendTitle(message);
        if (this.time.get() == 0) {
            this.player.setRespawning(false);
            this.player.show();
            this.plugin.getThreadHandler().submitSync(() -> this.player.spawn(this.player.getTeam().getSpawn().toBukkit(this.player.getArena().getWorld())));
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
