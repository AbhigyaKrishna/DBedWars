package org.zibble.dbedwars.script.action.translators;

import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.script.action.impl.hologram.HologramAction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HologramActionTranslator implements ActionTranslator<Hologram, HologramAction> {

    private static final Pattern TELEPORT_ACTION = Pattern.compile("teleport\\{(?<loc>.+)}");

    @Override
    public HologramAction serialize(String condition, ScriptVariable<?>... variables) {
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
                return new HologramAction(hologram, holo -> holo.teleport(loc.toBukkit(holo.getLocation().getWorld())));
            }
        }

        return null;
    }

    @Override
    public String deserialize(HologramAction condition) {
        return null;
    }

    @Override
    public Key getKey() {
        return Key.of("HOLOGRAM");
    }

}
