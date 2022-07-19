package org.zibble.dbedwars.script.action.impl;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.key.Key;

public class CommandAction implements ActionTranslator<CommandAction.Action> {

    @Override
    public Action serialize(String untranslated, ScriptVariable<?>... variables) {
        CommandSender sender = null;
        for (ScriptVariable<?> variable : variables) {
            if (variable.isSubClassOf(CommandSender.class)) {
                sender = (CommandSender) variable.value();
            } else if (variable.getKey().equals("PLACEHOLDER")) {
                untranslated = ((PlaceholderEntry) variable.value()).apply(untranslated);
            }
        }
        return new Action(untranslated, sender);
    }

    @Override
    public String deserialize(Action action) {
        return action.getCommand();
    }

    @Override
    public Key getKey() {
        return Key.of("COMMAND");
    }

    public static class Action implements org.zibble.dbedwars.api.script.action.Action {

        private final CommandSender sender;
        private final String command;

        public Action(String command, CommandSender sender) {
            this.sender = sender;
            this.command = command;
        }

        @Override
        public void execute() {
            Bukkit.dispatchCommand(this.getSender(), command);
        }

        public CommandSender getSender() {
            return this.sender;
        }

        public String getCommand() {
            return command;
        }

    }

}
