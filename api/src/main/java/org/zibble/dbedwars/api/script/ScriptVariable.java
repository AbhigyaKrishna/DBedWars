package org.zibble.dbedwars.api.script;

import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.key.Keyed;

public interface ScriptVariable<T> extends Keyed {

    static ScriptVariable<Object> nullValue() {
        return new ScriptVariable<Object>() {
            @Override
            public Object value() {
                return null;
            }

            @Override
            public void value(Object value) {
                throw new UnsupportedOperationException("Cannot assign to a null value");
            }

            @Override
            public boolean isSubClassOf(Class<?> clazz) {
                return false;
            }

            @Override
            public Key getKey() {
                return Key.of("null");
            }
        };
    }

    static <T> ScriptVariable<T> of(String key, T value) {
        return new ScriptVariable<T>() {

            private T scriptValue = value;

            @Override
            public T value() {
                return this.scriptValue;
            }

            @Override
            public void value(T value) {
                this.scriptValue = value;
            }

            @Override
            public boolean isSubClassOf(Class<?> clazz) {
                return clazz.isAssignableFrom(this.value().getClass());
            }

            @Override
            public Key getKey() {
                return Key.of(key);
            }
        };
    }

    static <T> ScriptVariable<T> ofFinal(String key, T value) {
        return new ScriptVariable<T>() {
            @Override
            public T value() {
                return value;
            }

            @Override
            public void value(T value) {
                throw new UnsupportedOperationException("Cannot assign to a final variable");
            }

            @Override
            public boolean isSubClassOf(Class<?> clazz) {
                return clazz.isAssignableFrom(this.value().getClass());
            }

            @Override
            public Key getKey() {
                return Key.of(key);
            }
        };
    }

    T value();

    void value(T value);

    boolean isSubClassOf(Class<?> clazz);

    default boolean isNull() {
        return value() == null;
    }

}
