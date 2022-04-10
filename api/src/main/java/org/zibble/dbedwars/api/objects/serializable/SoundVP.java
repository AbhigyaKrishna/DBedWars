package org.zibble.dbedwars.api.objects.serializable;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SoundVP implements Cloneable {

    static final Pattern PATTERN = Pattern.compile("^(?<name>[a-zA-Z0-9_\\-]+?)(?:::[+-]?(?<volume>\\d*\\.?\\d*)(?:::[+-]?(?<pitch>\\d*\\.?\\d*))?)?$", Pattern.CASE_INSENSITIVE);

    private final XSound sound;
    private float volume;
    private float pitch;

    private SoundVP(XSound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public static SoundVP of(Sound sound) {
        return of(XSound.matchXSound(sound));
    }

    public static SoundVP of(XSound sound) {
        return of(sound, 1.0F, 1.0F);
    }

    public static SoundVP of(Sound sound, float volume, float pitch) {
        return of(XSound.matchXSound(sound), volume, pitch);
    }

    public static SoundVP of(XSound sound, float volume, float pitch) {
        return new SoundVP(sound, volume, pitch);
    }

    public static SoundVP valueOf(String str) {
        Matcher matcher = PATTERN.matcher(str);
        if (matcher.matches()) {
            XSound sound = XSound.matchXSound(matcher.group("name")).orElse(null);
            if (sound == null) return null;

            float volume = matcher.groupCount() > 1 ? Float.parseFloat(matcher.group("volume")) : 1.0F;
            float pitch = matcher.groupCount() > 2 ? Float.parseFloat(matcher.group("pitch")) : 1.0F;

            return new SoundVP(sound, volume, pitch);
        }

        return null;
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
        return this.sound.name() + "::" + this.volume + "::" + this.pitch;
    }

    @Override
    protected SoundVP clone() {
        return new SoundVP(this.sound, this.volume, this.pitch);
    }

}
