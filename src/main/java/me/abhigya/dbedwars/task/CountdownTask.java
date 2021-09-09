package me.abhigya.dbedwars.task;

import com.google.common.primitives.Shorts;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.tasks.Workload;
import me.Abhigya.core.util.titles.TitleUtils;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.ArenaStatus;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CountdownTask implements Workload {

    private static final short[] TRIGGERS = new short[]{1, 2, 3, 4, 5, 10, 20, 30, 45, 60, 90, 120};
    private final Arena arena;
    private short countdown;
    private boolean started;

    private long lastExecute;

    public CountdownTask(Arena arena, short countdown) {
        this.arena = arena;
        this.countdown = countdown;
    }

    @Override
    public void compute() {
        this.lastExecute = System.currentTimeMillis();

        this.arena.setStatus(ArenaStatus.STARTING);

        this.arena.getPlayers().forEach(p -> this.sendTrigger(p.getPlayer()));
        this.countdown--;

        if (this.arena.getPlayers().size() == this.arena.getSettings().getMaxPlayer() && this.countdown > 5) {
            // TODO: change message and add configuration
            this.arena.broadcast(StringUtils.translateAlternateColorCodes("&aMaximum players reach shortening timer."));
            this.countdown = 5;
        }
    }

    @Override
    public boolean reSchedule() {
        if (this.arena.getPlayers().size() < this.arena.getSettings().getMinPlayers()) {
            // TODO: change message
            this.arena.broadcast(StringUtils.translateAlternateColorCodes("&cNot enough players to start!"));
            this.arena.setStatus(ArenaStatus.WAITING);
            return false;
        }
        if (this.countdown == 0) {
            // TODO: trigger match start
            if (!started) {
                this.arena.start();
                started = true;
            }
            return false;
        }

        return true;
    }

    @Override
    public boolean shouldExecute() {
        return System.currentTimeMillis() - this.lastExecute >= 1000;
    }

    private void sendTrigger(Player player) {
        if (Shorts.contains(TRIGGERS, this.countdown)) {
            // TODO: change message
            TitleUtils.send(player, ChatColor.YELLOW + String.valueOf(countdown), "");
            player.sendMessage(StringUtils.translateAlternateColorCodes("&7Match starting in &c" + this.countdown + " &7seconds."));
        }
        //TODO: update scoreboard
    }
}
