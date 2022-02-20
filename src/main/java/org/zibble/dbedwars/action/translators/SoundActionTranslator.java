package org.zibble.dbedwars.action.translators;

import org.zibble.dbedwars.action.actions.SoundAction;
import org.zibble.dbedwars.api.action.ActionPlaceholder;
import org.zibble.dbedwars.api.action.ActionTranslator;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.SoundVP;
import org.zibble.dbedwars.messaging.member.PlayerMember;

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
