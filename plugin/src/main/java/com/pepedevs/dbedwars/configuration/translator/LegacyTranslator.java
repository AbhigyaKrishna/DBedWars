package com.pepedevs.dbedwars.configuration.translator;

import com.pepedevs.corelib.adventure.AdventureUtils;
import net.kyori.adventure.text.Component;

import java.util.Collection;
import java.util.List;

public class LegacyTranslator implements ConfigTranslator{

    private final char CHAR;

    public LegacyTranslator(char c) {
        this.CHAR = c;
    }

    @Override
    public Component translate(String text) {
        return AdventureUtils.fromLegacyText(CHAR, text);
    }

    @Override
    public Component[] translate(String... texts) {
        return AdventureUtils.fromLegacyText(CHAR, texts);
    }

    @Override
    public List<Component> translate(Collection<String> texts) {
        return AdventureUtils.fromLegacyText(CHAR, texts);
    }

    @Override
    public String untranslate(Component component) {
        return AdventureUtils.toLegacyText(CHAR, component);
    }

    @Override
    public String[] untranslate(Component... components) {
        return AdventureUtils.toLegacyText(CHAR, components);
    }

    @Override
    public List<String> untranslate(Collection<Component> components) {
        return AdventureUtils.toLegacyText(CHAR, components);
    }
}
