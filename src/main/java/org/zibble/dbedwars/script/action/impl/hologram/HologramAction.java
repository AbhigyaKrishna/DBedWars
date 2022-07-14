package org.zibble.dbedwars.script.action.impl.hologram;

import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.key.Key;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HologramAction implements ActionTranslator<HologramAction.Action> {

    private static final Pattern TELEPORT_ACTION = Pattern.compile("teleport\\{(?<loc>.+)}");

    @Override
    public Action serialize(String condition, ScriptVariable<?>... variables) {
        Hologram hologram = null;
        for (ScriptVariable<?> variable : variables) {
            if (variable.isAssignableFrom(Hologram.class)) {
                hologram = (Hologram) variable.value();
            }
        }

        if (hologram == null) return null;

        Matcher matcher = TELEPORT_ACTION.matcher(condition);
        if (matcher.matches()) {
            LocationXYZYP loc = LocationXYZYP.valueOf(matcher.group("loc"));
            if (loc != null) {
                return new Action(hologram, holo -> holo.teleport(loc.toBukkit(holo.getLocation().getWorld())));
            }
        }

        return null;
    }

    @Override
    public String deserialize(Action condition) {
        return null;
    }

    @Override
    public Key getKey() {
        return Key.of("HOLOGRAM");
    }

    public static class Action implements org.zibble.dbedwars.api.script.action.Action {

        private final Hologram hologram;
        private final Consumer<Hologram> consumer;

        public Action(Hologram hologram, Consumer<Hologram> consumer) {
            this.hologram = hologram;
            this.consumer = consumer;
        }

        @Override
        public void execute() {
            this.consumer.accept(this.hologram);
        }

        public Hologram getHologram() {
            return this.hologram;
        }

    }

}
