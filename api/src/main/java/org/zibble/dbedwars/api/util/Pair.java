package org.zibble.dbedwars.api.util;

import com.google.common.base.Objects;

public class Pair<K, V> {

    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object value1) {
        if (this == value1) return true;
        if (!(value1 instanceof Pair)) return false;
        Pair<?, ?> pair = (Pair<?, ?>) value1;
        return Objects.equal(key, pair.key) && Objects.equal(value, pair.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key, value);
    }

}
