package org.zibble.dbedwars.messaging;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.zibble.dbedwars.configuration.MainConfiguration;

public class MiniMessageWrapper {

    private static final MiniMessage FULL_MINI;
    private static MiniMessage CONFIG;

    static {
        FULL_MINI = MiniMessage.builder()
                .tags(TagResolver.standard())
                .build();
    }

    public static void importConfig(MainConfiguration.LangSection.ModernSettingsSection settings) {
        MiniMessage.Builder miniBuilder = MiniMessage.builder();

        MainConfiguration.LangSection.ModernSettingsSection.TransformationsSection transformations = settings.getTransformations();
        TagResolver.Builder builder = TagResolver.builder();
        if (transformations.isClickEvent()) builder.resolver(StandardTags.clickEvent());
        if (transformations.isColor()) builder.resolver(StandardTags.color());
        if (transformations.isDecoration()) builder.resolver(StandardTags.decorations());
        if (transformations.isFont()) builder.resolver(StandardTags.font());
        if (transformations.isGradient()) builder.resolver(StandardTags.gradient());
        if (transformations.isHoverEvent()) builder.resolver(StandardTags.hoverEvent());
        if (transformations.isInsertion()) builder.resolver(StandardTags.insertion());
        if (transformations.isKeybind()) builder.resolver(StandardTags.keybind());
        if (transformations.isRainbow()) builder.resolver(StandardTags.rainbow());
        if (transformations.isTranslatable()) builder = builder.resolver(StandardTags.translatable());
        miniBuilder.tags(builder.build());
        CONFIG = miniBuilder.build();
    }

    public static MiniMessage getFullInstance() {
        return FULL_MINI;
    }

    public static MiniMessage getConfigInstance() {
        return CONFIG;
    }

}
