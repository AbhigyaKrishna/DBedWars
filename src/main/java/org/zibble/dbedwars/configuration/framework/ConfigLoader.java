package org.zibble.dbedwars.configuration.framework;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.api.util.DataType;
import org.zibble.dbedwars.api.util.EnumUtil;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;
import org.zibble.dbedwars.configuration.framework.annotations.Defaults;
import org.zibble.dbedwars.utils.Debugger;
import org.zibble.dbedwars.utils.reflection.general.FieldReflection;
import org.zibble.dbedwars.utils.reflection.resolver.ConstructorResolver;
import org.zibble.dbedwars.utils.reflection.resolver.FieldResolver;
import org.zibble.dbedwars.utils.reflection.resolver.MethodResolver;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.FieldWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface ConfigLoader<T> {

    Set<ConfigLoader<?>> LOADERS = new HashSet<>();

    ConfigLoader<Character> CHAR_LOADER = declare(DataType.CHARACTER::isType, (field, value) -> ((String) value).charAt(0));
    ConfigLoader<Integer> INT_LOADER = declare(DataType.INTEGER::isType, (field, value) -> {
        try {
            return  (int) value;
        } catch (ClassCastException e) {
            return NumberUtils.toInt(String.valueOf(value));
        }
    });
    ConfigLoader<Long> LONG_LOADER = declare(DataType.LONG::isType, (field, value) -> {
        try {
            return  (long) value;
        } catch (ClassCastException e) {
            return NumberUtils.toLong(String.valueOf(value));
        }
    });
    ConfigLoader<Float> FLOAT_LOADER = declare(DataType.FLOAT::isType, (field, value) -> {
        try {
            return (float) value;
        } catch (ClassCastException e) {
            return NumberUtils.toFloat(String.valueOf(value));
        }
    });
    ConfigLoader<Double> DOUBLE_LOADER = declare(DataType.DOUBLE::isType, (field, value) -> {
        try {
            return (double) value;
        } catch (ClassCastException e) {
            return NumberUtils.toDouble(String.valueOf(value));
        }
    });
    ConfigLoader<Boolean> BOOLEAN_LOADER = declare(DataType.BOOLEAN::isType, (field, value) -> {
        try {
            return (boolean) value;
        } catch (ClassCastException e) {
            return Boolean.valueOf(String.valueOf(value));
        }
    });
    ConfigLoader<String> STRING_LOADER = declare(String.class, (field, value) -> String.valueOf(value));
    ConfigLoader<UUID> UUID_LOADER = declare(UUID.class, (field, value) -> UUID.fromString((String) value));
    ConfigLoader<ConfigurationSection> CONFIGURATION_LOADER = declare(ConfigurationSection.class, (field, value) -> (ConfigurationSection) value);
    ConfigLoader<Enum> ENUM_LOADER = declare(Enum.class, (field, value) -> EnumUtil.matchEnum((String) value, ((Class<Enum>) field.getType()).getEnumConstants()));
    ConfigLoader<Loadable> LOADABLE_LOADER = declare(Loadable.class, (field, value) -> {
        Loadable loadable = (Loadable) new ConstructorResolver(field.getType()).resolveWrapper(new Class[0]).newInstance();
        loadable.load((ConfigurationSection) value);
        return loadable;
    });

    List<ConfigLoader<?>> PRIMITIVE_LOADER = Arrays.asList(CHAR_LOADER, INT_LOADER, LONG_LOADER, FLOAT_LOADER, DOUBLE_LOADER, BOOLEAN_LOADER, STRING_LOADER, UUID_LOADER, CONFIGURATION_LOADER, ENUM_LOADER, LOADABLE_LOADER);

    ConfigLoader<Collection> COLLECTION_LOADER = declare(Collection.class, (field, value) -> {
        Class<?> fieldGeneric = field.getGenericTypes()[0];
        Collection col = null;
        if (field.getType().isAssignableFrom(List.class)) {
            col = new LinkedList();
        } else if (field.getType().isAssignableFrom(Set.class)) {
            col = new LinkedHashSet();
        }

        FieldQuery query = new FieldQuery() {
            @Override
            public FieldWrapper getField() {
                return null;
            }

            @Override
            public Class<?> getType() {
                return fieldGeneric;
            }
        };

        for (ConfigLoader<?> configLoader : PRIMITIVE_LOADER) {
            if (configLoader.isAssignable(fieldGeneric)) {
                if (value instanceof Collection) {
                    for (Object val : (Collection) value) {
                        col.add(configLoader.load(query, val));
                    }
                } else if (value instanceof ConfigurationSection){
                    for (String key : ((ConfigurationSection) value).getKeys(false)) {
                        col.add(configLoader.load(query, ((ConfigurationSection) value).get(key)));
                    }
                }
            }
        }

        return col;
    });
    ConfigLoader<Map> MAP_LOADER = declare(Map.class, (field, value) -> {
        Class<?>[] classes = field.getGenericTypes();
        if (!classes[0].equals(String.class) || PRIMITIVE_LOADER.stream().noneMatch(loader -> loader.isAssignable(classes[1]))) {
            throw new IllegalArgumentException("Map field " + field.getField().getField().getName() + " is not a valid map field");
        }
        ConfigurationSection section = (ConfigurationSection) value;
        Map map = new LinkedHashMap();
        ConfigLoader<?> writer = PRIMITIVE_LOADER.stream().filter(loader -> loader.isAssignable(classes[1])).findFirst().orElse(null);
        for (String key : section.getKeys(false)) {
            map.put(key, writer.load(new FieldQuery() {
                @Override
                public FieldWrapper getField() {
                    return null;
                }

                @Override
                public Class<?> getType() {
                    return classes[1];
                }
            }, section.get(key)));
        }
        return map;
    });
    ConfigLoader<Multimap> MULTIMAP_LOADER = declare(Multimap.class, (field, value) -> {
        Class<?>[] classes = FieldReflection.getParameterizedTypesClasses(field.getField().getField());
        if (!classes[0].equals(String.class) || PRIMITIVE_LOADER.stream().noneMatch(loader -> loader.isAssignable(classes[1]))) {
            throw new IllegalArgumentException("Multimap field " + field.getField().getField().getName() + " is not a valid multimap field");
        }
        ConfigurationSection section = (ConfigurationSection) value;
        Multimap multimap = LinkedHashMultimap.create();
        for (String key : section.getKeys(false)) {
            Object val = section.get(key);
            Collection loaded = COLLECTION_LOADER.load(new FieldQuery() {
                @Override
                public FieldWrapper getField() {
                    return null;
                }

                @Override
                public Class<?> getType() {
                    return List.class;
                }

                @Override
                public Class<?>[] getGenericTypes() {
                    return new Class<?>[]{classes[1]};
                }
            }, val);
            multimap.putAll(key, loaded);
        }
        return multimap;
    });

    static <T> ConfigLoader<T> declare(Class<T> clazz, BiFunction<FieldQuery, Object, ? extends T> encoder) {
        ConfigLoader<T> loader = new ConfigLoader<T>() {
            @Override
            public boolean isAssignable(Class<?> clz) {
                return clazz.isAssignableFrom(clz);
            }

            @Override
            public T load(FieldQuery field, Object value) {
                return encoder.apply(field, value);
            }
        };
        LOADERS.add(loader);
        return loader;
    }

    static <T> ConfigLoader<T> declare(Predicate<Class<?>> predicate, BiFunction<FieldQuery, Object, ? extends T> encoder) {
        ConfigLoader<T> loader = new ConfigLoader<T>() {
            @Override
            public boolean isAssignable(Class<?> clz) {
                return predicate.test(clz);
            }

            @Override
            public T load(FieldQuery field, Object value) {
                return encoder.apply(field, value);
            }
        };
        LOADERS.add(loader);
        return loader;
    }

    static void load(Loadable loadable, ConfigurationSection section) {
        Debugger.debug("Loading " + loadable.getClass().getName() + " from " + section.getCurrentPath());

        Stack<Field> fields = new Stack<>();
        fields.addAll(Arrays.asList(loadable.getClass().getDeclaredFields()));
        fields.removeIf(field -> !field.isAnnotationPresent(ConfigPath.class));
        while (!fields.isEmpty()) {
            FieldWrapper field = new FieldWrapper(fields.pop());

            ConfigPath path = field.getField().getAnnotation(ConfigPath.class);

            if (Arrays.stream(path.type()).noneMatch(type -> type == ConfigPath.ConfigType.LOADABLE))
                continue;

            String key = path.value().isEmpty() ? field.getField().getName().replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase() : path.value();
            Object confValue = section.get(key);

            if (confValue == null) {
                Annotation annotation = null;
                for (Class<? extends Annotation> annotationClass : Defaults.DEFAULT_TYPES) {
                    if (field.getField().isAnnotationPresent(annotationClass)) {
                        annotation = field.getField().getAnnotation(annotationClass);
                        break;
                    }
                }

                if (annotation != null) {
                    if (annotation instanceof Defaults.Variable) {
                        field.set(loadable, new FieldResolver(loadable.getClass()).resolveWrapper(((Defaults.Variable) annotation).value()).get(loadable));
                    } else {
                        field.set(loadable, new MethodResolver(annotation.getClass()).resolveWrapper("value").invoke(annotation));
                    }
                }
            } else {
                for (ConfigLoader<?> loader : LOADERS) {
                    if (loader.isAssignable(field.getType())) {
                        loader.write(field, loadable, loader.load(() -> field, confValue));
                    }
                }
            }
        }
    }

    boolean isAssignable(Class<?> clazz);

    T load(FieldQuery field, Object object);

    default void write(FieldWrapper field, Loadable loadable, Object value) {
        field.set(loadable, value);
    }

    interface FieldQuery {

        FieldWrapper getField();

        default Class<?> getType() {
            return this.getField().getType();
        }

        default Class<?>[] getGenericTypes() {
            return FieldReflection.getParameterizedTypesClasses(this.getField().getField());
        }

    }

}
