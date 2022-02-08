package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.ConsoleCommandAction;
import com.pepedevs.dbedwars.api.action.ActionPlaceholder;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.api.util.Key;
import org.bukkit.command.CommandSender;

public class ConsoleCommandActionTranslator implements ActionTranslator<CommandSender, ConsoleCommandAction> {

    @Override
    public ConsoleCommandAction serialize(String untranslated, ActionPlaceholder<?, ?>... placeholders) {
        for (ActionPlaceholder<?, ?> placeholder : placeholders) {
            if (placeholder.getKey().equals("PLACEHOLDER"))
                untranslated = ((PlaceholderEntry) placeholder.getValue()).apply(untranslated);
        }
        return new ConsoleCommandAction(untranslated);
    }

    @Override
    public String deserialize(ConsoleCommandAction action) {
        return action.getCommand();
    }

    @Override
    public Key<String> getKey() {
        return Key.of("CONSOLE_COMMAND");
    }

}
