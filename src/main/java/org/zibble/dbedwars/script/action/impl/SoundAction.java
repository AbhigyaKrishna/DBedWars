package org.zibble.dbedwars.script.action.impl;

import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.messaging.member.PlayerMember;

public class SoundAction implements ActionTranslator<SoundAction.Action> {

    @Override
    public Action serialize(String untranslated, ScriptVariable<?>... variables) {
        PlayerMember member = null;
        for (ScriptVariable<?> variable : variables) {
            if (variable.isAssignableFrom(PlayerMember.class)) {
                member = (PlayerMember) variable.value();
            } else if (variable.getKey().equals(Key.of("PLACEHOLDER"))) {
                untranslated = ((PlaceholderEntry) variable.value()).apply(untranslated);
            }
        }
        return new Action(SoundVP.valueOf(untranslated), member);
    }

    @Override
    public String deserialize(Action action) {
        return action.getSound().toString();
    }

    @Override
    public Key getKey() {
        return Key.of("SOUND");
    }

    public static class Action implements org.zibble.dbedwars.api.script.action.Action {

        private final PlayerMember member;
        private final SoundVP sound;

        public Action(SoundVP sound, PlayerMember member) {
            this.member = member;
            this.sound = sound;
        }

        @Override
        public void execute() {
            this.sound.play(this.getMember().getPlayer());
        }

        public PlayerMember getMember() {
            return this.member;
        }

        public SoundVP getSound() {
            return this.sound;
        }

    }

}
