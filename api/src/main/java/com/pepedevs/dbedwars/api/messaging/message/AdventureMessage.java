package com.pepedevs.dbedwars.api.messaging.message;

import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class AdventureMessage extends Message{

    public static AdventureMessage from(String message, PlaceholderEntry... placeholders) {
        return new AdventureMessage(message, placeholders);
    }

    public static AdventureMessage from(String[] message, PlaceholderEntry... placeholders) {
        return new AdventureMessage(message, placeholders);
    }

    public static AdventureMessage from(Component component, PlaceholderEntry... placeholders) {
        return new AdventureMessage(Messaging.get().serializeMini(component), placeholders);
    }

    public static AdventureMessage from(Component[] component, PlaceholderEntry... placeholders) {
        String[] s = new String[component.length];
        for (int i = 0; i < component.length; i++) {
            s[i] = Messaging.get().serializeMini(component[i]);
        }
        return new AdventureMessage(s, placeholders);
    }

    protected AdventureMessage(String message, PlaceholderEntry... placeholders) {
        super(message, placeholders);
    }

    protected AdventureMessage(String[] message, PlaceholderEntry... placeholders) {
        super(message, placeholders);
    }

    @Override
    public Component[] asComponent() {
        PlaceholderEntry[] entries = this.placeholders.toArray(new PlaceholderEntry[0]);
        Component[] components = new Component[this.message.length];
        for (int i = 0; i < this.message.length; i++) {
            String replaced = Messaging.get().setPlaceholders(this.message[i], entries);
            components[i] = Messaging.get().parseMini(replaced);
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
            components[i] = Messaging.get().parseMini(replacedWithPAPI);
        }
        return components;
    }

}
