package org.zibble.dbedwars.configuration.translator;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.zibble.dbedwars.api.adventure.MiniMessageUtils;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;

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

    @Override
    public Message asMessage(String text, PlaceholderEntry... entries) {
        return AdventureMessage.from(text, entries);
    }

}
