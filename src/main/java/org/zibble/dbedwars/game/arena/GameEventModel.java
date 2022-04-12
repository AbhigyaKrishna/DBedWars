package org.zibble.dbedwars.game.arena;

import org.zibble.dbedwars.api.objects.serializable.Duration;

import java.time.Instant;
import java.util.Collection;
import java.util.Iterator;

public class GameEventModel {

    private final Iterator<GameEvent> events;
    private GameEvent current;
    private Instant updated;

    public GameEventModel(Collection<GameEvent> events) {
        this.events = events.iterator();
    }

    public GameEvent getCurrentEvent() {
        return this.current;
    }

    public void nextEvent() {
        if (this.events.hasNext()) {
            this.current = this.events.next();
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
        return this.events.hasNext();
    }

}
