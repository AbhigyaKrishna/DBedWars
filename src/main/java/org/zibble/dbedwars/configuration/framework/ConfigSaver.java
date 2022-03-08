package org.zibble.dbedwars.configuration.framework;

import com.google.common.collect.Multimap;
import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.api.util.Pair;
import org.zibble.dbedwars.api.util.TriFunction;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;
import org.zibble.dbedwars.configuration.framework.annotations.Defaults;
import org.zibble.dbedwars.configuration.util.yaml.YamlUtils;
import org.zibble.dbedwars.utils.reflection.DataType;
import org.zibble.dbedwars.utils.reflection.resolver.MethodResolver;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.FieldWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;

public interface ConfigSaver<T> {

    Set<ConfigSaver<?>> SAVERS = new HashSet<>();

    ConfigSaver<Character> CHAR_SAVER = declare(DataType.CHARACTER::isType, (pair, section, saveFunction) ->
            saveFunction.apply(pair.getKey(), String.valueOf((char) pair.getValue()), section) ? 1 : 0);
    ConfigSaver<Integer> INT_SAVER = declare(DataType.INTEGER::isType, (pair, section, saveFunction) ->
            saveFunction.apply(pair.getKey(), pair.getValue(), section) ? 1 : 0);
    ConfigSaver<Long> LONG_SAVER = declare(DataType.LONG::isType, (pair, section, saveFunction) ->
            saveFunction.apply(pair.getKey(), pair.getValue(), section) ? 1 : 0);
    ConfigSaver<Float> FLOAT_SAVER = declare(DataType.FLOAT::isType, (pair, section, saveFunction) ->
            saveFunction.apply(pair.getKey(), (double) pair.getValue(), section) ? 1 : 0);
    ConfigSaver<Double> DOUBLE_SAVER = declare(DataType.DOUBLE::isType, (pair, section, saveFunction) ->
            saveFunction.apply(pair.getKey(), pair.getValue(), section) ? 1 : 0);
    ConfigSaver<Boolean> BOOLEAN_SAVER = declare(DataType.BOOLEAN::isType, (pair, section, saveFunction) ->
            saveFunction.apply(pair.getKey(), pair.getValue(), section) ? 1 : 0);
    ConfigSaver<String> STRING_SAVER = declare(String.class, (pair, section, saveFunction) ->
            saveFunction.apply(pair.getKey(), pair.getValue(), section) ? 1 : 0);
    ConfigSaver<UUID> UUID_SAVER = declare(UUID.class, (pair, section, saveFunction) ->
            saveFunction.apply(pair.getKey(), pair.getValue().toString(), section) ? 1 : 0);
    ConfigSaver<ConfigurationSection> CONFIGURATION_SAVER = declare(ConfigurationSection.class, (pair, section, saveFunction) ->
            saveFunction.apply(pair.getKey(), pair.getValue(), section) ? 1 : 0);
    ConfigSaver<Enum> ENUM_SAVER = declare(Enum.class, (pair, section, saveFunction) ->
            saveFunction.apply(pair.getKey(), pair.getValue().name(), section) ? 1 : 0);
    ConfigSaver<Savable> SAVEABLE_SAVER = declare(Savable.class, (pair, section, saveFunction) -> {
        ConfigurationSection child = YamlUtils.createNotExisting(section, pair.getKey());
        return pair.getValue().save(child);
    });

    List<ConfigSaver<?>> PRIMITIVE_SAVER = Arrays.asList(CHAR_SAVER, INT_SAVER, LONG_SAVER, FLOAT_SAVER, DOUBLE_SAVER, BOOLEAN_SAVER, STRING_SAVER, UUID_SAVER, CONFIGURATION_SAVER, ENUM_SAVER, SAVEABLE_SAVER);

    ConfigSaver<Collection> COLLECTION_SAVER = declare(Collection.class, (pair, section, saveFunction) ->
            saveFunction.apply(pair.getKey(), pair.getValue(), section) ? 1 : 0);
    ConfigSaver<Map> MAP_SAVER = declare(Map.class, (pair, section, saveFunction) -> {
        int count = 0;
        for (Object o : pair.getValue().entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            ConfigSaver saver = PRIMITIVE_SAVER.stream().filter(s -> s.isAssignable(entry.getValue().getClass())).findFirst().orElse(null);
            if (saver == null)
                throw new IllegalStateException("No saver found for " + entry.getValue().getClass());
            count += saver.save((String) entry.getKey(), entry.getValue(), section, saveFunction);
        }
        return count;
    });
    ConfigSaver<Multimap> MUlTIMAP_SAVER = declare(Multimap.class, (pair, section, saveFunction) -> {
        int count = 0;
        for (Object o : pair.getValue().asMap().entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            count += COLLECTION_SAVER.save((String) entry.getKey(), (Collection) entry.getValue(), section, saveFunction);
        }
        return count;
    });

