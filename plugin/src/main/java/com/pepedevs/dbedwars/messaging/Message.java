package com.pepedevs.dbedwars.messaging;

import net.kyori.adventure.text.Component;

public class Message implements Cloneable {

    private Component component;

    protected Message(Component component) {
        this.component = component;
    }

    protected Message() {}

    public static Message fromText(String message) {
        return new Message(Component.text(message));
    }

    public static Message fromComponent(Component component) {
        return new Message(component);
    }

    public static Message fromString() {
        // TODO ADD FROM STRING
        return new Message();
    }

    public static Message fromLegacyText(String message) {
        // TODO: Format legacy text
        return new Message(Component.text(message));
    }

    public SentMessage send(MessagingMember sender, MessagingChannel channel) {
        return MessagingServer.connect().sendMessage(this, sender, channel);
    }

    public SentMessage sendToExcept(
            MessagingMember sender, MessagingChannel channel, MessagingMember... hiddenUsers) {
        return MessagingServer.connect().sendToExcept(this, sender, channel, hiddenUsers);
    }

    public SentMessage sendAsServer(MessagingChannel channel) {
        return MessagingServer.connect().sendMessage(this, MessagingMember.ofConsole(), channel);
    }

    public SentMessage logToConsole() {
        return MessagingServer.connect().sendToConsole(this);
    }

    public Component asComponent() {
        return this.component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    @Override
    public Message clone() {
        return new Message(asComponent());
    }
}
