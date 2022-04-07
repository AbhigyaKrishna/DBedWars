package org.zibble.dbedwars.api.util.key;

public class NamedKey<T> extends Key {

    protected final T value;

    protected NamedKey(String key, T value) {
        super(key);
        this.value = value;
    }

    public static <T> NamedKey<T> of(String key, T value) {
        return new NamedKey<>(key, value);
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NamedKey && ((NamedKey) obj).value.equals(this.value) && ((NamedKey) obj).key.equals(this.key);
    }

    @Override
    public NamedKey<T> clone() {
        return new NamedKey<>(key, value);
    }

    @Override
    public String toString() {
        return "NamedKey{" +
                "key=" + key + "'," +
                "value='" + value +
                '}';
    }

    @Override
    public int hashCode() {
        return value.hashCode() + key.hashCode();
    }

}
