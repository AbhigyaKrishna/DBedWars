package org.zibble.dbedwars.api.util.properies;

import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.key.NamedKey;

import java.util.*;
import java.util.function.Predicate;

public class NamedProperties implements Cloneable, Iterable<NamedKey<?>> {

    private final Set<NamedKey<?>> properties = Collections.synchronizedSet(new TreeSet<>(Comparator.comparing(Key::get)));

    public NamedProperties() {
    }

    public NamedProperties(NamedKey<?>... keys) {
        this(Arrays.asList(keys));
    }

    public NamedProperties(Collection<NamedKey<?>> keys) {
        this.properties.addAll(keys);
    }

    public static Builder builder() {
        return new Builder();
    }

    public <T> void add(String name, T value) {
        this.properties.add(NamedKey.of(name, value));
    }

    public <T> void add(Key name, T value) {
        this.properties.add(NamedKey.of(name.get(), value));
    }

    public <T> void add(NamedKey<T> key) {
        this.properties.add(key);
    }

    public void add(NamedKey<?>... keys) {
        this.properties.addAll(Arrays.asList(keys));
    }

    public void add(Collection<NamedKey<?>> keys) {
        this.properties.addAll(keys);
    }

    public <T> NamedKey<T> getProperty(String name) {
        for (NamedKey<?> property : this.properties) {
            if (property.get().equals(name)) {
                return (NamedKey<T>) property;
            }
        }
        return null;
    }

    public <T> NamedKey<T> getProperty(String name, T defaultValue) {
        for (NamedKey<?> property : this.properties) {
            if (property.get().equals(name)) {
                return (NamedKey<T>) property;
            }
        }
        return NamedKey.of(name, defaultValue);
    }

    public <T> T getValue(String name) {
        for (NamedKey<?> property : this.properties) {
            if (property.get().equals(name)) {
                return (T) property.get();
            }
        }
        return null;
    }

    public <T> T getValue(String name, T defaultValue) {
        for (NamedKey<?> property : this.properties) {
            if (property.get().equals(name)) {
                return (T) property.get();
            }
        }
        return defaultValue;
    }

    public <T> List<T> getAll(Class<T> clazz) {
        ArrayList<T> list = new ArrayList<>();
        for (NamedKey<?> property : this.properties) {
            if (property.get().getClass().equals(clazz)) {
                list.add((T) property.get());
            }
        }
        return list;
    }

    public List<NamedKey<?>> getAll() {
        return new ArrayList<>(this.properties);
    }

    public void removeIf(Predicate<NamedKey<?>> predicate) {
        this.properties.removeIf(predicate);
    }

    public <T> void remove(NamedKey<T> key) {
        this.properties.remove(key);
    }

    public void removeAll(NamedKey<?>... keys) {
        this.removeAll(Arrays.asList(keys));
    }

    public void removeAll(Collection<NamedKey<?>> keys) {
        this.properties.removeAll(keys);
    }

    public boolean contains(String key) {
        for (NamedKey<?> property : this.properties) {
            if (property.get().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return this.properties.isEmpty();
    }

    @Override
    public NamedProperties clone() {
        return new NamedProperties(properties);
    }

    @NotNull
    @Override
    public Iterator<NamedKey<?>> iterator() {
        return this.properties.iterator();
    }

    @Override
    public String toString() {
        return "NamedProperties{" +
                "properties=" + properties +
                '}';
    }

    public static class Builder implements org.zibble.dbedwars.api.util.mixin.Builder<NamedProperties> {

        private final ArrayList<NamedKey<?>> keys = new ArrayList<>();

        protected Builder() {
        }

        public <T> Builder add(String name, T value) {
            this.keys.add(NamedKey.of(name, value));
            return this;
        }

        public <T> Builder add(Key name, T value) {
            this.keys.add(NamedKey.of(name.get(), value));
            return this;
        }

        public <T> Builder add(NamedKey<T> key) {
            this.keys.add(key);
            return this;
        }

        public Builder add(NamedKey<?>... keys) {
            this.keys.addAll(Arrays.asList(keys));
            return this;
        }

        public Builder add(Collection<NamedKey<?>> keys) {
            this.keys.addAll(keys);
            return this;
        }

        @Override
        public NamedProperties build() {
            return new NamedProperties(keys);
        }

    }

}
