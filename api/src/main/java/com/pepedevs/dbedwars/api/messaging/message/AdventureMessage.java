package com.pepedevs.dbedwars.api.messaging.message;

import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class AdventureMessage extends Message{

    public static AdventureMessage from(String message, PlaceholderEntry... placeholders) {
        return new AdventureMessage(message, placeholders);
    }

    public static AdventureMessage from(Component component, PlaceholderEntry... placeholders) {
        return new AdventureMessage(Messaging.get().serializeMini(component), placeholders);
    }

    protected AdventureMessage(String message, PlaceholderEntry... placeholders) {
        super(message, placeholders);
    }

    @Override
    public Component asComponent() {
        String replaced = Messaging.get().setPlaceholders(this.message, this.placeholders.toArray(new PlaceholderEntry[0]));
        return Messaging.get().parseMini(replaced);
    }

    @Override
    public Component asComponentWithPAPI(Player player) {
        String replaced = Messaging.get().setPlaceholders(this.message, this.placeholders.toArray(new PlaceholderEntry[0]));
        String replacedWithPAPI = Messaging.get().setPlaceholders(replaced, player);
        return Messaging.get().parseMini(replacedWithPAPI);
    }

}
