package org.zibble.dbedwars.api.util.key;

public class Key implements Cloneable {

    protected final String key;

    protected Key(String key) {
        this.key = key;
    }

    public static Key of(String key) {
        return new Key(key);
    }

    public String get() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        return this.key.equals(obj) || (obj instanceof Key && ((Key) obj).key.equals(this.key));
    }

    @Override
    public Key clone() {
        return new Key(this.key);
    }

    @Override
    public String toString() {
        return "Key{" +
                "key=" + key +
                '}';
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

}
