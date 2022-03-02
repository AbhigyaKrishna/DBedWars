package org.zibble.dbedwars.api.messaging.message;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.Placeholder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class LegacyMessage extends Message {

    public static LegacyMessage empty() {
        return new LegacyMessage("");
    }

    public static LegacyMessage from(String message, Placeholder... placeholders) {
        return new LegacyMessage(message, placeholders);
    }

    public static LegacyMessage from(String[] message, Placeholder... placeholders) {
        return new LegacyMessage(message, placeholders);
    }

    protected LegacyMessage(String message, Placeholder... placeholders) {
        super(message, placeholders);
    }

    protected LegacyMessage(Collection<String> message, Placeholder... placeholders) {
        super(message, placeholders);
    }

    protected LegacyMessage(String[] message, Placeholder... placeholders) {
        super(new ArrayList<>(Arrays.asList(message)), placeholders);
    }

    @Override
    public Component[] asComponent() {
        Placeholder[] entries = this.placeholders.toArray(new Placeholder[0]);
        Component[] components = new Component[this.message.size()];
        for (int i = 0; i < this.message.size(); i++) {
            String replaced = Messaging.get().setPlaceholders(this.message.get(i), entries);
            components[i] = Messaging.get().translateAlternateColorCodes(replaced);
        }
        return components;
    }

    @Override
    public Component[] asComponentWithPAPI(Player player) {
        Placeholder[] entries = this.placeholders.toArray(new Placeholder[0]);
        Component[] components = new Component[this.message.size()];
        for (int i = 0; i < this.message.size(); i++) {
            String replaced = Messaging.get().setPlaceholders(this.message.get(i), player, entries);
            String replacedWithPAPI = Messaging.get().setRegisteredPlaceholders(replaced, player);
            components[i] = Messaging.get().translateAlternateColorCodes(replacedWithPAPI);
        }
        return components;
    }

    @Override
    public LegacyMessage clone() {
        return new LegacyMessage(this.message, this.placeholders.toArray(new Placeholder[0]));
    }

}
