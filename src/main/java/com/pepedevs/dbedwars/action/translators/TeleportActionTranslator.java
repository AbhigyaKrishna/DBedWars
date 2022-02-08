package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.TeleportAction;
import com.pepedevs.dbedwars.api.action.ActionPlaceholder;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.util.Key;
import org.bukkit.entity.Entity;

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
