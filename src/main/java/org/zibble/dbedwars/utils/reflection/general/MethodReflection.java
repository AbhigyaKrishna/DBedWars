package org.zibble.dbedwars.utils.reflection.general;

import org.zibble.dbedwars.api.util.DataType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodReflection {

    public static Method get(Class<?> clazz, String name, boolean declared, Class<?>... parameter_types) throws NoSuchMethodException, SecurityException {
        return declared ? clazz.getDeclaredMethod(name, parameter_types) : clazz.getMethod(name, parameter_types);
    }

    public static Method get(Class<?> clazz, String name, Class<?>... parameter_types) throws NoSuchMethodException, SecurityException {
        try {
            return get(clazz, name, false, parameter_types);
        } catch (NoSuchMethodException ex) {
            return get(clazz, name, true, parameter_types);
        }
    }

    public static Method getAccessible(Class<?> clazz, String name, boolean declared, Class<?>... parameter_types)
            throws NoSuchMethodException, SecurityException {
        final Method method = get(clazz, name, declared, parameter_types);
        method.setAccessible(true);
        return method;
    }

    public static Method getAccessible(Class<?> clazz, String name, Class<?>... parameter_types)
            throws NoSuchMethodException, SecurityException {
        final Method method = get(clazz, name, parameter_types);
        method.setAccessible(true);
        return method;
    }

    public static Object invoke(Method method, Object object, Object... arguments)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return method.invoke(object, arguments);
    }

    public static Object invokeAccessible(Method method, Object object, Object... arguments)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final boolean b0 = method.isAccessible();
        try {
            return method.invoke(object, arguments);
        } finally {
            method.setAccessible(b0);
        }
    }

    public static Object invoke(Object object, String name, Class<?>[] parameter_types, Object... arguments)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return invoke(get(object.getClass(), name, parameter_types), object, arguments);
    }

    public static Object invokeAccessible(Object object, String name, Class<?>[] parameter_types, Object... arguments)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return invokeAccessible(get(object.getClass(), name, parameter_types), object, arguments);
    }

    public static Object invoke(Object object, String name, Object... arguments)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return invoke(get(object.getClass(), name, DataType.getPrimitive(arguments)), object, arguments);
    }

    public static Object invokeAccessible(Object object, String name, Object... arguments)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return invokeAccessible(get(object.getClass(), name, DataType.getPrimitive(arguments)), object, arguments);
    }

    public static Method setAccessible(Method method, boolean accessible) throws SecurityException {
        method.setAccessible(accessible);
        return method;
    }

    public static Method setAccessible(Method method) throws SecurityException, IllegalArgumentException {
        return setAccessible(method, true);
    }

}
