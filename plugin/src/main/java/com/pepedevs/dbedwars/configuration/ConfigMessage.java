package com.pepedevs.dbedwars.configuration;

import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ConfigMessage extends Message {

    private static final Set<PlaceholderEntry> defaultSet;

    static {
        defaultSet = new HashSet<>();
    }

    public static ConfigMessage from(String message, boolean defaultPlaceholders, PlaceholderEntry... placeholders) {
        PlaceholderEntry[] array = new PlaceholderEntry[placeholders.length + defaultSet.size()];
        System.arraycopy(placeholders, 0, array, 0, placeholders.length);
        return new ConfigMessage(message, placeholders);
    }

    public static ConfigMessage from(String message, PlaceholderEntry... placeholders) {
        return from(message, true, placeholders);
    }

    protected ConfigMessage(String message, PlaceholderEntry... placeholders) {
        super(message, placeholders);
    }

    @Override
    public Component asComponent() {
        String replaced = Messaging.get().setPlaceholders(this.message, this.placeholders.toArray(new PlaceholderEntry[0]));
        return Lang.getTranslator().translate(replaced);
    }

    @Override
    public Component asComponentWithPAPI(Player player) {
        String replaced = Messaging.get().setPlaceholders(this.message, this.placeholders.toArray(new PlaceholderEntry[0]));
        String replacedWithPAPI = Messaging.get().setPlaceholders(replaced, player);
        return Lang.getTranslator().translate(replacedWithPAPI);
    }
}
