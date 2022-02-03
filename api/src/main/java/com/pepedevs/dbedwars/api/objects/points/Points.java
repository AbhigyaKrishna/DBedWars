package com.pepedevs.dbedwars.api.objects.points;

import com.pepedevs.dbedwars.api.util.Key;

import java.util.HashMap;
import java.util.Map;

public class Points {

    private final Map<Key<String>, Count<?>> points = new HashMap<>();

    public void registerCount(Key<String> key, Count<?> count) {
        if (this.points.containsKey(key)) {
            throw new IllegalArgumentException("A count with the name " + key.get() + " already exists!");
        }
        this.points.put(key, count);
    }

    public Count<?> unregisterCount(Key<String> key) {
        return this.points.remove(key);
    }

    public Count<?> getCount(Key<String> key) {
        return this.points.get(key);
    }

}
