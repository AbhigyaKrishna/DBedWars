package org.zibble.dbedwars.configuration.framework;

import com.google.common.collect.Multimap;
import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.configuration.framework.annotations.Defaults;
import org.zibble.dbedwars.configuration.framework.annotations.Load;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Loader {

    public static void load(Loadable loadable, ConfigurationSection section) {
        Stack<Field> fields = new Stack<>();
        fields.addAll(Arrays.asList(loadable.getClass().getDeclaredFields()));
        fields.removeIf(field -> !field.isAnnotationPresent(Load.class));
        while (!fields.isEmpty()) {
            Field field = fields.pop();
            field.setAccessible(true);
            String key = field.getAnnotation(Load.class).value();
            Object value = section.get(key);
            if (value == null) {
                defaultNull(loadable, field);
                continue;
            }
            LoadableType loader = LoadableType.get(field.getType());
            if (loader == null) throw new IllegalArgumentException("Unknown LoadableType: " + field.getType());
            switch (loader) {
                case COLLECTION: {
                    try {
                        List<?> list = (List<?>) value;
                        Collection collection = (Collection<?>) field.get(loadable);
                        for (Object o : list) {
                            collection.add(findValue(field, loader, o));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case MULTIMAP: {
                    try {
                        ConfigurationSection section1 = (ConfigurationSection) value;
                        Multimap multimap = (Multimap) field.get(loadable);
                        for (String key1 : section1.getKeys(false)) {
                            List<?> list = section1.getList(key1);
                            for (Object o : list) {
                                multimap.put(key1, findValue(field, loader, o));
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case MAP: {
                    try {
                        ConfigurationSection section1 = (ConfigurationSection) value;
                        Map map = (Map<String, ?>) field.get(loadable);
                        for (String s : section1.getKeys(false)) {
                            Object value1 = findValue(field, loader, section1.get(s));
                            map.put(s, value1);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case LOADABLE:
                case CHAR:
                case BYTE:
                case SHORT:
                case FLOAT:
                case LONG:
                case DOUBLE:
                case BOOLEAN:
                case INTEGER:
                case STRING: {
                    setValue(loadable, field, findValue(field, loader, value));
                    break;
                }
            }
        }
    }

    private static Object findValue(Field field, LoadableType loader, Object value) {
        switch (loader) {
            case LOADABLE: {
                try {
                    Loadable loadableValue = (Loadable) field.getType().getConstructor().newInstance();
                    load(loadableValue, (ConfigurationSection) value);
                    return loadableValue;
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
                break;
            }
            case CHAR: return ((String) value).charAt(0);
            case BYTE: return ((Integer) value).byteValue();
            case SHORT: return ((Integer) value).shortValue();
            case FLOAT: return ((Double) value).floatValue();

            case LONG:
            case DOUBLE:
            case BOOLEAN:
            case INTEGER:
            case STRING: return value;
        }
        throw new IllegalArgumentException("Unknown LoadableType: " + loader);
    }

    private static void setValue(Loadable loadable, Field field, Object value) {
        try {
            //TODO PRIMITIVE SUPPORT
            field.set(loadable, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void defaultNull(Loadable loadable, Field field) {
        /*if (field.isAnnotationPresent(Defaults.Character.class)) {
            Defaults.Character defaults = field.getAnnotation(Defaults.Character.class);
            setValue(loadable, field, defaults.value());
        } else if (field.isAnnotationPresent(Defaults.Byte.class)) {
            Defaults.Byte defaults = field.getAnnotation(Defaults.Byte.class);
            setValue(loadable, field, defaults.value());
        } else if (field.isAnnotationPresent(Defaults.Short.class)) {
            Defaults.Short defaults = field.getAnnotation(Defaults.Short.class);
            setValue(loadable, field, defaults.value());
        } else if (field.isAnnotationPresent(Defaults.Float.class)) {
            Defaults.Float defaults = field.getAnnotation(Defaults.Float.class);
            setValue(loadable, field, defaults.value());
        } else if (field.isAnnotationPresent(Defaults.Long.class)) {
            Defaults.Long defaults = field.getAnnotation(Defaults.Long.class);
            setValue(loadable, field, defaults.value());
        } else if (field.isAnnotationPresent(Defaults.Double.class)) {
            Defaults.Double defaults = field.getAnnotation(Defaults.Double.class);
            setValue(loadable, field, defaults.value());
        } else if (field.isAnnotationPresent(Defaults.Boolean.class)) {
            Defaults.Boolean defaults = field.getAnnotation(Defaults.Boolean.class);
            setValue(loadable, field, defaults.value());
        } else if (field.isAnnotationPresent(Defaults.Integer.class)) {
            Defaults.Integer defaults = field.getAnnotation(Defaults.Integer.class);
            setValue(loadable, field, defaults.value());
        } else if (field.isAnnotationPresent(Defaults.String.class)) {
            Defaults.String defaults = field.getAnnotation(Defaults.String.class);
            setValue(loadable, field, defaults.value());
        }*/
        //TODO CHANGE
    }

    private enum LoadableType {
        STRING(String.class),
        INTEGER(Integer.class, int.class),
        FLOAT(Float.class, float.class),
        BOOLEAN(Boolean.class, boolean.class),
        DOUBLE(Double.class, double.class),
        LONG(Long.class, long.class),
        SHORT(Short.class, short.class),
        BYTE(Byte.class, byte.class),
        CHAR(Character.class, char.class),

        LOADABLE(Loadable.class),

        COLLECTION(Collection.class),
        MULTIMAP(Multimap.class),
        MAP(Map.class),

        ;

        private final Class<?>[] classes;

        LoadableType(Class<?>... classes) {
            this.classes = classes;
        }

        public static LoadableType get(Class<?> clazz) {
            for (LoadableType loader : LoadableType.values()) {
                for (Class<?> aClass : loader.classes) {
                    if (aClass.isAssignableFrom(clazz) || aClass.equals(clazz)) return loader;
                }
            }
            return null;
        }
    }

}
