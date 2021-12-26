package com.pepedevs.dbedwars.configuration.translator;

import com.pepedevs.corelib.adventure.MiniMessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Collection;
import java.util.List;

public class MiniMessageTranslator implements ConfigTranslator {

    private final MiniMessage mini;

    public MiniMessageTranslator(MiniMessage miniMessage) {
        this.mini = miniMessage;
    }

    @Override
    public Component translate(String text) {
        return MiniMessageUtils.translate(mini, text);
    }

    @Override
    public Component[] translate(String... texts) {
        return MiniMessageUtils.translate(mini, texts);
    }

    @Override
    public List<Component> translate(Collection<String> texts) {
        return MiniMessageUtils.translate(mini, texts);
    }

    @Override
    public String untranslate(Component component) {
        return MiniMessageUtils.untranslate(mini, component);
    }

    @Override
    public String[] untranslate(Component... components) {
        return MiniMessageUtils.untranslate(mini, components);
    }

    @Override
    public List<String> untranslate(Collection<Component> components) {
        return MiniMessageUtils.untranslate(mini, components);
    }
}
