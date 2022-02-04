package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.SoundAction;
import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.action.ActionPlaceholder;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.api.util.SoundVP;
import com.pepedevs.dbedwars.messaging.member.PlayerMember;

public class SoundActionTranslator implements ActionTranslator<PlayerMember, SoundAction> {

    @Override
    public SoundAction serialize(String untranslated, ActionPlaceholder<?, ?>... placeholders) {
        SoundVP soundVP = SoundVP.valueOf(untranslated);
        if (soundVP == null) return null;
        return new SoundAction(soundVP);
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
