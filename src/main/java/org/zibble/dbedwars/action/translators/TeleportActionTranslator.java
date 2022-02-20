package org.zibble.dbedwars.action.translators;

import org.bukkit.entity.Entity;
import org.zibble.dbedwars.action.actions.TeleportAction;
import org.zibble.dbedwars.api.action.ActionPlaceholder;
import org.zibble.dbedwars.api.action.ActionTranslator;
import org.zibble.dbedwars.api.util.Key;

public class TeleportActionTranslator implements ActionTranslator<Entity, TeleportAction> {

    @Override
    public TeleportAction serialize(String untranslated, ActionPlaceholder<?, ?>... placeholders) {
        return null;
    }

    @Override
    public String deserialize(TeleportAction action) {
        return null;
    }

    @Override
    public Key<String> getKey() {
        return Key.of("TELEPORT");
    }

}
