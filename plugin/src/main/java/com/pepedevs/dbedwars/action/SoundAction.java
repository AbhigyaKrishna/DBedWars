package com.pepedevs.dbedwars.action;

import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.messaging.member.PlayerMember;
import com.pepedevs.radium.utils.xseries.XSound;

public class SoundAction implements Action<PlayerMember> {

    private final XSound sound;
    private final float volume;
    private final float pitch;

    public SoundAction(XSound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void execute(PlayerMember player) {
        player.getPlayer().playSound(player.getPlayer().getLocation(), sound.parseSound(), volume, pitch);
    }

    public XSound getSound() {
        return sound;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}
