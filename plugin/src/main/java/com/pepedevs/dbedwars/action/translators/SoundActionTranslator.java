package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.SoundAction;
import com.pepedevs.dbedwars.api.action.ActionPlaceholder;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.api.util.SoundVP;
import com.pepedevs.dbedwars.messaging.member.PlayerMember;

public class SoundActionTranslator implements ActionTranslator<PlayerMember, SoundAction> {

    @Override
    public SoundAction serialize(String untranslated, ActionPlaceholder<?, ?>... placeholders) {
        PlayerMember member = null;
        for (ActionPlaceholder<?, ?> placeholder : placeholders) {
            if (placeholder.getValue() instanceof PlayerMember) {
                member = (PlayerMember) placeholder.getValue();
            }
            if (placeholder.getKey().equals(Key.of("PLACEHOLDER"))) {
                untranslated = ((PlaceholderEntry) placeholder.getValue()).apply(untranslated);
            }
        }
        return new SoundAction(SoundVP.valueOf(untranslated), member);
    }

    @Override
    public String deserialize(SoundAction action) {
        return action.getSound().toString();
    }

    @Override
    public Key<String> getKey() {
        return Key.of("SOUND");
    }
}
