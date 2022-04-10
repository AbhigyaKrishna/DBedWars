package org.zibble.dbedwars.utils.reflection.general;

public class EnumReflection {

    public static <T extends Enum<T>> T getEnumConstant(Class<T> clazz, String name) {
        try {
            return Enum.valueOf(clazz, name);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static <T extends Enum<T>> T getEnumConstant(Class<T> enumClass, String enumName, int fallbackOrdinal) {
        try {
            return Enum.valueOf(enumClass, enumName);
        } catch (IllegalArgumentException e) {
            T[] constants = enumClass.getEnumConstants();
            if (constants.length > fallbackOrdinal) {
                return constants[fallbackOrdinal];
            }
            throw e;
        }
    }

}
