package com.pepedevs.dbedwars.action.actions;

import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.util.SoundVP;
import com.pepedevs.dbedwars.messaging.member.PlayerMember;
import com.pepedevs.radium.utils.xseries.XSound;

public class SoundAction implements Action<PlayerMember> {

    private final SoundVP sound;

    public SoundAction(SoundVP sound) {
        this.sound = sound;
    }

    @Override
    public void execute(PlayerMember player) {
        sound.play(player.getPlayer());
    }

    public SoundVP getSound() {
        return sound;
    }

}
