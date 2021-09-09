package me.abhigya.dbedwars.api.util;

import me.Abhigya.core.util.xseries.XPotion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectAT {

    public static final int MAX_DURATION = Integer.MAX_VALUE;

    private XPotion potion;
    private int amplifier;
    private int duration;
    private boolean particleOff;

    public PotionEffectAT(PotionEffectType effect) {
        this(XPotion.matchXPotion(effect));
    }

    public PotionEffectAT(PotionEffectType effect, int amplifier, int duration) {
        this(XPotion.matchXPotion(effect), amplifier, duration);
    }

    public PotionEffectAT(XPotion potion) {
        this(potion, 1, MAX_DURATION);
    }

    public PotionEffectAT(XPotion potion, int amplifier, int duration) {
        this.potion = potion;
        this.amplifier = amplifier;
        this.duration = duration;
    }

    public static PotionEffectAT valueOf(PotionEffect potion) {
        return new PotionEffectAT(potion.getType(), potion.getAmplifier(), potion.getDuration());
    }

    public static PotionEffectAT valueOf(String str) {
        String[] s = str.split(":");
        XPotion potion = XPotion.matchXPotion(s[0]).orElse(null);
        if (potion == null)
            return null;

        int amplifier = 1;
        if (s.length > 1) {
            try {
                amplifier = Integer.parseInt(s[1]);
            } catch (NumberFormatException ignored) {}
        }

        int duration = MAX_DURATION;
        if (s.length > 2) {
            if (s[2].equalsIgnoreCase("max"))
                duration = MAX_DURATION;
            else
                try {
                    duration = Integer.parseInt(s[2]);
                } catch (NumberFormatException ignored) {}
        }

        boolean particle = false;
        if (s.length > 3) {
            particle = Boolean.parseBoolean(s[3]);
        }

        PotionEffectAT potionEffect = new PotionEffectAT(potion, amplifier, duration);
        potionEffect.setParticle(particle);
        return potionEffect;
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

    @Override
    public String toString() {
        return this.potion.name() + ":" + this.amplifier + ":" + (this.duration == MAX_DURATION ? "max" : this.duration)
                + ":" + (this.particleOff ? "true" : "");
    }
}
