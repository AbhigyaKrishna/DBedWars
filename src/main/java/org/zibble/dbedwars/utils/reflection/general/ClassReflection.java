package org.zibble.dbedwars.utils.reflection.general;

import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.zibble.dbedwars.api.version.Version;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClassReflection {

    private static final Map<String, Class<?>> CACHED_CLASSES = new ConcurrentHashMap<>();

    public static Class<?> getSubClass(Class<?> root, String name, boolean declared)
            throws ClassNotFoundException {
        for (Class<?> clazz : declared ? root.getDeclaredClasses() : root.getClasses()) {
            if (clazz.getSimpleName().equals(name)) {
                return clazz;
            }
        }
        throw new ClassNotFoundException("The sub class " + name + " doesn't exist!");
    }

    public static Class<?> getSubClass(Class<?> root, String name) throws ClassNotFoundException {
        try {
            return getSubClass(root, name, true);
        } catch (ClassNotFoundException ex) {
            try {
                return getSubClass(root, name, false);
            } catch (ClassNotFoundException ignored) {
            }
        }
        throw new ClassNotFoundException("The sub class " + name + " doesn't exist!");
    }

    public static Class<?> getCraftClass(String name, @NotNull String package_name) {
        try {
            String s = package_name.isEmpty() ? "" : package_name.toLowerCase() + ".";
            String id = "craft-" + s + name;
            if (CACHED_CLASSES.containsKey(id)) return CACHED_CLASSES.get(id);

            Class<?> clazz = Class.forName(Version.SERVER_VERSION.getObcPackage() + "." + s + name);
            CACHED_CLASSES.put(id, clazz);
            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getNmsClass(String name, @NotNull String v17Package) {
        try {
            if (CACHED_CLASSES.containsKey(name)) return CACHED_CLASSES.get(name);

            Class<?> clazz;
            if (Version.SERVER_VERSION.isNewerEquals(Version.v1_17_R1))
                clazz = Class.forName("net.minecraft." + (v17Package.isEmpty() ? "" : v17Package.toLowerCase() + ".") + name);
            else clazz = Class.forName(Version.SERVER_VERSION.getNmsPackage() + "." + name);

            CACHED_CLASSES.put(name, clazz);
            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean nmsClassExist(String name, String v17Package) {
        return getNmsClass(name, v17Package) != null;
    }

    public static Set<Class<?>> getClasses(String packageName) {
        return getClasses(packageName, Object.class);
    }

    public static <T> Set<Class<? extends T>> getClasses(String packageName, Class<T> inherit) {
        Reflections ref = new Reflections(packageName);
        return ref.getSubTypesOf(inherit);
    }

}
