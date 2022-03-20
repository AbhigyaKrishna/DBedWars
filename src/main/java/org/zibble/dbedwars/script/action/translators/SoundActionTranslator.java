package org.zibble.dbedwars.script.action.translators;

import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.messaging.member.PlayerMember;
import org.zibble.dbedwars.script.action.impl.SoundAction;

public class SoundActionTranslator implements ActionTranslator<PlayerMember, SoundAction> {

    @Override
    public SoundAction serialize(String untranslated, ScriptVariable<?>... variables) {
        PlayerMember member = null;
        for (ScriptVariable<?> variable : variables) {
            if (variable.isAssignableFrom(PlayerMember.class)) {
                member = (PlayerMember) variable.value();
            } else if (variable.getKey().equals(Key.of("PLACEHOLDER"))) {
                untranslated = ((PlaceholderEntry) variable.value()).apply(untranslated);
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
