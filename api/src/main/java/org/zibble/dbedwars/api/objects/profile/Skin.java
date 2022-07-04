package org.zibble.dbedwars.api.objects.profile;

public class Skin extends Property {

    protected Skin(String value, String signature) {
        super("textures", value, signature);
    }

    public static Skin empty() {
        return new Skin(null, null);
    }

    public static Skin from(String value, String signature) {
        return new Skin(value, signature);
    }

    public static Skin from(String value) {
        return new Skin(value, null);
    }

    public static Skin from(Property property) {
        return new Skin(property.getValue(), property.getSignature());
    }

}
