package com.pepedevs.dbedwars.messaging;

import net.kyori.adventure.text.Component;

public class Message implements Cloneable {

    private Component component;

    protected Message() {

    }

    protected Message(Component component) {

    }

    public static Message fromText(String message) {
        return new Message(Component.text(message));
    }

    public static Message fromComponent(Component component) {
        return new Message(component);
    }

    public static Message fromString() {
        return new Message();
    }

    public static Message fromLegacyText(String message) {
        // TODO: Format legacy text
        return new Message(Component.text(message));
    }

    public SentMessage send(MessagingMember member, MessagingChannel channel){
        return MessagingServer.connect().sendMessage(this, member, channel);
    }

    public Component asComponent() {
        return this.component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    @Override
    public Message clone() throws CloneNotSupportedException {
        return (Message) super.clone();
    }
}
