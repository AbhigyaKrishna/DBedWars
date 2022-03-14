package org.zibble.dbedwars.api.objects.serializable;

import com.cryptomorin.xseries.XPotion;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.zibble.dbedwars.api.version.Version;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PotionEffectAT {

    static final Pattern PATTERN = Pattern.compile("^(?<name>[a-zA-Z0-9_\\-]+?)(?:::(?<amplifier>[+-]?\\d*\\.?\\d*)(?:::[+-]?(?<duration>max|\\d*\\.?\\d*)(?:::(?<particle>.*))?)?)?$", Pattern.CASE_INSENSITIVE);

    private XPotion potion;
    private int amplifier;
    private int duration;
    private boolean particleOff;

    public static PotionEffectAT of(PotionEffectType potion) {
        return of(XPotion.matchXPotion(potion));
    }

    public static PotionEffectAT of(XPotion potion) {
        return of(potion, 0, Integer.MAX_VALUE);
    }

    public static PotionEffectAT of(PotionEffectType potion, int amplifier, int duration) {
        return of(XPotion.matchXPotion(potion), amplifier, duration);
    }

    public static PotionEffectAT of(XPotion potion, int amplifier, int duration) {
        return new PotionEffectAT(potion, amplifier, duration);
    }

    private PotionEffectAT(XPotion potion, int amplifier, int duration) {
        this.potion = potion;
        this.amplifier = amplifier;
        this.duration = duration;
    }

    public static PotionEffectAT valueOf(PotionEffect potion) {
        return PotionEffectAT.of(potion.getType(), potion.getAmplifier(), potion.getDuration());
    }

    public static PotionEffectAT valueOf(String str) {
        Matcher matcher = PATTERN.matcher(str);
        if (matcher.matches()) {
            XPotion potion = XPotion.matchXPotion(matcher.group("name")).orElse(null);
            if (potion == null) return null;

            int amplifier = matcher.groupCount() > 1 ? (int) Double.parseDouble(matcher.group("amplifier")) : 1;

            String dur = matcher.groupCount() > 2 ? matcher.group("duration") : "max";
            int duration = dur.equalsIgnoreCase("max") ? Integer.MAX_VALUE : (int) Double.parseDouble(dur);

            boolean particle = matcher.groupCount() == 4 && Boolean.parseBoolean(matcher.group("particle"));

            PotionEffectAT potionEffect = new PotionEffectAT(potion, amplifier - 1, duration);
            potionEffect.setParticle(particle);
            return potionEffect;
        }
        return null;
    }

    public XPotion getPotion() {
        return this.potion;
    }

    public void setPotion(XPotion potion) {
        this.potion = potion;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public void setAmplifier(int amplifier) {
        this.amplifier = amplifier;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isParticleOff() {
        return this.particleOff;
    }

    public void setParticle(boolean particle) {
        this.particleOff = particle;
    }

    public void applyTo(LivingEntity entity) {
        if (Version.SERVER_VERSION.isOlderEquals(Version.v1_8_R3)) {
            PotionEffect effect =
                    new PotionEffect(
                            this.potion.getPotionEffectType(), this.duration, this.amplifier - 1);
            effect.apply(entity);
        } else {
            PotionEffect effect =
                    new PotionEffect(
                            this.potion.getPotionEffectType(),
                            this.duration,
                            this.amplifier - 1,
                            true,
                            this.particleOff);
            effect.apply(entity);
        }
    }

    @Override
    public String toString() {
        return this.potion.name() + "::" + (this.amplifier - 1) + "::" + (this.duration == Integer.MAX_VALUE ? "max" : this.duration) + (this.particleOff ? "::true" : "");
    }
}
