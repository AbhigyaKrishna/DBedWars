package com.pepedevs.dbedwars.api.util;

import java.util.Objects;

public class Key<T> implements Cloneable {

    protected final T key;

    protected Key(T key) {
        this.key = key;
    }

    public static <T> Key<T> of(T key) {
        return new Key<>(key);
    }

    public T get() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        return this.key.equals(obj) || (obj instanceof Key && ((Key) obj).key.equals(this.key));
    }

    @Override
    public Key<T> clone() {
        return new Key<>(this.key);
    }

    @Override
    public String toString() {
        return "Key{" +
                "key=" + key +
                '}';
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

}
