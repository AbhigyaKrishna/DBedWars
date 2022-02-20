package org.zibble.dbedwars.api.util;

@FunctionalInterface
public interface TriFunction <T, U, V, R> {
    R apply(T t, U u, V v);
}
