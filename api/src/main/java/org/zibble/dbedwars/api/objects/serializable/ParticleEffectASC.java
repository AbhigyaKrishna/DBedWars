package org.zibble.dbedwars.api.objects.serializable;

import org.apache.commons.lang.math.NumberUtils;
import org.zibble.dbedwars.api.util.EnumUtil;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParticleEffectASC implements Cloneable {

    static final Pattern PATTERN = Pattern.compile("^(?<name>[a-zA-Z0-9_\\-]+?)(?:::[+-]?(?<amount>\\d*\\.?\\d*)(?:::[+-]?(?<speed>\\d*\\.?\\d*))?)?(?:::[Rr](?<R>\\d+)[Gg](?<G>\\d+)[Bb](?<B>\\d+))?$", Pattern.CASE_INSENSITIVE);

    private ParticleEffect effect;
    private int amount;
    private float speed;
    private Color color;

    public static ParticleEffectASC of(ParticleEffect effect) {
        return new ParticleEffectASC(effect, 0, 1);
    }

    public static ParticleEffectASC of(ParticleEffect effect, int amount) {
        return new ParticleEffectASC(effect, amount, 1);
    }

    public static ParticleEffectASC of(ParticleEffect effect, int amount, float speed) {
        return new ParticleEffectASC(effect, amount, speed);
    }

    public static ParticleEffectASC of(ParticleEffect effect, int amount, float speed, Color color) {
        return new ParticleEffectASC(effect, amount, speed, color);
    }

    public static ParticleEffectASC valueOf(String str) {
        Matcher matcher = PATTERN.matcher(str);
        if (matcher.matches()) {
            String name = matcher.group("name");
            ParticleEffect effect = EnumUtil.matchEnum(name, ParticleEffect.values());
            if (effect == null) return null;
            int amount = NumberUtils.toInt(matcher.group("amount"), 0);
            float speed = NumberUtils.toFloat(matcher.group("speed"), 1);

            ParticleEffectASC e = new ParticleEffectASC(effect, amount, speed);

            int r = NumberUtils.toInt(matcher.group("R"), -1);
            int g = NumberUtils.toInt(matcher.group("G"), -1);
            int b = NumberUtils.toInt(matcher.group("B"), -1);

            if (r != -1 && g != -1 && b != -1) e.setColor(new Color(r, g, b));

            return e;
        }
        return null;
    }

    private ParticleEffectASC(ParticleEffect particle, int amount, float speed) {
        this.effect = particle;
        this.amount = amount;
        this.speed = speed;
    }

    private ParticleEffectASC(ParticleEffect particle, int amount, float speed, Color color) {
        this.effect = particle;
        setSpeed(speed);
        setAmount(amount);
        setColor(color);
    }

    public ParticleEffect getEffect() {
        return effect;
    }

    public void setEffect(ParticleEffect effect) {
        this.effect = effect;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ParticleBuilder build() {
        ParticleBuilder builder = new ParticleBuilder(effect);
        builder.setAmount(amount);
        builder.setSpeed(speed);
        if (color != null) builder.setColor(color);
        return builder;
    }

    @Override
    public String toString() {
        return this.effect.toString() + "::" + this.amount + "::" + this.speed + "::R" + this.color.getRed() + "G" + this.color.getGreen() + "B" + this.color.getBlue();
    }

    @Override
    public ParticleEffectASC clone() {
        return new ParticleEffectASC(this.effect, this.amount, this.speed, this.color);
    }

}
