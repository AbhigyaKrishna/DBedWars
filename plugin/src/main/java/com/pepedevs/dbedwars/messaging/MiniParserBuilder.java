package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.configuration.configurable.ConfigurableMessaging;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;

public class MiniParserBuilder {

    private MiniMessage.Builder builder;

    public MiniParserBuilder(){
        this.builder = MiniMessage.builder();
    }

    public MiniParserBuilder(ConfigurableMessaging.ConfigurableModernSettings settings){
        this.clearDefaults()
                .addTransformations(settings.getTransformations())
                .strict(settings.isStrict());

        if (settings.isDiscordFlavour()) this.addDiscordFlavor();

    }

    public MiniParserBuilder clearDefaults(){
        builder = builder.removeDefaultTransformations();
        return this;
    }

    public MiniParserBuilder addTransformations(ConfigurableMessaging.ConfigurableModernSettings.ConfigurableTransformations transformations){

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

        return this;

    }

    public MiniParserBuilder strict(boolean strict){
        builder = builder.strict(strict);
        return this;
    }

    public MiniParserBuilder addDiscordFlavor(){
        builder = builder.markdown();
        builder = builder.markdownFlavor(DiscordFlavor.get());
        return this;
    }

    public MiniMessage.Builder build(){
        return this.builder;
    }

}
