package com.pepedevs.dbedwars.action.actions;

import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.util.SoundVP;
import com.pepedevs.dbedwars.messaging.member.PlayerMember;

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
