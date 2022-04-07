package org.zibble.dbedwars.api.objects.points;

import org.zibble.dbedwars.api.util.key.Key;

import java.util.HashMap;
import java.util.Map;

public class Points {

    private final Map<Key, Count<?>> points = new HashMap<>();

    public void registerCount(Key key, Count<?> count) {
        if (this.points.containsKey(key)) {
            throw new IllegalArgumentException("A count with the name " + key.get() + " already exists!");
        }
        this.points.put(key, count);
    }

    public Count<?> unregisterCount(Key key) {
        return this.points.remove(key);
    }

    public Count<?> getCount(Key key) {
        return this.points.get(key);
    }

}
