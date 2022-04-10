package org.zibble.dbedwars.api.util.key;

public class LenientKey extends Key {

    protected LenientKey(String key) {
        super(key);
    }

    public static LenientKey of(String key) {
        return new LenientKey(key);
    }

    @Override
    public boolean equals(Object obj) {
        return this.key.equals(obj)
                || (obj instanceof Key && ((Key) obj).key.equals(this.key))
                || (obj instanceof String && this.lenientEqual((String) obj))
                || (obj instanceof LenientKey && this.lenientEqual(((LenientKey) obj).key));
    }

    private boolean lenientEqual(String s) {
        return s.equalsIgnoreCase(key) || s.replace("_", " ").replace("-", " ").equalsIgnoreCase(key);
    }

    @Override
    public LenientKey clone() {
        return new LenientKey(key);
    }

    @Override
    public String toString() {
        return "LenientStringKey{" +
                "key=" + key +
                '}';
    }

}
