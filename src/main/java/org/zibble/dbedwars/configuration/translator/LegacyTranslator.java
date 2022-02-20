package org.zibble.dbedwars.configuration.translator;

import net.kyori.adventure.text.Component;
import org.zibble.dbedwars.api.adventure.AdventureUtils;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.messaging.message.LegacyMessage;
import org.zibble.dbedwars.api.messaging.message.Message;

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

    @Override
    public Message asMessage(String text, PlaceholderEntry... entries) {
        return LegacyMessage.from(text, entries);
    }

    public char getCHAR() {
        return CHAR;
    }
}