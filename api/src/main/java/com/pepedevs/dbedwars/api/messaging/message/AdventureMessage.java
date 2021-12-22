package com.pepedevs.dbedwars.api.messaging.message;

import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import net.kyori.adventure.text.Component;

public class AdventureMessage extends Message{

    public static AdventureMessage from(String message, PlaceholderEntry... placeholders) {
        return new AdventureMessage(message, placeholders);
    }

    public static AdventureMessage from(Component component, PlaceholderEntry... placeholders) {
        return new AdventureMessage(Messaging.get().serialize(component), placeholders);
    }

    protected AdventureMessage(String message, PlaceholderEntry... placeholders) {
        super(message, placeholders);
    }

    @Override
    public Component asComponent() {
        return Messaging.get().deserialize(this.message);
    }

}
