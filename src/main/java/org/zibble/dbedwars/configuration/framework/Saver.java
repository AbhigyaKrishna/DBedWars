package org.zibble.dbedwars.configuration.framework;

import com.google.common.collect.Multimap;
import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.configuration.framework.annotations.Save;
import org.zibble.dbedwars.configuration.util.Saveable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Stack;

public class Saver {

    protected static int save(Savable savable, ConfigurationSection section) {
        Stack<Field> fields = new Stack<>();
        fields.addAll(Arrays.asList(savable.getClass().getDeclaredFields()));
        fields.removeIf(field -> !field.isAnnotationPresent(Save.class));
        int count = 0;
        while (!fields.isEmpty()) {
            Field field = fields.pop();
            field.setAccessible(true);
            String key = field.getAnnotation(Save.class).value();
            Object value = getField(field, savable);
            if (value == null) {
                defaultNull(savable, section, key, field);
                continue;
            }
            SavableType saver = SavableType.get(field.getType());
            if (saver == null) throw new IllegalArgumentException("Unknown SavableType: " + field.getType());
            switch (saver) {
                case COLLECTION: {

                }
                case STRING:
                case INTEGER:
                case BOOLEAN:
                case DOUBLE:
                case CHAR:
                case BYTE:
                case SHORT:
                case LONG:
                case FLOAT: {
                    Object a = findValue(saver, savable, section, key, field);
                    section.set(key, a);
                    count++;
                    break;
                }
                case SAVABLE: {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    private static Object getField(Field field, Savable instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void defaultNull(Savable savable, ConfigurationSection section, String key, Field value) {

    }

    private static boolean setter (Savable savable, ConfigurationSection section, String key, Field field) {
        Object value = getField(field, savable);
        if (value == null) return false;
        return
    }

    private static Object findValue(SavableType type, Savable savable, ConfigurationSection section, String key, Field field) {
        switch (type) {
            case STRING:
            case INTEGER:
            case BOOLEAN:
            case DOUBLE:
            case CHAR:
            case BYTE:
            case SHORT:
            case LONG:
            case FLOAT: {
                return getField(field, savable);
            }
            case SAVABLE: {
                Savable value = (Savable) getField(field, savable);
                save(value, section.getConfigurationSection(key));
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown SavableType: " + type);
    }

    private enum SavableType {
        STRING(String.class),
        INTEGER(Integer.class, int.class),
        FLOAT(Float.class, float.class),
        BOOLEAN(Boolean.class, boolean.class),
        DOUBLE(Double.class, double.class),
        LONG(Long.class, long.class),
        SHORT(Short.class, short.class),
        BYTE(Byte.class, byte.class),
        CHAR(Character.class, char.class),

        SAVABLE(Savable.class),

        COLLECTION(Collection.class),
        MULTIMAP(Multimap.class),
        MAP(Map.class),

        ;

        private final Class<?>[] classes;

        SavableType(Class<?>... classes) {
            this.classes = classes;
        }

        public static SavableType get(Class<?> clazz) {
            for (SavableType saver : SavableType.values()) {
                for (Class<?> aClass : saver.classes) {
                    if (aClass.isAssignableFrom(clazz) || aClass.equals(clazz)) return saver;
                }
            }
            return null;
        }
    }

}
