package org.zibble.dbedwars.game.arena;

import org.zibble.dbedwars.api.objects.serializable.Duration;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

public class GameEventModel {

    private final Queue<GameEventImpl> events;
    private GameEventImpl current;
    private Instant updated;

    public GameEventModel(Collection<GameEventImpl> events) {
        this.events = new ArrayDeque<>(events);
    }

    public GameEventImpl getCurrentEvent() {
        return this.current;
    }

    public GameEventImpl peek() {
        return this.events.peek();
    }

    public void nextEvent() {
        if (!this.hasNextEvent()) {
            this.current = this.events.poll();
            this.updated = Instant.now();
        }
    }

    public Instant getUpdated() {
        return updated;
    }

    public Duration getRemainingTime() {
        if (this.current == null) {
            return Duration.ZERO;
        }
        return Duration.between(this.updated, Instant.now());
    }

    public boolean hasNextEvent() {
        return !this.events.isEmpty();
    }

}
