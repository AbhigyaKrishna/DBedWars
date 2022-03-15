package org.zibble.dbedwars.configuration.framework;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.api.util.EnumUtil;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;
import org.zibble.dbedwars.configuration.framework.annotations.Defaults;
import org.zibble.dbedwars.utils.ConfigurationUtils;
import org.zibble.dbedwars.utils.reflection.DataType;
import org.zibble.dbedwars.utils.reflection.general.FieldReflection;
import org.zibble.dbedwars.utils.reflection.resolver.FieldResolver;
import org.zibble.dbedwars.utils.reflection.resolver.MethodResolver;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.ClassWrapper;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.FieldWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface ConfigLoader<T> {

    Set<ConfigLoader<?>> LOADERS = new HashSet<>();
    
    ConfigLoader<Character> CHAR_LOADER = declare(DataType.CHARACTER::isType, (field, value) -> ((String) value).charAt(0));
    ConfigLoader<Integer> INT_LOADER = declare(DataType.INTEGER::isType, (field, value) -> (int) value);
    ConfigLoader<Long> LONG_LOADER = declare(DataType.LONG::isType, (field, value) -> (long) value);
    ConfigLoader<Float> FLOAT_LOADER = declare(DataType.FLOAT::isType, (field, value) -> (float) value);
    ConfigLoader<Double> DOUBLE_LOADER = declare(DataType.DOUBLE::isType, (field, value) -> (double) value);
    ConfigLoader<Boolean> BOOLEAN_LOADER = declare(DataType.BOOLEAN::isType, (field, value) -> (boolean) value);
    ConfigLoader<String> STRING_LOADER = declare(String.class, (field, value) -> (String) value);
    ConfigLoader<UUID> UUID_LOADER = declare(UUID.class, (field, value) -> UUID.fromString((String) value));
    ConfigLoader<ConfigurationSection> CONFIGURATION_LOADER = declare(ConfigurationSection.class, (field, value) -> (ConfigurationSection) value);
    ConfigLoader<Enum> ENUM_LOADER = declare(Enum.class, (field, value) -> EnumUtil.matchEnum((String) value, ((Class<Enum>) field.getType()).getEnumConstants()));
    ConfigLoader<Loadable> LOADABLE_LOADER = declare(Loadable.class, (field, value) -> {
        Loadable loadable = new ClassWrapper<>((Class<Loadable>) field.getType()).newInstance();
        loadable.load((ConfigurationSection) value);
        return loadable;
    });

    List<ConfigLoader<?>> PRIMITIVE_LOADER = Arrays.asList(CHAR_LOADER, INT_LOADER, LONG_LOADER, FLOAT_LOADER, DOUBLE_LOADER, BOOLEAN_LOADER, STRING_LOADER, UUID_LOADER, CONFIGURATION_LOADER, ENUM_LOADER, LOADABLE_LOADER);

    ConfigLoader<Collection> COLLECTION_LOADER = declare(Collection.class, (field, value) -> {
        Class<?> fieldGeneric = FieldReflection.getParameterizedTypesClasses(field.getField())[0];
        Class<?> valueGeneric = (Class<?>) ((ParameterizedType) value.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Collection col = null;
        if (field.getType().isAssignableFrom(List.class)) {
            col = new ArrayList();
        } else if (field.getType().isAssignableFrom(Set.class)) {
            col = new LinkedHashSet();
        }
        if (value.getClass().equals(field.getType()) && fieldGeneric.equals(valueGeneric)) {
            col.addAll((Collection) value);
            return col;
        } else {
            ConfigLoader<?> loader = PRIMITIVE_LOADER.stream().filter(l -> l.isAssignable(fieldGeneric)).findFirst().orElse(null);
            if (loader == null)
                throw new IllegalArgumentException("No loader found for " + fieldGeneric.getName());
            for (Object o : (Collection) value)
                col.add(loader.load(field, o));
            return col;
        }
    });
    ConfigLoader<Map> MAP_LOADER = declare(Map.class, (field, value) -> {
        Class<?>[] classes = FieldReflection.getParameterizedTypesClasses(field.getField());
        if (!classes[0].equals(String.class) || PRIMITIVE_LOADER.stream().noneMatch(loader -> loader.isAssignable(classes[1]))) {
            throw new IllegalArgumentException("Map field " + field.getField().getName() + " is not a valid map field");
        }
        ConfigurationSection section = (ConfigurationSection) value;
        Map map = new LinkedHashMap();
        ConfigLoader<?> writer = PRIMITIVE_LOADER.stream().filter(loader -> loader.isAssignable(classes[1])).findFirst().orElse(null);
        for (String key : section.getKeys(false)) {
            map.put(key, writer.load(field, section.get(key)));
        }
        return map;
    });
    ConfigLoader<Multimap> MULTIMAP_LOADER = declare(Multimap.class, (field, value) -> {
        Class<?>[] classes = FieldReflection.getParameterizedTypesClasses(field.getField());
        if (!classes[0].equals(String.class) || PRIMITIVE_LOADER.stream().noneMatch(loader -> loader.isAssignable(classes[1]))) {
            throw new IllegalArgumentException("Multimap field " + field.getField().getName() + " is not a valid multimap field");
        }
        ConfigurationSection section = (ConfigurationSection) value;
        Multimap multimap = LinkedHashMultimap.create();
        for (String key : section.getKeys(false)) {
            Object val = section.get(key);
            Class<?> valueGeneric = (Class<?>) ((ParameterizedType) val.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            if (valueGeneric.equals(classes[1])) {
                multimap.putAll(key, (Collection) val);
            } else {
                ConfigLoader<?> loader = PRIMITIVE_LOADER.stream().filter(l -> l.isAssignable(classes[1])).findFirst().orElse(null);
                if (loader == null)
                    throw new IllegalArgumentException("No loader found for " + classes[1].getName());
                for (Object o : (Collection) val)
                    multimap.put(key, loader.load(field, o));
            }
        }
        return multimap;
    });
    
    static <T> ConfigLoader<T> declare(Class<T> clazz, BiFunction<FieldWrapper, Object, ? extends T> encoder) {
        ConfigLoader<T> loader = new ConfigLoader<T>() {
            @Override
            public boolean isAssignable(Class<?> clz) {
                return clz.isAssignableFrom(clazz);
            }

            @Override
            public T load(FieldWrapper field, Object value) {
                return encoder.apply(field, value);
            }
        };
        LOADERS.add(loader);
        return loader;
    }

    static <T> ConfigLoader<T> declare(Predicate<Class<?>> predicate, BiFunction<FieldWrapper, Object, ? extends T> encoder) {
        ConfigLoader<T> loader = new ConfigLoader<T>() {
            @Override
            public boolean isAssignable(Class<?> clz) {
                return predicate.test(clz);
            }

            @Override
            public T load(FieldWrapper field, Object value) {
                return encoder.apply(field, value);
            }
        };
        LOADERS.add(loader);
        return loader;
    }

    static void load(Loadable loadable, ConfigurationSection section) {
        Stack<Field> fields = new Stack<>();
        fields.addAll(Arrays.asList(loadable.getClass().getDeclaredFields()));
        fields.removeIf(field -> !field.isAnnotationPresent(ConfigPath.class));
        while (!fields.isEmpty()) {
            FieldWrapper field = new FieldWrapper(fields.pop());

            ConfigPath path = field.getField().getAnnotation(ConfigPath.class);

            if (Arrays.stream(path.type()).noneMatch(type -> type == ConfigPath.ConfigType.LOADABLE))
                continue;

            String key = path.value().isEmpty() ? field.getField().getName().replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase() : path.value();
            Object value = section.get(key);

            if (value == null) {
                Annotation annotation = null;
                for (Class<? extends Annotation> annotationClass : Defaults.DEFAULT_TYPES) {
                    if (field.getField().isAnnotationPresent(annotationClass)) {
                        annotation = field.getField().getAnnotation(annotationClass);
                        break;
                    }
                }

                if (annotation != null) {
                    if (annotation instanceof Defaults.Variable) {
                        value = new FieldResolver(loadable.getClass()).resolveWrapper(((Defaults.Variable) annotation).value()).get(loadable);
                    } else {
                        value = new MethodResolver(annotation.getClass()).resolveWrapper("value").invoke(annotation);
                    }
                }
            }

            if (value == null) {
                continue;
            }

            for (ConfigLoader<?> loader : LOADERS) {
                if (loader.isAssignable(field.getType())) {
                    loader.write(field, loadable, value);
                }
            }
        }
    }

    boolean isAssignable(Class<?> clazz);
    
    T load(FieldWrapper field, Object object);

    default void write(FieldWrapper field, Object loadable, Object value) {
        field.set(loadable, this.load(field, value));
    }

}
