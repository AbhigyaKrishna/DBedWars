package org.zibble.dbedwars.utils.reflection.annotation;

import com.pepedevs.radium.utils.version.Version;
import org.zibble.dbedwars.utils.reflection.accessor.FieldAccessor;
import org.zibble.dbedwars.utils.reflection.resolver.ClassResolver;
import org.zibble.dbedwars.utils.reflection.resolver.FieldResolver;
import org.zibble.dbedwars.utils.reflection.resolver.MethodResolver;
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

    static final Pattern classRefPattern = Pattern.compile("@Class\\((.*)\\)");

    private ReflectionAnnotations() {}

    public void load(Object toLoad) {
        if (toLoad == null) throw new IllegalArgumentException("toLoad cannot be null");

        ClassResolver classResolver = new ClassResolver();

        for (java.lang.reflect.Field field : toLoad.getClass().getDeclaredFields()) {
            Class classAnnotation = field.getAnnotation(Class.class);
            org.zibble.dbedwars.utils.reflection.annotation.Field fieldAnnotation = field.getAnnotation(org.zibble.dbedwars.utils.reflection.annotation.Field.class);
            org.zibble.dbedwars.utils.reflection.annotation.Method methodAnnotation = field.getAnnotation(org.zibble.dbedwars.utils.reflection.annotation.Method.class);

            if (classAnnotation == null && fieldAnnotation == null && methodAnnotation == null) {
                continue;
            } else {
                field.setAccessible(true);
            }

            if (classAnnotation != null) {
                List<String> nameList = this.parseAnnotationVersions(Class.class, classAnnotation);
                if (nameList.isEmpty())
                    throw new IllegalArgumentException("@Class names cannot be empty");
                String[] names = nameList.toArray(new String[0]);
                for (int i = 0; i < names.length; i++) {
                    names[i] =
                            names[i].replace("{nms}", Version.SERVER_VERSION.getNmsPackage())
                                    .replace("{obc}", Version.SERVER_VERSION.getObcPackage());
                }
                try {
                    if (ClassWrapper.class.isAssignableFrom(field.getType())) {
                        field.set(toLoad, classResolver.resolveWrapper(names));
                    } else if (java.lang.Class.class.isAssignableFrom(field.getType())) {
                        field.set(toLoad, classResolver.resolve(names));
                    } else {
                        this.throwInvalidFieldType(field, toLoad, "Class or ClassWrapper");
                        return;
                    }
                } catch (ReflectiveOperationException e) {
                    if (!classAnnotation.ignoreExceptions()) {
                        this.throwReflectionException("@Class", field, toLoad, e);
                        return;
                    }
                }
            } else if (fieldAnnotation != null) {
                List<String> nameList = this.parseAnnotationVersions(org.zibble.dbedwars.utils.reflection.annotation.Field.class, fieldAnnotation);
                if (nameList.isEmpty())
                    throw new IllegalArgumentException("@Field names cannot be empty");
                String[] names = nameList.toArray(new String[0]);
                try {
                    FieldResolver fieldResolver =
                            new FieldResolver(
                                    this.parseClass(org.zibble.dbedwars.utils.reflection.annotation.Field.class, fieldAnnotation, toLoad));
                    if (FieldAccessor.class.isAssignableFrom(field.getType())) {
                        field.set(toLoad, fieldResolver.resolveAccessor(names));
                    } else if (FieldWrapper.class.isAssignableFrom(field.getType())) {
                        field.set(toLoad, fieldResolver.resolveWrapper(names));
                    } else if (java.lang.reflect.Field.class.isAssignableFrom(field.getType())) {
                        field.set(toLoad, fieldResolver.resolve(names));
                    } else {
                        this.throwInvalidFieldType(
                                field, toLoad, "Field, FieldWrapper, or FieldAccessor");
                        return;
                    }
                } catch (ReflectiveOperationException e) {
                    if (!fieldAnnotation.ignoreExceptions()) {
                        this.throwReflectionException("@Field", field, toLoad, e);
                        return;
                    }
                }
            } else if (methodAnnotation != null) {
                List<String> nameList =
                        this.parseAnnotationVersions(org.zibble.dbedwars.utils.reflection.annotation.Method.class, methodAnnotation);
                if (nameList.isEmpty())
                    throw new IllegalArgumentException("@Method names cannot be empty");
                String[] names = nameList.toArray(new String[0]);

                boolean isSignature =
                        names[0].contains(
                                " "); // Only signatures can contain spaces (e.g. "void aMethod()")
                for (String s : names) {
                    if (s.contains(" ") != isSignature) {
                        throw new IllegalArgumentException(
                                "Inconsistent method names: Cannot have mixed signatures/names");
                    }
                }

                try {
                    MethodResolver methodResolver =
                            new MethodResolver(
                                    this.parseClass(org.zibble.dbedwars.utils.reflection.annotation.Method.class, methodAnnotation, toLoad));
                    if (MethodWrapper.class.isAssignableFrom(field.getType())) {
                        if (isSignature) {
                            field.set(toLoad, methodResolver.resolveSignatureWrapper(names));
                        } else {
                            field.set(toLoad, methodResolver.resolveWrapper(names));
                        }
                    } else if (java.lang.reflect.Method.class.isAssignableFrom(field.getType())) {
                        if (isSignature) {
                            field.set(toLoad, methodResolver.resolveSignature(names));
                        } else {
                            field.set(toLoad, methodResolver.resolve(names));
                        }
                    } else {
                        this.throwInvalidFieldType(field, toLoad, "Method or MethodWrapper");
                        return;
                    }
                } catch (ReflectiveOperationException e) {
                    if (!methodAnnotation.ignoreExceptions()) {
                        this.throwReflectionException("@Method", field, toLoad, e);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Parses an annotation to the current server version. Removes all entries that don't match the
     * version, but keeps the original order for matching names.
     *
     * @param clazz Class of the annotation
     * @param annotation annotation
     * @param <A> annotation type
     * @return a list of matching names
     */
    <A extends Annotation> List<String> parseAnnotationVersions(
            java.lang.Class<A> clazz, A annotation) {
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
                        if (names[i].startsWith(">")
                                && Version.SERVER_VERSION.isNewer(
                                        versions[i])) { // Match if the current version is newer
                            list.add(names[i].substring(1));
                        } else if (names[i].startsWith("<")
                                && Version.SERVER_VERSION.isOlder(
                                        versions[i])) { // Match if the current version is older
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

    <A extends Annotation> String parseClass(
            java.lang.Class<A> clazz, A annotation, Object toLoad) {
        try {
            String className = (String) clazz.getMethod("className").invoke(annotation);
            Matcher matcher = classRefPattern.matcher(className);
            while (matcher.find()) {
                if (matcher.groupCount() != 1) continue;
                String fieldName =
                        matcher.group(1); // It's a reference to a previously loaded class
                java.lang.reflect.Field field = toLoad.getClass().getField(fieldName);
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

    void throwInvalidFieldType(java.lang.reflect.Field field, Object toLoad, String expected) {
        throw new IllegalArgumentException(
                "Field "
                        + field.getName()
                        + " in "
                        + toLoad.getClass()
                        + " is not of type "
                        + expected
                        + ", it's "
                        + field.getType());
    }

    void throwReflectionException(
            String annotation,
            java.lang.reflect.Field field,
            Object toLoad,
            ReflectiveOperationException exception) {
        throw new RuntimeException(
                "Failed to set "
                        + annotation
                        + " field "
                        + field.getName()
                        + " in "
                        + toLoad.getClass(),
                exception);
    }
}
