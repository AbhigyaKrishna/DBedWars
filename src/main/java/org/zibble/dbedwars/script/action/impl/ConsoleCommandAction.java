package org.zibble.dbedwars.script.action.impl;

import org.bukkit.Bukkit;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.key.Key;

public class ConsoleCommandAction implements ActionTranslator<ConsoleCommandAction.Action> {

    @Override
    public Action serialize(String untranslated, ScriptVariable<?>... variables) {
        for (ScriptVariable<?> variable : variables) {
            if (variable.getKey().equals("PLACEHOLDER"))
                untranslated = ((PlaceholderEntry) variable.value()).apply(untranslated);
        }
        return new Action(untranslated);
    }

    @Override
    public String deserialize(Action action) {
        return action.getCommand();
    }

    @Override
    public Key getKey() {
        return Key.of("CONSOLE_COMMAND");
    }

    public static class Action extends CommandAction.Action {

        public Action(String command) {
            super(command, Bukkit.getConsoleSender());
        }

    }

}
