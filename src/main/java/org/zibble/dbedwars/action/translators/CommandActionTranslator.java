package org.zibble.dbedwars.action.translators;

import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.action.actions.CommandAction;
import org.zibble.dbedwars.api.action.ActionPlaceholder;
import org.zibble.dbedwars.api.action.ActionTranslator;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.util.Key;

public class CommandActionTranslator implements ActionTranslator<CommandSender, CommandAction> {

    @Override
    public CommandAction serialize(String untranslated, ActionPlaceholder<?, ?>... placeholders) {
        CommandSender sender = null;
        for (ActionPlaceholder<?, ?> placeholder : placeholders) {
            if (placeholder.getValue() instanceof CommandSender) {
                sender = (CommandSender) placeholder.getValue();
            } else if (placeholder.getKey().equals("PLACEHOLDER")) {
                untranslated = ((PlaceholderEntry) placeholder.getValue()).apply(untranslated);
            }
        }
        return new CommandAction(untranslated, sender);
    }

    @Override
    public String deserialize(CommandAction action) {
        return action.getCommand();
    }

    @Override
    public Key<String> getKey() {
        return Key.of("COMMAND");
    }

}
