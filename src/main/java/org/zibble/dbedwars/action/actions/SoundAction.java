package org.zibble.dbedwars.action.actions;

import org.zibble.dbedwars.api.action.Action;
import org.zibble.dbedwars.api.util.SoundVP;
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
