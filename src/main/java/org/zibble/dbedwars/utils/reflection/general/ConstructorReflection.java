package org.zibble.dbedwars.utils.reflection.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class ConstructorReflection {

    public static Constructor<?> get(Class<?> clazz, boolean declared, Class<?>... parameter_types) throws NoSuchMethodException, SecurityException {
        return declared ? clazz.getDeclaredConstructor(parameter_types) : clazz.getConstructor(parameter_types);
    }

    public static Constructor<?> get(Class<?> clazz, Class<?>... parameter_types) throws NoSuchMethodException, SecurityException {
        try {
            return get(clazz, false, parameter_types);
        } catch (NoSuchMethodException ex) {
            return get(clazz, true, parameter_types);
        }
    }

    public static Constructor<?> getAccessible(Class<?> clazz, boolean declared, Class<?>... parameter_types) throws NoSuchMethodException, SecurityException {
        final Constructor<?> constructor = get(clazz, declared, parameter_types);
        constructor.setAccessible(true);
        return constructor;
    }

    public static Constructor<?> getAccessible(Class<?> clazz, Class<?>... parameter_types)
            throws NoSuchMethodException, SecurityException {
        try {
            return getAccessible(clazz, false, parameter_types);
        } catch (NoSuchMethodException ex) {
            return getAccessible(clazz, true, parameter_types);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz, boolean declared, Class<?>[] parameter_types, Object... arguments)
            throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        return (T) getAccessible(clazz, declared, parameter_types).newInstance(arguments);
    }

    public static <T> T newInstance(Class<?> clazz, Class<?>[] parameter_types, Object... arguments)
            throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        try {
            return newInstance(clazz, false, parameter_types, arguments);
        } catch (NoSuchMethodException ex) {
            return newInstance(clazz, true, parameter_types, arguments);
        }
    }

    public static <T> Constructor<T> setAccessible(Constructor<T> constructor, boolean accessible) throws SecurityException {
        constructor.setAccessible(accessible);
        if (accessible) {
            try {
                Field modifiersField = Constructor.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(constructor, constructor.getModifiers() & ~Modifier.FINAL);
                modifiersField.setAccessible(false);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return constructor;
    }

    public static <T> Constructor<T> setAccessible(Constructor<T> constructor) throws SecurityException, IllegalArgumentException {
        return setAccessible(constructor, true);
    }

}
