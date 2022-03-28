package org.zibble.dbedwars.api.objects.profile;

public class Skin extends Property {

    public static Skin empty() {
        return new Skin(null, null);
    }

    protected Skin(String value, String signature) {
        super("textures", value, signature);
    }

    public static Skin from(String value, String signature) {
        return new Skin(value, signature);
    }

    public static Skin from(String value) {
        return new Skin(value, null);
    }

}
