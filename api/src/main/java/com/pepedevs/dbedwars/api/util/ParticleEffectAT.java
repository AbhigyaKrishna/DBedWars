package com.pepedevs.dbedwars.api.util;

import com.google.common.util.concurrent.AtomicDouble;
import me.Abhigya.core.particle.particlelib.ParticleBuilder;
import me.Abhigya.core.particle.particlelib.ParticleEffect;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings(value = "unused")
public class ParticleEffectAT {

    private ParticleEffect effect;
    private AtomicInteger amount;
    private AtomicDouble speed;
    private Color color;

    public ParticleEffectAT(ParticleEffect effect) {
        this.effect = effect;
    }

    public ParticleEffectAT(ParticleEffect particle, int amount, float speed) {
        this.effect = particle;
        setSpeed(speed);
        setAmount(amount);
    }

    public ParticleEffectAT(ParticleEffect particle, int amount) {
        this.effect = particle;
        setAmount(amount);
    }

    public ParticleEffectAT(ParticleEffect particle, float speed) {
        this.effect = particle;
        setSpeed(speed);
    }

    public ParticleEffectAT(ParticleEffect effect, Color color) {
        this.effect = effect;
        setColor(color);
    }

    public ParticleEffectAT(ParticleEffect particle, int amount, float speed, Color color) {
        this.effect = particle;
        setSpeed(speed);
        setAmount(amount);
        setColor(color);
    }

    public ParticleEffectAT(ParticleEffect particle, int amount, Color color) {
        this.effect = particle;
        setAmount(amount);
        setColor(color);
    }

    public ParticleEffectAT(ParticleEffect particle, float speed, Color color) {
        this.effect = particle;
        setSpeed(speed);
        setColor(color);
    }

    public ParticleEffectAT(String particle) {
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
        //        ColorUtils.format();
        setColor(Color.getColor(split[3]));
    }

    public ParticleEffect getEffect() {
        return effect;
    }

    public float getSpeed() {
        return speed.floatValue();
    }

    public int getAmount() {
        return amount.get();
    }

    public Color getColor() {
        return color;
    }

    public void setEffect(ParticleEffect effect) {
        this.effect = effect;
    }

    public void setSpeed(float speed) {
        if (this.speed == null) this.speed = new AtomicDouble();
        this.speed.set(speed);
    }

    public void setAmount(int amount) {
        if (this.amount == null) this.amount = new AtomicInteger();
        this.amount.set(amount);
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
