package com.pepedevs.dbedwars.api.messaging.message;

import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class LegacyMessage extends Message{

    public static LegacyMessage from(String message, PlaceholderEntry... placeholders) {
        return new LegacyMessage(message, placeholders);
    }

    public static LegacyMessage from(String[] message, PlaceholderEntry... placeholders) {
        return new LegacyMessage(message, placeholders);
    }

    protected LegacyMessage(String message, PlaceholderEntry... placeholders) {
        super(message, placeholders);
    }

    protected LegacyMessage(String[] message, PlaceholderEntry... placeholders) {
        super(message, placeholders);
    }

    @Override
    public Component[] asComponent() {
        PlaceholderEntry[] entries = this.placeholders.toArray(new PlaceholderEntry[0]);
        Component[] components = new Component[this.message.length];
        for (int i = 0; i < this.message.length; i++) {
            String replaced = Messaging.get().setPlaceholders(this.message[i], entries);
            components[i] = Messaging.get().translateAlternateColorCodes(replaced);
        }
        return components;
    }

    @Override
    public Component[] asComponentWithPAPI(Player player) {
        PlaceholderEntry[] entries = this.placeholders.toArray(new PlaceholderEntry[0]);
        Component[] components = new Component[this.message.length];
        for (int i = 0; i < this.message.length; i++) {
            String replaced = Messaging.get().setPlaceholders(this.message[i], entries);
            String replacedWithPAPI = Messaging.get().setPlaceholders(replaced, player);
            components[i] = Messaging.get().translateAlternateColorCodes(replacedWithPAPI);
        }
        return components;
    }

}
