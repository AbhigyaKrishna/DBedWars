package org.zibble.dbedwars.script.action.translators;

import org.bukkit.Location;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.objects.serializable.FireworkEffectC;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.script.action.impl.FireworkAction;

public class FireworkActionTranslator implements ActionTranslator<Location, FireworkAction> {

    @Override
    public FireworkAction serialize(String untranslated, ScriptVariable<?>... variables) {
        Location location = null;
        for (ScriptVariable<?> variable : variables) {
            if (variable.isAssignableFrom(Location.class)) {
                location = (Location) variable.value();
            } else if (variable.getKey().equals(Key.of("PLACEHOLDER"))) {
                untranslated = ((PlaceholderEntry) variable.value()).apply(untranslated);
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
