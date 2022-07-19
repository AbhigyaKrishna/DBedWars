package org.zibble.dbedwars.script.action.impl;

import org.bukkit.Location;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.objects.serializable.FireworkEffectC;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.key.Key;

public class FireworkAction implements ActionTranslator<FireworkAction.Action> {

    @Override
    public Action serialize(String untranslated, ScriptVariable<?>... variables) {
        Location location = null;
        for (ScriptVariable<?> variable : variables) {
            if (variable.isSubClassOf(Location.class)) {
                location = (Location) variable.value();
            } else if (variable.getKey().equals(Key.of("PLACEHOLDER"))) {
                untranslated = ((PlaceholderEntry) variable.value()).apply(untranslated);
            }
        }
        return new Action(FireworkEffectC.valueOf(untranslated), location);
    }

    @Override
    public String deserialize(Action action) {
        return action.getFireworkEffectAT().toString();
    }

    @Override
    public Key getKey() {
        return Key.of("FIREWORK");
    }

    public static class Action implements org.zibble.dbedwars.api.script.action.Action {

        private final Location location;
        private final FireworkEffectC fireworkEffectAT;

        public Action(FireworkEffectC fireworkEffectAT, Location location) {
            this.location = location;
            this.fireworkEffectAT = fireworkEffectAT;
        }

        @Override
        public void execute() {
            this.fireworkEffectAT.spawn(this.getLocation());
        }

        public Location getLocation() {
            return this.location;
        }

        public FireworkEffectC getFireworkEffectAT() {
            return this.fireworkEffectAT;
        }

    }

}
