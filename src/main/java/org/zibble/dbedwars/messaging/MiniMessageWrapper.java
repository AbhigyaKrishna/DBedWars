package org.zibble.dbedwars.messaging;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;
import org.zibble.dbedwars.configuration.MainConfiguration;

public class MiniMessageWrapper {

    private static final MiniMessage FULL_MINI;
    private static MiniMessage CONFIG;

    static {
        FULL_MINI = MiniMessage.builder().transformations(
                TransformationRegistry.builder()
                        .add(TransformationType.COLOR)
                        .add(TransformationType.DECORATION)
                        .add(TransformationType.HOVER_EVENT)
                        .add(TransformationType.CLICK_EVENT)
                        .add(TransformationType.KEYBIND)
                        .add(TransformationType.TRANSLATABLE)
                        .add(TransformationType.INSERTION)
                        .add(TransformationType.FONT)
                        .add(TransformationType.GRADIENT)
                        .add(TransformationType.RAINBOW)
                        .build()
                ).build();
    }

    public static void importConfig(MainConfiguration.LangSection.ModernSettingsSection settings) {
        MiniMessage.Builder miniBuilder = MiniMessage.builder();

        MainConfiguration.LangSection.ModernSettingsSection.TransformationsSection transformations = settings.getTransformations();
        TransformationRegistry.Builder builder = TransformationRegistry.builder();
        if (transformations.isClickEvent()) builder.add(TransformationType.CLICK_EVENT);
        if (transformations.isColor()) builder.add(TransformationType.COLOR);
        if (transformations.isDecoration()) builder.add(TransformationType.DECORATION);
        if (transformations.isFont()) builder.add(TransformationType.FONT);
        if (transformations.isGradient()) builder.add(TransformationType.GRADIENT);
        if (transformations.isHoverEvent()) builder.add(TransformationType.HOVER_EVENT);
        if (transformations.isInsertion()) builder.add(TransformationType.INSERTION);
        if (transformations.isKeybind()) builder.add(TransformationType.KEYBIND);
        if (transformations.isRainbow()) builder.add(TransformationType.RAINBOW);
        if (transformations.isTranslatable()) builder = builder.add(TransformationType.TRANSLATABLE);
        miniBuilder.transformations(builder.build());
        CONFIG = miniBuilder.build();
    }

    public static MiniMessage getFullInstance() {
        return FULL_MINI;
    }

    public static MiniMessage getConfigInstance() {
        return CONFIG;
    }
}
