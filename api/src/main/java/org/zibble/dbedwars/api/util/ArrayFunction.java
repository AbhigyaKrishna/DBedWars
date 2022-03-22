package org.zibble.dbedwars.api.util;

@FunctionalInterface
public interface ArrayFunction<T, R> {

    R apply(T... t);

}
