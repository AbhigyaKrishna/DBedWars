package org.zibble.dbedwars.task.implementations;

import com.google.common.primitives.Shorts;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaStatus;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.task.Workload;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class ArenaStartTask implements Workload {

    private static final short[] TRIGGERS =
            new short[] {1, 2, 3, 4, 5, 10, 20, 30, 45, 60, 90, 120};

    // TODO Message config
    private final Message TITLE_MESSAGE = AdventureMessage.from("<yellow><countdown>",
            PlaceholderEntry.symbol("countdown", new Supplier<String>() {
                @Override
                public String get() {
                    return String.valueOf(ArenaStartTask.this.countdown.get());
                }
            }));
    private final Message TRIGGER_MESSAGE = AdventureMessage.from("<grey>Match starting in <red><countdown> <grey>seconds.",
            PlaceholderEntry.symbol("countdown", new Supplier<String>() {
                @Override
                public String get() {
                    return String.valueOf(ArenaStartTask.this.countdown.get());
                }
            }));

    private final Arena arena;
    private final AtomicInteger countdown;
    private boolean started;

    private long lastExecute;

    public ArenaStartTask(Arena arena, short countdown) {
        this.arena = arena;
        this.countdown = new AtomicInteger(countdown);
    }

    @Override
    public void compute() {
        this.lastExecute = System.currentTimeMillis();

        this.arena.setStatus(ArenaStatus.STARTING);

        this.sendTrigger();
        this.countdown.decrementAndGet();

        if (this.arena.getPlayers().size() == this.arena.getSettings().getMaxPlayer()
                && this.countdown.get() > 5) {
            // TODO: change message and add configuration
            this.arena.sendMessage(
                    AdventureMessage.from("<green>Maximum players reached. Shortening timer"));
            this.countdown.set(5);
        }
    }

    @Override
    public boolean reSchedule() {
        if (this.arena.getPlayers().size() < this.arena.getSettings().getMinPlayers()) {
            // TODO: change message
            this.arena.sendMessage(AdventureMessage.from("<red>Not enough players to start!"));
            this.arena.setStatus(ArenaStatus.WAITING);
            return false;
        }
        if (this.countdown.get() == 0) {
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

    private void sendTrigger() {
        if (Shorts.contains(TRIGGERS, this.countdown.shortValue())) {
            this.arena.sendTitle(TITLE_MESSAGE);
            this.arena.sendMessage(TRIGGER_MESSAGE);
        }
        // TODO: update scoreboard
    }
}
