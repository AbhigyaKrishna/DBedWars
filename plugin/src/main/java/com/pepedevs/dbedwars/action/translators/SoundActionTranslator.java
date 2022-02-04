package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.SoundAction;
import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.api.util.SoundVP;
import com.pepedevs.dbedwars.messaging.member.PlayerMember;

public class SoundActionTranslator implements ActionTranslator<PlayerMember> {

    @Override
    public Action<PlayerMember> serialize(String untranslated) {
        SoundVP soundVP = SoundVP.valueOf(untranslated);
        if (soundVP == null) return null;
        return new SoundAction(soundVP.getSound(), soundVP.getVolume(), soundVP.getPitch());
    }

    @Override
    public String deserialize(Action<PlayerMember> action) {
        SoundAction soundAction = (SoundAction) action;
        return new SoundVP(soundAction.getSound(), soundAction.getVolume(), soundAction.getPitch()).toString();
    }

    @Override
    public Key<String> getKey() {
        return Key.of("SOUND");
    }
}
