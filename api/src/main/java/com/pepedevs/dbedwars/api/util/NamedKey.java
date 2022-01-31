package com.pepedevs.dbedwars.api.util;

public class NamedKey<T> extends Key<T> {

    protected final String identifier;

    protected NamedKey(String identifier, T key) {
        super(key);
        this.identifier = identifier;
    }

    public static <T> NamedKey<T> of(String identifier, T key) {
        return new NamedKey<>(identifier, key);
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NamedKey && ((NamedKey) obj).identifier.equals(this.identifier) && ((NamedKey) obj).key.equals(this.key);
    }

    @Override
    public NamedKey<T> clone() {
        return new NamedKey<>(identifier, key);
    }

    @Override
    public String toString() {
        return "NamedKey{" +
                "identifier='" + identifier + "'," +
                "key=" + key +
                '}';
    }

}