    static <T> ConfigSaver<T> declare(Class<T> clazz, TriFunction<Pair<String, T>, ConfigurationSection, TriFunction<String, Object, ConfigurationSection, Boolean>, Integer> writer) {
        ConfigSaver<T> saver = new ConfigSaver<T>() {
            @Override
            public boolean isAssignable(Class<?> clz) {
                return clz.isAssignableFrom(clazz);
            }

            @Override
            public int save(String key, T value, ConfigurationSection section, TriFunction<String, Object, ConfigurationSection, Boolean> saveFunction) {
                return writer.apply(Pair.of(key, value), section, saveFunction);
            }
        };
        SAVERS.add(saver);
        return saver;
    }

    static <T> ConfigSaver<T> declare(Predicate<Class<?>> predicate, TriFunction<Pair<String, T>, ConfigurationSection, TriFunction<String, Object, ConfigurationSection, Boolean>, Integer> writer) {
        ConfigSaver<T> saver = new ConfigSaver<T>() {
            @Override
            public boolean isAssignable(Class<?> clz) {
                return predicate.test(clz);
            }

            @Override
            public int save(String key, T value, ConfigurationSection section, TriFunction<String, Object, ConfigurationSection, Boolean> saveFunction) {
                return writer.apply(Pair.of(key, value), section, saveFunction);
            }
        };
        SAVERS.add(saver);
        return saver;
    }

    static int save(Savable savable, ConfigurationSection section) {
        Stack<Field> fields = new Stack<>();
        fields.addAll(Arrays.asList(savable.getClass().getDeclaredFields()));
        fields.removeIf(field -> !field.isAnnotationPresent(ConfigPath.class));
        int count = 0;
        while (!fields.isEmpty()) {
            FieldWrapper field = new FieldWrapper(fields.pop());

            ConfigPath path = field.getField().getAnnotation(ConfigPath.class);

            if (Arrays.stream(path.type()).noneMatch(type -> type == ConfigPath.ConfigType.SAVEABLE))
                continue;

            String key = path.value().isEmpty() ? field.getField().getName().replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase() : path.value();
            Object value = field.get(savable);
            if (value == null)
                continue;

            for (ConfigSaver saver : SAVERS) {
                if (saver.isAssignable(value.getClass())) {
                    count += saver.save(key, value, section, (TriFunction<String, Object, ConfigurationSection, Boolean>) (k, v, s) -> {
                        switch (path.saveAction()) {
                            case NOT_SET: {
                                return YamlUtils.setNotSet(s, k, v);
                            }
                            case NOT_EQUAL: {
                                return YamlUtils.setNotEqual(s, k, v);
                            }
                            case NORMAL:
                            default: {
                                s.set(k, v);
                                return true;
                            }
                        }
                    });
                }
            }
        }
        return count;
    }

    static int saveDefaults(Savable savable, ConfigurationSection section) {
        Stack<Field> fields = new Stack<>();
        fields.addAll(Arrays.asList(savable.getClass().getDeclaredFields()));

        fields.removeIf(field -> !field.isAnnotationPresent(ConfigPath.class) || Defaults.DEFAULT_TYPES.stream().noneMatch(field::isAnnotationPresent));
        int count = 0;
        while (!fields.isEmpty()) {
            FieldWrapper field = new FieldWrapper(fields.pop());

            ConfigPath path = field.getField().getAnnotation(ConfigPath.class);
            Annotation annotation = null;
            for (Class<? extends Annotation> annotationClass : Defaults.DEFAULT_TYPES) {
                if (field.getField().isAnnotationPresent(annotationClass)) {
                    annotation = field.getField().getAnnotation(annotationClass);
                    break;
                }
            }

            String key = path.value().isEmpty() ? field.getField().getName().replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase() : path.value();

            MethodResolver resolver = new MethodResolver(annotation.getClass());
            SaveActionType saveAction = (SaveActionType) resolver.resolveWrapper("saveAction").invoke(annotation);
            Object value = resolver.resolveWrapper("value").invoke(annotation);

            for (ConfigSaver saver : SAVERS) {
                if (saver.isAssignable(value.getClass())) {
                    count += saver.save(key, value, section, (TriFunction<String, Object, ConfigurationSection, Boolean>) (k, v, s) -> {
                        switch (saveAction) {
                            case NOT_SET: {
                                return YamlUtils.setNotSet(s, k, v);
                            }
                            case NOT_EQUAL: {
                                return YamlUtils.setNotEqual(s, k, v);
                            }
                            case NORMAL:
                            default: {
                                s.set(k, v);
                                return true;
                            }
                        }
                    });
                }
            }
        }
        return count;
    }

    boolean isAssignable(Class<?> clazz);

    int save(String key, T value, ConfigurationSection section, TriFunction<String, Object, ConfigurationSection, Boolean> saveFunction);

}
