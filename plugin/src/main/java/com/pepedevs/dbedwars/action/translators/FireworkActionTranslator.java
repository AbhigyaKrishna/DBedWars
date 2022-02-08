package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.FireworkAction;
import com.pepedevs.dbedwars.api.action.ActionPlaceholder;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.api.util.FireworkEffectC;
import com.pepedevs.dbedwars.api.util.Key;
import org.bukkit.Location;

public class FireworkActionTranslator implements ActionTranslator<Location, FireworkAction> {

    @Override
    public FireworkAction serialize(String untranslated, ActionPlaceholder<?, ?>... placeholders) {
        Location location = null;
        for (ActionPlaceholder<?, ?> placeholder : placeholders) {
            if (placeholder.getValue() instanceof Location) {
                location = (Location) placeholder.getValue();
            }
            if (placeholder.getKey().equals(Key.of("PLACEHOLDER"))) {
                untranslated = ((PlaceholderEntry) placeholder.getValue()).apply(untranslated);
            }
        }
        return new FireworkAction(FireworkEffectC.valueOf(untranslated), location);
    }

    @Override
    public String deserialize(FireworkAction action) {
        return action.getFireworkEffectAT().toString();
    }

    @Override
    public Key<String> getKey() {
        return Key.of("FIREWORK");
    }

}
