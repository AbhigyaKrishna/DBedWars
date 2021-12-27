package com.pepedevs.dbedwars.configuration.translator;

import com.pepedevs.corelib.adventure.MiniMessageUtils;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import net.kyori.adventure.text.Component;

import java.util.Collection;
import java.util.List;

public interface ConfigTranslator {

    Component translate(String text);

    Component[] translate(String... texts);

    List<Component> translate(Collection<String> texts);

    String untranslate(Component component);

    String[] untranslate(Component... components);

    List<String> untranslate(Collection<Component> components);

    default String strip(Component component) {
        return MiniMessageUtils.strip(component);
    }

    Message asMessage(String text, PlaceholderEntry... entries);

}
