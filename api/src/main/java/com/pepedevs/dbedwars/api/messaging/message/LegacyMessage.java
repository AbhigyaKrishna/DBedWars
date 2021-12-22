package com.pepedevs.dbedwars.api.messaging.message;

import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import net.kyori.adventure.text.Component;

public class LegacyMessage extends Message{

    public static LegacyMessage from(String message, PlaceholderEntry... placeholders) {
        return new LegacyMessage(message, placeholders);
    }

    protected LegacyMessage(String message, PlaceholderEntry... placeholders) {
        super(message, placeholders);
    }

    @Override
    public Component asComponent() {
        return Component.text(this.message);
    }
}
