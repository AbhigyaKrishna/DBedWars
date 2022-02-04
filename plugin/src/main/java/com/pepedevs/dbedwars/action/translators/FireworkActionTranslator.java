package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.FireworkAction;
import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.util.FireworkEffectAT;
import com.pepedevs.dbedwars.api.util.Key;
import org.bukkit.Location;

public class FireworkActionTranslator implements ActionTranslator<Location> {

    @Override
    public Action<Location> serialize(String untranslated) {
        return new FireworkAction(FireworkEffectAT.valueOf(untranslated));
    }

    @Override
    public String deserialize(Action<Location> action) {
        return ((FireworkAction) action).getFireworkEffectAT().toString();
    }

    @Override
    public Key<String> getKey() {
        return Key.of("FIREWORK");
    }

}
