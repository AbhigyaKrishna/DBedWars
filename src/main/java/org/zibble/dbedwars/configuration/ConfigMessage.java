package org.zibble.dbedwars.configuration;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.Placeholder;
import org.zibble.dbedwars.api.messaging.message.Message;

import java.util.ArrayList;
import java.util.Arrays;

public class ConfigMessage extends Message {

    public static ConfigMessage from(String message, Placeholder... placeholders) {
        return new ConfigMessage(message, placeholders);
    }

    public static ConfigMessage from(String[] message, Placeholder... placeholders) {
        return new ConfigMessage(message, placeholders);
    }

    protected ConfigMessage(String message, Placeholder... placeholders) {
        super(message, placeholders);
    }

    protected ConfigMessage(String[] message, Placeholder... placeholders) {
        super(new ArrayList<>(Arrays.asList(message)), placeholders);
    }

    @Override
    public Component[] asComponent() {
        Placeholder[] entries = this.placeholders.toArray(new Placeholder[0]);
        Component[] components = new Component[this.message.size()];
        for (int i = 0; i < this.message.size(); i++) {
            String replaced = Messaging.get().setPlaceholders(this.message.get(i), entries);
            components[i] = Lang.getTranslator().translate(replaced);
        }
        return components;
    }

    @Override
    public Component[] asComponentWithPAPI(Player player) {
        Placeholder[] entries = this.placeholders.toArray(new Placeholder[0]);
        Component[] components = new Component[this.message.size()];
        for (int i = 0; i < this.message.size(); i++) {
            String replaced = Messaging.get().setPlaceholders(this.message.get(i), player, entries);
            String replacedWithPAPI = Messaging.get().setPapiPlaceholders(replaced, player);
            components[i] = Lang.getTranslator().translate(replacedWithPAPI);
        }
        return components;
    }
}
