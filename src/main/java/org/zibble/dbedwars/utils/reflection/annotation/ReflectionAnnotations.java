package org.zibble.dbedwars.utils.reflection.annotation;

import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.version.Version;
import org.zibble.dbedwars.utils.reflection.accessor.FieldAccessor;
import org.zibble.dbedwars.utils.reflection.resolver.ClassResolver;
import org.zibble.dbedwars.utils.reflection.resolver.FieldResolver;
import org.zibble.dbedwars.utils.reflection.resolver.MethodResolver;
import org.zibble.dbedwars.utils.reflection.resolver.ResolverQuery;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.ClassWrapper;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.FieldWrapper;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.MethodWrapper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReflectionAnnotations {

    public static final ReflectionAnnotations INSTANCE = new ReflectionAnnotations();

    static final Pattern CLASS_REF_PATTERN = Pattern.compile("@Class\\((.*)\\)");
    static final Pattern PARAMETER_PATTERN = Pattern.compile(".*\\((.*)\\)");
    static final Pattern COMMAS_PATTERN = Pattern.compile("([^,\\[]*\\[[^]]*])(?:,|$)|([^,\\[\\]]+(?:,|$))");

    ClassResolver classResolver = new ClassResolver();

    private ReflectionAnnotations() {}

    public void load(@NotNull java.lang.Class<?> clazz) {
        load(clazz, null);
    }

    public <T> void load(@NotNull java.lang.Class<T> clazz, T toLoad) {

        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            Class classAnnotation = field.getAnnotation(Class.class);
            Field fieldAnnotation = field.getAnnotation(Field.class);
            Method methodAnnotation = field.getAnnotation(Method.class);

            if (classAnnotation == null && fieldAnnotation == null && methodAnnotation == null) {
                continue;
            } else {
                field.setAccessible(true);
            }

            if (classAnnotation != null) {
                List<String> nameList = this.parseAnnotationVersions(Class.class, classAnnotation);
                try {
                    if (ClassWrapper.class.isAssignableFrom(field.getType())) {
                        field.set(toLoad, this.resolveClass(nameList, ClassWrapper.class));
                    } else if (java.lang.Class.class.isAssignableFrom(field.getType())) {
                        field.set(toLoad, this.resolveClass(nameList, java.lang.Class.class));
                    } else {
                        this.throwInvalidFieldType(field, clazz, "Class or ClassWrapper");
                        return;
                    }
                } catch (ReflectiveOperationException e) {
                    if (!classAnnotation.ignoreExceptions()) {
                        this.throwReflectionException("@Class", field, clazz, e);
                        return;
                    }
                }
            } else if (fieldAnnotation != null) {
                List<String> nameList = this.parseAnnotationVersions(Field.class, fieldAnnotation);
                if (nameList.isEmpty())
                    throw new IllegalArgumentException("@Field names cannot be empty");
                String[] names = nameList.toArray(new String[0]);
                try {
                    FieldResolver fieldResolver = new FieldResolver(this.parseClass(Field.class, fieldAnnotation, clazz, toLoad));
                    if (FieldAccessor.class.isAssignableFrom(field.getType())) {
                        field.set(toLoad, fieldResolver.resolveAccessor(names));
                    } else if (FieldWrapper.class.isAssignableFrom(field.getType())) {
                        field.set(toLoad, fieldResolver.resolveWrapper(names));
                    } else if (java.lang.reflect.Field.class.isAssignableFrom(field.getType())) {
                        field.set(toLoad, fieldResolver.resolve(names));
                    } else {
                        this.throwInvalidFieldType(field, clazz, "Field, FieldWrapper, or FieldAccessor");
                        return;
                    }
                } catch (ReflectiveOperationException e) {
                    if (!fieldAnnotation.ignoreExceptions()) {
                        this.throwReflectionException("@Field", field, clazz, e);
                        return;
                    }
                }
            } else if (methodAnnotation != null) {
                List<String> nameList = this.parseAnnotationVersions(Method.class, methodAnnotation);
                if (nameList.isEmpty())
                    throw new IllegalArgumentException("@Method names cannot be empty");
                String[] names = nameList.toArray(new String[0]);

                try {
                    MethodResolver methodResolver = new MethodResolver(this.parseClass(Method.class, methodAnnotation, clazz, toLoad));
                    ResolverQuery.Builder query = ResolverQuery.builder();
                    for (String name : names) {
                        Matcher matcher = PARAMETER_PATTERN.matcher(name);
                        String parameters = matcher.matches() ? matcher.group(1) : "";
                        Matcher commasMatcher = COMMAS_PATTERN.matcher(parameters);
                        List<java.lang.Class<?>> parameterList = new ArrayList<>(commasMatcher.groupCount());
                        while (commasMatcher.find()) {
                            String p = commasMatcher.group().trim();
                            parameterList.add(this.resolveClass(Collections.singletonList(this.parseClass(p, clazz, toLoad)), java.lang.Class.class));
                        }
                        query.with(name, parameterList.toArray(new java.lang.Class<?>[0]));
                    }
                    if (MethodWrapper.class.isAssignableFrom(field.getType())) {
                        field.set(toLoad, methodResolver.resolveWrapper(query.build()));
                    } else if (java.lang.reflect.Method.class.isAssignableFrom(field.getType())) {
                        field.set(toLoad, methodResolver.resolve(query.build()));
                    } else {
                        this.throwInvalidFieldType(field, clazz, "Method or MethodWrapper");
                        return;
                    }
                } catch (ReflectiveOperationException e) {
                    if (!methodAnnotation.ignoreExceptions()) {
                        this.throwReflectionException("@Method", field, clazz, e);
                        return;
                    }
                }
            }
        }
    }

    <T> T resolveClass(List<String> nameList, java.lang.Class<T> clazz) throws ClassNotFoundException {
        if (nameList.isEmpty())
            throw new IllegalArgumentException("@Class names cannot be empty");
        String[] names = nameList.toArray(new String[0]);
        for (int i = 0; i < names.length; i++) {
            names[i] = names[i].replace("{nms}", Version.SERVER_VERSION.getNmsPackage())
                    .replace("{obc}", Version.SERVER_VERSION.getObcPackage());
        }
        if (clazz.equals(java.lang.Class.class)) {
            return (T) this.classResolver.resolve(names);
        } else if (clazz.equals(ClassWrapper.class)) {
            return (T) this.classResolver.resolveWrapper(names);
        }
        return null;
    }

    <A extends Annotation> List<String> parseAnnotationVersions(java.lang.Class<A> clazz, A annotation) {
        List<String> list = new ArrayList<>();

        try {
            String[] names = (String[]) clazz.getMethod("value").invoke(annotation);
            Version[] versions = (Version[]) clazz.getMethod("versions").invoke(annotation);

            if (versions.length == 0) { // No versions specified -> directly use the names
                Collections.addAll(list, names);
            } else {
                if (versions.length > names.length) {
                    throw new RuntimeException(
                            "versions array cannot have more elements than the names ("
                                    + clazz
                                    + ")");
                }
                for (int i = 0; i < versions.length; i++) {
                    if (Version.SERVER_VERSION == versions[i]) { // Wohoo, perfect match!
                        list.add(names[i]);
                    } else {
                        if (names[i].startsWith(">") && Version.SERVER_VERSION.isNewer(versions[i])) { // Match if the current version is newer
                            list.add(names[i].substring(1));
                        } else if (names[i].startsWith("<") && Version.SERVER_VERSION.isOlder(versions[i])) { // Match if the current version is older
                            list.add(names[i].substring(1));
                        }
                    }
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    <A extends Annotation, T> String parseClass(java.lang.Class<A> clazz, A annotation, java.lang.Class<T> objClazz, T toLoad) {
        try {
            String className;
            if (annotation instanceof Method) {
                className = ((Method) annotation).className();
            } else if (annotation instanceof Field) {
                className = ((Field) annotation).className();
            } else {
                className = (String) clazz.getMethod("className").invoke(annotation);
            }
            return parseClass(className, objClazz, toLoad);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    String parseClass(String className, java.lang.Class<?> objClazz, Object toLoad) {
        try {
            Matcher matcher = CLASS_REF_PATTERN.matcher(className);
            while (matcher.find()) {
                if (matcher.groupCount() != 1) continue;
                String fieldName = matcher.group(1); // It's a reference to a previously loaded class
                java.lang.reflect.Field field = objClazz.getField(fieldName);
                if (ClassWrapper.class.isAssignableFrom(field.getType())) {
                    return ((ClassWrapper<?>) field.get(toLoad)).getName();
                } else if (java.lang.Class.class.isAssignableFrom(field.getType())) {
                    return ((java.lang.Class<?>) field.get(toLoad)).getName();
                }
            }
            return className;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    void throwInvalidFieldType(java.lang.reflect.Field field, java.lang.Class<?> clazz, String expected) {
        throw new IllegalArgumentException(
                "Field "
                        + field.getName()
                        + " in "
                        + clazz
                        + " is not of type "
                        + expected
                        + ", it's "
                        + field.getType());
    }

    void throwReflectionException(String annotation, java.lang.reflect.Field field, java.lang.Class<?> clazz, ReflectiveOperationException exception) {
        throw new RuntimeException(
                "Failed to set "
                        + annotation
                        + " field "
                        + field.getName()
                        + " in "
                        + clazz,
                exception);
    }
}
