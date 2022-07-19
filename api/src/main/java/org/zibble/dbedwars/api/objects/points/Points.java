package org.zibble.dbedwars.api.objects.points;

import com.google.common.base.Preconditions;
import org.zibble.dbedwars.api.util.key.Key;

import java.util.HashMap;
import java.util.Map;

public class Points {

    private final Map<Key, Count<?>> points = new HashMap<>();
    private final Map<Key, Integer> delta = new HashMap<>();

    public <T extends Number> void registerCount(Key key, Count<T> count, int delta) {
        Preconditions.checkArgument(!this.points.containsKey(key), "A count with the name " + key.get() + " already exists!");

        this.points.put(key, count);
        this.delta.put(key, delta);
    }

    public Count<?> unregisterCount(Key key) {
        this.delta.remove(key);
        return this.points.remove(key);
    }

    public Count<?> getCount(Key key) {
        return this.points.get(key);
    }

    public int getDelta(Key key) {
        return this.delta.get(key);
    }

    public double getPoints(Key key) {
        return this.points.get(key).doubleValue() * this.delta.get(key);
    }

    public double getTotalPoints() {
        double total = 0;
        for (Key key : this.points.keySet()) {
            total += this.getPoints(key);
        }
        return total;
    }

}
