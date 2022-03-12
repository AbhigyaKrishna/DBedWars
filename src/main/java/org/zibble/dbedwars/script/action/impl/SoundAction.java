package org.zibble.dbedwars.script.action.impl;

import org.zibble.dbedwars.api.script.action.Action;
import org.zibble.dbedwars.api.objects.serializable.SoundVP;
import org.zibble.dbedwars.messaging.member.PlayerMember;

public class SoundAction implements Action<PlayerMember> {

    private final PlayerMember member;
    private final SoundVP sound;

    public SoundAction(SoundVP sound, PlayerMember member) {
        this.member = member;
        this.sound = sound;
    }

    @Override
    public void execute() {
        this.sound.play(this.getHandle().getPlayer());
    }

    @Override
    public PlayerMember getHandle() {
        return this.member;
    }

    public SoundVP getSound() {
        return this.sound;
    }

}
