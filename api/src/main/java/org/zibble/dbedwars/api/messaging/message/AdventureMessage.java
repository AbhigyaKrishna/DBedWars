package org.zibble.dbedwars.api.messaging.message;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.Placeholder;

import java.util.ArrayList;
import java.util.Arrays;

public class AdventureMessage extends Message {

    public static AdventureMessage empty() {
        return new AdventureMessage("");
    }

    public static AdventureMessage from(String message, Placeholder... placeholders) {
        return new AdventureMessage(message, placeholders);
    }

    public static AdventureMessage from(String[] message, Placeholder... placeholders) {
        return new AdventureMessage(message, placeholders);
    }

    public static AdventureMessage from(Component component, Placeholder... placeholders) {
        return new AdventureMessage(Messaging.get().serializeMini(component), placeholders);
    }

    public static AdventureMessage from(Component[] component, Placeholder... placeholders) {
        String[] s = new String[component.length];
        for (int i = 0; i < component.length; i++) {
            s[i] = Messaging.get().serializeMini(component[i]);
        }
        return new AdventureMessage(s, placeholders);
    }

    protected AdventureMessage(String message, Placeholder... placeholders) {
        super(message, placeholders);
    }

    protected AdventureMessage(String[] message, Placeholder... placeholders) {
        super(new ArrayList<>(Arrays.asList(message)), placeholders);
    }

    @Override
    public Component[] asComponent() {
        Placeholder[] entries = this.placeholders.toArray(new Placeholder[0]);
        Component[] components = new Component[this.message.size()];
        for (int i = 0; i < this.message.size(); i++) {
            String replaced = Messaging.get().setPlaceholders(this.message.get(i), entries);
            components[i] = Messaging.get().parseMini(replaced);
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
            components[i] = Messaging.get().parseMini(replacedWithPAPI);
        }
        return components;
    }

}