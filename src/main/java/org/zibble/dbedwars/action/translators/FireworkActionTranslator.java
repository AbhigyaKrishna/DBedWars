package org.zibble.dbedwars.action.translators;

import org.bukkit.Location;
import org.zibble.dbedwars.action.actions.FireworkAction;
import org.zibble.dbedwars.api.action.ActionPlaceholder;
import org.zibble.dbedwars.api.action.ActionTranslator;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.objects.serializable.FireworkEffectC;
import org.zibble.dbedwars.api.util.Key;

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
