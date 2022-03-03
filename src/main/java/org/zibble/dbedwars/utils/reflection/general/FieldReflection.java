package org.zibble.dbedwars.utils.reflection.general;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class FieldReflection {

    public static Field get(Class<?> clazz, String name, boolean declared) throws SecurityException, NoSuchFieldException {
        return declared ? clazz.getDeclaredField(name) : clazz.getField(name);
    }

    public static Field get(Class<?> clazz, String name) throws SecurityException, NoSuchFieldException {
        try {
            return get(clazz, name, false);
        } catch (NoSuchFieldException ex) {
            return get(clazz, name, true);
        }
    }

    public static Field getAccessible(Class<?> clazz, String name, boolean declared) throws SecurityException, NoSuchFieldException {
        final Field field = get(clazz, name, declared);
        field.setAccessible(true);
        return field;
    }

    public static Field getAccessible(Class<?> clazz, String name)
            throws SecurityException, NoSuchFieldException {
        final Field field = get(clazz, name);
        field.setAccessible(true);
        return field;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(Object object, String name, boolean declared)
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        final Field field = getAccessible(object.getClass(), name, declared);
        final boolean b0 = field.isAccessible();

        field.setAccessible(true);
        try {
            return (T) field.get(object);
        } catch (Throwable ex) {
            throw ex;
        } finally {
            field.setAccessible(b0);
        }
    }

    public static <T> T getValue(Object object, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        try {
            return getValue(object, name, false);
        } catch (NoSuchFieldException ex) {
            return getValue(object, name, true);
        }
    }

    public static Object setValue(Object object, String name, boolean declared, Object value)
            throws SecurityException, NoSuchFieldException, IllegalAccessException {
        final Field field = getAccessible(object.getClass(), name, declared);
        final boolean b0 = field.isAccessible();

        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (Throwable ex) {
            throw ex;
        } finally {
            field.setAccessible(b0);
        }
        return object;
    }

    public static Object setValue(Object object, String name, Object value)
            throws SecurityException, NoSuchFieldException, IllegalAccessException {
        try {
            return setValue(object, name, false, value);
        } catch (NoSuchFieldException ex) {
            return setValue(object, name, true, value);
        }
    }

    public static Field setAccessible(Field field, boolean accessible) throws SecurityException {
        field.setAccessible(accessible);
        if (accessible) {
            try {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }

        return field;
    }

    public static Field setAccessible(Field field) throws SecurityException, IllegalArgumentException {
        return setAccessible(field, true);
    }

    public static Class<?>[] getParameterizedTypesClasses(Field field) {
        if (!(field.getGenericType() instanceof ParameterizedType)) {
            throw new IllegalArgumentException("The field doesn't represent a parameterized type!");
        }

        ParameterizedType parameterized_type = (ParameterizedType) field.getGenericType();
        Type[] types_arguments = parameterized_type.getActualTypeArguments();
        Class<?>[] classes = new Class<?>[types_arguments.length];
        for (int i = 0; i < classes.length; i++) {
            classes[i] = (Class<?>) types_arguments[i];
        }
        return classes;
    }

}
