package com.pepedevs.dbedwars.configuration;

import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class ConfigMessage extends Message {

    public static ConfigMessage from(String message, PlaceholderEntry... placeholders) {
        return new ConfigMessage(message, placeholders);
    }

    public static ConfigMessage from(String[] message, PlaceholderEntry... placeholders) {
        return new ConfigMessage(message, placeholders);
    }

    protected ConfigMessage(String message, PlaceholderEntry... placeholders) {
        super(message, placeholders);
    }

    protected ConfigMessage(String[] message, PlaceholderEntry... placeholders) {
        super(message, placeholders);
    }

    @Override
    public Component[] asComponent() {
        PlaceholderEntry[] entries = this.placeholders.toArray(new PlaceholderEntry[0]);
        Component[] components = new Component[this.message.length];
        for (int i = 0; i < this.message.length; i++) {
            String replaced = Messaging.get().setPlaceholders(this.message[i], entries);
            components[i] = Lang.getTranslator().translate(replaced);
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
            components[i] = Lang.getTranslator().translate(replacedWithPAPI);
        }
        return components;
    }
}
