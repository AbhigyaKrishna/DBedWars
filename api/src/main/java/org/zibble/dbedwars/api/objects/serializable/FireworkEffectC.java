package org.zibble.dbedwars.api.objects.serializable;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.Contract;
import org.zibble.dbedwars.api.util.EnumUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FireworkEffectC implements Cloneable {

    private static final String SEPERATOR = "::";
    private static final String SEPERATOR_2 = ":";

    private final FireworkEffect.Builder builder;

    public static FireworkEffectC of(FireworkEffect.Builder builder) {
        return new FireworkEffectC(builder);
    }

    public static FireworkEffectC of(FireworkEffect.Type type, Collection<Color> baseColors) {
        return new FireworkEffectC(type, baseColors);
    }

    public static FireworkEffectC of(FireworkEffect.Type type, boolean twinkle, boolean trail, Collection<Color> baseColors, Collection<Color> fadeColors) {
        return new FireworkEffectC(type, twinkle, trail, baseColors, fadeColors);
    }

    private FireworkEffectC(FireworkEffect.Builder builder) {
        this.builder = builder;
    }

    private FireworkEffectC(FireworkEffect.Type type, Collection<Color> baseColors) {
        this.builder = FireworkEffect.builder().with(type);
        for (Color color : baseColors) {
            builder.withColor(color);
        }
    }

    private FireworkEffectC(FireworkEffect.Type type, boolean twinkle, boolean trail, Collection<Color> baseColors, Collection<Color> fadeColors) {
        this.builder = FireworkEffect.builder().with(type).flicker(twinkle).trail(trail);
        for (Color color : baseColors) {
            builder.withColor(color);
        }
        for (Color fadeColor : fadeColors) {
            builder.withFade(fadeColor);
        }
    }

    public static FireworkEffectC valueOf(String firework) {
        String[] params = firework.split(SEPERATOR);
        if (params.length < 1) return null;
        FireworkEffect.Type type = EnumUtil.matchEnum(params[0], FireworkEffect.Type.values());
        FireworkEffectC returnVal = new FireworkEffectC((type == null ? FireworkEffect.Type.BALL : type), Collections.emptyList());
        if (params.length < 2) return returnVal;
        String[] p = params[1].split(SEPERATOR_2);
        for (String s : p) {
            if (s.equalsIgnoreCase("TWINKLE")) returnVal.builder.withFlicker();
            if (s.equalsIgnoreCase("TRAIL")) returnVal.builder.withTrail();
        }
        if (params.length < 3) return returnVal;
        String[] p2 = params[2].split(SEPERATOR_2);
        for (String s : p2) {
            returnVal.builder.withColor(getColor(s));
        }
        if (params.length < 4) return returnVal;
        String[] p3 = params[3].split(SEPERATOR_2);
        for (String s : p3) {
            returnVal.builder.withFade(getColor(s));
        }
        return returnVal;
    }

    private static Color getColor(String color) {
        org.zibble.dbedwars.api.util.Color color1 = EnumUtil.matchEnum(color, org.zibble.dbedwars.api.util.Color.VALUES);
        if (color1 != null) return color1.getColor();
        return Color.fromRGB(java.awt.Color.decode(color).getRGB());
    }

    private static String getString(Color color) {
        for (org.zibble.dbedwars.api.util.Color value : org.zibble.dbedwars.api.util.Color.VALUES) {
            if (value.getColor().equals(color)) return value.name();
        }
        return "#" + Integer.toHexString(color.getRed()) + Integer.toHexString(color.getGreen()) + Integer.toHexString(color.getBlue());
    }

    public FireworkEffect getEffect() {
        return builder.build();
    }

    public void addBaseColors(Color... colors) {
        this.builder.withColor(colors);
    }

    public List<Color> getBaseColors() {
        return builder.build().getColors();
    }

    public void addFadeColors(Color... colors) {
        this.builder.withFade(colors);
    }

    public List<Color> getFadeColors() {
        return builder.build().getFadeColors();
    }

    public FireworkEffect.Type getType() {
        return builder.build().getType();
    }

    @Contract
    public void setType(FireworkEffect.Type type) {
        builder.with(type);
    }

    public boolean isTwinkling() {
        return builder.build().hasFlicker();
    }

    public void setTwinkling(boolean twinkle) {
        builder.flicker(twinkle);
    }

    public boolean isTrailing() {
        return builder.build().hasTrail();
    }

    public void setTrailing(boolean trail) {
        builder.trail(trail);
    }

    public void spawn(Location location) {
        Firework firework = location.getWorld().spawn(location, Firework.class);
        this.applyEffects(firework);
        firework.detonate();
    }

    public void applyEffects(Firework firework) {
        FireworkMeta meta = firework.getFireworkMeta();
        meta.addEffect(this.getEffect());
        firework.setFireworkMeta(meta);
    }

    @Override
    public String toString() {
        FireworkEffect effect = builder.build();
        StringBuilder returnVal = new StringBuilder();
        returnVal.append(effect.getType().name()).append(SEPERATOR);
        boolean twinkle = effect.hasFlicker();
        boolean trail = effect.hasTrail();
        if (!(twinkle || trail)) returnVal.append("NONE");
        if (twinkle && trail) returnVal.append("TWINKLE").append(SEPERATOR_2).append("TRAIL");
        if (twinkle) returnVal.append("TWINKLE");
        if (trail) returnVal.append("TRAIL");
        returnVal.append(SEPERATOR);
        colorStringBuilder(effect, returnVal, true);
        colorStringBuilder(effect, returnVal, false);
        return returnVal.toString();
    }

    private void colorStringBuilder(FireworkEffect effect, StringBuilder returnVal, boolean base) {
        List<Color> colors = (base ? effect.getColors() : effect.getFadeColors());
        if (colors.size() == 0) {
            returnVal.append(SEPERATOR);
        } else if (colors.size() == 1) {
            returnVal.append(getString(colors.get(0))).append(SEPERATOR);
        } else {
            for (Color color : colors) {
                returnVal.append(getString(color)).append(SEPERATOR_2);
            }
            returnVal.append(SEPERATOR);
        }
    }

    @Override
    public FireworkEffectC clone() {
        return new FireworkEffectC(this.builder);
    }

}
