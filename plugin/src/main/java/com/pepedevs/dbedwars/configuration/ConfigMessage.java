package com.pepedevs.dbedwars.configuration;

import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

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
        super(new ArrayList<>(Arrays.asList(message)), placeholders);
    }

    @Override
    public Component[] asComponent() {
        PlaceholderEntry[] entries = this.placeholders.toArray(new PlaceholderEntry[0]);
        Component[] components = new Component[this.message.size()];
        for (int i = 0; i < this.message.size(); i++) {
            String replaced = Messaging.get().setPlaceholders(this.message.get(i), entries);
            components[i] = Lang.getTranslator().translate(replaced);
        }
        return components;
    }

    @Override
    public Component[] asComponentWithPAPI(Player player) {
        PlaceholderEntry[] entries = this.placeholders.toArray(new PlaceholderEntry[0]);
        Component[] components = new Component[this.message.size()];
        for (int i = 0; i < this.message.size(); i++) {
            String replaced = Messaging.get().setPlaceholders(this.message.get(i), entries);
            String replacedWithPAPI = Messaging.get().setPlaceholders(replaced, player);
            components[i] = Lang.getTranslator().translate(replacedWithPAPI);
        }
        return components;
    }
}
