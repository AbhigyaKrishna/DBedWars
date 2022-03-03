package org.zibble.dbedwars.utils.reflection.accessor;

import org.zibble.dbedwars.utils.reflection.DataType;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class FieldAccessor {

    private final Field field;

    public FieldAccessor(Field field) {
        this.field = field;
        field.setAccessible(true);
    }

    private static void setField(Object object, Object value, Field foundField) {
        boolean isStatic = (foundField.getModifiers() & Modifier.STATIC) == Modifier.STATIC;
        if (isStatic) {
            setStaticFieldUsingUnsafe(foundField, value);
        } else {
            setFieldUsingUnsafe(foundField, object, value);
        }
    }

    private static void setStaticFieldUsingUnsafe(final Field field, final Object newValue) {
        try {
            field.setAccessible(true);
            int fieldModifiersMask = field.getModifiers();
            boolean isFinalModifierPresent =
                    (fieldModifiersMask & Modifier.FINAL) == Modifier.FINAL;
            if (isFinalModifierPresent) {
                AccessController.doPrivileged(
                        (PrivilegedAction<Object>)
                                () -> {
                                    try {
                                        Unsafe unsafe = getUnsafe();
                                        long offset = unsafe.staticFieldOffset(field);
                                        Object base = unsafe.staticFieldBase(field);
                                        setFieldUsingUnsafe(base, field.getType(), offset, newValue, unsafe);
                                        return null;
                                    } catch (Throwable t) {
                                        throw new RuntimeException(t);
                                    }
                                });
            } else {
                field.set(null, newValue);
            }
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void setFieldUsingUnsafe(final Field field, final Object object, final Object newValue) {
        try {
            field.setAccessible(true);
            int fieldModifiersMask = field.getModifiers();
            boolean isFinalModifierPresent = (fieldModifiersMask & Modifier.FINAL) == Modifier.FINAL;
            if (isFinalModifierPresent) {
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                    try {
                        Unsafe unsafe = getUnsafe();
                        long offset = unsafe.objectFieldOffset(field);
                        setFieldUsingUnsafe(object, field.getType(), offset, newValue, unsafe);
                        return null;
                    } catch (Throwable t) {
                        throw new RuntimeException(t);
                    }
                });
            } else {
                try {
                    field.set(object, newValue);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Unsafe getUnsafe()
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
            SecurityException {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return (Unsafe) field.get(null);
    }

    private static void setFieldUsingUnsafe(Object base, Class<?> type, long offset, Object newValue, Unsafe unsafe) {
        if (DataType.INTEGER.isType(type)) {
            unsafe.putInt(base, offset, ((Integer) newValue));
        } else if (DataType.SHORT.isType(type)) {
            unsafe.putShort(base, offset, ((Short) newValue));
        } else if (DataType.LONG.isType(type)) {
            unsafe.putLong(base, offset, ((Long) newValue));
        } else if (DataType.BYTE.isType(type)) {
            unsafe.putByte(base, offset, ((Byte) newValue));
        } else if (DataType.BOOLEAN.isType(type)) {
            unsafe.putBoolean(base, offset, ((Boolean) newValue));
        } else if (DataType.FLOAT.isType(type)) {
            unsafe.putFloat(base, offset, ((Float) newValue));
        } else if (DataType.DOUBLE.isType(type)) {
            unsafe.putDouble(base, offset, ((Double) newValue));
        } else if (DataType.CHARACTER.isType(type)) {
            unsafe.putChar(base, offset, ((Character) newValue));
        } else {
            unsafe.putObject(base, offset, newValue);
        }
    }

    public boolean isStatic() {
        return Modifier.isStatic(field.getModifiers());
    }

    public boolean isPublic() {
        return Modifier.isPublic(field.getModifiers());
    }

    public boolean isFinal() {
        return Modifier.isFinal(field.getModifiers());
    }

    public <T> T get(Object obj) {
        try {
            //noinspection unchecked
            return (T) field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void set(Object obj, T value) {
        setField(obj, value, field);
    }

    public Class<?> getType() {
        return this.field.getType();
    }

    public Field getField() {
        return this.field;
    }

}
