package org.zibble.dbedwars.utils.reflection;

import java.util.HashMap;
import java.util.Map;

/** Represents an enumeration of Java data types with corresponding classes */
public enum DataType {
    BYTE(byte.class, Byte.class),
    SHORT(short.class, Short.class),
    INTEGER(int.class, Integer.class),
    LONG(long.class, Long.class),
    CHARACTER(char.class, Character.class),
    FLOAT(float.class, Float.class),
    DOUBLE(double.class, Double.class),
    BOOLEAN(boolean.class, Boolean.class);

    private static final Map<Class<?>, DataType> CLASS_MAP = new HashMap<Class<?>, DataType>();

    // Initialize map for quick class lookup
    static {
        for (DataType type : values()) {
            CLASS_MAP.put(type.primitive, type);
            CLASS_MAP.put(type.reference, type);
        }
    }

    private final Class<?> primitive;
    private final Class<?> reference;

    /**
     * Construct a new data type
     *
     * <p>
     *
     * @param primitive Primitive class of this data type
     * @param reference Reference class of this data type
     */
    DataType(Class<?> primitive, Class<?> reference) {
        this.primitive = primitive;
        this.reference = reference;
    }

    /**
     * Returns the data type with the given primitive/reference class
     *
     * <p>
     *
     * @param clazz Primitive/Reference class of the data type
     * @return The data type
     */
    public static DataType fromClass(Class<?> clazz) {
        return CLASS_MAP.get(clazz);
    }

    /**
     * Returns the primitive class of the data type with the given reference class
     *
     * <p>
     *
     * @param clazz Reference class of the data type
     * @return The primitive class
     */
    public static Class<?> getPrimitive(Class<?> clazz) {
        DataType type = fromClass(clazz);
        return type == null ? clazz : type.getPrimitive();
    }

    /**
     * Returns the reference class of the data type with the given primitive class
     *
     * <p>
     *
     * @param clazz Primitive class of the data type
     * @return The reference class
     */
    public static Class<?> getReference(Class<?> clazz) {
        DataType type = fromClass(clazz);
        return type == null ? clazz : type.getReference();
    }

    /**
     * Returns the primitive class array of the given class array
     *
     * <p>
     *
     * @param classes Given class array
     * @return The primitive class array
     */
    public static Class<?>[] getPrimitive(Class<?>[] classes) {
        int length = classes == null ? 0 : classes.length;
        Class<?>[] types = new Class<?>[length];
        for (int index = 0; index < length; index++) {
            types[index] = getPrimitive(classes[index]);
        }
        return types;
    }

    /**
     * Returns the reference class array of the given class array
     *
     * <p>
     *
     * @param classes Given class array
     * @return The reference class array
     */
    public static Class<?>[] getReference(Class<?>[] classes) {
        int length = classes == null ? 0 : classes.length;
        Class<?>[] types = new Class<?>[length];
        for (int index = 0; index < length; index++) {
            types[index] = getReference(classes[index]);
        }
        return types;
    }

    /**
     * Returns the primitive class array of the given object array
     *
     * <p>
     *
     * @param objects Given object array
     * @return The primitive class array
     */
    public static Class<?>[] getPrimitive(Object[] objects) {
        int length = objects == null ? 0 : objects.length;
        Class<?>[] types = new Class<?>[length];
        for (int index = 0; index < length; index++) {
            types[index] = getPrimitive(objects[index].getClass());
        }
        return types;
    }

    /**
     * Returns the reference class array of the given object array
     *
     * <p>
     *
     * @param objects Given object array
     * @return The reference class array
     */
    public static Class<?>[] getReference(Object[] objects) {
        int length = objects == null ? 0 : objects.length;
        Class<?>[] types = new Class<?>[length];
        for (int index = 0; index < length; index++) {
            types[index] = getReference(objects[index].getClass());
        }
        return types;
    }

    /**
     * Compares two class arrays on equivalence
     *
     * <p>
     *
     * @param primary Primary class array
     * @param secondary Class array which is compared to the primary array
     * @return Whether these arrays are equal or not
     */
    public static boolean compare(Class<?>[] primary, Class<?>[] secondary) {
        if (primary == null || secondary == null || primary.length != secondary.length) {
            return false;
        }
        for (int index = 0; index < primary.length; index++) {
            Class<?> primaryClass = primary[index];
            Class<?> secondaryClass = secondary[index];
            if (primaryClass.equals(secondaryClass)
                    || primaryClass.isAssignableFrom(secondaryClass)) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * Returns the primitive class of this data type
     *
     * <p>
     *
     * @return The primitive class
     */
    public Class<?> getPrimitive() {
        return primitive;
    }

    /**
     * Returns the reference class of this data type
     *
     * <p>
     *
     * @return The reference class
     */
    public Class<?> getReference() {
        return reference;
    }

    /**
     * Returns true if the given Object is a valid instance of this data type.
     *
     * <p>
     *
     * @param obj Object to check
     * @return true if the given Object is a valid instance of this data type
     */
    public boolean isInstance(Object obj) {
        return obj != null
                && (obj.getClass().equals(getPrimitive()) || obj.getClass().equals(getReference()));
    }

    public boolean isType(Class<?> clazz) {
        return clazz.equals(getPrimitive()) || clazz.equals(getReference());
    }

    /**
     * Gets whether this represents a number data type.
     *
     * <p>
     *
     * @return Whether this represents a number data type
     */
    public boolean isNumber() {
        return Number.class.isAssignableFrom(reference);
    }
}
