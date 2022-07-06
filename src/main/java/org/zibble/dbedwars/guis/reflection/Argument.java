package org.zibble.dbedwars.guis.reflection;

public class Argument<T> {

    private final Class<T> type;
    private final T value;

    public static <T> Argument<T> of(Class<T> clazz, T value) {
        return new Argument<>(clazz, value);
    }

    public Argument(Class<T> type, T value) {
        this.type = type;
        this.value = value;
    }

    public Class<T> getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public boolean isFor(Class<?> other) {
        return other.isAssignableFrom(type);
    }

}
