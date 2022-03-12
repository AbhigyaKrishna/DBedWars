package org.zibble.dbedwars.script.action.translators;

import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.script.action.impl.ConsoleCommandAction;

public class ConsoleCommandActionTranslator implements ActionTranslator<CommandSender, ConsoleCommandAction> {

    @Override
    public ConsoleCommandAction serialize(String untranslated, ScriptVariable<?>... variables) {
        for (ScriptVariable<?> variable : variables) {
            if (variable.getKey().equals("PLACEHOLDER"))
                untranslated = ((PlaceholderEntry) variable.value()).apply(untranslated);
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
