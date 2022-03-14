package org.zibble.dbedwars.utils.reflection.annotation;

import org.zibble.dbedwars.api.version.Version;
import org.zibble.dbedwars.utils.reflection.resolver.*;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.ClassWrapper;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.ConstructorWrapper;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.FieldWrapper;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.MethodWrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReflectionAnnotations {

    public static final ReflectionAnnotations INSTANCE = new ReflectionAnnotations();

    static final Pattern VARIABLE_REF_PATTERN = Pattern.compile("@var\\((.*)\\)");
    static final Pattern PARAMETER_PATTERN = Pattern.compile("(.*)\\((.*)\\)");
    static final Pattern COMMAS_PATTERN = Pattern.compile("([^,\\[]*\\[[^]]*])(?:,|$)|([^,\\[\\]]+(?:,|$))");

    ClassResolver classResolver = new ClassResolver();

    private ReflectionAnnotations() {
    }

    public void load(Class<?> clazz) {
        this.load(clazz, null);
    }

    public <T> void load(Class<T> clazz, T object) {
        fields: for (Field declaredField : clazz.getDeclaredFields()) {
            FieldWrapper field = new FieldWrapper(declaredField);

            if (declaredField.isAnnotationPresent(ClassRef.class)) {
                ClassRef[] annotations = declaredField.getAnnotationsByType(ClassRef.class);

                for (ClassRef annotation : annotations) {
                    try {
                        if (this.isValid(annotation.versionPolicy())) {
                            String[] classNames = annotation.value();
                            for (int i = 0; i < classNames.length; i++) {
                                classNames[i] = classNames[i].replace("{nms}", Version.SERVER_VERSION.getNmsPackage())
                                        .replace("{obc}", Version.SERVER_VERSION.getObcPackage()).trim();
                            }
                            if (ClassWrapper.class.isAssignableFrom(field.getType())) {
                                field.set(object, this.classResolver.resolveWrapper(classNames));
                            } else if (Class.class.isAssignableFrom(field.getType())) {
                                field.set(object, this.classResolver.resolve(classNames));
                            } else {
                                throw new UnsupportedOperationException("Class reference can only be used on wrapper or class type");
                            }
                            continue fields;
                        }
                    } catch (ReflectiveOperationException e) {
                        if (!annotation.ignoreExceptions()) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } else if (declaredField.isAnnotationPresent(ConstructorRef.class)) {
                ConstructorRef[] annotations = declaredField.getAnnotationsByType(ConstructorRef.class);

                for (ConstructorRef annotation : annotations) {
                    try {
                        if (this.isValid(annotation.versionPolicy())) {
                            ConstructorResolver resolver;
                            if (annotation.className().isEmpty()) {
                                resolver = new ConstructorResolver(annotation.clazz());
                            } else {
                                resolver = new ConstructorResolver(this.parseClass(annotation.className(), clazz, object));
                            }
                            if (ConstructorWrapper.class.isAssignableFrom(field.getType())) {
                                field.set(object, resolver.resolveWrapper(annotation.parameters()));
                            } else if (Constructor.class.isAssignableFrom(field.getType())) {
                                field.set(object, resolver.resolve(annotation.parameters()));
                            } else {
                                throw new UnsupportedOperationException("Constructor reference can only be used on wrapper or class type");
                            }
                            continue fields;
                        }
                    } catch (ReflectiveOperationException e) {
                        if (!annotation.ignoreExceptions()) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } else if (declaredField.isAnnotationPresent(MethodRef.class)) {
                MethodRef[] annotations = declaredField.getAnnotationsByType(MethodRef.class);

                for (MethodRef annotation : annotations) {
                    try {
                        if (this.isValid(annotation.versionPolicy())) {
                            MethodResolver resolver;
                            if (annotation.className().isEmpty()) {
                                resolver = new MethodResolver(annotation.clazz());
                            } else {
                                resolver = new MethodResolver(this.parseClass(annotation.className(), clazz, object));
                            }
                            ResolverQuery.Builder query = ResolverQuery.builder();
                            for (String name : annotation.value()) {
                                Matcher matcher = PARAMETER_PATTERN.matcher(name);
                                String parameters = matcher.matches() ? matcher.group(2) : "";
                                String s = matcher.matches() ? matcher.group(1) : name;
                                Matcher commasMatcher = COMMAS_PATTERN.matcher(parameters);
                                List<Class<?>> parameterList = new ArrayList<>(commasMatcher.groupCount());
                                while (commasMatcher.find()) {
                                    String p = commasMatcher.group().trim();
                                    parameterList.add(this.parseClass(p, clazz, object));
                                }
                                query.with(s, parameterList.toArray(new java.lang.Class<?>[0]));
                            }
                            if (MethodWrapper.class.isAssignableFrom(field.getType())) {
                                field.set(object, resolver.resolveWrapper(query.build()));
                            } else if (Method.class.isAssignableFrom(field.getType())) {
                                field.set(object, resolver.resolve(query.build()));
                            } else {
                                throw new UnsupportedOperationException("Method reference can only be used on wrapper or class type");
                            }
                            continue fields;
                        }
                    } catch (ReflectiveOperationException e) {
                        if (!annotation.ignoreExceptions()) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } else if (declaredField.isAnnotationPresent(FieldRef.class)) {
                FieldRef[] annotations = declaredField.getAnnotationsByType(FieldRef.class);

                for (FieldRef annotation : annotations) {
                    try {
                        if (this.isValid(annotation.versionPolicy())) {
                            FieldResolver resolver;
                            if (annotation.className().isEmpty()) {
                                resolver = new FieldResolver(annotation.clazz());
                            } else {
                                resolver = new FieldResolver(this.parseClass(annotation.className(), clazz, object));
                            }
                            String[] values = annotation.value();
                            ResolverQuery.Builder query = ResolverQuery.builder();
                            for (String name : values) {
                                String[] modifier = name.split(" ");
                                if (modifier.length > 1) {
                                    query.with(modifier[0], this.parseClass(modifier[1], clazz, object));
                                } else {
                                    query.with(modifier[0]);
                                }
                            }
                            if (FieldWrapper.class.isAssignableFrom(field.getType())) {
                                field.set(object, resolver.resolveWrapper(query.build()));
                            } else if (Field.class.isAssignableFrom(field.getType())) {
                                field.set(object, resolver.resolve(query.build()));
                            } else {
                                throw new UnsupportedOperationException("Field reference can only be used on wrapper or class type");
                            }
                            continue fields;
                        }
                    } catch (ReflectiveOperationException e) {
                        if (!annotation.ignoreExceptions()) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    boolean isValid(VersionControl[] policies) {
        if (policies.length == 0) return true;
        for (VersionControl policy : policies) {
            if (policy.checkEqualRevision() && !Version.SERVER_VERSION.equalsRevision(policy.value())) continue;
            if (policy.checkEquals() && !Version.SERVER_VERSION.equalsVersion(policy.value())) continue;
            if (policy.checkNewer() && !Version.SERVER_VERSION.isNewer(policy.value())) continue;
            if (policy.checkNewerEqual() && !Version.SERVER_VERSION.isNewerEquals(policy.value())) continue;
            if (policy.checkOlder() && !Version.SERVER_VERSION.isOlder(policy.value())) continue;
            if (policy.checkOlderEqual() && !Version.SERVER_VERSION.isOlderEquals(policy.value())) continue;
            return true;
        }
        return false;
    }

    Class<?> parseClass(String className, java.lang.Class<?> objClazz, Object toLoad) throws ReflectiveOperationException {
        Matcher matcher = VARIABLE_REF_PATTERN.matcher(className);
        while (matcher.find()) {
            if (matcher.groupCount() != 1) continue;
            String fieldName = matcher.group(1); // It's a reference to a previously loaded class
            java.lang.reflect.Field field = objClazz.getField(fieldName);
            if (ClassWrapper.class.isAssignableFrom(field.getType())) {
                return ((ClassWrapper<?>) field.get(toLoad)).getClazz();
            } else if (java.lang.Class.class.isAssignableFrom(field.getType())) {
                return (java.lang.Class<?>) field.get(toLoad);
            }
        }

        if (className.equals("boolean"))
            return boolean.class;
        if (className.equals("byte"))
            return byte.class;
        if (className.equals("char"))
            return char.class;
        if (className.equals("double"))
            return double.class;
        if (className.equals("float"))
            return float.class;
        if (className.equals("int"))
            return int.class;
        if (className.equals("long"))
            return long.class;
        if (className.equals("short"))
            return short.class;
        if (className.equals("void"))
            return void.class;

        return this.classResolver.resolve(className.replace("{nms}", Version.SERVER_VERSION.getNmsPackage())
                .replace("{obc}", Version.SERVER_VERSION.getObcPackage()));
    }

}
