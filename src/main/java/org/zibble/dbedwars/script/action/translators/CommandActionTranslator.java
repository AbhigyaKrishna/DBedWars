package org.zibble.dbedwars.script.action.translators;

import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.script.action.impl.CommandAction;

public class CommandActionTranslator implements ActionTranslator<CommandSender, CommandAction> {

    @Override
    public CommandAction serialize(String untranslated, ScriptVariable<?>... variables) {
        CommandSender sender = null;
        for (ScriptVariable<?> variable : variables) {
            if (variable.isAssignableFrom(CommandSender.class)) {
                sender = (CommandSender) variable.value();
            } else if (variable.getKey().equals("PLACEHOLDER")) {
                untranslated = ((PlaceholderEntry) variable.value()).apply(untranslated);
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
