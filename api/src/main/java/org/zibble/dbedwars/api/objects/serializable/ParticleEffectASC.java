package org.zibble.dbedwars.api.objects.serializable;

import com.google.common.util.concurrent.AtomicDouble;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class ParticleEffectASC {

    static final Pattern PATTERN = Pattern.compile("^(?<name>[a-zA-Z0-9_\\-]+?)(?:::[+-]?(?<amount>\\d*\\.?\\d*)(?:::[+-]?(?<speed>\\d*\\.?\\d*))?)?$");

    private ParticleEffect effect;
    private AtomicInteger amount;
    private AtomicDouble speed;
    private Color color;

    public ParticleEffectASC(ParticleEffect effect) {
        this.effect = effect;
    }

    public ParticleEffectASC(ParticleEffect particle, int amount, float speed) {
        this.effect = particle;
        setSpeed(speed);
        setAmount(amount);
    }

    public ParticleEffectASC(ParticleEffect particle, int amount) {
        this.effect = particle;
        setAmount(amount);
    }

    public ParticleEffectASC(ParticleEffect particle, float speed) {
        this.effect = particle;
        setSpeed(speed);
    }

    public ParticleEffectASC(ParticleEffect effect, Color color) {
        this.effect = effect;
        setColor(color);
    }

    public ParticleEffectASC(ParticleEffect particle, int amount, float speed, Color color) {
        this.effect = particle;
        setSpeed(speed);
        setAmount(amount);
        setColor(color);
    }

    public ParticleEffectASC(ParticleEffect particle, int amount, Color color) {
        this.effect = particle;
        setAmount(amount);
        setColor(color);
    }

    public ParticleEffectASC(ParticleEffect particle, float speed, Color color) {
        this.effect = particle;
        setSpeed(speed);
        setColor(color);
    }

    public ParticleEffectASC(String particle) {
        String[] split = particle.split(":");

        if (split.length == 0)
            throw new IllegalArgumentException("You cant use <" + particle + "> input");
        if (split.length == 1) this.effect = ParticleEffect.valueOf(split[0]);
        if (split.length == 2) {
            this.effect = ParticleEffect.valueOf(split[0]);
            setAmount(Integer.parseInt(split[1]));
        }
        if (split.length == 3) {
            this.effect = ParticleEffect.valueOf(split[0]);
            setAmount(Integer.parseInt(split[1]));
            setSpeed((float) Double.parseDouble(split[2]));
        }

        this.effect = ParticleEffect.valueOf(split[0]);
        setAmount(Integer.parseInt(split[1]));
        setSpeed((float) Double.parseDouble(split[2]));
        setColor(Color.getColor(split[3]));
    }

    public static ParticleEffectASC valueOf(String str) {

    }

    public ParticleEffect getEffect() {
        return effect;
    }

    public void setEffect(ParticleEffect effect) {
        this.effect = effect;
    }

    public float getSpeed() {
        return speed.floatValue();
    }

    public void setSpeed(float speed) {
        if (this.speed == null) this.speed = new AtomicDouble();
        this.speed.set(speed);
    }

    public int getAmount() {
        return amount.get();
    }

    public void setAmount(int amount) {
        if (this.amount == null) this.amount = new AtomicInteger();
        this.amount.set(amount);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ParticleBuilder build() {
        ParticleBuilder builder = new ParticleBuilder(effect);
        if (this.amount != null) builder.setAmount(amount.get());
        if (this.speed != null) builder.setSpeed(speed.floatValue());
        return builder;
    }
}
