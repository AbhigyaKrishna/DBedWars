package org.zibble.dbedwars.api.util.function;

@FunctionalInterface
public interface ArrayFunction<T, R> {

    R apply(T... t);

}
