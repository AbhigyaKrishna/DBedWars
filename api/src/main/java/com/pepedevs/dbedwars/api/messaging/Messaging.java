package com.pepedevs.dbedwars.api.messaging;

import net.kyori.adventure.text.Component;

public abstract class Messaging {

    private static Messaging instance;

    public Messaging() {
        instance = this;
    }

    public static Messaging get() {
        return instance;
    }

    public abstract String serialize(Component component);

    public abstract Component deserialize(String message);
}
