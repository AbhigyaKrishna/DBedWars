package me.abhigya.dbedwars.api.util;

import me.Abhigya.core.util.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;

public class SoundVP implements Cloneable {

    private final XSound sound;
    private float volume;
    private float pitch;

    public SoundVP(XSound sound) {
        this(sound, 1.0F, 1.0F);
    }

    public SoundVP(XSound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public static SoundVP valueOf(String str) {
        String[] s = str.split(":");
        XSound sound = XSound.matchXSound(s[0]).orElse(null);
        if (sound == null)
            return null;

        float volume = 1.0F;
        float pitch = 1.0F;

        if (s.length > 1) {
            try {
                volume = Float.parseFloat(s[1]);
            } catch (NumberFormatException ignored) {}
        }

        if (s.length > 2) {
            try {
                pitch = Float.parseFloat(s[2]);
            } catch (NumberFormatException ignored) {}
        }

        return new SoundVP(sound, volume, pitch);
    }

    public static SoundVP valueOf(Sound sound) {
        return new SoundVP(XSound.matchXSound(sound));
    }

    public XSound getSound() {
        return this.sound;
    }

    public float getVolume() {
        return this.volume;
    }

    public SoundVP setVolume(float volume) {
        this.volume = volume;
        return this;
    }

    public float getPitch() {
        return this.pitch;
    }

    public SoundVP setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public void play(Location location) {
        this.sound.play(location, this.volume, this.pitch);
    }

    public void play(Entity entity) {
        this.sound.play(entity, this.volume, this.pitch);
    }

    @Override
    public String toString() {
        return this.sound.name() + ":" + this.volume + ":" + this.pitch;
    }

    @Override
    protected SoundVP clone() {
        try {
            return (SoundVP) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
