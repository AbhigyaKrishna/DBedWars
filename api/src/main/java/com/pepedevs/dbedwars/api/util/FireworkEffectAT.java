package com.pepedevs.dbedwars.api.util;

import com.pepedevs.corelib.utils.reflection.general.EnumReflection;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;

import java.util.List;

public class FireworkEffectAT {

    // FIREWORK[SMALL/LARGE/STAR/CREEPEER/BURST:NONE/TWINKLE/TRAIL:BASECOLORS:FADECOLORS]

    private static final String SEPERATOR = ":";
    private static final String SEPERATOR_2 = ",";

    private final FireworkEffect.Builder builder;

    public FireworkEffectAT(FireworkEffect.Type type, Color... baseColors) {
        this.builder = FireworkEffect.builder().with(type);
        for (Color color : baseColors) {
            builder.withColor(color);
        }
    }

    public FireworkEffectAT(FireworkEffect.Type type, boolean twinkle, boolean trail, Color... baseColors) {
        this.builder = FireworkEffect.builder().with(type).flicker(twinkle).trail(trail);
        for (Color color : baseColors) {
            builder.withColor(color);
        }
    }

    public static FireworkEffectAT valueOf(String firework) {
        String[] params = firework.split(SEPERATOR);
        if (params.length < 1) return null;
        FireworkEffect.Type type = EnumReflection.getEnumConstant(FireworkEffect.Type.class, params[0]);
        FireworkEffectAT returnVal = new FireworkEffectAT((type == null ? FireworkEffect.Type.BALL : type));
        if (params.length < 2) return returnVal;
        String[] p = params[1].split(SEPERATOR_2);
        for (String s : p) {
            if (s.equals("TWINKLE")) returnVal.builder.withFlicker();
            if (s.equals("TRAIL")) returnVal.builder.withTrail();
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

    public FireworkEffect getEffect() {
        return builder.build();
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
        }else if (colors.size() == 1) {
            returnVal.append(getString(colors.get(0))).append(SEPERATOR);
        }else {
            for (Color color : colors) {
                returnVal.append(getString(color)).append(SEPERATOR_2);
            }
            returnVal.append(SEPERATOR);
        }
    }

    private static Color getColor(String color) {
        com.pepedevs.dbedwars.api.util.Color color1 = EnumReflection.getEnumConstant(com.pepedevs.dbedwars.api.util.Color.class, color);
        if (color1 != null) return color1.getColor();
        return Color.fromRGB(java.awt.Color.decode(color).getRGB());
    }

    private static String getString(Color color) {
        for (com.pepedevs.dbedwars.api.util.Color value : com.pepedevs.dbedwars.api.util.Color.values()) {
            if (value.getColor().equals(color)) return value.name();
        }
        return "#" + Integer.toHexString(color.getRed()) + Integer.toHexString(color.getGreen()) + Integer.toHexString(color.getBlue());
    }
}
