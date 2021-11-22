package com.pepedevs.dbedwars.messaging;

import net.kyori.adventure.text.Component;

public class Message implements Cloneable {

    private Component component;

    protected Message(Component component) {
        this.component = component;
    }

    public static Message fromComponent(Component component) {
        return new Message(component);
    }

    public static Message fromText(String message) {
        return new Message(Component.text(message));
    }

    public static Message fromLegacyText(String message) {
        // TODO: Format legacy text
        return new Message(Component.text(message));
    }

    public Component asComponent() {
        return this.component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    @Override
    public Message clone() {
        return new Message(this.component);
    }
}
