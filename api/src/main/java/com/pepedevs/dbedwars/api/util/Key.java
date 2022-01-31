package com.pepedevs.dbedwars.api.util;

public class Key <T> {

    private final T key;

    private Key(T key) {
        this.key = key;
    }

    public static <T> Key<T> of(T key) {
        return new Key<T>(key);
    }

    public T getKey() {
        return key;
    }
}
