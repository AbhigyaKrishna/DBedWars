package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.configuration.configurable.ConfigurableMessaging;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;

@SuppressWarnings("unchecked")
public class MiniMessageWrapper {

    private static final MiniMessage FULL_MINI;
    private static MiniMessage CONFIG;

    static {
        FULL_MINI = MiniMessage.builder()
                .removeDefaultTransformations()
                .transformations(
                        TransformationType.COLOR,
                        TransformationType.DECORATION,
                        TransformationType.HOVER_EVENT,
                        TransformationType.CLICK_EVENT,
                        TransformationType.KEYBIND,
                        TransformationType.TRANSLATABLE,
                        TransformationType.INSERTION,
                        TransformationType.FONT,
                        TransformationType.GRADIENT,
                        TransformationType.RAINBOW,
                        TransformationType.RESET,
                        TransformationType.PRE)
                .markdownFlavor(DiscordFlavor.get())
                .build();
    }

    public static void importConfig(ConfigurableMessaging.ConfigurableModernSettings settings) {
        MiniMessage.Builder builder = MiniMessage.builder();
        builder.removeDefaultTransformations();

        ConfigurableMessaging.ConfigurableModernSettings.ConfigurableTransformations transformations = settings.getTransformations();
        if (transformations.isClickEvent()) builder = builder.transformation(TransformationType.CLICK_EVENT);
        if (transformations.isColor()) builder = builder.transformation(TransformationType.COLOR);
        if (transformations.isDecoration()) builder = builder.transformation(TransformationType.DECORATION);
        if (transformations.isFont()) builder = builder.transformation(TransformationType.FONT);
        if (transformations.isGradient()) builder = builder.transformation(TransformationType.GRADIENT);
        if (transformations.isHoverEvent()) builder = builder.transformation(TransformationType.HOVER_EVENT);
        if (transformations.isInsertion()) builder = builder.transformation(TransformationType.INSERTION);
        if (transformations.isKeybind()) builder = builder.transformation(TransformationType.KEYBIND);
        if (transformations.isPre()) builder = builder.transformation(TransformationType.PRE);
        if (transformations.isRainbow()) builder = builder.transformation(TransformationType.RAINBOW);
        if (transformations.isReset()) builder = builder.transformation(TransformationType.RESET);
        if (transformations.isTranslatable()) builder = builder.transformation(TransformationType.TRANSLATABLE);

        builder = builder.strict(settings.isStrict());
        if (settings.isDiscordFlavour()) builder.markdown().markdownFlavor(DiscordFlavor.get());

        CONFIG = builder.build();
    }

    public static MiniMessage getFullInstance() {
        return FULL_MINI;
    }

    public static MiniMessage getConfigInstance() {
        return CONFIG;
    }
}
