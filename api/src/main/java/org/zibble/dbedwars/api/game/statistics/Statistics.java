package org.zibble.dbedwars.api.game.statistics;

import java.util.HashMap;

public class Statistics<T, K, V> extends HashMap<K, V> {

    private final T tracker;

    public Statistics(T tracker) {
        this.tracker = tracker;
    }

    public T getTracker() {
        return tracker;
    }

}
