package com.pepedevs.dbedwars.api.messaging.message;

import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class LegacyMessage extends Message{

    public static LegacyMessage from(String message, PlaceholderEntry... placeholders) {
        return new LegacyMessage(message, placeholders);
    }

    protected LegacyMessage(String message, PlaceholderEntry... placeholders) {
        super(message, placeholders);
    }

    @Override
    public Component asComponent() {
        String replaced = Messaging.get().setPlaceholders(this.message, this.placeholders.toArray(new PlaceholderEntry[0]));
        return Messaging.get().translateAlternateColorCodes(replaced);
    }

    @Override
    public Component asComponentWithPAPI(Player player) {
        String replaced = Messaging.get().setPlaceholders(this.message, this.placeholders.toArray(new PlaceholderEntry[0]));
        String replacedWithPAPI = Messaging.get().setPlaceholders(replaced, player);
        return Messaging.get().translateAlternateColorCodes(replacedWithPAPI);
    }

}
