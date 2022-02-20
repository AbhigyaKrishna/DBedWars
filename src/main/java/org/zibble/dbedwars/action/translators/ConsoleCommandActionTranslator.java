package org.zibble.dbedwars.action.translators;

import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.action.actions.ConsoleCommandAction;
import org.zibble.dbedwars.api.action.ActionPlaceholder;
import org.zibble.dbedwars.api.action.ActionTranslator;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.util.Key;

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
