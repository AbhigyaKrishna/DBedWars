package org.zibble.dbedwars.script.action.translators;

import org.bukkit.entity.Entity;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.script.action.impl.TeleportAction;

public class TeleportActionTranslator implements ActionTranslator<Entity, TeleportAction> {

    @Override
    public TeleportAction serialize(String untranslated, ScriptVariable<?>... variables) {
        return null;
    }

    @Override
    public String deserialize(TeleportAction action) {
        return null;
    }

    @Override
    public Key getKey() {
        return Key.of("TELEPORT");
    }

}
