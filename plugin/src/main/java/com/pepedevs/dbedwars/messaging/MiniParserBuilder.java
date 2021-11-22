package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.configuration.configurable.ConfigurableMessaging;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;

public class MiniParserBuilder {

    private MiniMessage.Builder builder;

    public MiniParserBuilder() {
        this.builder = MiniMessage.builder();
    }

    public static MiniParserBuilder fromConfig(ConfigurableMessaging.ConfigurableModernSettings settings) {
        MiniParserBuilder builder = new MiniParserBuilder();
        builder.clearDefaults()
                .strict(settings.isStrict());

        if (settings.getTransformations().isClickEvent())
            builder.addTransformation(TransformationType.CLICK_EVENT);
        if (settings.getTransformations().isColor())
            builder.addTransformation(TransformationType.COLOR);
        if (settings.getTransformations().isDecoration())
            builder.addTransformation(TransformationType.DECORATION);
        if (settings.getTransformations().isFont())
            builder.addTransformation(TransformationType.FONT);
        if (settings.getTransformations().isGradient())
            builder.addTransformation(TransformationType.GRADIENT);
        if (settings.getTransformations().isHoverEvent())
            builder.addTransformation(TransformationType.HOVER_EVENT);
        if (settings.getTransformations().isInsertion())
            builder.addTransformation(TransformationType.INSERTION);
        if (settings.getTransformations().isKeybind())
            builder.addTransformation(TransformationType.KEYBIND);
        if (settings.getTransformations().isPre())
            builder.addTransformation(TransformationType.PRE);
        if (settings.getTransformations().isRainbow())
            builder.addTransformation(TransformationType.RAINBOW);
        if (settings.getTransformations().isReset())
            builder.addTransformation(TransformationType.RESET);
        if (settings.getTransformations().isTranslatable())
            builder.addTransformation(TransformationType.TRANSLATABLE);

        if (settings.isDiscordFlavour()) builder.addDiscordFlavor();

        return builder;
    }

    public MiniParserBuilder clearDefaults() {
        builder = builder.removeDefaultTransformations();
        return this;
    }

    public MiniParserBuilder addTransformation(TransformationType<? extends Transformation> transformation) {
        this.builder.transformation(transformation);
        return this;
    }

    public MiniParserBuilder addTransformation(TransformationType<? extends Transformation>... transformations) {
        for (TransformationType<? extends Transformation> transformation : transformations) {
            this.addTransformation(transformation);
        }

        return this;
    }

    public MiniParserBuilder strict(boolean strict) {
        builder.strict(strict);
        return this;
    }

    public MiniParserBuilder addDiscordFlavor() {
        builder.markdown();
        builder.markdownFlavor(DiscordFlavor.get());
        return this;
    }

    public MiniMessage build() {
        return this.builder.build();
    }
}
