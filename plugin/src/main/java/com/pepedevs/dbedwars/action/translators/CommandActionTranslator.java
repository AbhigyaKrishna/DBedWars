package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.CommandAction;
import com.pepedevs.dbedwars.api.action.ActionPlaceholder;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.util.Key;
import org.bukkit.command.CommandSender;

public class CommandActionTranslator implements ActionTranslator<CommandSender, CommandAction> {

    @Override
    public CommandAction serialize(String untranslated, ActionPlaceholder<?, ?>... placeholders) {
        CommandSender sender = null;
        for (ActionPlaceholder<?, ?> placeholder : placeholders) {
            if (placeholder.getKey().equals("SENDER")) {
                sender = (CommandSender) placeholder.getPlaceholder();
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
