package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.util.Key;
import org.bukkit.entity.Entity;

public class TeleportActionTranslator implements ActionTranslator<Entity> {

    @Override
    public Action<Entity> serialize(String untranslated) {
        return null;
    }

    @Override
    public String deserialize(Action<Entity> action) {
        return null;
    }

    @Override
    public Key<String> getKey() {
        return Key.of("TELEPORT");
    }

}
