package org.zibble.dbedwars.utils.reflection.resolver.wrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class ConstructorWrapper<R> extends WrapperAbstract {

    private final Constructor<R> constructor;

    public ConstructorWrapper(Constructor<R> constructor) {
        this.constructor = constructor;
    }

    @Override
    public boolean exists() {
        return this.constructor != null;
    }

    public R newInstance(Object... args) {
        try {
            return this.constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public R newInstanceSilent(Object... args) {
        try {
            return this.constructor.newInstance(args);
        } catch (InvocationTargetException
                | InstantiationException
                | IllegalAccessException ignored) {
        }
        return null;
    }

    public Class<?>[] getParameterTypes() {
        return this.constructor.getParameterTypes();
    }

    public Constructor<R> getConstructor() {
        return constructor;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        ConstructorWrapper<?> that = (ConstructorWrapper<?>) object;

        return Objects.equals(constructor, that.constructor);
    }

    @Override
    public int hashCode() {
        return constructor != null ? constructor.hashCode() : 0;
    }
}
